package com.fp.xmol.navigation;

import java.util.*;

public class NavDao {
    private static List<Menu> menuList = new LinkedList<>();

    static{
        initMenus();
    }

    static public void initMenus(){
        Menu mAdmin = new Menu("Admin", "z-icon-home");
        //menuD.setCounter(Util.nextInt(1, 10));
        Menu msUser = new Menu("User");
        msUser.setPath(NavigationModel.ADMIN_USER_ZUL);
        Menu msDept = new Menu("Departemen");
        msDept.setPath(NavigationModel.ADMIN_DEPT_ZUL);
        Menu msBankSoal = new Menu("Bank Soal");
        msBankSoal.setPath(NavigationModel.ADMIN_BANKSOAL_ZUL);
        List<Menu> subMadmin = new ArrayList<>();
        subMadmin.add(msUser);
        subMadmin.add(msDept);
        subMadmin.add(msBankSoal);
        mAdmin.setSubMenus(subMadmin);
        menuList.add(mAdmin);
        
        Menu mEvaluasi = new Menu("Evaluasi", "z-icon-flag-o");
        Menu mKandidat = new Menu("Daftar Kandidat");
        mKandidat.setPath(NavigationModel.KANDIDAT_ZUL);
        Menu mPending = new Menu("Pending Request");
        mPending.setPath(NavigationModel.PENDINGREQ_ZUL);
        List<Menu> subMevaluasi = new ArrayList<>();
        subMevaluasi.add(mKandidat);
        subMevaluasi.add(mPending);
        mEvaluasi.setSubMenus(subMevaluasi);
        menuList.add(mEvaluasi);
        
        Menu mLogout = new Menu("Logout", "z-icon-power-off");
        mLogout.setPath(NavigationModel.LOGOUT_ZUL);  
        menuList.add(mLogout);
    }

    static public List<Menu> queryMenu(){
        return menuList;
    }
}
