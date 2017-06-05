package com.whpe.services;

import com.alibaba.fastjson.JSONObject;
import com.whpe.bean.NfcCardRecharge;
import com.whpe.bean.Nhrequestresult;
import com.whpe.bean.Nshresresult;
import com.whpe.bean.OrderT;

import javax.servlet.http.HttpSession;
import java.text.ParseException;

public interface AppInterfaceService {

    void updateSysPeople(JSONObject requestJson, JSONObject result, HttpSession session);

    void bindCard(JSONObject requestJson, JSONObject result, HttpSession session);

    void queryMyCard(JSONObject requestJson, JSONObject result, HttpSession session);

    void commitOrder(JSONObject requestJson, JSONObject result, HttpSession session);

    void applyPay(JSONObject requestJson, JSONObject result, HttpSession session);

    void applyMallPay(JSONObject requestJson, JSONObject result, HttpSession session);

    void payConfirm(JSONObject requestJson, JSONObject result, HttpSession session);

    void applyRecharge(JSONObject requestJson, JSONObject result, HttpSession session);

    boolean updateNfcCardRechargeOrder(NfcCardRecharge nfcCardRecharge);

    void queryOrder(JSONObject requestJson, JSONObject result, HttpSession session);

    void rechargeConfirm(JSONObject requestJson, JSONObject result, HttpSession session);

    int saveAbcRequestResult(Nhrequestresult nhrequestresult);

    int saveNshRequestResult(Nshresresult nshresresult);

    void generateOrderNo(JSONObject requestJson, JSONObject result, HttpSession session);

    void queryCitzenCardNoByID(JSONObject requestJson, JSONObject result, HttpSession session);

    void queryTourYearCardByCardNo(JSONObject requestJson, JSONObject result, HttpSession session);

    void feedback(JSONObject requestJson, JSONObject result, HttpSession session);

    void applyYearCardRenew(JSONObject requestJson, JSONObject result, HttpSession session) throws ParseException;

    void queryDictionary(JSONObject requestJson, JSONObject result, HttpSession session);

    boolean updateMallOrder(OrderT order);

    void generateLeaseVouchers(JSONObject requestJson, JSONObject result, HttpSession session);
}
