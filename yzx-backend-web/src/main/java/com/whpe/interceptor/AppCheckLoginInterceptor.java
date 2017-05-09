package com.whpe.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.whpe.bean.Result;
import com.whpe.bean.SysAppUser;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.services.LoginRegisterService;
import com.whpe.services.impl.AppInterfaceServiceImpl;
import com.whpe.utils.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;

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

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        RequestMapping requestMappingAnn = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        if("/api/interface".equals(requestMappingAnn.value()[0])){
            String content = request.getParameter("content");
            if(StringUtils.isEmpty(content)){
                String temp;
                content = "";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                while((temp = bufferedReader.readLine()) != null){
                    content += (temp+System.getProperty("line.separator"));
                }
            }
            if (StringUtils.isNotEmpty(content)){
                request.setAttribute("content", content);
                JSONObject requestJson = JSONObject.parseObject(content);
                JSONObject commonInfo = requestJson.getJSONObject("common");
                String commonToken = commonInfo.getString("token");
                if(StringUtils.isNotEmpty(commonToken) && StringUtils.isEmpty(token)){
                    token = commonToken;
                }
            }
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
        JSONObject result = new JSONObject();
        AppInterfaceServiceImpl.makeRetInfo("S0002", "未登录", result);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter pWriter = null;
        try {
            pWriter = response.getWriter();
            pWriter.write(result.toJSONString());
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
