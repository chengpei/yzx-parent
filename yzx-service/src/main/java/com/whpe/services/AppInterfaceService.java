package com.whpe.services;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;

public interface AppInterfaceService {

    void updateSysPeople(JSONObject requestJson, JSONObject result, HttpSession session);

    void bindCard(JSONObject requestJson, JSONObject result, HttpSession session);

}
