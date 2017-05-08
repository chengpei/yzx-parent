package com.whpe.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Nhrequestresult {
    private String trxtype;

    private String orderno;

    private Double amount;

    private String batchno;

    private String voucherno;

    private Date hostdatetime;

    private String merchantremarks;

    private String paytype;

    private String notifytype;

    private String irspref;

    private String returncode;

    private String errormessage;

    public String getTrxtype() {
        return trxtype;
    }

    public void setTrxtype(String trxtype) {
        this.trxtype = trxtype == null ? null : trxtype.trim();
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno == null ? null : batchno.trim();
    }

    public String getVoucherno() {
        return voucherno;
    }

    public void setVoucherno(String voucherno) {
        this.voucherno = voucherno == null ? null : voucherno.trim();
    }

    public Date getHostdatetime() {
        return hostdatetime;
    }

    public void setHostdatetime(Date hostdatetime) {
        this.hostdatetime = hostdatetime;
    }

    public String getMerchantremarks() {
        return merchantremarks;
    }

    public void setMerchantremarks(String merchantremarks) {
        this.merchantremarks = merchantremarks == null ? null : merchantremarks.trim();
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype == null ? null : paytype.trim();
    }

    public String getNotifytype() {
        return notifytype;
    }

    public void setNotifytype(String notifytype) {
        this.notifytype = notifytype == null ? null : notifytype.trim();
    }

    public String getIrspref() {
        return irspref;
    }

    public void setIrspref(String irspref) {
        this.irspref = irspref == null ? null : irspref.trim();
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode == null ? null : returncode.trim();
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage == null ? null : errormessage.trim();
    }
}