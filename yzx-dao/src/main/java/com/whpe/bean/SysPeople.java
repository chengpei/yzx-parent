package com.whpe.bean;

import java.math.BigDecimal;
import java.util.Date;

public class SysPeople {
    private String puPeopleId;

    private String puAppId;

    private String puRealName;

    private String puNickname;

    private String puSex;

    private String puIcon;

    private Date puCreatedate;

    private String puCreateid;

    private Date puUpdatedate;

    private String puUpdateid;

    private String puAddress;

    private BigDecimal puProvince;

    private BigDecimal puCity;

    private BigDecimal puDistrict;

    private BigDecimal puStreet;

    private String puRemark;

    private String uId;

    private long bonusPoint;

    public String getPuPeopleId() {
        return puPeopleId;
    }

    public void setPuPeopleId(String puPeopleId) {
        this.puPeopleId = puPeopleId == null ? null : puPeopleId.trim();
    }

    public String getPuAppId() {
        return puAppId;
    }

    public void setPuAppId(String puAppId) {
        this.puAppId = puAppId == null ? null : puAppId.trim();
    }

    public String getPuRealName() {
        return puRealName;
    }

    public void setPuRealName(String puRealName) {
        this.puRealName = puRealName == null ? null : puRealName.trim();
    }

    public String getPuNickname() {
        return puNickname;
    }

    public void setPuNickname(String puNickname) {
        this.puNickname = puNickname == null ? null : puNickname.trim();
    }

    public String getPuSex() {
        return puSex;
    }

    public void setPuSex(String puSex) {
        this.puSex = puSex == null ? null : puSex.trim();
    }

    public String getPuIcon() {
        return puIcon;
    }

    public void setPuIcon(String puIcon) {
        this.puIcon = puIcon == null ? null : puIcon.trim();
    }

    public Date getPuCreatedate() {
        return puCreatedate;
    }

    public void setPuCreatedate(Date puCreatedate) {
        this.puCreatedate = puCreatedate;
    }

    public String getPuCreateid() {
        return puCreateid;
    }

    public void setPuCreateid(String puCreateid) {
        this.puCreateid = puCreateid == null ? null : puCreateid.trim();
    }

    public Date getPuUpdatedate() {
        return puUpdatedate;
    }

    public void setPuUpdatedate(Date puUpdatedate) {
        this.puUpdatedate = puUpdatedate;
    }

    public String getPuUpdateid() {
        return puUpdateid;
    }

    public void setPuUpdateid(String puUpdateid) {
        this.puUpdateid = puUpdateid == null ? null : puUpdateid.trim();
    }

    public String getPuAddress() {
        return puAddress;
    }

    public void setPuAddress(String puAddress) {
        this.puAddress = puAddress == null ? null : puAddress.trim();
    }

    public BigDecimal getPuProvince() {
        return puProvince;
    }

    public void setPuProvince(BigDecimal puProvince) {
        this.puProvince = puProvince;
    }

    public BigDecimal getPuCity() {
        return puCity;
    }

    public void setPuCity(BigDecimal puCity) {
        this.puCity = puCity;
    }

    public BigDecimal getPuDistrict() {
        return puDistrict;
    }

    public void setPuDistrict(BigDecimal puDistrict) {
        this.puDistrict = puDistrict;
    }

    public BigDecimal getPuStreet() {
        return puStreet;
    }

    public void setPuStreet(BigDecimal puStreet) {
        this.puStreet = puStreet;
    }

    public String getPuRemark() {
        return puRemark;
    }

    public void setPuRemark(String puRemark) {
        this.puRemark = puRemark == null ? null : puRemark.trim();
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public long getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(long bonusPoint) {
        this.bonusPoint = bonusPoint;
    }
}