package com.fp.xmol.viewmodel;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.zkoss.zk.ui.Executions;
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
import org.zkoss.zul.Window;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.MdepartmentDAO;
import com.fp.xmol.dao.MdepartmentDAO;
import com.fp.xmol.domain.Mdepartment;
import com.fp.xmol.util.Util;
import com.fp.xmol.domain.Mdepartment;

public class DeptVM {
	
	private Session session;
	private Transaction trx;
	private MdepartmentDAO oDao = new MdepartmentDAO();
	
	private Mdepartment objForm;
	
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

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component	 view){
    	Selectors.wireComponents(view, this, false);
    	
    	listbox.setItemRenderer(new ListitemRenderer<Mdepartment>() {

			@Override
			public void render(Listitem item, final Mdepartment data, int index)
					throws Exception {
				Listcell cell = new Listcell(String.valueOf(index + 1));
				item.appendChild(cell);
				cell = new Listcell(data.getDeptcode());
				item.appendChild(cell);
				cell = new Listcell(data.getDeptname());
				item.appendChild(cell);
				cell = new Listcell(data.getPicname());
				item.appendChild(cell);
				cell = new Listcell(data.getPichp());
				item.appendChild(cell);
				cell = new Listcell(data.getPicemail());
				item.appendChild(cell);								
			}
		});
    	
    	listbox.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (listbox.getSelectedIndex() != -1) {
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
    		listbox.setModel(new ListModelList<>(oDao.listByFilter(filter, "deptcode")));
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
									BindUtils.postNotifyChange(null, null, DeptVM.this, "*");
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
		objForm = new Mdepartment();
		btnCancel.setDisabled(true);
		btnDelete.setDisabled(true);
		btnSave.setLabel(Labels.getLabel("common.save"));
		doSearch();
	}
    
    public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String deptcode = (String) ctx.getProperties("deptcode")[0].getValue();
				String deptname = (String) ctx.getProperties("deptname")[0].getValue();
				String picname = (String) ctx.getProperties("picname")[0].getValue();
				String pichp = (String) ctx.getProperties("pichp")[0].getValue();
				String picemail = (String) ctx.getProperties("picemail")[0].getValue();
				
				if (deptcode == null || "".equals(deptcode))
					this.addInvalidMessage(ctx, "deptcode", Labels.getLabel("common.validator.empty"));
				if (deptname == null || "".equals(deptname))
					this.addInvalidMessage(ctx, "deptname", Labels.getLabel("common.validator.empty"));
				if (picname == null || "".equals(picname))
					this.addInvalidMessage(ctx, "picname", Labels.getLabel("common.validator.empty"));
				if (pichp == null || "".equals(pichp))
					this.addInvalidMessage(ctx, "pichp", Labels.getLabel("common.validator.empty"));
				if (picemail == null || "".equals(picemail))
					this.addInvalidMessage(ctx, "picemail", Labels.getLabel("common.validator.empty"));
				else if (!Util.emailValidator(picemail))
					this.addInvalidMessage(ctx, "picemail", "Invalid format e-mail");
					
			}
		};
	}

	public Mdepartment getObjForm() {
		return objForm;
	}

	public void setObjForm(Mdepartment objForm) {
		this.objForm = objForm;
	}

}
