package com.whpe.bean.vo;

import com.whpe.bean.SysAppUser;
import com.whpe.bean.SysPeople;

public class SysAppUserVO extends SysAppUser{

    private SysPeople sysPeople;

    public SysPeople getSysPeople() {
        return sysPeople;
    }

    public void setSysPeople(SysPeople sysPeople) {
        this.sysPeople = sysPeople;
    }
}
