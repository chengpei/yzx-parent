package com.whpe.services;

/**
 * Created by chengpei on 2017/2/26.
 */
public interface LoginRegisterService {

    boolean sendSMSCheckCode(String phoneNumber, String checkCode);

    int countCurrDaySMS(String phoneNumber);

    boolean doRegister(String phoneNumber, String password);
}
