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
     * �ж��ֻ����Ƿ��Ѿ�ע��
     * @param phoneNumber
     * @return  ����true��ʾ�Ѿ�ע�ᣬ û��ע�������false
     */
    boolean checkPhoneExist(String phoneNumber);

    SysAppUser doLogin(String phoneNumber, String password);

    SysAppUser selectBeanByCondition(SysAppUser sysAppUser);

    int updateByPrimaryKeySelective(SysAppUser appUser);
}
