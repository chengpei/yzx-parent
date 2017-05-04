package com.whpe.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

public class NfcCardRecharge {
    private String gid;

    private String orderno;

    private String cardno;

    private String commcode;

    private String orderseq;

    private String phoneno;

    private String ordermount;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date ordertime;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date ordervalidtime;

    private String success;

    private String orderrefoundid;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date reqtime;

    private String backphone;

    private String backimei;

    private String imei;

    private String paytype;

    private String csn;

    private String mklx;

    private String zklx;

    private BigDecimal ye;

    private String tac;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date chsj;

    private String jylx;

    private String xjbz;

    private String flag;

    private String zcsdm;

    private String ccsdm;

    private BigDecimal jyxh;

    private String randomdata;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date qssj;

    private String sysrandom;

    private String ifReturn;

    private String qsflag;

    private Long cccount;

    private String backrcvresponse;

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno == null ? null : cardno.trim();
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid == null ? null : gid.trim();
    }

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode == null ? null : commcode.trim();
    }

    public String getOrderseq() {
        return orderseq;
    }

    public void setOrderseq(String orderseq) {
        this.orderseq = orderseq == null ? null : orderseq.trim();
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno == null ? null : phoneno.trim();
    }

    public String getOrdermount() {
        return ordermount;
    }

    public void setOrdermount(String ordermount) {
        this.ordermount = ordermount == null ? null : ordermount.trim();
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public Date getOrdervalidtime() {
        return ordervalidtime;
    }

    public void setOrdervalidtime(Date ordervalidtime) {
        this.ordervalidtime = ordervalidtime;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success == null ? null : success.trim();
    }

    public String getOrderrefoundid() {
        return orderrefoundid;
    }

    public void setOrderrefoundid(String orderrefoundid) {
        this.orderrefoundid = orderrefoundid == null ? null : orderrefoundid.trim();
    }

    public Date getReqtime() {
        return reqtime;
    }

    public void setReqtime(Date reqtime) {
        this.reqtime = reqtime;
    }

    public String getBackphone() {
        return backphone;
    }

    public void setBackphone(String backphone) {
        this.backphone = backphone == null ? null : backphone.trim();
    }

    public String getBackimei() {
        return backimei;
    }

    public void setBackimei(String backimei) {
        this.backimei = backimei == null ? null : backimei.trim();
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype == null ? null : paytype.trim();
    }

    public String getCsn() {
        return csn;
    }

    public void setCsn(String csn) {
        this.csn = csn == null ? null : csn.trim();
    }

    public String getMklx() {
        return mklx;
    }

    public void setMklx(String mklx) {
        this.mklx = mklx == null ? null : mklx.trim();
    }

    public String getZklx() {
        return zklx;
    }

    public void setZklx(String zklx) {
        this.zklx = zklx == null ? null : zklx.trim();
    }

    public BigDecimal getYe() {
        return ye;
    }

    public void setYe(BigDecimal ye) {
        this.ye = ye;
    }

    public String getTac() {
        return tac;
    }

    public void setTac(String tac) {
        this.tac = tac == null ? null : tac.trim();
    }

    public Date getChsj() {
        return chsj;
    }

    public void setChsj(Date chsj) {
        this.chsj = chsj;
    }

    public String getJylx() {
        return jylx;
    }

    public void setJylx(String jylx) {
        this.jylx = jylx == null ? null : jylx.trim();
    }

    public String getXjbz() {
        return xjbz;
    }

    public void setXjbz(String xjbz) {
        this.xjbz = xjbz == null ? null : xjbz.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public String getZcsdm() {
        return zcsdm;
    }

    public void setZcsdm(String zcsdm) {
        this.zcsdm = zcsdm == null ? null : zcsdm.trim();
    }

    public String getCcsdm() {
        return ccsdm;
    }

    public void setCcsdm(String ccsdm) {
        this.ccsdm = ccsdm == null ? null : ccsdm.trim();
    }

    public BigDecimal getJyxh() {
        return jyxh;
    }

    public void setJyxh(BigDecimal jyxh) {
        this.jyxh = jyxh;
    }

    public String getRandomdata() {
        return randomdata;
    }

    public void setRandomdata(String randomdata) {
        this.randomdata = randomdata == null ? null : randomdata.trim();
    }

    public Date getQssj() {
        return qssj;
    }

    public void setQssj(Date qssj) {
        this.qssj = qssj;
    }

    public String getSysrandom() {
        return sysrandom;
    }

    public void setSysrandom(String sysrandom) {
        this.sysrandom = sysrandom == null ? null : sysrandom.trim();
    }

    public String getIfReturn() {
        return ifReturn;
    }

    public void setIfReturn(String ifReturn) {
        this.ifReturn = ifReturn == null ? null : ifReturn.trim();
    }

    public String getQsflag() {
        return qsflag;
    }

    public void setQsflag(String qsflag) {
        this.qsflag = qsflag;
    }

    public Long getCccount() {
        return cccount;
    }

    public void setCccount(Long cccount) {
        this.cccount = cccount;
    }

    public String getBackrcvresponse() {
        return backrcvresponse;
    }

    public void setBackrcvresponse(String backrcvresponse) {
        this.backrcvresponse = backrcvresponse;
    }
}