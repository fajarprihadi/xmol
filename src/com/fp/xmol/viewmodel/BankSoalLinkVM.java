package com.fp.xmol.viewmodel;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.dao.TbanksoalDAO;
import com.fp.xmol.domain.Tbanksoal;
import com.fp.xmol.util.Util;

public class BankSoalLinkVM {
	
	private TbanksoalDAO oDao = new TbanksoalDAO();
	
	private Tbanksoal obj;
	private String linkid;
	private String examlink;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tbanksoal obj) {
    	Selectors.wireComponents(view, this, false);
    	this.obj = obj;    	
    	try {
    		Properties prop = new Properties();
    		String propFilename = "config.properties";
    		
    		InputStream input = getClass().getClassLoader().getResourceAsStream(propFilename);
    		if (input != null) {
    			prop.load(input);
    			examlink = prop.getProperty("examlink");
    			System.out.println(examlink);
    		} else {
    			throw new FileNotFoundException("property file '" + propFilename + "' not found in the classpath");
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	linkid = examlink + obj.getLinkid();    	
    }
    
    @Command
    @NotifyChange("*")
    public void doGenerate() {
    	Session session = StoreHibernateUtil.openSession();
    	Transaction trx = session.beginTransaction();
    	try {
    		linkid = Util.keyGenerator();
    		obj.setLinkid(linkid);
    		oDao.save(session, obj);
    		trx.commit();
    		Clients.showNotification("Re-Generate Link berhasil", "info", null, "middle_center", 3000);
    		linkid = examlink + obj.getLinkid();
    	} catch (Exception e) {
    		e.printStackTrace();
		} finally {
			session.close();
		}    	
    }
    

	public Tbanksoal getObj() {
		return obj;
	}

	public void setObj(Tbanksoal obj) {
		this.obj = obj;
	}

	public String getLinkid() {
		return linkid;
	}

	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}
    

}
