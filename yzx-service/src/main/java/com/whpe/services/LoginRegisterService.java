package com.whpe.services;

import com.whpe.bean.SysAppUser;

/**
 * Created by chengpei on 2017/2/26.
 */
public interface LoginRegisterService {

    boolean sendSMSCheckCode(String phoneNumber, String checkCode);

    int countCurrDaySMS(String phoneNumber);

    boolean doRegister(String phoneNumber, String password);

    /**
     * 判断手机号是否已经注册
     * @param phoneNumber
     * @return  返回true表示已经注册， 没有注册过返回false
     */
    boolean checkPhoneExist(String phoneNumber);

    SysAppUser doLogin(String phoneNumber, String password);

    SysAppUser selectBeanByCondition(SysAppUser sysAppUser);

    int updateByPrimaryKeySelective(SysAppUser appUser);
}
