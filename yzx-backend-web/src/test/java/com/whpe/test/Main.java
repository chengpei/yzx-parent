package com.whpe.test;

import com.alibaba.fastjson.JSON;
import com.whpe.bean.Result;
import com.whpe.services.impl.AppInterfaceServiceImpl;
import com.whpe.utils.HttpUtils;
import com.whpe.utils.StringUtils;

import java.io.IOException;
import java.util.*;

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

//        System.out.println(String.format("%015d", 1));

//        Set<String> stringSet = new HashSet<>();
//        for (int i=0;i<10000;i++){
//            stringSet.add(AppInterfaceServiceImpl.generateVouchers());
//            stringSet.add(StringUtils.getRadomString(16));
//        }
//        System.out.println(stringSet.size());

//        System.out.println(StringUtils.getRadomString2(16));

        Thread t = new Thread(){
            @Override
            public void run() {
                test();
            }
        };
        Thread t1 = new Thread(){
            @Override
            public void run() {
                test();
            }
        };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                test();
            }
        };
        t.setDaemon(false);
        t1.setDaemon(false);
        t2.setDaemon(false);

        t1.start();
        t2.start();
        t.start();


    }

    public static int i=0;
    public static synchronized void test(){
        if(i != 10){
            i++;
            test();
        }
        System.out.println(i);
    }
}
