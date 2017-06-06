package com.whpe.bean;

import java.util.Date;

public class LeaseVouchers {
    private String id;

    private String orderId;

    private String userId;

    private String vouchers;

    private Date createTime;

    private Date endTime;

    private String useObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

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

    public String getVouchers() {
        return vouchers;
    }

    public void setVouchers(String vouchers) {
        this.vouchers = vouchers == null ? null : vouchers.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUseObject() {
        return useObject;
    }

    public void setUseObject(String useObject) {
        this.useObject = useObject == null ? null : useObject.trim();
    }
}