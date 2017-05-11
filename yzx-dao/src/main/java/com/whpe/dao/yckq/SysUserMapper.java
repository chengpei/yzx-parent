package com.whpe.dao.yckq;

import com.whpe.bean.SysUser;

public interface SysUserMapper {
    int deleteByPrimaryKey(String uId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String uId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
}