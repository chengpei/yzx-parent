package com.whpe.services.impl;

import com.whpe.bean.SmsSendLog;
import com.whpe.services.CommonService;
import com.whpe.services.LoginRegisterService;
import com.whpe.services.SendSMSService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by chengpei on 2017/2/26.
 */
@Service
public class LoginRegisterServiceImpl extends CommonService implements LoginRegisterService{

    @Resource
    private SendSMSService sendSMSService;

    @Override
    public boolean sendSMSCheckCode(String phoneNumber, String checkCode) {
        SmsSendLog smsSendLog = new SmsSendLog();
        smsSendLog.setAcceptPhone(phoneNumber);
        smsSendLog.setSmsContent("您的验证码为{" + checkCode + "}");
        try {
            if(sendSMSService.sendSms(smsSendLog)){
                smsSendLog.setSendStatus("2");
            }
            return true;
        } catch (IOException e) {
            logger.error("短信发送失败", e);
            smsSendLog.setSendStatus("3");
            smsSendLog.setRemark(e.getMessage());
            return false;
        }finally {
            // TODO 写短信日志表

        }
    }

    @Override
    public int countCurrDaySMS(String phoneNumber) {
        return 0;
    }

    @Override
    public boolean doRegister(String phoneNumber, String password) {
        // TODO 写用户表

        return true;
    }

}
