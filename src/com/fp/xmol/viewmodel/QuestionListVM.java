package com.fp.xmol.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.TbanksoalDAO;
import com.fp.xmol.dao.TquestDAO;
import com.fp.xmol.dao.TquestanswerDAO;
import com.fp.xmol.domain.Tbanksoal;
import com.fp.xmol.domain.Tquest;
import com.fp.xmol.domain.Tquestanswer;
import com.fp.xmol.util.Util;

public class QuestionListVM {
	
	private TbanksoalDAO oDao = new TbanksoalDAO();
	private TquestDAO tquestDao = new TquestDAO();
	private TquestanswerDAO tquestanswerDao = new TquestanswerDAO();
	
	private Tbanksoal obj;
	
	private List<Tquest> tquestList = new ArrayList<>();
	private List<Tquestanswer> tquestanswerList = new ArrayList<>();
	private Map<Integer, List<Tquestanswer>> mapAnswer = new HashMap<>();
	
	@Wire
	private Div divQuestList;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tbanksoal obj) {
    	Selectors.wireComponents(view, this, false);
    	this.obj = obj;
    	doLoad();
    	doRender();
    }
    
    public void doLoad() {	
    	try {
    		tquestList = tquestDao.listByFilter("tbanksoal.tbanksoalpk = " + obj.getTbanksoalpk(), "tquestpk");
    		tquestanswerList = tquestanswerDao.listByFilter("tquest.tbanksoal.tbanksoalpk = " + obj.getTbanksoalpk(), "tquest.tquestpk, answerno");
    		List<Tquestanswer> oList = new ArrayList<>();
    		Integer pk = -1;
    		for (Tquestanswer answer: tquestanswerList) {
    			if (!answer.getTquest().getTquestpk().equals(pk)) {
    				if (oList.size() > 0)
    					mapAnswer.put(pk, oList);
    				oList = new ArrayList<>();
    				pk = answer.getTquest().getTquestpk();
    			}
    			oList.add(answer);
    		}
    		if (oList.size() > 0)
				mapAnswer.put(pk, oList);    		    	
    	} catch (Exception e) {
    		e.printStackTrace();
		}		
	}
    
    public void doRender() {	
    	try {
    		divQuestList.getChildren().clear();
    		int no = 1;
    		for (Tquest quest: tquestList) {    	
    			if (quest.getQuestionimg() != null) {
					AImage aimage = new AImage("", quest.getQuestionimg());
					Image img = new Image();
					img.setContent(aimage);
					Hlayout hlayout = new Hlayout();
					hlayout.appendChild(new Label(no++ + ". "));
					Vlayout vlayout = new Vlayout();
					vlayout.appendChild(img);
					
					Textbox tbQuest = new Textbox(quest.getQuestiontext());
					tbQuest.setMultiline(true);
					tbQuest.setCols(70);
					tbQuest.setReadonly(true);
					int rows = quest.getQuestiontext().length() / 75;
					long enter = Util.countOccurrencesOf(quest.getQuestiontext(), '\n');
    				enter = enter + 1;		
    				rows += enter;					
					tbQuest.setRows(rows);
					
					vlayout.appendChild(tbQuest);
					hlayout.appendChild(vlayout);
					divQuestList.appendChild(hlayout);
				} else {
					Hlayout hlayout = new Hlayout();
					hlayout.appendChild(new Label(no++ + ". "));					
					Textbox tbQuest = new Textbox(quest.getQuestiontext());
					tbQuest.setMultiline(true);
					tbQuest.setCols(70);
					tbQuest.setReadonly(true);
					int rows = quest.getQuestiontext().length() / 75;
					long enter = Util.countOccurrencesOf(quest.getQuestiontext(), '\n');
    				enter = enter + 1;		
    				rows += enter;					
					tbQuest.setRows(rows);
					hlayout.appendChild(tbQuest);
					divQuestList.appendChild(hlayout);					
				}    			
    			
    			Div divanswer = new Div();
    			List<Tquestanswer> oList = mapAnswer.get(quest.getTquestpk());
    			if (oList != null) {
    				for (Tquestanswer answer: oList) {
    					Label lblAnswer = new Label(answer.getAnswerno() + ". " + answer.getAnswertext());
    					if (answer.getIsright().equals("Y"))
    						lblAnswer.setStyle("font-weight: bold");
        				divanswer.appendChild(lblAnswer);
        				divanswer.appendChild(new Separator());
        			}
    			}    			
    			divQuestList.appendChild(divanswer);
    			Div divAct = new Div();
    			divAct.setSclass("btn-group");
    			divAct.setAttribute("obj", quest);
    			
    			Button btEdit = new Button("Edit");
				btEdit.setAutodisable("self");
				btEdit.setSclass("btn btn-primary btn-sm");
				btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", quest);
						Window win = (Window) Executions
								.createComponents("/view/admin/questionedit.zul", null, map);
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								doLoad();
								doRender();
								BindUtils.postNotifyChange(null, null, QuestionListVM.this, "*");
							}
						});
					}
				});	
				
				Button btDelete = new Button("Hapus");
				btDelete.setAutodisable("self");
				btDelete.setSclass("btn btn-danger btn-sm");
				btDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						try {
							Messagebox.show(Labels.getLabel("common.delete.confirm"), WebApps.getCurrent().getAppName(),
									Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

										@Override
										public void onEvent(Event event) throws Exception {
											if (event.getName().equals("onOK")) {
												Tquest tquest = (Tquest) divAct.getAttribute("obj");
												Session session = StoreHibernateUtil.openSession();
												Transaction trx = session.beginTransaction();
												try {													
													tquestDao.delete(session, tquest);
													
													obj.setJumlahsoal(obj.getJumlahsoal() - 1);
													if (obj.getExamsoal() > obj.getJumlahsoal())
														obj.setExamsoal(obj.getJumlahsoal());
													oDao.save(session, obj);
													
													trx.commit();

													Clients.showNotification(Labels.getLabel("common.delete.success"), "info", null,
															"middle_center", 3000);

													doLoad();
													doRender();
													BindUtils.postNotifyChange(null, null, QuestionListVM.this, "*");
												} catch (HibernateException e) {
													trx.rollback();
													Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
															Messagebox.ERROR);
													e.printStackTrace();
												} catch (Exception e) {
													Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
															Messagebox.ERROR);
													e.printStackTrace();
												} finally {
													session.close();
												}
											}
										}

									});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});	
				divAct.appendChild(btEdit);
				divAct.appendChild(btDelete);
    			
    			divQuestList.appendChild(divAct);
    			divQuestList.appendChild(new HtmlNativeComponent("hr"));
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }

	public Tbanksoal getObj() {
		return obj;
	}

	public void setObj(Tbanksoal obj) {
		this.obj = obj;
	}
    

}
