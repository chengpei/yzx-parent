package com.whpe.bean;

import java.util.Date;

public class OrderDetailT {
    private String orderDetailId;

    private String orderId;

    private String productOfferId;

    private Integer offerNumber;

    private Long singlePrice;

    private Long totalPrice;

    private Date createTime;

    private String state;

    private Long settleAccountPrice;

    private Short evaluateFlag;

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId == null ? null : orderDetailId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getProductOfferId() {
        return productOfferId;
    }

    public void setProductOfferId(String productOfferId) {
        this.productOfferId = productOfferId == null ? null : productOfferId.trim();
    }

    public Integer getOfferNumber() {
        return offerNumber;
    }

    public void setOfferNumber(Integer offerNumber) {
        this.offerNumber = offerNumber;
    }

    public Long getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(Long singlePrice) {
        this.singlePrice = singlePrice;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
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

    public Long getSettleAccountPrice() {
        return settleAccountPrice;
    }

    public void setSettleAccountPrice(Long settleAccountPrice) {
        this.settleAccountPrice = settleAccountPrice;
    }

    public Short getEvaluateFlag() {
        return evaluateFlag;
    }

    public void setEvaluateFlag(Short evaluateFlag) {
        this.evaluateFlag = evaluateFlag;
    }
}