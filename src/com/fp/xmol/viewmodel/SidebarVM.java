package com.fp.xmol.viewmodel;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.fp.xmol.domain.Muser;
import com.fp.xmol.navigation.*;

import java.util.List;

import static com.fp.xmol.viewmodel.MainVM.NAVIGATION;

public class SidebarVM {
	private Session zkSession = Sessions.getCurrent();
    private NavigationModel navigationModel;
    private Muser oUser;
    private List<Menu> menuList;
    private boolean collapsed = false; //sidebar is collapsed for narrow screen

    @Init
    public void init(@ScopeParam(NAVIGATION)NavigationModel navModel){
    	oUser = (Muser) zkSession.getAttribute("oUser");
    	if (oUser == null)
    		Executions.sendRedirect("/timeout.zul");
    	else {
    		navigationModel = navModel;
            menuList = NavDao.queryMenu();
    	}            
    }

    @Command
    public void navigate(@BindingParam("menu")Menu menu){
        String targetPath = menu.getPath();
        if (!targetPath.equals(NavigationModel.LOGOUT_ZUL)) {
            navigationModel.setContentUrl(targetPath);
            BindUtils.postNotifyChange(null, null, navigationModel, "contentUrl");
        } else {
        	Executions.sendRedirect(targetPath);
        }
    }

    // medium breakpoint 768 + 190 (sidebar width)
    @MatchMedia("all and (min-width: 958px)")
    @NotifyChange("collapsed")
    public void beWide(){
        collapsed = false;
    }

    @MatchMedia("all and (max-width: 957px)")
    @NotifyChange("collapsed")
    public void beNarrow(){
        collapsed = true;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public NavigationModel getNavigationModel() {
        return navigationModel;
    }

	public Muser getoUser() {
		return oUser;
	}

	public void setoUser(Muser oUser) {
		this.oUser = oUser;
	}
    
}
