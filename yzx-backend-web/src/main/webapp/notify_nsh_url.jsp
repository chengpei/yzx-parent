<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.whpe.services.AppInterfaceService" %>
<%@ page import="com.whpe.bean.Nshresresult" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.whpe.bean.NfcCardRecharge" %>
<%@ page contentType="text/html; charset=utf-8" %>
<% response.setHeader("Cache-Control", "no-cache"); %>
<%
  //1、取得MSG参数，并利用此参数值生成支付结果对象
  String branchId = request.getParameter("branchId");//成员行编号
  String merCode = request.getParameter("merCode");//商户编号
  String orderNum = request.getParameter("orderNum");//订单号
  String orderAmt = request.getParameter("orderAmt");//订单金额 单位元 2.00
  String curType = request.getParameter("curType");//币种
  String orderDate = request.getParameter("orderDate");//yyyyMMdd
  String orderTime=request.getParameter("orderTime");//HHmmss
  String tranSerialNo = request.getParameter("tranSerialNo");//交易流水号
  String tranResult= request.getParameter("tranResult");//10： 处理中 20： 交易成功30： 交易失败99： 交易可疑
  String comments = request.getParameter("comment");//失败原因
  String merRemarks = request.getParameter("merRemarks");//商户备注
  String signDataStr = request.getParameter("signDataStr");//签名原数据
  String signData = request.getParameter("signData");//签名原数据
  String payType =  request.getParameter("payType");//签名原数据


/* 
String signDataSrc1=new String(Base64.encode(signDataStr.getBytes("GB2312")));
Svs2ClientHelper helper = Svs2ClientHelper.getInstance();
//初始化签名证书和私钥，即.pfx(或.p12)文件名和口令（口令不能为空）
String pfx =  Global.CongfigMap.getProperty("nsh.pfx");
String psw =  Global.CongfigMap.getProperty("nsh.psw");
helper.setPfx_NXY(pfx,psw);
String verify_nxy = "0";			
SvsResultData  r = helper.pkcs7AttachVerify_NXY(signData, signDataSrc1.getBytes());
			
if(r.m_errno!=0){
	verify_nxy = "失败:"+r.m_errno;
} */
//入库
  Logger logger = Logger.getLogger(Object.class);
  WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
  AppInterfaceService appInterfaceService = (AppInterfaceService) applicationContext.getBean("appInterfaceService");
  Nshresresult nshresresult = new Nshresresult();
  nshresresult.setBranchid(branchId);
  nshresresult.setMercode(merCode);
  nshresresult.setOrdernum(orderNum);
  nshresresult.setOrderamt(orderAmt);
  nshresresult.setCurtype(curType);
  nshresresult.setOrderdate(orderDate);
  nshresresult.setOrdertime(orderTime);
  nshresresult.setTranserialno(tranSerialNo);
  nshresresult.setTranresult(tranResult);
  nshresresult.setComment(comments);
  nshresresult.setMerremarks(merRemarks);
  nshresresult.setSigndata(signData);
  nshresresult.setPaytype(payType);

  int saveNshRequestResult = appInterfaceService.saveNshRequestResult(nshresresult);

  NfcCardRecharge nfcCardRecharge = new NfcCardRecharge();

  nfcCardRecharge.setOrderno(orderNum);
  nfcCardRecharge.setBackrcvresponse("01");
  nfcCardRecharge.setCommcode("0000100606"); // 农信商户编号
  if(appInterfaceService.updateNfcCardRechargeOrder(nfcCardRecharge)){
    logger.info("订单状态更新成功，订单【"+orderNum+"】更新为【"+nfcCardRecharge.getBackrcvresponse()+"】");
  }
  if (saveNshRequestResult > 0){
    logger.info("入库成功");
  }else{
    logger.info("入库失败");
  }

  out.print("success");

%>
<HTML>
<HEAD><TITLE>农商行网上支付平台-支付结果接收</TITLE>
  <script type="text/javascript">
    function CallAndroid_NSH(orderNo,amount,returnCode, msg)
    {
      CallbackBtn.OnClick(orderNo,amount,returnCode, msg);
    }
  </script>
</HEAD>
<BODY BGCOLOR='#FFFFFF' TEXT='#000000' LINK='#0000FF' VLINK='#0000FF' ALINK='#FF0000' onload="CallAndroid_NSH('<%=orderNum%>','<%=orderAmt %>','<%=tranResult%>','<%=comments%>')">
<CENTER>支付结果<br>

  <!--<input type = "button" value="callback" onclick="CallAndroid_NSH('<%=orderNum%>','<%=orderAmt %>','<%=tranResult%>','<%=comments%>')"/>
  -->

</BODY></HTML>