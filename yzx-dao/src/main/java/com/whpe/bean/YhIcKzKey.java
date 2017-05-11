package com.whpe.bean;

public class YhIcKzKey {
    private String kzbh;

    private String mklx;

    public String getKzbh() {
        return kzbh;
    }

    public void setKzbh(String kzbh) {
        this.kzbh = kzbh == null ? null : kzbh.trim();
    }

    public String getMklx() {
        return mklx;
    }

    public void setMklx(String mklx) {
        this.mklx = mklx == null ? null : mklx.trim();
    }
}