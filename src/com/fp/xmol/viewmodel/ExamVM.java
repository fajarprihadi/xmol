package com.fp.xmol.viewmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;

import com.fp.utils.SysUtils;
import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.TbanksoalDAO;
import com.fp.xmol.dao.TcounterengineDAO;
import com.fp.xmol.dao.TexamDAO;
import com.fp.xmol.dao.TexamanswerDAO;
import com.fp.xmol.dao.TexamdocDAO;
import com.fp.xmol.dao.TexamquestDAO;
import com.fp.xmol.dao.TquestDAO;
import com.fp.xmol.dao.TquestanswerDAO;
import com.fp.xmol.domain.Tbanksoal;
import com.fp.xmol.domain.Texam;
import com.fp.xmol.domain.Texamanswer;
import com.fp.xmol.domain.Texamdoc;
import com.fp.xmol.domain.Texamquest;
import com.fp.xmol.domain.Tquest;
import com.fp.xmol.domain.Tquestanswer;
import com.fp.xmol.handler.DuplicateHandler;
import com.fp.xmol.util.AppUtil;
import com.fp.xmol.util.Util;

public class ExamVM {
	
	private Session session;
	private Transaction trx;
	private TexamDAO oDao = new TexamDAO();
	private TquestDAO tquestDao = new TquestDAO();
	private TquestanswerDAO tquestanswerDao = new TquestanswerDAO();
	private TexamquestDAO texamquestDao = new TexamquestDAO();
	private TexamanswerDAO texamanswerDao = new TexamanswerDAO();
	private TexamdocDAO texamdocDao = new TexamdocDAO();
	
	private Texam objForm;
	private List<Media> listMedia = new ArrayList<>();
	
	@Wire
	private Div divExam;
	@Wire
	private Div divFiles;
	@Wire
	private Button btStart;

	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view) {		
		Selectors.wireComponents(view, this, false);
		String fp = Executions.getCurrent().getParameter("fp");		
		System.out.println(fp);
		doReset();
		
		try {
			boolean isValid = true;
			if (fp == null) {
				isValid = false;
			} else {
				Tbanksoal banksoal = new TbanksoalDAO().findById(fp);
				if (banksoal == null) {
					isValid = false;					
				} else {
					isValid = true;					
					objForm.setTbanksoal(banksoal);
					objForm.setDeptcode(banksoal.getMdepartment().getDeptcode());
					objForm.setDurasi(banksoal.getDurasi());
					objForm.setJumlahsoal(banksoal.getExamsoal());
					objForm.setPassingscore(banksoal.getPassingscore());
					if (objForm.getJumlahsoal() == 0) {
						btStart.setDisabled(true);
						Messagebox.show("Soal tidak tersedia. Harap hubungi Administrator.", WebApps.getCurrent().getAppName(), Messagebox.OK,
								Messagebox.INFORMATION);
					}
				}
			}
			
			if (!isValid) {
				Executions.sendRedirect("/unauthorized.zul");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@NotifyChange("*")
	public void doReset() {
		objForm = new Texam();
	}
	
	@Command
	public void doUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		try {
			UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
			for (Media media: event.getMedias()) {
				listMedia.add(media);
				Hlayout hlayout = new Hlayout();
				hlayout.appendChild(new Label(media.getName()));
				hlayout.appendChild(new Separator("vertical"));
				A aDel = new A("Hapus");
				aDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						divFiles.removeChild(hlayout);
						listMedia.remove(media);
					}
				});
				hlayout.appendChild(aDel);				
				divFiles.appendChild(hlayout);				
			}		    						
		} catch (Exception e) {
			e.printStackTrace();
		}	    
	}
	
	@Command
	@NotifyChange("*")	
	public void doStart() {
		try {			
			if (!new DuplicateHandler().examDuplicateChecker(objForm)) {
				session = StoreHibernateUtil.openSession();
				try {					
					trx = session.beginTransaction();
					objForm.setWaktumulaitest(new Date());
					objForm.setIspass("");
					objForm.setTotaldoc(listMedia.size());	
					objForm.setStatus(AppUtil.STATUS_TES);
					objForm.setIswait("N");
					objForm.setIsdone("N");
					oDao.save(session, objForm);
					
					for (Media media: listMedia) {
						String docid = new TcounterengineDAO().generateCounter("DOC");
						String path = Executions.getCurrent().getDesktop().getWebApp()
								.getRealPath(SysUtils.FILES_ROOT_PATH + SysUtils.DOC_PATH);
						if (media.isBinary()) {
							Files.copy(new File(path + "/" + docid), media.getStreamData());
						} else {
							BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + docid));
							Files.copy(writer, media.getReaderData());
							writer.close();
						}
						
						Texamdoc texamdoc = new Texamdoc();
						texamdoc.setTexam(objForm);
						texamdoc.setDocid(docid);
						texamdoc.setDocfilename(media.getName());
						texamdoc.setUploadedby("Kandidat");
						texamdoc.setUploadtime(new Date());
						texamdocDao.save(session, texamdoc);
					}
					
					List<Texamquest> texamquestList = new ArrayList<Texamquest>();
					List<Tquest> listQuest = tquestDao.listByFilter("tbanksoal.tbanksoalpk = " + objForm.getTbanksoal().getTbanksoalpk() , "tquestpk");
					Collections.shuffle(listQuest); 
					int jumlahsoal = 0;
					for (Tquest quest: listQuest) {						
						if (jumlahsoal == objForm.getJumlahsoal())
							break;
						jumlahsoal++;
						Texamquest examquest = new Texamquest();
						examquest.setTexam(objForm);
						examquest.setQuestionimg(quest.getQuestionimg());
						examquest.setQuestiontext(quest.getQuestiontext());
						examquest.setRightanswer(quest.getRightanswer());
						examquest.setIsright("N");						
						examquest.setHisanswer("");
						texamquestDao.save(session, examquest);
						
						List<Tquestanswer> listAnswer = tquestanswerDao.listByFilter("tquest.tquestpk = " + quest.getTquestpk(), "answerno");
						for (Tquestanswer answer: listAnswer) {
							Texamanswer examanswer = new Texamanswer();
							examanswer.setTexamquest(examquest);
							examanswer.setAnswerimg(answer.getAnswerimg());
							examanswer.setAnswerno(answer.getAnswerno());
							examanswer.setAnswertext(answer.getAnswertext());
							examanswer.setIsright(answer.getIsright());
							texamanswerDao.save(session, examanswer);
						}
						
						texamquestList.add(examquest);						
					}
					
					trx.commit();
					session.close();
					
					divExam.getChildren().clear();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("texam", objForm);
					map.put("texamquestList", texamquestList);
					
					Executions.createComponents("/view/exam/examquest.zul", divExam, map);
				} catch (HibernateException e) {
					trx.rollback();
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					session.close();
				}	
			} else {
				Messagebox.show("Anda sudah pernah mengikuti test ini", WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.INFORMATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String nama = (String) ctx.getProperties("nama")[0].getValue();				
				Date tgllahir = (Date) ctx.getProperties("tgllahir")[0].getValue();
				String alamat = (String) ctx.getProperties("alamat")[0].getValue();
				String kota = (String) ctx.getProperties("kota")[0].getValue();	
				String nohp = (String) ctx.getProperties("nohp")[0].getValue();
				String email = (String) ctx.getProperties("email")[0].getValue();
				
				if (nama == null || "".equals(nama))
					this.addInvalidMessage(ctx, "nama", Labels.getLabel("common.validator.empty"));
				if (tgllahir == null)
					this.addInvalidMessage(ctx, "tgllahir", Labels.getLabel("common.validator.empty"));
				if (alamat == null || "".equals(alamat))
					this.addInvalidMessage(ctx, "alamat", Labels.getLabel("common.validator.empty"));
				if (kota == null || "".equals(kota))
					this.addInvalidMessage(ctx, "kota", Labels.getLabel("common.validator.empty"));
				if (nohp == null || "".equals(nohp))
					this.addInvalidMessage(ctx, "nohp", Labels.getLabel("common.validator.empty"));
				if (email == null || "".equals(email))
					this.addInvalidMessage(ctx, "email", Labels.getLabel("common.validator.empty"));
				else if (!Util.emailValidator(email))
					this.addInvalidMessage(ctx, "email", "Invalid format e-mail");
			}
		};
	}

	public Texam getObjForm() {
		return objForm;
	}

	public void setObjForm(Texam objForm) {
		this.objForm = objForm;
	}

}
