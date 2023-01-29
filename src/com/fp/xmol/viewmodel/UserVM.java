package com.fp.xmol.viewmodel;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.fp.utils.SysUtils;
import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.MuserDAO;
import com.fp.xmol.domain.Muser;

public class UserVM {
	
	private Session session;
	private Transaction trx;
	private MuserDAO oDao = new MuserDAO();
	
	private Muser objForm;
	
	private boolean isInsert;
	private String filter;
	
	@Wire
	private Button btnSave;
	@Wire
	private Button btnCancel;
	@Wire
	private Button btnDelete;
	@Wire
	private Listbox listbox;
	@Wire
	private Textbox tbUserid;
	@Wire
	private Textbox tbPass;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component	 view){
    	Selectors.wireComponents(view, this, false);
    	
    	listbox.setItemRenderer(new ListitemRenderer<Muser>() {

			@Override
			public void render(Listitem item, final Muser data, int index)
					throws Exception {
				Listcell cell = new Listcell(String.valueOf(index + 1));
				item.appendChild(cell);
				cell = new Listcell(data.getUserid());
				item.appendChild(cell);
				cell = new Listcell(data.getUsername());
				item.appendChild(cell);
				
				Button btResetPass = new Button("Reset Password");
				btResetPass.setAutodisable("self");
				btResetPass.setSclass("btn btn-default btn-sm");
				btResetPass.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Anda ingin melakukan reset password atas user " + data.getUserid() + "?", WebApps.getCurrent().getAppName(),
								Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

									@Override
									public void onEvent(Event event) throws Exception {
										if (event.getName().equals("onOK")) {
											Session session = StoreHibernateUtil.openSession();
											Transaction trx = session.beginTransaction();
											try {
												objForm.setPassword(SysUtils.encryptionCommand(SysUtils.USERS_PASSWORD_DEFAULT));
												oDao.save(session, data);
												trx.commit();
												
												Clients.showNotification("Reset password berhasil", "info", null,
														"middle_center", 3000);
											} catch (Exception e) {
												e.printStackTrace();
											} finally {
												session.close();
											}
										}
									}
						});
					}
				});	
				cell = new Listcell();
				cell.appendChild(btResetPass);
				item.appendChild(cell);												
			}
		});
    	
    	listbox.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (listbox.getSelectedIndex() != -1) {
					tbUserid.setReadonly(true);
					tbPass.setDisabled(true);					
					
					isInsert = false;
					btnSave.setLabel(Labels.getLabel("common.update"));
					btnCancel.setDisabled(false);
					btnDelete.setDisabled(false);					
				}
			}
		});
    	
    	doReset();
    }
    
    @NotifyChange("pageTotalSize")
	public void refreshModel() {	
    	try {
    		listbox.setModel(new ListModelList<>(oDao.listByFilter(filter, "username")));
    	} catch (Exception e) {
    		e.printStackTrace();
		}		
	}
    
    @Command
	@NotifyChange("pageTotalSize")
	public void doSearch() {
    	refreshModel();
    }
    
    @Command
	@NotifyChange("objForm")
	public void doCancel() {
		doReset();
	}
    
    @Command
	@NotifyChange("objForm")
	public void doSave() {
		try {
			session = StoreHibernateUtil.openSession();
			trx = session.beginTransaction();
			if (isInsert) {
				objForm.setPassword(SysUtils.encryptionCommand(objForm.getPassword()));
			}
			oDao.save(session, objForm);
			trx.commit();
			session.close();
			if (isInsert) {
				Clients.showNotification(Labels.getLabel("common.add.success"), "info", null, "middle_center", 3000);
			} else
				Clients.showNotification(Labels.getLabel("common.update.success"), "info", null, "middle_center", 3000);
			doReset();
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
									oDao.delete(session, objForm);
									trx.commit();
									session.close();

									Clients.showNotification(Labels.getLabel("common.delete.success"), "info", null,
											"middle_center", 3000);

									doReset();
									BindUtils.postNotifyChange(null, null, UserVM.this, "*");
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
    
    @NotifyChange("*")
	public void doReset() {
		isInsert = true;
		objForm = new Muser();
		btnCancel.setDisabled(true);
		btnDelete.setDisabled(true);
		btnSave.setLabel(Labels.getLabel("common.save"));
		tbUserid.setReadonly(false);
		tbPass.setDisabled(false);		
		doSearch();
	}
    
    public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String userid = (String) ctx.getProperties("userid")[0].getValue();
				String username = (String) ctx.getProperties("username")[0].getValue();
				String password = (String) ctx.getProperties("password")[0].getValue();
				
				if (userid == null || "".equals(userid))
					this.addInvalidMessage(ctx, "userid", Labels.getLabel("common.validator.empty"));
				if (username == null || "".equals(username))
					this.addInvalidMessage(ctx, "username", Labels.getLabel("common.validator.empty"));
				if (password == null || "".equals(password))
					this.addInvalidMessage(ctx, "password", Labels.getLabel("common.validator.empty"));					
			}
		};
	}

	public Muser getObjForm() {
		return objForm;
	}

	public void setObjForm(Muser objForm) {
		this.objForm = objForm;
	}

}
