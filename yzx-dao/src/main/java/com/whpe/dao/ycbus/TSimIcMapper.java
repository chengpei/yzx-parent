package com.whpe.dao.ycbus;

import com.whpe.bean.TSimIc;

public interface TSimIcMapper {
    int deleteByPrimaryKey(String iccid);

    int insert(TSimIc record);

    int insertSelective(TSimIc record);

    TSimIc selectByPrimaryKey(String iccid);

    int updateByPrimaryKeySelective(TSimIc record);

    int updateByPrimaryKey(TSimIc record);
}