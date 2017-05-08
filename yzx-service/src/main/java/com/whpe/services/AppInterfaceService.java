package com.whpe.services;

import com.alibaba.fastjson.JSONObject;
import com.whpe.bean.NfcCardRecharge;
import com.whpe.bean.Nhrequestresult;

import javax.servlet.http.HttpSession;

public interface AppInterfaceService {

    void updateSysPeople(JSONObject requestJson, JSONObject result, HttpSession session);

    void bindCard(JSONObject requestJson, JSONObject result, HttpSession session);

    void commitOrder(JSONObject requestJson, JSONObject result, HttpSession session);

    void applyPay(JSONObject requestJson, JSONObject result, HttpSession session);

    void applyRecharge(JSONObject requestJson, JSONObject result, HttpSession session);

    boolean updateNfcCardRechargeOrder(NfcCardRecharge nfcCardRecharge);

    void queryOrder(JSONObject requestJson, JSONObject result, HttpSession session);

    void rechargeConfirm(JSONObject requestJson, JSONObject result, HttpSession session);

    int saveAbcRequestResult(Nhrequestresult nhrequestresult);
}