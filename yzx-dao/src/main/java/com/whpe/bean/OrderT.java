package com.whpe.bean;

import java.util.Date;

public class OrderT {
    private String orderId;

    private String userId;

    private Long orderMoney;

    private Long realGetMoney;

    private String priceType;

    private Date createTime;

    private String state;

    private String remark;

    private String payFlag;

    private String payState;

    private String receivePerson;

    private String receivePhone;

    private String receiveAddress;

    private Short sendState;

    private String expressNo;

    private String sendPerson;

    private Short evaluateFlag;

    private Short receiveFlag;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Long getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(Long orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Long getRealGetMoney() {
        return realGetMoney;
    }

    public void setRealGetMoney(Long realGetMoney) {
        this.realGetMoney = realGetMoney;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType == null ? null : priceType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag == null ? null : payFlag.trim();
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState == null ? null : payState.trim();
    }

    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson == null ? null : receivePerson.trim();
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone == null ? null : receivePhone.trim();
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress == null ? null : receiveAddress.trim();
    }

    public Short getSendState() {
        return sendState;
    }

    public void setSendState(Short sendState) {
        this.sendState = sendState;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo == null ? null : expressNo.trim();
    }

    public String getSendPerson() {
        return sendPerson;
    }

    public void setSendPerson(String sendPerson) {
        this.sendPerson = sendPerson == null ? null : sendPerson.trim();
    }

    public Short getEvaluateFlag() {
        return evaluateFlag;
    }

    public void setEvaluateFlag(Short evaluateFlag) {
        this.evaluateFlag = evaluateFlag;
    }

    public Short getReceiveFlag() {
        return receiveFlag;
    }

    public void setReceiveFlag(Short receiveFlag) {
        this.receiveFlag = receiveFlag;
    }
}