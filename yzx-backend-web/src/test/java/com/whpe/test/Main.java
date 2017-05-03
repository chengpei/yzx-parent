package com.whpe.test;

import com.alibaba.fastjson.JSON;
import com.whpe.bean.Result;
import com.whpe.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String args[]) throws IOException {
//        Map<String, String> params = new HashMap<>();
//        params.put("phoneNumber", "13476073978");
//        params.put("smsType", "1");
//        String jsonStr = HttpUtils.urlPost("https://127.0.0.1:8443/api/sendSMSCheckCode.do", params);
//        System.out.println("发送验证码:" + jsonStr);
//        Result result = JSON.parseObject(jsonStr, Result.class);

//        params.clear();
//        params.put("phoneNumber", "13476073978");
//        params.put("newPassword", "");
//        jsonStr = HttpUtils.urlPost("https://192.168.100.240:8443/api/changePassword.do", params);
//        System.out.println("修改密码:" + jsonStr);

        System.out.println(String.format("%015d", 1));
    }
}
