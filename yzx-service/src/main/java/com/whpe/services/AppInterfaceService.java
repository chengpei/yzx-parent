package com.whpe.services;

import com.alibaba.fastjson.JSONObject;
import com.whpe.bean.NfcCardRecharge;

import javax.servlet.http.HttpSession;

public interface AppInterfaceService {

    void updateSysPeople(JSONObject requestJson, JSONObject result, HttpSession session);

    void bindCard(JSONObject requestJson, JSONObject result, HttpSession session);

    void commitOrder(JSONObject requestJson, JSONObject result, HttpSession session);

    void applyPay(JSONObject requestJson, JSONObject result, HttpSession session);

    boolean updateNfcCardRechargeOrder(NfcCardRecharge nfcCardRecharge);

    void queryOrder(JSONObject requestJson, JSONObject result, HttpSession session);
}
