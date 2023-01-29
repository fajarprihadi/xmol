package com.fp.xmol.viewmodel;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;

import com.fp.utils.SysUtils;
import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.MuserDAO;
import com.fp.xmol.domain.Muser;

public class AuthentificationVm {
	
	private String userid;
	private String password;
	private String lblMessage;
		
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange("lblMessage")
	public void doLogin() {	
		Session session = null;
		try {											
			if (userid != null && !userid.trim().equals("") && password != null && !password.trim().equals("")) {
				session = StoreHibernateUtil.openSession();
				Muser oForm = new MuserDAO().login(session, userid);
				if (oForm != null) {					
					if (password != null) password = password.trim();								
					String passencrypted = SysUtils.encryptionCommand(password);
					if (oForm.getPassword().equals(passencrypted)) {
						Transaction trx = session.beginTransaction();
						try {
							oForm.setLastlogin(new Date());
							trx.commit();
						} catch (Exception e) {
							lblMessage = e.getMessage();
							e.printStackTrace();
						} 						
						Sessions.getCurrent().setAttribute("oUser", oForm);						
						Executions.sendRedirect("/view/index.zul");
					} else {
						lblMessage = "Invalid your password";
					}				
				} else {
					lblMessage = "Invalid your login id";
				}							
				session.close();
			}			
		} catch (Exception e) {
			lblMessage = e.getMessage();
			e.printStackTrace();
		}		
	}
		
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getLblMessage() {
		return lblMessage;
	}

	public void setLblMessage(String lblMessage) {
		this.lblMessage = lblMessage;
	}
	
}
