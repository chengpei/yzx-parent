package com.whpe.services.impl;

import com.whpe.bean.SysAppUser;
import com.whpe.bean.dto.SysPeopleDTO;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.dao.yckq.SysAppUserMapper;
import com.whpe.dao.yckq.SysPeopleMapper;
import com.whpe.services.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{

    @Resource
    private SysAppUserMapper sysAppUserMapper;

    @Resource
    private SysPeopleMapper sysPeopleMapper;

    @Override
    public SysAppUserVO selectByCondition(SysAppUser sysAppUser) {
        SysAppUserVO appUser = sysAppUserMapper.selectByCondition(sysAppUser);
        return appUser;
    }

    @Override
    public int updateSysPeople(SysPeopleDTO sysPeopleDTO) {

        return sysPeopleMapper.updateByPrimaryKeySelective(sysPeopleDTO);
    }
}
