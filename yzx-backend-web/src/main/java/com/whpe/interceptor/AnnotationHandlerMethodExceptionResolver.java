package com.whpe.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.whpe.services.AppInterfaceService;
import com.whpe.services.impl.AppInterfaceServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 不必在Controller中对异常进行处理，抛出即可，由此异常解析器统一控制。<br>
 * ajax请求（有@ResponseBody的Controller）发生错误，输出JSON。<br>
 * 页面请求（无@ResponseBody的Controller）发生错误，输出错误页面。<br>
 * 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters<br>
 * Controller中需要有专门处理异常的方法。
 */
public class AnnotationHandlerMethodExceptionResolver extends ExceptionHandlerExceptionResolver {
    private Log logger =  LogFactory.getLog(getClass());

    // 默认错误页面
    private String defaultErrorView;

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
        if (handlerMethod == null) {
            return null;
        }
        Method method = handlerMethod.getMethod();
        if (method == null) {
            return null;
        }
//        StringBuffer errorMesg = new StringBuffer();
        ModelAndView returnValue = new ModelAndView();
        // 处理自定义异常将业务异常信息返回
//        if (exception instanceof BusinessException) {
//            errorMesg.append(exception.getMessage());
//            returnValue.setViewName(defaultErrorView);
//        } else if (exception instanceof BindException) {
//            List<FieldError> fes = ((BindException) exception).getFieldErrors();
//            for (FieldError fe : fes) {
//                errorMesg.append(fe.getDefaultMessage());
//                errorMesg.append("<br>");
//            }
//            // 记录系统异常
//            this.insertLogError(handlerMethod, exception, method);
//        } else { // 其他异常跳到500页面
//            returnValue.setViewName("error/500");
//            // 记录系统异常
//            this.insertLogError(handlerMethod, exception, method);
//        }

        logger.error(exception.getMessage(), exception);

        ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
        RequestMapping requestMappingAnn = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        // 如果是ajax请求则返回json对象Result 这里的ajax请求是返回值有@ResponseBody的方法
        // 所有的ajax请求方法最好使用@ResponseBody标示 否则这里无法识别是否是ajax请求
        if (responseBodyAnn != null) {
            JSONObject result = new JSONObject();
            if("/api/interface".equals(requestMappingAnn.value()[0])){
                AppInterfaceServiceImpl.makeRetInfo("E0001", exception.getMessage(), result);
            }else{
                result.put("message", exception.getMessage());
                result.put("success", false);
            }
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
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
            return null;

        } else {
            returnValue.setViewName(defaultErrorView);
            returnValue.addObject("error", exception.getMessage());
            return returnValue;
        }

    }
//    /**
//     * Description ： 捕获异常记录数据库<br>
//     *
//     * yangwb
//     *
//     * @param handlerMethod
//     * @param exception
//     * @param method
//     * @since
//     *
//     */
//    private void insertLogError(HandlerMethod handlerMethod, Exception exception, Method method) {
//        // for (int i = 0; i < 1000; i++) {
//        // 记录logerror 错误信息
//        String expMessage = null;
//        try {
//            ByteArrayOutputStream buf = new ByteArrayOutputStream();
//            exception.printStackTrace(new PrintWriter(buf, true));
//            expMessage = buf.toString();
//            buf.close();
//            LogError logError = new LogError();
//            // 区域
//            logError.setOperatorAreaId(SystemConstant.SYS_ID);
//            logError.setCreateBy(1);
//            logError.setErrorDate(new Date());
//            //
//            logError.setErrorMessage(expMessage);
//            StringBuffer sb = new StringBuffer();
//            sb.append(handlerMethod.getBeanType().getName()).append(".").append(method.getName());
//            // 方法名
//            logError.setMethodName(sb.toString());
//            // 保存至 log_error 链表中
//            // LogQueue.offerLogError(logError);
//            // }
//            taskExecutor.execute(new LogThread(logErrorService, logError));
//
//        } catch (IOException e1) {
//
//        }
//
//    }

    public String getDefaultErrorView() {
        return defaultErrorView;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

}
