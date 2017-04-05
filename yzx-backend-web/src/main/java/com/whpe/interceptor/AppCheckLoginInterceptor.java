package com.whpe.interceptor;

import com.whpe.bean.Result;
import com.whpe.bean.SysAppUser;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.services.LoginRegisterService;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户登录拦截器，未登录用户转向登录页面
 */
public class AppCheckLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private LoginRegisterService loginRegisterService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        String token = request.getParameter("token");
        if(StringUtils.isEmpty(token)){
            token = (String) session.getAttribute("token");
        }else{
            session.setAttribute("token", token);
        }

        if(token == null){
            returnWithoutLogin(response);
            return false;
        }
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setToken(token);
        SysAppUserVO appUser = loginRegisterService.selectBeanByCondition(sysAppUser);
        if(appUser == null){
            returnWithoutLogin(response);
            return false;
        }
        session.setAttribute("user", appUser);
        return true;
    }

    /**
     * 未登录的返回
     * @param response
     */
    private void returnWithoutLogin(HttpServletResponse response){
        Result result = new Result(false, "未登录");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter pWriter = null;
        try {
            pWriter = response.getWriter();
            pWriter.write(result.toString());
        } catch (IOException e) {

        } finally {
            if (pWriter != null) {
                pWriter.flush();
                pWriter.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(httpServletRequest, httpServletResponse, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object handler, Exception e) throws Exception {
        super.afterCompletion(httpServletRequest, httpServletResponse, handler, e);
    }
}
