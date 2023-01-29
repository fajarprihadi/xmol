package com.fp.xmol.viewmodel;

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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Window;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.MdepartmentDAO;
import com.fp.xmol.dao.TbanksoalDAO;
import com.fp.xmol.domain.Mdepartment;
import com.fp.xmol.domain.Tbanksoal;
import com.fp.xmol.util.Util;

public class BankSoalVM {
	
	private Session session;
	private Transaction trx;
	private TbanksoalDAO oDao = new TbanksoalDAO();
	
	private Tbanksoal objForm;
	
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
	private Combobox cbDept;
	@Wire
	private Spinner spinExamsoal;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component	 view){
    	Selectors.wireComponents(view, this, false);
    	    	
    	listbox.setItemRenderer(new ListitemRenderer<Tbanksoal>() {

			@Override
			public void render(Listitem item, final Tbanksoal data, int index)
					throws Exception {
				Listcell cell = new Listcell(String.valueOf(index + 1));
				item.appendChild(cell);
				cell = new Listcell(data.getMdepartment().getDeptcode());
				item.appendChild(cell);
				cell = new Listcell(data.getKodesoal());
				item.appendChild(cell);
				cell = new Listcell(data.getDeskripsi());
				item.appendChild(cell);				
				cell = new Listcell(String.valueOf(data.getJumlahsoal()));
				item.appendChild(cell);
				cell = new Listcell(String.valueOf(data.getExamsoal()));
				item.appendChild(cell);
				cell = new Listcell(String.valueOf(data.getDurasi()));
				item.appendChild(cell);
				cell = new Listcell(String.valueOf(data.getPassingscore()));
				item.appendChild(cell);					
				
				Button btLink = new Button("Link Id");
				btLink.setAutodisable("self");
				btLink.setSclass("btn btn-default btn-sm");
				btLink.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						Window win = (Window) Executions
								.createComponents("/view/admin/banksoallink.zul", null, map);
						win.setClosable(true);
						win.doModal();						
					}
				});	
				cell = new Listcell();
				cell.appendChild(btLink);
				item.appendChild(cell);
				
				Button btCreate = new Button("Buat Soal");
				btCreate.setAutodisable("self");
				btCreate.setSclass("btn btn-default btn-sm");
				btCreate.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						Window win = (Window) Executions
								.createComponents("/view/admin/question.zul", null, map);
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								refreshModel();
								BindUtils.postNotifyChange(null, null, BankSoalVM.this, "*");
							}
						});
					}
				});				
				
				Button btList = new Button("Daftar Soal");
				btList.setAutodisable("self");
				btList.setSclass("btn btn-default btn-sm");
				btList.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						Window win = (Window) Executions
								.createComponents("/view/admin/questionlist.zul", null, map);
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								refreshModel();
								BindUtils.postNotifyChange(null, null, BankSoalVM.this, "*");
							}
						});
					}
				});
				
				Div div = new Div();
				div.setSclass("btn-group");
				div.appendChild(btCreate);
				div.appendChild(btList);
				
				cell = new Listcell();
				cell.appendChild(div);
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
					
					cbDept.setValue(objForm.getMdepartment().getDeptcode());
					
					spinExamsoal.setConstraint("no empty,min 0 max " + objForm.getJumlahsoal());
				}
			}
		});
    	
    	doReset();
    }
    
    @NotifyChange("pageTotalSize")
	public void refreshModel() {	
    	try {
    		listbox.setModel(new ListModelList<>(oDao.listByFilter(filter, "kodesoal")));
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
				objForm.setExamsoal(0);
				objForm.setJumlahsoal(0);
				objForm.setLinkid(Util.keyGenerator());
			}			
			objForm.setLastupdated(new Date());
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
									BindUtils.postNotifyChange(null, null, BankSoalVM.this, "*");
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
		objForm = new Tbanksoal();
		cbDept.setValue(null);
		btnCancel.setDisabled(true);
		btnDelete.setDisabled(true);
		btnSave.setLabel(Labels.getLabel("common.save"));
		doSearch();
	}
        
    public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				Mdepartment mdepartment = (Mdepartment) ctx.getProperties("mdepartment")[0].getValue();
				String kodesoal = (String) ctx.getProperties("kodesoal")[0].getValue();
				String deskripsi = (String) ctx.getProperties("deskripsi")[0].getValue();
				Integer durasi = (Integer) ctx.getProperties("durasi")[0].getValue();
				Integer passingscore = (Integer) ctx.getProperties("passingscore")[0].getValue();
				Integer jumlahsoal = (Integer) ctx.getProperties("jumlahsoal")[0].getValue();
				Integer examsoal = (Integer) ctx.getProperties("examsoal")[0].getValue();
				
				if (mdepartment == null)
					this.addInvalidMessage(ctx, "mdepartment", Labels.getLabel("common.validator.empty"));
				if (kodesoal == null || "".equals(kodesoal))
					this.addInvalidMessage(ctx, "kodesoal", Labels.getLabel("common.validator.empty"));
				if (deskripsi == null || "".equals(deskripsi))
					this.addInvalidMessage(ctx, "deskripsi", Labels.getLabel("common.validator.empty"));
				if (durasi == null)
					this.addInvalidMessage(ctx, "durasi", Labels.getLabel("common.validator.empty"));	
				if (passingscore == null)
					this.addInvalidMessage(ctx, "passingscore", Labels.getLabel("common.validator.empty"));	
				
				if (jumlahsoal == null)
					jumlahsoal = 0;
				if (examsoal == null)
					examsoal = 0;
				
				if (examsoal > jumlahsoal)
					this.addInvalidMessage(ctx, "examsoal", "Angka melebihi jumlah soal");	
			}
		};
	}

	public Tbanksoal getObjForm() {
		return objForm;
	}

	public void setObjForm(Tbanksoal objForm) {
		this.objForm = objForm;
	}

	public ListModelList<Mdepartment> getMdepartment() {
		ListModelList<Mdepartment> oList = null;
		try {
			oList = new ListModelList<>(new MdepartmentDAO().listByFilter("0=0", "deptcode"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}
}
