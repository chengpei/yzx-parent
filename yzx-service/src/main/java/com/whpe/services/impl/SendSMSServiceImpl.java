package com.whpe.services.impl;

import com.whpe.bean.SmsSendLog;
import com.whpe.services.CommonService;
import com.whpe.services.SendSMSService;
import com.whpe.utils.HttpUtils;
import com.whpe.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public boolean sendSms(SmsSendLog smsSendLog) throws Exception {
        Map<String, String> params = buildSendSMSRequestParams(smsSendLog);
        String responseInfo = HttpUtils.urlPost(smsApiAddress, params, "GBK");
        Map<String, String> resultMap = buildResultMap(responseInfo);
        if(resultMap == null || !"0".equals(resultMap.get("result"))){
            throw new RuntimeException(resultMap.get("description"));
        }
        return true;
    }

    /**
     * 根据回复内容构建Map
     * @param responseInfo  result=0&description=发送短信成功&taskid=202181499560555&faillist=&task_id=202181499560555
     * @return
     */
    private Map<String, String> buildResultMap(String responseInfo) {
        if(StringUtils.isEmpty(responseInfo)){
            return null;
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        String[] params = responseInfo.split("&");
        for(int i=0;i<params.length;i++){
            String param = params[i];
            String[] keyValue = param.split("=");
            if(keyValue.length == 1){
                resultMap.put(keyValue[0], "");
            }else{
                resultMap.put(keyValue[0], keyValue[1]);
            }
        }
        return resultMap;
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
