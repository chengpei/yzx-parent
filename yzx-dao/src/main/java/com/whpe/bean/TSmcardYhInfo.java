package com.whpe.bean;

import java.util.Date;

public class TSmcardYhInfo {
    private String klx;

    private String sfzhm;

    private String xm;

    private String zt;

    private String smkno;

    private String schoolname;

    private String gradename;

    private String jkkgj;

    private Date addtime;

    private String fwm;

    private String fxkh;

    public String getKlx() {
        return klx;
    }

    public void setKlx(String klx) {
        this.klx = klx == null ? null : klx.trim();
    }

    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm == null ? null : sfzhm.trim();
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm == null ? null : xm.trim();
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt == null ? null : zt.trim();
    }

    public String getSmkno() {
        return smkno;
    }

    public void setSmkno(String smkno) {
        this.smkno = smkno == null ? null : smkno.trim();
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname == null ? null : schoolname.trim();
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename == null ? null : gradename.trim();
    }

    public String getJkkgj() {
        return jkkgj;
    }

    public void setJkkgj(String jkkgj) {
        this.jkkgj = jkkgj == null ? null : jkkgj.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getFwm() {
        return fwm;
    }

    public void setFwm(String fwm) {
        this.fwm = fwm == null ? null : fwm.trim();
    }

    public String getFxkh() {
        return fxkh;
    }

    public void setFxkh(String fxkh) {
        this.fxkh = fxkh == null ? null : fxkh.trim();
    }
}