package com.whpe.services;

public interface PayService {

    /**
     * 根据订单号、金额，获取银联支付的TN
     * @param orderNo
     * @param ordermount
     * @return
     */
    String getUnionPayTN(String orderNo, String ordermount);

    /**
     * 根据订单号、金额，生成农行支付的URL
     * @param orderNo
     * @param ordermount
     * @return
     */
    boolean generateAbcPayHtml(String orderNo, String ordermount);

    boolean generateNxhPayHtml(String orderNo, String ordermount);

    String getUnionpayMerchantId();
}
