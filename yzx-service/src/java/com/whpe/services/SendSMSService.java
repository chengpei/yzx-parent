package com.whpe.services;

import com.whpe.bean.SmsSendLog;

import java.io.IOException;

/**
 * Created by chengpei on 2017/2/26.
 */
public interface SendSMSService {

    boolean sendSms(SmsSendLog smsSendLog) throws IOException;

}
