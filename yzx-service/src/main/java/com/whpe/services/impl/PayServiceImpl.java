package com.whpe.services.impl;

import com.abc.pay.client.Constants;
import com.abc.pay.client.MerchantConfig;
import com.abc.pay.client.MerchantPara;
import com.abc.pay.client.TrxException;
import com.abc.pay.client.ebus.PaymentRequest;
import com.unionpay.acp.demo.DemoBase;
import com.unionpay.acp.sdk.SDKConfig;
import com.whpe.services.CommonService;
import com.whpe.services.PayService;
import com.whpe.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class PayServiceImpl extends CommonService implements PayService{

    @Value("${unionpay.frontUrl}")
    private String frontUrl;

    @Value("${unionpay.backUrl}")
    private String backUrl;

    @Value("${unionpay.merchantId}")
    private String merchantId;

    @Value("${abcpay.backUrl}")
    private String abcBackUrl;

    @PostConstruct
    public void init(){
        // 从classpath加载acp_sdk.properties文件
        SDKConfig.getConfig().loadPropertiesFromSrc();
    }

    @Override
    public String getUnionPayTN(String orderNo, String ordermount) {
        Map<String, String> data = new HashMap<String, String>();
        // 版本号
        data.put("version", "5.0.0");
        // 字符集编码 默认"UTF-8"
        data.put("encoding", "UTF-8");
        // 签名方法 01 RSA
        data.put("signMethod", "01");
        // 交易类型 01-消费
        data.put("txnType", "01");
        // 交易子类型 01:自助消费 02:订购 03:分期付款
        data.put("txnSubType", "01");
        // 业务类型
        data.put("bizType", "000201");
        // 渠道类型，07-PC，08-手机
        data.put("channelType", "08");
        // 前台通知地址 ，控件接入方式无作用
        data.put("frontUrl", frontUrl);
        // 后台通知地址
        data.put("backUrl", backUrl);
        // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
        data.put("accessType", "0");
        // 商户号码，请改成自己的商户号898430341111001 898430341111002 141052004816001
        data.put("merId", merchantId);
        // 商户订单号，8-40位数字字母
        data.put("orderId", orderNo);
        // 订单发送时间，取系统时间
        data.put("txnTime", DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss"));
        // 交易金额，单位分
        data.put("txnAmt", ordermount);
        // 交易币种
        data.put("currencyCode", "156");
        // 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
        // data.put("reqReserved", "透传信息");
        // 订单描述，可不上送，上送时控件中会显示该信息
        // data.put("orderDesc", "订单描述");
        data = DemoBase.signData(data);

        // 交易请求url 从配置文件读取
        String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();

        Map<String, String> resmap = DemoBase.submitUrl(data, requestAppUrl);

        String respCode = resmap.get("respCode");
        String tn = resmap.get("tn");
        if(!"00".equals(respCode)){
            return null;
        }
        return  tn;
    }

    @Override
    public boolean generateAbcPayHtml(String orderNo, String orderMount) {
        String paymentType = "A";
        String ResultNotifyURL = abcBackUrl;
        String[] returnMsg = new String[2];
        MerchantPara merchantPara = getMerchantPara(orderNo, orderMount, "", paymentType, ResultNotifyURL, returnMsg);
        Map<String, String> hiddens = new HashMap<String, String>();
        String sTrustPayIETrxURL = merchantPara.getTrustPayTrxIEURL();
        String sErrorUrl = merchantPara.getMerchantErrorURL();
        String tSignature = returnMsg[0];
        hiddens.put("MSG", tSignature);
        hiddens.put("errorPage", sErrorUrl);

        String htmlTxt = DemoBase.createHtml(sTrustPayIETrxURL, hiddens);
        logger.info(htmlTxt);
        //创建文件
        String webappRoot = System.getProperty("webapp.root");
        logger.debug("webappRoot == " + webappRoot);
        File abcPayHtmlFile = new File(webappRoot + File.separator + "abcPayHtml");
        if(!abcPayHtmlFile.exists()){
            abcPayHtmlFile.mkdirs();
        }
        File file = new File(webappRoot + File.separator + "abcPayHtml" + File.separator + orderNo + ".html");
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] dat = htmlTxt.getBytes("GBK");
            out.write(dat);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            logger.error("generateAbcPayHtml 异常", e);
            return false;
        }
    }

    /**
     *
     * @param orderNo 订单号
     * @param paymentRequestAmount 支付金额 单位分
     * @param buyIP    支付IP
     * @param paymentType ：农行卡支付 2：国际卡支付 3：农行贷记卡支付 5:基于第三方的跨行支付 A:支付方式合并 6：银联跨行支付，7:对公户 *必
     * @param ResultNotifyURL  异步通知URL
     * @return
     */
    @SuppressWarnings("unchecked")
    public static MerchantPara getMerchantPara(String orderNo,String paymentRequestAmount,String buyIP,String paymentType,String ResultNotifyURL, String[] returnMsg){
        //1、生成订单对象
        PaymentRequest tPaymentRequest = new PaymentRequest();
        DecimalFormat decimal = new DecimalFormat("#.00");
        //订单金额保留两位小数
        double o = Integer.parseInt(paymentRequestAmount)/100.00;

        tPaymentRequest.dicOrder.put("PayTypeID", "ImmediatePay");                   //设定交易类型
        Date currDate = new Date();
        tPaymentRequest.dicOrder.put("OrderDate", DateUtils.getFormatDate(currDate, "yyyy/MM/dd"));//设定订单日期 （必要信息 - YYYY/MM/DD）
        tPaymentRequest.dicOrder.put("OrderTime", DateUtils.getFormatDate(currDate, "HH:mm:ss"));  //设定订单时间 （必要信息 - HH:MM:SS）

        Calendar caltemp = Calendar.getInstance();
        caltemp.add(Calendar.HOUR, 1);

        tPaymentRequest.dicOrder.put("orderTimeoutDate", DateUtils.getFormatDate(caltemp.getTime(), "yyyyMMddHHmmss"));//设定订单有效期

        tPaymentRequest.dicOrder.put("OrderNo", orderNo);        //设定订单编号 （必要信息）
        tPaymentRequest.dicOrder.put("CurrencyCode", "156");             //设定交易币种
        tPaymentRequest.dicOrder.put("OrderAmount", decimal.format(o));      //设定交易金额 保留小数点后两位数字,*必输
        tPaymentRequest.dicOrder.put("Fee", "");                               //设定手续费金额
        tPaymentRequest.dicOrder.put("OrderDesc","");                   //设定订单说明
        tPaymentRequest.dicOrder.put("OrderURL", "");                     //设定订单地址
        tPaymentRequest.dicOrder.put("ReceiverAddress","宜昌公交集团");       //收货地址
        tPaymentRequest.dicOrder.put("InstallmentMark", "0");       //分期标识 1：分期；0：否。*必输
        tPaymentRequest.dicOrder.put("CommodityType", "0101");           //设置商品种类0101:支付账户充值
        //0201:虚拟类,0202:传统类,0203:实名类
        //0301:本行转账,0302:他行转账
        //0401:水费,0402:电费,0403:煤气费,0404:有线电视费,0405:通讯费,0406:物业费,0407:保险费,0408:行政费用,0409:税费,0410:学费,0499:其他
        //0501:基金,0502:理财产品,0599:其他, *必输

        tPaymentRequest.dicOrder.put("BuyIP", buyIP);                           //IP
        tPaymentRequest.dicOrder.put("ExpiredDate", "30");               //设定订单保存时间
        //2、订单明细
        LinkedHashMap<String, String> orderitem = new LinkedHashMap<String, String>();
        orderitem.put("SubMerName", "宜昌公交");    //设定二级商户名称
        orderitem.put("SubMerId", "103881739010024");    //设定二级商户代码
        orderitem.put("SubMerMCC", "0000");   //设定二级商户MCC码
        orderitem.put("SubMerchantRemarks", "");   //二级商户备注项
        orderitem.put("ProductID", "CZ000001");//商品代码，预留字段
        orderitem.put("ProductName", "空中充值服务");//商品名称
        orderitem.put("UnitPrice", "1.00");//商品总价
        orderitem.put("Qty", "1");//商品数量
        orderitem.put("ProductRemarks", "空中圈存"); //商品备注项
        orderitem.put("ProductType", "充值类");//商品类型
        orderitem.put("ProductDiscount", "1");//商品折扣
        orderitem.put("ProductExpiredDate", "10");//商品有效期
        tPaymentRequest.orderitems.put(1, orderitem);

		/*orderitem = new LinkedHashMap<String, String>();
		orderitem.put("SubMerName", "宜昌公交");    //设定二级商户名称
		orderitem.put("SubMerId", "12345");    //设定二级商户代码
		orderitem.put("SubMerMCC", "0000");   //设定二级商户MCC码
		orderitem.put("SubMerchantRemarks", "测试");   //二级商户备注项
		orderitem.put("ProductID", "CZ000001");//商品代码，预留字段
		orderitem.put("ProductName", "中国移动IP卡");//商品名称
		orderitem.put("UnitPrice", "1.00");//商品总价
		orderitem.put("Qty", "2");//商品数量
		orderitem.put("ProductRemarks", "测试商品"); //商品备注项
		orderitem.put("ProductType", "充值类");//商品类型
		orderitem.put("ProductDiscount", "0.9");//商品折扣
		orderitem.put("ProductExpiredDate", "10");//商品有效期
		tPaymentRequest.orderitems.put(2, orderitem);*/

        //3、生成支付请求对象
        //String paymentType = request.getParameter("PaymentType");
        tPaymentRequest.dicRequest.put("PaymentType", paymentType);            //设定支付类型
        String paymentLinkType = "2";
        tPaymentRequest.dicRequest.put("PaymentLinkType", paymentLinkType);    //设定支付接入方式
        if (paymentType.equals(Constants.PAY_TYPE_UCBP) && paymentLinkType.equals(Constants.PAY_LINK_TYPE_MOBILE))
        {
            tPaymentRequest.dicRequest.put("UnionPayLinkType","1");  //当支付类型为6，支付接入方式为2的条件满足时，需要设置银联跨行移动支付接入方式
        }

        tPaymentRequest.dicRequest.put("ReceiveAccount", "");    //设定收款方账号
        tPaymentRequest.dicRequest.put("ReceiveAccName", "");    //设定收款方户名
        tPaymentRequest.dicRequest.put("NotifyType", "1");    //设定通知方式0：URL页面通知 1：服务器通知，*必输
        tPaymentRequest.dicRequest.put("ResultNotifyURL",ResultNotifyURL);    //设定通知URL地址
        tPaymentRequest.dicRequest.put("MerchantRemarks","充值扣款");    //设定附言
        tPaymentRequest.dicRequest.put("IsBreakAccount", "0");    //设定交易是否分账
        tPaymentRequest.dicRequest.put("SplitAccTemplate", "");      //分账模版编号

        MerchantPara para =  null;
        try {
            para = MerchantConfig.getUniqueInstance().getPara();
            String tSignature = tPaymentRequest.genSignature(1);
            returnMsg[0] = tSignature;
        } catch (TrxException e) {
            e.printStackTrace();
            return null;
        }

        return para;
    }
}
