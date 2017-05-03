package com.whpe.test;

import com.whpe.bean.SmsSendLog;
import com.whpe.bean.SysAppUser;
import com.whpe.bean.SysUser;
import com.whpe.dao.SmsSendLogMapper;
import com.whpe.dao.SysAppUserMapper;
import com.whpe.dao.SysUserMapper;
import com.whpe.services.LoginRegisterService;
import com.whpe.services.SendSMSService;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by chengpei on 2017/2/26.
 */
public class AppApiTest extends SpringTestContext{

    @Resource
    private SendSMSService sendSMSService;

    @Resource
    private LoginRegisterService loginRegisterService;

    @Resource
    private SmsSendLogMapper smsSendLogMapper;

    @Resource
    private SysAppUserMapper sysAppUserMapper;

    @Test
    public void sendSMSTest() throws IOException {
        SmsSendLog smsSendLog = new SmsSendLog();
        smsSendLog.setAcceptPhone("13476073978");
        smsSendLog.setSmsContent("您的验证码为{123456}");
        boolean b = sendSMSService.sendSms(smsSendLog);
        logger.info("发送结果 == " + b);
    }

    @Test
    public void test1() throws IOException {
        System.out.println(smsSendLogMapper.countCurrDaySMS("13476073978"));
    }

    @Test
    public void test2() throws IOException {
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setuPhone("13476073978");
        sysAppUser.setuEnabled("2");
        SysAppUser appUser = sysAppUserMapper.selectByCondition(sysAppUser);
        System.out.println(appUser);
    }

    @Test
    public void test3() throws IOException {
//        loginRegisterService.doRegister("13476073978", "123456");
        logger.info("发送次数 === " + loginRegisterService.countCurrDaySMS("15629169373"));
    }

    @Test
    public void loginTest() throws IOException {
        SysAppUser sysAppUser = loginRegisterService.doLogin("13476073978", "123456");
        System.out.println(sysAppUser);
    }

    @Test
    public void testSMSTemplate(){
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("checkCode", "231234");
        String smsContentByParams = loginRegisterService.getSmsContentByParams("1", params);
        logger.info("短信为 === " + smsContentByParams);
    }

    @Test
    public void testCheckSmsOneMinutes(){
        boolean b = loginRegisterService.checkSmsOneMinutes("13476073978");
        System.out.println(b);
    }

}
