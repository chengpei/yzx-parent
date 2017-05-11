package com.whpe.dao.yckq;

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

    int updateCode2ByPhone(SysAppUser sysAppUser);
}