package com.whpe.api;

import com.alibaba.fastjson.JSONObject;
import com.whpe.controller.CommonController;
import com.whpe.services.AppInterfaceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

@Controller
public class AppInterfaceController extends CommonController{

    @Resource
    private AppInterfaceService appInterfaceService;

    @RequestMapping(value = "/api/interface", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject appInterface(String content, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        // 返回的结果对象
        JSONObject result = new JSONObject();
        logger.info("调用接口入参 == " + content);
        JSONObject requestJson = JSONObject.parseObject(content);
        JSONObject commonInfo = requestJson.getJSONObject("common");
        String method = commonInfo.getString("txCode");
        Class clz = appInterfaceService.getClass();
        Method thd = clz.getMethod(method, requestJson.getClass(), result.getClass(), HttpSession.class);
        thd.invoke(appInterfaceService, new Object[] { requestJson, result, session });
        logger.info("调用接口返回 == " + result.toJSONString());
        return result;
    }

}
