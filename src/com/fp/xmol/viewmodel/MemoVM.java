package com.fp.xmol.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.TexammemoDAO;
import com.fp.xmol.domain.Muser;
import com.fp.xmol.domain.Texam;
import com.fp.xmol.domain.Texammemo;

public class MemoVM {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Muser oUser;
	
	private Session session;
	private Transaction trx;
	private TexammemoDAO oDao = new TexammemoDAO();
	
	private Texam obj;
	private Texammemo objForm;
	private String status;
	private String memo;
	
	private SimpleDateFormat datetimeLocalFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	@Wire
	private Grid grid;
		
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Texam obj) {		
		Selectors.wireComponents(view, this, false);
		this.obj = obj;
		oUser = (Muser) zkSession.getAttribute("oUser");
		
		grid.setRowRenderer(new RowRenderer<Texammemo>() {

			@Override
			public void render(Row row, Texammemo data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(++index)));
				row.getChildren().add(new Label(data.getMemo()));
				row.getChildren().add(new Label(datetimeLocalFormatter.format(data.getMemotime())));
				row.getChildren().add(new Label(data.getMemoby()));
			}
		});
		
		doReset();
	}
	
	private void doRefreshModel() {
		try {
			grid.setModel(new ListModelList<>(oDao.listByFilter("texam.texampk = " + obj.getTexampk(), "texammemopk")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doReset() {
		objForm = new Texammemo();
		objForm.setTexam(obj);
		doRefreshModel();
	}
	
	@Command
	@NotifyChange("*")
	public void doSave() {
		session = StoreHibernateUtil.openSession();
		try {
			trx = session.beginTransaction();
			objForm.setMemotime(new Date());
			objForm.setMemoby(oUser.getUsername());
			oDao.save(session, objForm);
			trx.commit();
			Clients.showNotification(Labels.getLabel("common.add.success"), "info", null, "middle_center", 3000);			
			doReset();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}		
	
	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String memo = (String) ctx.getProperties("memo")[0].getValue();
				
				if (memo == null || "".equals(memo))
					this.addInvalidMessage(ctx, "memo", Labels.getLabel("common.validator.empty"));				
			}
		};
	}

	public Texammemo getObjForm() {
		return objForm;
	}

	public void setObjForm(Texammemo objForm) {
		this.objForm = objForm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
