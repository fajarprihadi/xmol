package com.fp.xmol.viewmodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;

import com.fp.utils.SysUtils;
import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.TcounterengineDAO;
import com.fp.xmol.dao.TexamDAO;
import com.fp.xmol.dao.TexamdocDAO;
import com.fp.xmol.domain.Texam;
import com.fp.xmol.domain.Texamdoc;

public class ExamDocVM {
	
	private Session session;
	private Transaction trx;
	private TexamdocDAO oDao = new TexamdocDAO();
	private TexamDAO texamDao = new TexamDAO();
	
	private Texam obj;
	private Texamdoc objForm;
	private String docsrc;
	
	private List<Media> listMedia;
	
	@Wire
	private Div divFiles;
	@Wire
	private Listbox listbox;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Texam obj) {
    	Selectors.wireComponents(view, this, false);
    	this.obj = obj;
    	listbox.setItemRenderer(new ListitemRenderer<Texamdoc>() {

			@Override
			public void render(Listitem item, final Texamdoc data, int index)
					throws Exception {
				Listcell cell = new Listcell(data.getDocfilename());
				item.appendChild(cell);				
				
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
												session = StoreHibernateUtil.openSession();
												trx = session.beginTransaction();
												oDao.delete(session, data);
												
												obj.setTotaldoc(obj.getTotaldoc() - 1);
												texamDao.save(session, obj);
												
												trx.commit();
												session.close();

												Clients.showNotification(Labels.getLabel("common.delete.success"), "info", null,
														"middle_center", 3000);

												doReset();
												BindUtils.postNotifyChange(null, null, ExamDocVM.this, "*");
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
					}
				});	
				cell = new Listcell();
				cell.appendChild(btDelete);
				item.appendChild(cell);
			}
		});
    	
    	listbox.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (listbox.getSelectedIndex() != -1) {
					docsrc = SysUtils.FILES_ROOT_PATH + SysUtils.DOC_PATH + "/" + objForm.getDocid();
					BindUtils.postNotifyChange(null, null, ExamDocVM.this, "*");
				}
			}
		});
    	doReset();
    }
    
    @NotifyChange("pageTotalSize")
	public void refreshModel() {	
    	try {
    		listbox.setModel(new ListModelList<>(oDao.listByFilter("texam.texampk = " + obj.getTexampk(), "docname")));
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
	public void doSave() {
		try {
			if (listMedia.size() > 0) {
				session = StoreHibernateUtil.openSession();
				trx = session.beginTransaction();	
				for (Media media: listMedia) {
					Texamdoc objForm = new Texamdoc();
					objForm.setTexam(obj);
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
					
					objForm.setDocid(docid);
					objForm.setDocfilename(media.getName());
					objForm.setUploadtime(new Date());
					oDao.save(session, objForm);
				}			
				
				obj.setTotaldoc(obj.getTotaldoc() + listMedia.size());
				texamDao.save(session, obj);
				
				trx.commit();
				
				session.close();
							
				Clients.showNotification(Labels.getLabel("common.add.success"), "info", null, "middle_center", 3000);
				doReset();
			} else {
				Messagebox.show("Tidak ada dokumen yang diupload. Silahkan upload dokumen.", WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
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
    
    @NotifyChange("*")
	public void doReset() {
    	listMedia = new ArrayList<>();
    	divFiles.getChildren().clear();
		doSearch();
	}

	public Texam getObj() {
		return obj;
	}

	public void setObj(Texam obj) {
		this.obj = obj;
	}

	public Texamdoc getObjForm() {
		return objForm;
	}

	public void setObjForm(Texamdoc objForm) {
		this.objForm = objForm;
	}

	public String getDocsrc() {
		return docsrc;
	}

	public void setDocsrc(String docsrc) {
		this.docsrc = docsrc;
	}
}
