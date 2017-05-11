package com.whpe.dao.yckq;

import com.whpe.bean.SysPeople;
import com.whpe.bean.dto.SysPeopleDTO;

public interface SysPeopleMapper {
    int deleteByPrimaryKey(String puPeopleId);

    int insert(SysPeople record);

    int insertSelective(SysPeople record);

    SysPeople selectByPrimaryKey(String puPeopleId);

    int updateByPrimaryKeySelective(SysPeople record);

    int updateByPrimaryKey(SysPeople record);

    SysPeople selectSysPeopleByUid(String uId);

    int updateSysPeopleByUid(SysPeopleDTO sysPeopleDTO);
}