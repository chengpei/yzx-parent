package com.whpe.services.impl;

import com.whpe.bean.SmsSendLog;
import com.whpe.services.CommonService;
import com.whpe.services.SendSMSService;
import com.whpe.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengpei on 2017/2/26.
 */
@Service
public class SendSMSServiceImpl extends CommonService implements SendSMSService{

    @Value("${sms_api_address}")
    private String smsApiAddress;

    @Value("${smsSpCode}")
    private String smsSpCode;

    @Value("${smsLoginName}")
    private String smsLoginName;

    @Value("${smsPassword}")
    private String smsPassword;

    private static final SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHH:mm:ss.SSS");

    @Override
    public boolean sendSms(SmsSendLog smsSendLog) throws IOException {
        Map<String, String> params = buildSendSMSRequestParams(smsSendLog);
        HttpUtils.urlPost(smsApiAddress, params, "GBK");
        return true;
    }

    /**
     * 构造发送短信的请求参数
     * @param smsSendLog
     * @return
     */
    private Map<String, String> buildSendSMSRequestParams(SmsSendLog smsSendLog) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("SpCode", smsSpCode);
        params.put("LoginName", smsLoginName);
        params.put("Password", smsPassword);
        params.put("MessageContent", smsSendLog.getSmsContent());
        params.put("UserNumber", smsSendLog.getAcceptPhone());
        params.put("SerialNumber", sdf.format(new Date()));
        params.put("ScheduleTime", "");
        params.put("ExtendAccessNum", "");
        params.put("f", "1");
        return params;
    }

}
