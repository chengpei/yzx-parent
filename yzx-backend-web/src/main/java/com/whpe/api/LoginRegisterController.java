package com.whpe.api;

import com.whpe.bean.Result;
import com.whpe.bean.SmsSendLog;
import com.whpe.controller.CommonController;
import com.whpe.services.LoginRegisterService;
import com.whpe.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
public class LoginRegisterController extends CommonController{

    @Resource
    private LoginRegisterService loginRegisterService;

    @RequestMapping(value = "/api/doLogin", method = RequestMethod.POST)
    public Result doLogin(){


        return new Result(true, "登陆成功");
    }

    @RequestMapping(value = "/api/doRegister", method = RequestMethod.POST)
    public Result doRegister(){


        return new Result(true, "注册成功");
    }

    @RequestMapping(value = "/api/sendSMSCheckCode", method = RequestMethod.POST)
    public Result sendSMSCheckCode(String phoneNumber, HttpSession session){
        if(StringUtils.isEmpty(phoneNumber)){
            return new Result(false, "手机号为空");
        }
        // 统计该手机号当天发送的短信次数
        if(loginRegisterService.countCurrDaySMS(phoneNumber) > 5){
            return new Result(false, "手机号获取短信次数达上限");
        }
        String checkCode = StringUtils.getRadomString(6);
        logger.debug("生成的验证码为 == " + checkCode);
        if(loginRegisterService.sendSMSCheckCode(phoneNumber, checkCode)){
            // session中存放手机号对应的验证码
            session.setAttribute(phoneNumber, checkCode);
            return new Result(true, "发送成功");
        }else {
            return new Result(false, "发送失败");
        }
    }

}
