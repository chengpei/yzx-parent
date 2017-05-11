package com.whpe.dao.yckq;

import com.whpe.bean.SmsTemplate;

public interface SmsTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SmsTemplate record);

    int insertSelective(SmsTemplate record);

    SmsTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SmsTemplate record);

    int updateByPrimaryKey(SmsTemplate record);

    SmsTemplate selectByTemplateType(String templateType);
}