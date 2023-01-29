package com.fp.xmol.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Desktop;

import com.fp.xmol.navigation.NavigationModel;

public class MainVM {

    public static final String NAVIGATION = "navigation";
    private NavigationModel navigationModel;

    @Init
    public void init(@ContextParam(ContextType.DESKTOP) Desktop desktop){
        navigationModel = new NavigationModel();
        desktop.setAttribute(NAVIGATION, navigationModel);
    }

    public String getContentUrl() {
        return navigationModel.getContentUrl();
    }

    public NavigationModel getNavigationModel() {
        return navigationModel;
    }
}
