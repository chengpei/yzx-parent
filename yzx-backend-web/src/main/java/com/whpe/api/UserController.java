package com.whpe.api;

import com.whpe.bean.Result;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.controller.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController extends CommonController{

    @RequestMapping(value = "/api/queryUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result queryUserInfo(HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        Result result = new Result(true, "查询成功");
        result.put("sysPeople", appUser.getSysPeople());
        return result;
    }

}
