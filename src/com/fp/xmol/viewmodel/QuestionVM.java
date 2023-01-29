package com.fp.xmol.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlNativeComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
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

public class QuestionVM {

	private Session session;
	private Transaction trx;
	private TbanksoalDAO oDao = new TbanksoalDAO();
	private TquestDAO tquestDao = new TquestDAO();
	private TquestanswerDAO tquestanswerDao = new TquestanswerDAO();

	private Tbanksoal obj;
	private Tquest objForm;
	private Tquestanswer objAnswer;
	private Tquestanswer objAnswerEdit;
	private List<Tquestanswer> listAnswers;
	private Boolean isSetRight;

	private Div divRowEdit;

	@Wire
	private Image img;
	@Wire
	private Button btnSave;
	@Wire
	private Div divAnswers;
	@Wire
	private Checkbox chkRight;
	@Wire
	private Div divQuestLast;

	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tbanksoal obj) {
		Selectors.wireComponents(view, this, false);
		this.obj = obj;
		doReset();
	}

	@Command
	@NotifyChange("objAnswer")
	public void doSaveAnswer() {
		try {
			if (objAnswerEdit != null) {
				divAnswers.removeChild(divRowEdit.getNextSibling());
				divAnswers.removeChild(divRowEdit);
				listAnswers.remove(objAnswerEdit);
				
				if (!chkRight.isChecked() && objAnswerEdit.getIsright().equals("Y")) {
					isSetRight = false;
				}
			}
			Label lblAnswer = new Label(objAnswer.getAnswertext());
			if (chkRight.isChecked()) {
				isSetRight = true;
				objAnswer.setIsright("Y");
				lblAnswer.setStyle("font-weight: bold");
			} else
				objAnswer.setIsright("N");
			listAnswers.add(objAnswer);

			Div divRow = new Div();
			divRow.setClass("row");

			Div divCol1 = new Div();
			divCol1.setClass("col-8");
			divCol1.appendChild(lblAnswer);
			divRow.appendChild(divCol1);

			Div divCol2 = new Div();
			divCol2.setClass("col-4");

			Div divGroup = new Div();
			divGroup.setAttribute("obj", objAnswer);
			divGroup.setClass("btn-group");
			Button btEdit = new Button("Edit");
			btEdit.setAutodisable("self");
			btEdit.setSclass("btn btn-default btn-sm");
			btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					divRowEdit = divRow;
					objAnswerEdit = (Tquestanswer) divGroup.getAttribute("obj");
					objAnswer = objAnswerEdit;
					if (objAnswerEdit.getIsright() != null && objAnswerEdit.getIsright().equals("Y")) {
						chkRight.setDisabled(false);
						chkRight.setChecked(true);
					} else {
						chkRight.setChecked(false);
						if (isSetRight)
							chkRight.setDisabled(true);
						else chkRight.setDisabled(false);
					}					
					
					BindUtils.postNotifyChange(null, null, QuestionVM.this, "objAnswer");
				}
			});
			divGroup.appendChild(btEdit);

			Button btDelete = new Button("Hapus");
			btDelete.setAutodisable("self");
			btDelete.setSclass("btn btn-default btn-sm");
			btDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					Messagebox.show(Labels.getLabel("common.delete.confirm"), WebApps.getCurrent().getAppName(),
							Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

								@Override
								public void onEvent(Event event) throws Exception {
									if (event.getName().equals("onOK")) {
										try {
											Tquestanswer objDel = (Tquestanswer) divGroup.getAttribute("obj");
											listAnswers.remove(objDel);
											divAnswers.removeChild(divRow.getNextSibling());
											divAnswers.removeChild(divRow);
											if (objDel.getIsright() != null && objDel.getIsright().equals("Y")) {
												isSetRight = false;
												chkRight.setDisabled(false);
											}
											doResetAnswer();
											BindUtils.postNotifyChange(null, null, QuestionVM.this, "objAnswer");
											BindUtils.postNotifyChange(null, null, QuestionVM.this, "objAnswerEdit");
										} catch (Exception e) {
											Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(),
													Messagebox.OK, Messagebox.ERROR);
											e.printStackTrace();
										}
									}
								}

							});
				}
			});
			divGroup.appendChild(btDelete);

			divCol2.appendChild(divGroup);
			divRow.appendChild(divCol2);

			divAnswers.appendChild(divRow);

			divAnswers.appendChild(new HtmlNativeComponent("hr"));
			doResetAnswer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doResetAnswer() {
		objAnswer = new Tquestanswer();
		chkRight.setChecked(false);
		if (isSetRight != null && isSetRight)
			chkRight.setDisabled(true);
		objAnswerEdit = null;
		divRowEdit = null;
	}

	@Command
	@NotifyChange("objForm")
	public void doCancel() {
		doReset();
	}

	@Command
	public void doUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		try {
			UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
			Media media = event.getMedia();
			if (media instanceof org.zkoss.image.Image) {
				objForm.setQuestionimg(media.getByteData());
				img.setContent((org.zkoss.image.Image) media);
			} else {
				media = null;
				Messagebox.show("Not an image: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		if (isSetRight != null && isSetRight) {
			session = StoreHibernateUtil.openSession();
			try {
				trx = session.beginTransaction();
				objForm.setLastupdated(new Date());
				tquestDao.save(session, objForm);

				int no = 0;
				for (Tquestanswer answer : listAnswers) {
					answer.setTquest(objForm);
					answer.setAnswerno(Util.ALPHABETS[no]);
					answer.setLastupdated(new Date());
					tquestanswerDao.save(session, answer);

					if (answer.getIsright().equals("Y"))
						objForm.setRightanswer(Util.ALPHABETS[no]);

					no++;
				}

				obj.setJumlahsoal(obj.getJumlahsoal() + 1);
				oDao.save(session, obj);

				trx.commit();

				trx = session.beginTransaction();
				tquestDao.save(session, objForm);
				trx.commit();				

				doQuestLast();

				Clients.showNotification(Labels.getLabel("common.add.success"), "info", null, "middle_center", 3000);
				doReset();
			} catch (HibernateException e) {
				trx.rollback();
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
			} catch (Exception e) {
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
			} finally {
				session.close();
			}
		} else {
			Messagebox.show("Pertanyaan yang Anda buat belum memiliki jawaban benar",
					WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	@Command
	@NotifyChange("objForm")
	public void doDelete() {
		try {
			Messagebox.show(Labels.getLabel("common.delete.confirm"), WebApps.getCurrent().getAppName(),
					Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							if (event.getName().equals("onOK")) {
								try {
									session = StoreHibernateUtil.openSession();
									trx = session.beginTransaction();
									tquestDao.delete(session, objForm);
									trx.commit();
									session.close();

									Clients.showNotification(Labels.getLabel("common.delete.success"), "info", null,
											"middle_center", 3000);

									doReset();
									BindUtils.postNotifyChange(null, null, QuestionVM.this, "*");
								} catch (HibernateException e) {
									trx.rollback();
									Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
											Messagebox.ERROR);
									e.printStackTrace();
								} catch (Exception e) {
									Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK,
											Messagebox.ERROR);
									e.printStackTrace();
								}
							}
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doQuestLast() {				
		divQuestLast.getChildren().clear();		
		if (objForm.getQuestionimg() != null) {
			try {
				AImage aimage = new AImage("", objForm.getQuestionimg());
				Image img = new Image();
				img.setContent(aimage);			
				Vlayout vlayout = new Vlayout();
				vlayout.appendChild(img);
				
				Textbox tbQuest = new Textbox(objForm.getQuestiontext());
				tbQuest.setMultiline(true);
				tbQuest.setCols(70);
				tbQuest.setReadonly(true);
				int rows = objForm.getQuestiontext().length() / 75;
				long enter = Util.countOccurrencesOf(objForm.getQuestiontext(), '\n');
				enter = enter + 1;		
				rows += enter;					
				tbQuest.setRows(rows);
				
				vlayout.appendChild(tbQuest);
				divQuestLast.appendChild(vlayout);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} else {
			Textbox tbQuest = new Textbox(objForm.getQuestiontext());
			tbQuest.setMultiline(true);
			tbQuest.setCols(70);
			tbQuest.setReadonly(true);
			int rows = objForm.getQuestiontext().length() / 75;
			long enter = Util.countOccurrencesOf(objForm.getQuestiontext(), '\n');
			enter = enter + 1;		
			rows += enter;					
			tbQuest.setRows(rows);
			divQuestLast.appendChild(tbQuest);					
		}    
		
		divQuestLast.appendChild(new Separator());
		for (Tquestanswer answer : listAnswers) {
			Div divAnswer = new Div();

			Label lblAnswer = new Label(answer.getAnswerno() + ". " + answer.getAnswertext());
			if (answer.getIsright() != null && answer.getIsright().equals("Y"))
				lblAnswer.setStyle("font-weight: bold");

			divAnswer.appendChild(lblAnswer);
			divAnswer.appendChild(new HtmlNativeComponent("hr"));
			divQuestLast.appendChild(divAnswer);
		}
	}

	@Command
	public void doListQuestion() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("obj", obj);
		Window win = (Window) Executions.createComponents("/view/admin/questionlist.zul", null, map);
		win.setClosable(true);
		win.doModal();
	}

	@NotifyChange("*")
	public void doReset() {
		isSetRight = false;
		objForm = new Tquest();
		objForm.setTbanksoal(obj);
		objAnswer = new Tquestanswer();
		listAnswers = new ArrayList<>();
		divAnswers.getChildren().clear();
		chkRight.setDisabled(false);
		btnSave.setLabel(Labels.getLabel("common.save"));
		img.setSrc(null);
		doResetAnswer();		
	}

	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String questiontext = (String) ctx.getProperties("questiontext")[0].getValue();

				if (questiontext == null || "".equals(questiontext))
					this.addInvalidMessage(ctx, "questiontext", Labels.getLabel("common.validator.empty"));
			}
		};
	}

	public Validator getValidatorAnswer() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String answertext = (String) ctx.getProperties("answertext")[0].getValue();

				if (answertext == null || "".equals(answertext))
					this.addInvalidMessage(ctx, "answertext", Labels.getLabel("common.validator.empty"));
			}
		};
	}

	public Tbanksoal getObj() {
		return obj;
	}

	public void setObj(Tbanksoal obj) {
		this.obj = obj;
	}

	public Tquest getObjForm() {
		return objForm;
	}

	public void setObjForm(Tquest objForm) {
		this.objForm = objForm;
	}

	public Tquestanswer getObjAnswer() {
		return objAnswer;
	}

	public void setObjAnswer(Tquestanswer objAnswer) {
		this.objAnswer = objAnswer;
	}

}
