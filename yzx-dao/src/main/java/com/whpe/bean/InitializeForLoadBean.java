package com.whpe.bean;

/**
 * Created by Administrator on 2017/5/4.
 */
public class InitializeForLoadBean {

    private String oldBalance; // 旧余额 16进制

    private String tradeSeq; // 充值交易序号 16进制

    private String keyVersion; // 密钥版本号

    private String sfFlag; // 算法标志

    private String random; // 随机数

    private String mac1;

    public String getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(String oldBalance) {
        this.oldBalance = oldBalance;
    }

    public String getTradeSeq() {
        return tradeSeq;
    }

    public void setTradeSeq(String tradeSeq) {
        this.tradeSeq = tradeSeq;
    }

    public String getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(String keyVersion) {
        this.keyVersion = keyVersion;
    }

    public String getSfFlag() {
        return sfFlag;
    }

    public void setSfFlag(String sfFlag) {
        this.sfFlag = sfFlag;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getMac1() {
        return mac1;
    }

    public void setMac1(String mac1) {
        this.mac1 = mac1;
    }
}
