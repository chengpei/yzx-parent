package com.whpe.bean;

import java.util.Date;

public class TSimIc {
    private String iccid;

    private String fxkh;

    private String wlkh;

    private Date addtime;

    private String wlkh2;

    private String str09;

    private String uFlag;

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid == null ? null : iccid.trim();
    }

    public String getFxkh() {
        return fxkh;
    }

    public void setFxkh(String fxkh) {
        this.fxkh = fxkh == null ? null : fxkh.trim();
    }

    public String getWlkh() {
        return wlkh;
    }

    public void setWlkh(String wlkh) {
        this.wlkh = wlkh == null ? null : wlkh.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getWlkh2() {
        return wlkh2;
    }

    public void setWlkh2(String wlkh2) {
        this.wlkh2 = wlkh2 == null ? null : wlkh2.trim();
    }

    public String getStr09() {
        return str09;
    }

    public void setStr09(String str09) {
        this.str09 = str09 == null ? null : str09.trim();
    }

    public String getuFlag() {
        return uFlag;
    }

    public void setuFlag(String uFlag) {
        this.uFlag = uFlag == null ? null : uFlag.trim();
    }
}