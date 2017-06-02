package com.whpe.api;

import com.alibaba.fastjson.JSONObject;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;
import com.whpe.bean.NfcCardRecharge;
import com.whpe.controller.CommonController;
import com.whpe.services.AppInterfaceService;
import com.whpe.services.PayService;
import com.whpe.services.impl.AppInterfaceServiceImpl;
import com.whpe.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class AppInterfaceController extends CommonController{

    @Resource
    private AppInterfaceService appInterfaceService;

    @Resource
    private PayService payService;

    @RequestMapping(value = "/api/interface", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject appInterface(String content, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        // 返回的结果对象
        JSONObject result = new JSONObject();
        if(StringUtils.isEmpty(content)){
            content = (String) request.getAttribute("content");
        }
        session.setAttribute("javax.servlet.http.HttpServletRequest", request);
        logger.info("调用接口入参 == " + content);
        JSONObject requestJson = JSONObject.parseObject(content);
        JSONObject commonInfo = requestJson.getJSONObject("common");
        String method = commonInfo.getString("txCode");
        Class clz = appInterfaceService.getClass();
        try {
            Method thd = clz.getMethod(method, requestJson.getClass(), result.getClass(), HttpSession.class);
            thd.invoke(appInterfaceService, new Object[] { requestJson, result, session });
        }catch (NoSuchMethodException e){
            logger.error("接口不存在", e);
            AppInterfaceServiceImpl.makeRetInfo("E0001", "接口【"+method+"】不存在", result);
        }
        logger.info("调用接口返回 == " + result.toJSONString());
        return result;
    }

    @RequestMapping(value = "/unionpay/back")
    @ResponseBody
    public void unionpayBack(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        request.setCharacterEncoding("ISO-8859-1");
        String encoding = request.getParameter(SDKConstants.param_encoding);
        // 获取请求参数中所有的信息
        Map<String, String> reqParam = getAllRequestParam(request);
        // 打印请求报文
        logger.info("收到银联异步通知 == " + reqParam);
        LogUtil.printRequestLog(reqParam);

        Map<String, String> valideData = null;
        if (null != reqParam && !reqParam.isEmpty()) {
            Iterator<Map.Entry<String, String>> it = reqParam.entrySet().iterator();
            valideData = new HashMap<String, String>(reqParam.size());
            while (it.hasNext()) {
                Map.Entry<String, String> e = it.next();
                String key = (String) e.getKey();
                String value = (String) e.getValue();
                value = new String(value.getBytes("ISO-8859-1"), encoding);
                valideData.put(key, value);
            }
        }
        String orderNo = valideData.get("orderId");
        NfcCardRecharge nfcCardRecharge = new NfcCardRecharge();
        nfcCardRecharge.setOrderno(orderNo);
        nfcCardRecharge.setCommcode(payService.getUnionpayMerchantId());
        nfcCardRecharge.setPaytype("08");
        // 验证签名
        if (!SDKUtil.validate(valideData, encoding)) {
            LogUtil.writeLog("验证签名结果[失败].");
            logger.error("验证签名失败【"+orderNo+"】");
            nfcCardRecharge.setBackrcvresponse("02");
        } else {
            LogUtil.writeLog("验证签名结果[成功].");
            logger.error("验证签名成功【"+orderNo+"】");
            nfcCardRecharge.setBackrcvresponse("01");
        }
        if(appInterfaceService.updateNfcCardRechargeOrder(nfcCardRecharge)){
            logger.info("订单状态更新成功，订单【"+orderNo+"】更新为【"+nfcCardRecharge.getBackrcvresponse()+"】");
        }else{
            logger.error("订单状态更新失败，订单【"+orderNo+"】更新为【"+nfcCardRecharge.getBackrcvresponse()+"】失败！");
        }
    }

    /**
     * 获取请求参数中所有的信息
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                //System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }
}
