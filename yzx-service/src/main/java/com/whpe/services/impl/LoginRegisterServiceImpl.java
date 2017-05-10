package com.whpe.services.impl;

import com.whpe.bean.SmsSendLog;
import com.whpe.bean.SmsTemplate;
import com.whpe.bean.SysAppUser;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.dao.SmsSendLogMapper;
import com.whpe.dao.SmsTemplateMapper;
import com.whpe.dao.SysAppUserMapper;
import com.whpe.services.CommonService;
import com.whpe.services.LoginRegisterService;
import com.whpe.services.SendSMSService;
import com.whpe.utils.MD5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private SmsTemplateMapper smsTemplateMapper;

    @Override
    public boolean sendSMSCheckCode(String phoneNumber, String smsTemplateType, String checkCode) {
        SmsSendLog smsSendLog = new SmsSendLog();
        smsSendLog.setAcceptPhone(phoneNumber);
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        params.put("checkCode", checkCode);
        smsSendLog.setSmsContent(getSmsContentByParams(smsTemplateType, params));
        smsSendLog.setCreateTime(new Date());
        try {
            if(sendSMSService.sendSms(smsSendLog)){
                smsSendLog.setSendStatus("2");
                smsSendLog.setSendTime(new Date());
                return true;
            }else{
                smsSendLog.setSendStatus("3");
                return false;
            }
        } catch (Exception e) {
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
    public SysAppUserVO doLogin(String phoneNumber, String password) {
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setuPhone(phoneNumber);
        sysAppUser.setuPassword(MD5Utils.getMD5(password).toString());
        SysAppUserVO appUser = sysAppUserMapper.selectByCondition(sysAppUser);

        // 登录成功更新令牌
        if(appUser != null && "2".equals(appUser.getuEnabled())){
            appUser.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
            sysAppUserMapper.updateByPrimaryKeySelective(appUser);
        }
        return appUser;
    }

    @Override
    public SysAppUserVO selectBeanByCondition(SysAppUser sysAppUser) {
        return sysAppUserMapper.selectByCondition(sysAppUser);
    }

    @Override
    public int updateByPrimaryKeySelective(SysAppUser appUser) {
        return sysAppUserMapper.updateByPrimaryKeySelective(appUser);
    }

    @Override
    public String getSmsContentByParams(String templateType, LinkedHashMap<String, String> params) {
        SmsTemplate smsTemplate = smsTemplateMapper.selectByTemplateType(templateType);
        String templateContent = smsTemplate.getTemplateContent();
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
            templateContent = templateContent.replaceAll("\\$\\{"+key+"\\}", value);
        }
        return templateContent;
    }

    @Override
    public boolean checkSmsOneMinutes(String phoneNumber) {
        SmsSendLog smsSendLog = smsSendLogMapper.selectLastSms(phoneNumber);
        if(smsSendLog == null){
            return true;
        }
        Date sendTime = smsSendLog.getSendTime();
        return System.currentTimeMillis() - sendTime.getTime() > 60000;
    }

}
