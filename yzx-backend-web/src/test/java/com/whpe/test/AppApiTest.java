package com.whpe.test;

import com.whpe.bean.SmsSendLog;
import com.whpe.services.SendSMSService;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by chengpei on 2017/2/26.
 */
public class AppApiTest extends SpringTestContext{

    @Resource
    private SendSMSService sendSMSService;

    @Test
    public void sendSMSTest() throws IOException {
        SmsSendLog smsSendLog = new SmsSendLog();
        smsSendLog.setAcceptPhone("13476073978");
        smsSendLog.setSmsContent("您的验证码为{123456}");
        boolean b = sendSMSService.sendSms(smsSendLog);
        logger.info("发送结果 == " + b);
    }
}
