package com.fp.xmol.viewmodel;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;

import com.fp.xmol.util.AppUtil;

public class PendingReqVM {
	
	private String title;
	
	@Wire
	private Div tabPage;
	
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view) {		
		Selectors.wireComponents(view, this, false);
		doTab("interview");
	}
	
	@Command
	@NotifyChange("title")
    public void doTab(@BindingParam("tab") String tab) {
    	tabPage.getChildren().clear();
		Map<String, Object> map = new HashMap<>();	
		map.put("isAdmin", new Boolean(true));
    	if (tab.equals("interview")) {
    		map.put("statusreq", AppUtil.STATUS_MENUNGGUINTERVIEW);  
    		title = "Pending Interview";
    	} else if (tab.equals("psikotes")) {
    		map.put("statusreq", AppUtil.STATUS_MENUNGGUPSIKOTES);
    		title = "Pending Psikotes";
    	} else if (tab.equals("rekrutmen")) {
    		map.put("statusreq", AppUtil.STATUS_MENUNGGUREKRUT);
    		title = "Pending Rekrutmen";
    	}
    	Executions.createComponents("/view/exam/examresult.zul", tabPage, map);
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
