package com.whpe.bean;

public class Nshresresult {
    private String branchid;

    private String mercode;

    private String ordernum;

    private String orderamt;

    private String curtype;

    private String orderdate;

    private String ordertime;

    private String transerialno;

    private String tranresult;

    private String comment;

    private String merremarks;

    private String signdatastr;

    private String signdata;

    private String paytype;

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid == null ? null : branchid.trim();
    }

    public String getMercode() {
        return mercode;
    }

    public void setMercode(String mercode) {
        this.mercode = mercode == null ? null : mercode.trim();
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum == null ? null : ordernum.trim();
    }

    public String getOrderamt() {
        return orderamt;
    }

    public void setOrderamt(String orderamt) {
        this.orderamt = orderamt == null ? null : orderamt.trim();
    }

    public String getCurtype() {
        return curtype;
    }

    public void setCurtype(String curtype) {
        this.curtype = curtype == null ? null : curtype.trim();
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate == null ? null : orderdate.trim();
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime == null ? null : ordertime.trim();
    }

    public String getTranserialno() {
        return transerialno;
    }

    public void setTranserialno(String transerialno) {
        this.transerialno = transerialno == null ? null : transerialno.trim();
    }

    public String getTranresult() {
        return tranresult;
    }

    public void setTranresult(String tranresult) {
        this.tranresult = tranresult == null ? null : tranresult.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getMerremarks() {
        return merremarks;
    }

    public void setMerremarks(String merremarks) {
        this.merremarks = merremarks == null ? null : merremarks.trim();
    }

    public String getSigndatastr() {
        return signdatastr;
    }

    public void setSigndatastr(String signdatastr) {
        this.signdatastr = signdatastr == null ? null : signdatastr.trim();
    }

    public String getSigndata() {
        return signdata;
    }

    public void setSigndata(String signdata) {
        this.signdata = signdata == null ? null : signdata.trim();
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype == null ? null : paytype.trim();
    }
}