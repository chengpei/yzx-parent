package com.whpe.dao;

import com.whpe.bean.SysAppUser;
import com.whpe.bean.vo.SysAppUserVO;

public interface SysAppUserMapper {
    int deleteByPrimaryKey(String uId);

    int insert(SysAppUser record);

    int insertSelective(SysAppUser record);

    SysAppUser selectByPrimaryKey(String uId);

    int updateByPrimaryKeySelective(SysAppUser record);

    int updateByPrimaryKey(SysAppUser record);

    SysAppUserVO selectByCondition(SysAppUser sysAppUser);
}