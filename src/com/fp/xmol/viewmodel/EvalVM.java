package com.fp.xmol.viewmodel;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Row;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.TexamDAO;
import com.fp.xmol.dao.TexammemoDAO;
import com.fp.xmol.domain.Muser;
import com.fp.xmol.domain.Texam;
import com.fp.xmol.domain.Texammemo;
import com.fp.xmol.util.AppData;
import com.fp.xmol.util.AppUtil;

public class EvalVM {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Muser oUser;
	
	private Session session;
	private Transaction trx;
	private TexamDAO oDao = new TexamDAO();
	private TexammemoDAO texammemoDao = new TexammemoDAO();
	
	private Texam objForm;
	private String status;
	private String statuseval;
	private String memo;
	private Boolean isAdmin;
	
	@Wire
	private Combobox cbStatus;
	@Wire
	private Row rowDecision;
	@Wire
	private Row rowInterview;
	@Wire
	private Row rowPsikotes;
	@Wire
	private Row rowJoin;
	@Wire
	private Row rowMemo;
	@Wire
	private Button btSave;
	@Wire
	private Datebox dtInterview;
	@Wire
	private Datebox dtJoin;
	@Wire
	private Datebox dtPsikotes;
		
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Texam obj, @ExecutionArgParam("isAdmin") Boolean isAdmin) {		
		Selectors.wireComponents(view, this, false);
		oUser = (Muser) zkSession.getAttribute("oUser");
		objForm = obj;
		statuseval = AppData.getStatusLabel(objForm.getStatus());
		this.isAdmin = isAdmin;
		if (isAdmin != null && isAdmin) {
			for (Comboitem item: AppData.getStatusComboResp(objForm.getStatus())) {
				cbStatus.appendChild(item);
			}
		} else {
			if (objForm.getIswait().equals("Y") || objForm.getIsdone().equals("Y")) {
				doCompDone();
			}
			for (Comboitem item: AppData.getStatusComboReq(objForm.getStatus())) {
				cbStatus.appendChild(item);
			}
			if (objForm.getTglinterview() != null) {
				rowInterview.setVisible(true);
				dtInterview.setReadonly(true);
				dtInterview.setButtonVisible(false);
			}
			if (objForm.getTglpsikotes() != null) {
				rowPsikotes.setVisible(true);
				dtPsikotes.setReadonly(true);
				dtPsikotes.setButtonVisible(false);
			}
			if (objForm.getTgljoin() != null) {
				rowJoin.setVisible(true);
				dtJoin.setReadonly(true);
				dtJoin.setButtonVisible(false);
			}
		}
	}
	
	private void doCompDone() {
		rowDecision.setVisible(false);
		rowMemo.setVisible(false);
		btSave.setVisible(false);
	}
	
	@Command
	public void doSelectStatus(@BindingParam("item") String item) {
		if (item != null && isAdmin != null && isAdmin) {			
			if (item.equals(AppUtil.STATUS_EVALUASIINTERVIEW))
				rowInterview.setVisible(true);
			else rowInterview.setVisible(false);
			
			if (item.equals(AppUtil.STATUS_EVALUASIPSIKOTES))
				rowPsikotes.setVisible(true);
			else rowPsikotes.setVisible(false);
			
			if (item.equals(AppUtil.STATUS_SUKSESREKRUT))
				rowJoin.setVisible(true);
			else rowJoin.setVisible(false);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doSave() {
		session = StoreHibernateUtil.openSession();
		try {
			trx = session.beginTransaction();
			objForm.setStatus(status);
			
			if (isAdmin != null && isAdmin) {
				objForm.setIswait("N");
			} else {
				objForm.setIswait("Y");
			}
			
			if (status.equals(AppUtil.STATUS_BATALINTERVIEW)) {
				objForm.setTglinterview(null);
				objForm.setIsdone("Y");
			} else if (status.equals(AppUtil.STATUS_BATALPSIKOTES)) {
				objForm.setTglpsikotes(null);
				objForm.setIsdone("Y");
			} else if (status.equals(AppUtil.STATUS_GAGALREKRUT)) {
				objForm.setTgljoin(null);
				objForm.setIsdone("Y");
			} else if (status.equals(AppUtil.STATUS_SUKSESREKRUT)) {
				objForm.setIsdone("Y");
			}
			oDao.save(session, objForm);
			
			if (memo != null && memo.trim().length() > 0) {
				Texammemo objMemo = new Texammemo();
				objMemo.setTexam(objForm);
				objMemo.setMemo(memo);
				objMemo.setMemotime(new Date());
				objMemo.setMemoby(oUser.getUsername());
				texammemoDao.save(session, objMemo);
			}
			
			trx.commit();
			Clients.showNotification("Perubahan status evaluasi berhasil", "info", null, "middle_center", 3000);	
			btSave.setDisabled(true);
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
				
				if (status == null || "".equals(status))
					this.addInvalidMessage(ctx, "status", Labels.getLabel("common.validator.empty"));
				else if (status.equals(AppUtil.STATUS_EVALUASIINTERVIEW)) {
					Date tglinterview = (Date) ctx.getProperties("tglinterview")[0].getValue();
					if (tglinterview == null)
						this.addInvalidMessage(ctx, "tglinterview", Labels.getLabel("common.validator.empty"));
				} else if (status.equals(AppUtil.STATUS_EVALUASIPSIKOTES)) {
					Date tglpsikotes = (Date) ctx.getProperties("tglpsikotes")[0].getValue();
					if (tglpsikotes == null)
						this.addInvalidMessage(ctx, "tglpsikotes", Labels.getLabel("common.validator.empty"));
				} else if (status.equals(AppUtil.STATUS_SUKSESREKRUT)) {
					Date tgljoin = (Date) ctx.getProperties("tgljoin")[0].getValue();
					if (tgljoin == null)
						this.addInvalidMessage(ctx, "tgljoin", Labels.getLabel("common.validator.empty"));
				}
				
				/*if (memo == null || "".equals(memo))
					this.addInvalidMessage(ctx, "memo", Labels.getLabel("common.validator.empty"));	*/			
			}
		};
	}

	public Texam getObjForm() {
		return objForm;
	}

	public void setObjForm(Texam objForm) {
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

	public String getStatuseval() {
		return statuseval;
	}

	public void setStatuseval(String statuseval) {
		this.statuseval = statuseval;
	}

}
