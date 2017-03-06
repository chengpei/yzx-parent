package com.whpe.dao;

import com.whpe.bean.SysPeople;

public interface SysPeopleMapper {
    int deleteByPrimaryKey(String puPeopleId);

    int insert(SysPeople record);

    int insertSelective(SysPeople record);

    SysPeople selectByPrimaryKey(String puPeopleId);

    int updateByPrimaryKeySelective(SysPeople record);

    int updateByPrimaryKey(SysPeople record);

    SysPeople selectSysPeopleByUid(String uId);
}