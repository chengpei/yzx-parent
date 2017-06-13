package com.whpe.test;

import com.alibaba.fastjson.JSON;
import com.whpe.bean.BusOrder;
import com.whpe.bean.Result;
import com.whpe.services.impl.AppInterfaceServiceImpl;
import com.whpe.utils.DateUtils;
import com.whpe.utils.HttpUtils;
import com.whpe.utils.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

public class Main {
    public static void main(String args[]) throws IOException, ParseException {
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

        BusOrder busOrder = new BusOrder();
        busOrder.setOrderNo("BUS20170613153253000000000000001");
        busOrder.setLinkName("张三");
        busOrder.setLinkPhone("13476073978");
        busOrder.setRemark("公司旅游");
        busOrder.setReserveMoney(new BigDecimal("199"));
        busOrder.setBudgetMoney(new BigDecimal("1500"));
        busOrder.setProductOfferId("e973e1c7a15449108d0366655948ee9b");
        busOrder.setUseTime(DateUtils.getDateForString("20170620093000", "yyyyMMddHHmmss"));
        busOrder.setUseDay(1L);
        busOrder.setUseType(0L);
        busOrder.setCompanyName("武汉公用电子");
        busOrder.setDepartSite("古田二路汇丰企业总部");
        busOrder.setTargetSite("XXXX风景区");
        busOrder.setRealMoney(new BigDecimal("2000"));
        busOrder.setBusLicense("鄂A12345");
        System.out.println(JSON.toJSONString(busOrder));
    }
}
