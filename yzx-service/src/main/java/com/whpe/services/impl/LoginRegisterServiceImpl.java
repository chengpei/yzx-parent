package com.whpe.services.impl;

import com.whpe.bean.SmsSendLog;
import com.whpe.bean.SysAppUser;
import com.whpe.dao.SmsSendLogMapper;
import com.whpe.dao.SysAppUserMapper;
import com.whpe.services.CommonService;
import com.whpe.services.LoginRegisterService;
import com.whpe.services.SendSMSService;
import com.whpe.utils.MD5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by chengpei on 2017/2/26.
 */
@Service
public class LoginRegisterServiceImpl extends CommonService implements LoginRegisterService{

    @Resource
    private SendSMSService sendSMSService;

    @Resource
    private SmsSendLogMapper smsSendLogMapper;

    @Resource
    private SysAppUserMapper sysAppUserMapper;

    @Override
    public boolean sendSMSCheckCode(String phoneNumber, String checkCode) {
        SmsSendLog smsSendLog = new SmsSendLog();
        smsSendLog.setAcceptPhone(phoneNumber);
        smsSendLog.setSmsContent("您的验证码为{" + checkCode + "}");
        smsSendLog.setCreateTime(new Date());
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
            // 写短信日志表
            smsSendLogMapper.insertSelective(smsSendLog);
        }
    }

    @Override
    public int countCurrDaySMS(String phoneNumber) {
        return smsSendLogMapper.countCurrDaySMS(phoneNumber);
    }

    @Override
    public boolean doRegister(String phoneNumber, String password) {
        // 写用户表
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setuPhone(phoneNumber);
        sysAppUser.setuAccount(phoneNumber);
        sysAppUser.setuPassword(MD5Utils.getMD5(password).toString());
        sysAppUser.setuCreatedate(new Date());
        return sysAppUserMapper.insertSelective(sysAppUser) > 0;
    }

    @Override
    public boolean checkPhoneExist(String phoneNumber) {
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setuPhone(phoneNumber);
        sysAppUser.setuEnabled("2");// 启用状态
        return sysAppUserMapper.selectByCondition(sysAppUser) != null;
    }

    @Override
    public SysAppUser doLogin(String phoneNumber, String password) {
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setuPhone(phoneNumber);
        sysAppUser.setuPassword(MD5Utils.getMD5(password).toString());
        SysAppUser appUser = sysAppUserMapper.selectByCondition(sysAppUser);

        // 登录成功更新令牌
        if(appUser != null){
            appUser.setDef1(UUID.randomUUID().toString().replaceAll("-", ""));
            sysAppUserMapper.updateByPrimaryKeySelective(appUser);
        }
        return appUser;
    }

    @Override
    public SysAppUser selectBeanByCondition(SysAppUser sysAppUser) {
        return sysAppUserMapper.selectByCondition(sysAppUser);
    }

    @Override
    public int updateByPrimaryKeySelective(SysAppUser appUser) {
        return sysAppUserMapper.updateByPrimaryKeySelective(appUser);
    }

}
