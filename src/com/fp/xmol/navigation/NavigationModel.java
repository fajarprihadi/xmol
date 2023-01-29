package com.fp.xmol.navigation;

public class NavigationModel {
	public static final String ADMIN_USER_ZUL = "/view/admin/user.zul";
	public static final String ADMIN_DEPT_ZUL = "/view/admin/dept.zul";
	public static final String ADMIN_BANKSOAL_ZUL = "/view/admin/banksoal.zul";
	
	public static final String KANDIDAT_ZUL = "/view/exam/examresult.zul";
	public static final String PENDINGREQ_ZUL = "/view/exam/pendingreq.zul";
	
    public static final String DASHBOARD_ECOMMERCE_ZUL = "/ecommerce/ecommerce.zul";
    public static final String DASHBOARD_PROJECT_ZUL = "/project/project.zul";
    public static final String BLANK_ZUL = "/blank.zul";
    public static final String LOGOUT_ZUL = "/logout.zul";

    private String contentUrl = KANDIDAT_ZUL;

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
