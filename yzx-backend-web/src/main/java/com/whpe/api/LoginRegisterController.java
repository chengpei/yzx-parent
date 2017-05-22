package com.whpe.api;

import com.whpe.bean.Result;
import com.whpe.bean.SysAppUser;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.controller.CommonController;
import com.whpe.services.LoginRegisterService;
import com.whpe.utils.MD5Utils;
import com.whpe.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class LoginRegisterController extends CommonController{

    @Resource
    private LoginRegisterService loginRegisterService;

    @RequestMapping(value = "/api/doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Result doLogin(String phoneNumber, String password, HttpSession session){
        if(StringUtils.isEmpty(phoneNumber)){
            return new Result(false, "手机号不能为空");
        }
        if(StringUtils.isEmpty(password)){
            return new Result(false, "密码不能为空");
        }
        SysAppUserVO sysAppUser = loginRegisterService.doLogin(phoneNumber, password);
        if(sysAppUser == null){
            return new Result(false, "用户名或密码不正确");
        }
        if(!"2".equals(sysAppUser.getuEnabled())){
            return new Result(false, "帐号未启用");
        }
        session.setAttribute("token", sysAppUser.getToken());
        Result result = new Result(true, "登陆成功");
        result.put("token", sysAppUser.getToken());
        result.put("sysPeople", sysAppUser.getSysPeople());
        result.put("uid", sysAppUser.getuId());
        return result;
    }

    @RequestMapping(value = "/api/doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Result doRegister(String phoneNumber, String password, HttpSession session){
        if(StringUtils.isEmpty(phoneNumber)){
            return new Result(false, "手机号不能为空");
        }
        if(StringUtils.isEmpty(password)){
            return new Result(false, "密码不能为空");
        }
        // 判断该手机号是否已经注册
        if(loginRegisterService.checkPhoneExist(phoneNumber)){
            return new Result(false, "该手机号已经注册过");
        }
        session.removeAttribute(phoneNumber);
        if(loginRegisterService.doRegister(phoneNumber, password)){
            return new Result(true, "注册成功");
        }else {
            return new Result(false, "注册失败");
        }
    }

    @RequestMapping(value = "/api/sendSMSCheckCode", method = RequestMethod.POST)
    @ResponseBody
    public Result sendSMSCheckCode(String phoneNumber, String smsType, HttpSession session){
        if(StringUtils.isEmpty(phoneNumber)){
            return new Result(false, "手机号为空");
        }
        if(StringUtils.isEmpty(smsType)){
            return new Result(false, "短信类型为空");
        }
        // 短信类型注册，判断手机号是否已经注册
        if("1".equals(smsType) && loginRegisterService.checkPhoneExist(phoneNumber)){
            return new Result(false, "该手机号已经注册过");
        }
        // 找回密码类型，判断手机号是否注册过
        if("2".equals(smsType)){
            SysAppUser user = new SysAppUser();
            user.setuPhone(phoneNumber);
            SysAppUserVO sysAppUserVO = loginRegisterService.selectBeanByCondition(user);
            if(sysAppUserVO == null){
                return new Result(false, "该手机号未注册");
            }
        }
        // 检查该手机号 上次发送短信时间是否超过一分钟
        if(!loginRegisterService.checkSmsOneMinutes(phoneNumber)){
            return new Result(false, "一分钟内只能发送一次短信");
        }
        // 统计该手机号当天发送的短信次数
        if(loginRegisterService.countCurrDaySMS(phoneNumber) >= 5){
            return new Result(false, "手机号获取短信次数达上限");
        }
        String checkCode = StringUtils.getRadomString(6);
        logger.debug("生成的验证码为 == " + checkCode);
        if(loginRegisterService.sendSMSCheckCode(phoneNumber, smsType, checkCode)){
            // session中存放手机号对应的验证码
            session.setAttribute(phoneNumber, checkCode);
            return new Result(true, "发送成功");
        }else {
            return new Result(false, "发送失败");
        }
    }

    @RequestMapping(value = "/api/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public Result changePassword(String phoneNumber, String newPassword, HttpSession session){
        if(StringUtils.isEmpty(phoneNumber)){
            return new Result(false, "手机号不能为空");
        }
        if(StringUtils.isEmpty(newPassword)){
            return new Result(false, "新密码不能为空");
        }

        // 检查用户是否存在
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setuPhone(phoneNumber);
        SysAppUser appUser = loginRegisterService.selectBeanByCondition(sysAppUser);
        if(appUser == null){
            return new Result(false, "用户没有注册");
        }
        appUser.setuPassword(MD5Utils.getMD5(newPassword).toString());
        if(loginRegisterService.updateByPrimaryKeySelective(appUser) > 0){
            return new Result(true, "修改成功");
        }else {
            return new Result(false, "修改失败");
        }
    }

    @RequestMapping(value = "/api/examineCheckCode", method = RequestMethod.POST)
    @ResponseBody
    public Result examineCheckCode(String phoneNumber, String checkCode, HttpSession session){
        if(StringUtils.isEmpty(phoneNumber)){
            return new Result(false, "手机号不能为空");
        }
        if(StringUtils.isEmpty(checkCode)){
            return new Result(false, "验证码不能为空");
        }
        String rightCheckCode = (String) session.getAttribute(phoneNumber);
        logger.info("发送的验证码：" + checkCode + "，正确验证码为" + rightCheckCode);
        if(StringUtils.isEmpty(rightCheckCode)){
            return new Result(false, "验证码已经失效");
        }
        if(!checkCode.equals(rightCheckCode)){
            return new Result(false, "验证码错误");
        }else {
            session.removeAttribute(phoneNumber);
            return new Result(true, "验证码正确");
        }
    }

}
