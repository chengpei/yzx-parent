package com.whpe.api;

import com.alibaba.fastjson.JSON;
import com.whpe.bean.Result;
import com.whpe.bean.SysAppUser;
import com.whpe.bean.SysPeople;
import com.whpe.bean.dto.SysPeopleDTO;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.controller.CommonController;
import com.whpe.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class UserController extends CommonController{

    @Resource
    private UserService userService;

    @RequestMapping(value = "/api/queryUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result queryUserInfo(HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setuPhone(appUser.getuPhone());
        appUser = userService.selectByCondition(sysAppUser);
        session.setAttribute("user", appUser);
        Result result = new Result(true, "查询成功");
        result.put("sysPeople", appUser.getSysPeople());
        return result;
    }

    @RequestMapping(value = "/api/updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result updateUserInfo(SysPeopleDTO sysPeopleDTO, HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        sysPeopleDTO.setuId(appUser.getuId());
        int update = userService.updateSysPeople(sysPeopleDTO);
        Result result = new Result(true, "更新成功");
        return result;
    }

}
