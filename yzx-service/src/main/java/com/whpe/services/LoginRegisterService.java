package com.whpe.services;

import com.whpe.bean.SysAppUser;
import com.whpe.bean.vo.SysAppUserVO;

import java.util.LinkedHashMap;

/**
 * Created by chengpei on 2017/2/26.
 */
public interface LoginRegisterService {

    boolean sendSMSCheckCode(String phoneNumber, String smsType, String checkCode);

    int countCurrDaySMS(String phoneNumber);

    boolean doRegister(String phoneNumber, String password);

    /**
     * �ж��ֻ���Ƿ��Ѿ�ע��
     * @param phoneNumber
     * @return  ����true��ʾ�Ѿ�ע�ᣬ û��ע����false
     */
    boolean checkPhoneExist(String phoneNumber);

    SysAppUserVO doLogin(String phoneNumber, String password);

    SysAppUserVO selectBeanByCondition(SysAppUser sysAppUser);

    int updateByPrimaryKeySelective(SysAppUser appUser);

    /**
     * ��ݶ���ģ�����ͺͲ����ȡ��������
     * @param templateType
     * @param params
     * @return
     */
    String getSmsContentByParams(String templateType, LinkedHashMap<String, String> params);
}
