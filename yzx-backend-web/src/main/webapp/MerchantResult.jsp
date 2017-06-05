<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import = "com.abc.pay.client.ebus.*" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="com.whpe.services.AppInterfaceService" %>
<%@ page import="com.whpe.bean.Nhrequestresult" %>
<%@ page import="com.whpe.utils.DateUtils" %>
<%@ page import="com.whpe.bean.NfcCardRecharge" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.whpe.bean.OrderT" %>
<% response.setHeader("Cache-Control", "no-cache"); %>

<%
    Logger logger = Logger.getLogger(Object.class);

    //1、取得MSG参数，并利用此参数值生成支付结果对象
    String msg = request.getParameter("MSG");

    if(msg==null){
        out.print("<script>window.close();</script>");
    }

    PaymentResult tResult = new PaymentResult(msg);

    String TrxType= "";
    String OrderNo ="";
    String Amount = "";
    String BatchNo = "";
    String  VoucherNo = "";
    String hostDateTime ="";
    String MerchantRemarks = "";
    String PayType = "";
    String NotifyType = "";
    String iRspRef = "";
    String ReturnCode ="";
    String ErrorMessage ="";

    //2、判断支付结果状态，进行后续操作
    if (tResult.isSuccess()) {
        //3、支付成功并且验签、解析成功
        TrxType= tResult.getValue("TrxType");
        OrderNo =tResult.getValue("OrderNo");
        Amount = tResult.getValue("Amount" );
        BatchNo = tResult.getValue("BatchNo");
        VoucherNo = tResult.getValue("VoucherNo");
        hostDateTime =tResult.getValue("HostDate")+" "+tResult.getValue("HostTime");
        MerchantRemarks = tResult.getValue("MerchantRemarks");
        PayType = tResult.getValue("PayType");
        NotifyType =  tResult.getValue("NotifyType");
        iRspRef = tResult.getValue("iRspRef") ;
        ErrorMessage =  tResult.getErrorMessage();
        out.println("TrxType         = [" + tResult.getValue("TrxType") + "]<br>");
        out.println("OrderNo         = [" + tResult.getValue("OrderNo") + "]<br>");
        out.println("Amount          = [" + tResult.getValue("Amount" ) + "]<br>");
        out.println("BatchNo         = [" + tResult.getValue("BatchNo") + "]<br>");
        out.println("VoucherNo       = [" + tResult.getValue("VoucherNo") + "]<br>");
        out.println("HostDate        = [" + tResult.getValue("HostDate") + "]<br>");
        out.println("HostTime        = [" + tResult.getValue("HostTime") + "]<br>");
        out.println("MerchantRemarks = [" + tResult.getValue("MerchantRemarks") + "]<br>");
        out.println("PayType         = [" + tResult.getValue("PayType") + "]<br>");
        out.println("NotifyType      = [" + tResult.getValue("NotifyType") + "]<br>");
        out.println("TrnxNo          = [" + tResult.getValue("iRspRef") + "]<br>");
        out.println("ReturnCode   = [" + tResult.getReturnCode() + "]<br>");
        out.println("ErrorMessage = [" + tResult.getErrorMessage() + "]<br>");
        //入库
        ReturnCode =  tResult.getReturnCode();
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        AppInterfaceService appInterfaceService = (AppInterfaceService) applicationContext.getBean("appInterfaceServiceImpl");

        Nhrequestresult nhrequestresult = new Nhrequestresult();
        nhrequestresult.setTrxtype(TrxType);
        nhrequestresult.setOrderno(OrderNo);
        nhrequestresult.setAmount(Double.valueOf(Amount));
        nhrequestresult.setBatchno(BatchNo);
        nhrequestresult.setVoucherno(VoucherNo);
        nhrequestresult.setHostdatetime(DateUtils.getDateForString(hostDateTime, "yyyy-MM-dd HH:mm:ss"));
        nhrequestresult.setMerchantremarks(MerchantRemarks);
        nhrequestresult.setPaytype(PayType);
        nhrequestresult.setNotifytype(NotifyType);
        nhrequestresult.setIrspref(iRspRef);

        int saveAbcRequestResult = appInterfaceService.saveAbcRequestResult(nhrequestresult);

        if(OrderNo.startsWith("M")){
            // 商城订单
            OrderT order = new OrderT();
            order.setOrderId(OrderNo);
            order.setPayState("1");
            if(appInterfaceService.updateMallOrder(order)) {
                logger.info("订单状态更新成功，订单【"+OrderNo+"】更新为【"+order.getPayState()+"】");
            }
        }else{
            NfcCardRecharge nfcCardRecharge = new NfcCardRecharge();
            nfcCardRecharge.setOrderno(OrderNo);
            nfcCardRecharge.setBackrcvresponse("01");
            nfcCardRecharge.setCommcode("103881739010024"); // 农行商户编号
            nfcCardRecharge.setPaytype("06");
            if(appInterfaceService.updateNfcCardRechargeOrder(nfcCardRecharge)){
                logger.info("订单状态更新成功，订单【"+OrderNo+"】更新为【"+nfcCardRecharge.getBackrcvresponse()+"】");
            }
        }

        if(saveAbcRequestResult > 0){
            logger.info("入库成功");
        }else{
            logger.info("入库失败");
        }
    }
    else {
        //4、支付成功但是由于验签或者解析报文等操作失败
        logger.info("ReturnCode   = [" + tResult.getReturnCode  () + "]<br>");
        logger.info("ErrorMessage = [" + tResult.getErrorMessage() + "]<br>");
        ReturnCode = tResult.getReturnCode();
        ErrorMessage = tResult.getErrorMessage();
    }
%>

<HTML>
<HEAD><TITLE>农行网上支付平台-支付结果接收</TITLE>
    <script type="text/javascript">
        function CallAndroid(orderNo,amount,returnCode, msg)
        {
            CallbackBtn.OnClick(orderNo,amount,returnCode, msg);
        }
    </script>
</HEAD>
<BODY BGCOLOR='#FFFFFF' TEXT='#000000' LINK='#0000FF' VLINK='#0000FF' ALINK='#FF0000' onload="CallAndroid('<%=OrderNo%>','<%=Amount %>','<%=ReturnCode%>','<%=ErrorMessage%>')">
<CENTER>支付结果<br>
    <!--<input type = "button" value="callback" onclick="CallAndroid('<%=OrderNo%>','<%=Amount %>','<%=ReturnCode%>','<%=ErrorMessage%>')"/>  -->


</BODY></HTML>