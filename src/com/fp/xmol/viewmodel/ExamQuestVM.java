package com.fp.xmol.viewmodel;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.TexamDAO;
import com.fp.xmol.dao.TexamanswerDAO;
import com.fp.xmol.dao.TexamquestDAO;
import com.fp.xmol.domain.Texam;
import com.fp.xmol.domain.Texamanswer;
import com.fp.xmol.domain.Texamquest;
import com.fp.xmol.util.Util;

public class ExamQuestVM {
	
	private Session session;
	private Transaction trx;
	private TexamDAO oDao = new TexamDAO();
	private TexamquestDAO texamquestDao = new TexamquestDAO();
	private TexamanswerDAO texamanswerDao = new TexamanswerDAO();
	
	private Texam texam;
	private Texamquest texamquest;
	private List<Texamquest> texamquestList;
	private Map<Integer, String> mapAnswer = new HashMap<Integer, String>();
	
	private int questno;
	private int questindex;
	private int totalanswer;
	private int jumlahbenar;
	private int progress;
	private String answerno;
	private String countdown;
	private int menit;
	private int detik;
	private String smenit;
	private String sdetik;
	
	@Wire
	private Image img;
	@Wire
	private Radiogroup radioAnswer;
	@Wire
	private Button btNext;
	@Wire
	private Button btPrev;
	@Wire
	private Button btFinish;
	@Wire
	private Timer timer;
	@Wire
	private Textbox tbQuest;
	@Wire
	private Div divProgress;
	
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("texam") Texam texam, 
			@ExecutionArgParam("texamquestList") List<Texamquest> texamquestList) {
		Selectors.wireComponents(view, this, false);
		this.texam = texam;
		this.texamquestList = texamquestList;
		doLoadQuest("begin");
		
		menit = texam.getDurasi() - 1;
		detik = 59;		
		timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				boolean isStop = false;
				if (detik == 0) {
					if (menit == 0) {
						isStop = true;
						timer.setRepeats(false);
						doSaveExam();
						Messagebox.show("Waktu anda telah habis", WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.INFORMATION);
					} else {
						detik = 60;
						menit--;
					}					
				}
				
				if (!isStop) {
					detik--;
					
					smenit = "00" + menit;
					sdetik = "00" + detik;
					
					countdown = smenit.substring(smenit.length() - 2, smenit.length()) + ":" + sdetik.substring(sdetik.length() - 2, sdetik.length());
					BindUtils.postNotifyChange(null, null, ExamQuestVM.this, "countdown");
				}				
			}
		});
	}
	
	@Command
	@NotifyChange("*")
	public void doLoadQuest(@BindingParam("act") String act) {
		if (texamquest != null) {
			String val = mapAnswer.get(texamquest.getTexamquestpk());	
			if (val == null)
				val = "";
			if (answerno != null && !val.equals(answerno)) {
				mapAnswer.put(texamquest.getTexamquestpk(), answerno);
				session = StoreHibernateUtil.openSession();
				try {
					trx = session.beginTransaction();
					texamquestDao.save(session, texamquest);					
					trx.commit();										
				} catch (HibernateException e) {
					trx.rollback();
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					session.close();
				}
			}
		}
		
		answerno = null;
		boolean isValid = false;
		if (act.equals("next")) {
			if (questindex < texamquestList.size()) {
				questno++;
				questindex++;
				isValid = true;
			}
		} else if (act.equals("prev")) {
			if (questindex > 0) {
				questno--;
				questindex--;
				isValid = true;
			}
		} else if (act.equals("begin")) {
			questno = 1;
			questindex = 0;			
			isValid = true;
		}
		
		if (isValid) {			
			texamquest = texamquestList.get(questindex);
			int rows = texamquest.getQuestiontext().length() / 75;
			long enter = Util.countOccurrencesOf(texamquest.getQuestiontext(), '\n');
			enter = enter + 1;		
			rows += enter;					
			tbQuest.setRows(rows);
			if (texamquest.getQuestionimg() != null) {
				AImage aimage;
				try {
					aimage = new AImage("", texamquest.getQuestionimg());
					img.setContent(aimage);					
				} catch (IOException e) {				
					e.printStackTrace();
				}				
			} else {
				img.setSrc(null);
			}
			
			if (questindex < texamquestList.size()) {
				texamquest = texamquestList.get(questindex);
				
				try {
					radioAnswer.getChildren().clear();
					for (Texamanswer answer: texamanswerDao.listByFilter("texamquest.texamquestpk = " + texamquest.getTexamquestpk(), "answerno")) {
						Radio radio = new Radio(answer.getAnswertext());
						radio.setValue(answer.getAnswerno());	
						String valanswer = mapAnswer.get(texamquest.getTexamquestpk());	
						if (valanswer != null && valanswer.equals(answer.getAnswerno())) {
							answerno = valanswer;
						}					
						radioAnswer.appendChild(radio);										
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (questindex == 0) {
			btPrev.setVisible(false);
		} else {
			btPrev.setVisible(true);
		}
		if (questindex == texamquestList.size()-1) {
			btNext.setVisible(false);
		} else {
			btNext.setVisible(true);
		}
	}	
	
	@Command
	public void doAnswer() {
		try {			
			if (texamquest.getHisanswer().equals("")) { // new answer
				if (answerno.equals(texamquest.getRightanswer())) {
					texamquest.setIsright("Y");
					jumlahbenar++;
				}				
				
				totalanswer++;				
				if (totalanswer == texam.getJumlahsoal()) // show finish
					btFinish.setVisible(true);
				
				Double dProgress = (new Double(totalanswer) / new Double (texam.getJumlahsoal())) * 100;
				progress = dProgress.intValue();
				
				divProgress.getChildren().clear();
				HtmlNativeComponent divProgressbar = new HtmlNativeComponent("div"); 
				divProgressbar.setClientAttribute("class", "progress-bar");
				divProgressbar.setClientAttribute("role", "progressbar");
				divProgressbar.setClientAttribute("style", "width: " + progress + "%");
				divProgressbar.setClientAttribute("aria-valuenow", String.valueOf(progress));
				divProgressbar.setClientAttribute("aria-valuemin", "0");
				divProgressbar.setClientAttribute("aria-valuemax", "100");
				divProgressbar.appendChild(new Label(progress + "%"));
				divProgress.appendChild(divProgressbar);
			} else { // update answer
				if (answerno.equals(texamquest.getRightanswer())) {
					texamquest.setIsright("Y");
					jumlahbenar++;
				} else {
					if (texamquest.getIsright().equals("Y")) {
						texamquest.setIsright("N");
						jumlahbenar--;
					}
				}
			}
			texamquest.setHisanswer(answerno);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doFinish() {
		Messagebox.show("Anda yakin sudah menjawab dengan benar untuk semua pertanyaan dan ingin mengakhiri tes ini?", WebApps.getCurrent().getAppName(),
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals("onOK")) {
							doSaveExam();
						}
					}
		});
	}
	
	@NotifyChange("*")
	private void doSaveExam() {
		if (answerno != null) {
			mapAnswer.put(texamquest.getTexamquestpk(), answerno);
			session = StoreHibernateUtil.openSession();
			try {
				trx = session.beginTransaction();
				texamquestDao.save(session, texamquest);				
				trx.commit();
			} catch (HibernateException e) {
				trx.rollback();
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
		}
		
		session = StoreHibernateUtil.openSession();
		try {
			trx = session.beginTransaction();						
			texam.setJumlahbenar(jumlahbenar);
			Double score = (new Double(jumlahbenar) / new Double (texam.getJumlahsoal())) * 100;
			texam.setScore(score.intValue());
			if (score >= texam.getPassingscore())
				texam.setIspass("Y");
			else texam.setIspass("N");			
			texam.setWaktuakhirtest(new Date());
			
			long diff = texam.getWaktuakhirtest().getTime() - texam.getWaktumulaitest().getTime();
			long diffMinutes = diff / (60 * 1000) % 60;
			System.out.println("diffMinutes : " + diffMinutes);
			texam.setLamates((int) diffMinutes);
			
			oDao.save(session, texam);
			trx.commit();
		} catch (HibernateException e) {
			trx.rollback();
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		Executions.sendRedirect("/view/exam/examdone.zul");
	}

	public Texam getTexam() {
		return texam;
	}

	public void setTexam(Texam texam) {
		this.texam = texam;
	}

	public Texamquest getTexamquest() {
		return texamquest;
	}

	public void setTexamquest(Texamquest texamquest) {
		this.texamquest = texamquest;
	}

	public String getCountdown() {
		return countdown;
	}

	public void setCountdown(String countdown) {
		this.countdown = countdown;
	}

	public String getAnswerno() {
		return answerno;
	}

	public void setAnswerno(String answerno) {
		this.answerno = answerno;
	}

	public int getQuestno() {
		return questno;
	}

	public void setQuestno(int questno) {
		this.questno = questno;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	
}
