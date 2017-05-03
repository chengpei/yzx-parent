package com.whpe.services;

import com.whpe.bean.SysAppUser;
import com.whpe.bean.dto.SysPeopleDTO;
import com.whpe.bean.vo.SysAppUserVO;

public interface UserService {
    SysAppUserVO selectByCondition(SysAppUser sysAppUser);

    int updateSysPeople(SysPeopleDTO sysPeopleDTO);
}
