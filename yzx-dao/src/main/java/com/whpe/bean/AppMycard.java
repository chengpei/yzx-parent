package com.whpe.bean;

public class AppMycard {
    private String appUserid;

    private String appFxkh;

    private String appBz;

    private String appCreatedate;

    private String appCreateid;

    private String appUpdatedate;

    private String appUpdateid;

    public String getAppUserid() {
        return appUserid;
    }

    public void setAppUserid(String appUserid) {
        this.appUserid = appUserid == null ? null : appUserid.trim();
    }

    public String getAppFxkh() {
        return appFxkh;
    }

    public void setAppFxkh(String appFxkh) {
        this.appFxkh = appFxkh == null ? null : appFxkh.trim();
    }

    public String getAppBz() {
        return appBz;
    }

    public void setAppBz(String appBz) {
        this.appBz = appBz == null ? null : appBz.trim();
    }

    public String getAppCreatedate() {
        return appCreatedate;
    }

    public void setAppCreatedate(String appCreatedate) {
        this.appCreatedate = appCreatedate == null ? null : appCreatedate.trim();
    }

    public String getAppCreateid() {
        return appCreateid;
    }

    public void setAppCreateid(String appCreateid) {
        this.appCreateid = appCreateid == null ? null : appCreateid.trim();
    }

    public String getAppUpdatedate() {
        return appUpdatedate;
    }

    public void setAppUpdatedate(String appUpdatedate) {
        this.appUpdatedate = appUpdatedate == null ? null : appUpdatedate.trim();
    }

    public String getAppUpdateid() {
        return appUpdateid;
    }

    public void setAppUpdateid(String appUpdateid) {
        this.appUpdateid = appUpdateid == null ? null : appUpdateid.trim();
    }
}