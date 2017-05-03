package com.whpe.dao;

import com.whpe.bean.SmsSendLog;

public interface SmsSendLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SmsSendLog record);

    int insertSelective(SmsSendLog record);

    SmsSendLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SmsSendLog record);

    int updateByPrimaryKey(SmsSendLog record);

    int countCurrDaySMS(String phoneNumber);

    SmsSendLog selectLastSms(String phoneNumber);
}