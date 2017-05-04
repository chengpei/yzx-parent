package com.unionpay.acp.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import Common.Global;
//
//import com.mac.Des;
import com.unionpay.acp.sdk.SDKConfig;

/**
 * 
 * 
 * 名称： 第一卷 商户卷 第5部分 手机支付 ——手机控件支付产品<br>
 * 功能： 6.2　消费类交易<br>
 * 前台交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form_6_2_AppConsume extends DemoBase {

	public static void main(String[] args) {

		/**
		 * 参数初始化
		 * 在java main 方式运行时必须每次都执行加载
		 * 如果是在web应用开发里,这个方写在可使用监听的方式写入缓存,无须在这出现
		 */
		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件

//		String returnIp = Global.CongfigMap.get("pay.returnIP").toString();
//		String port = Global.CongfigMap.get("pay.port").toString();
		/**
		 * 组装请求报文
		 */
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
//		data.put("frontUrl", "http://"+returnIp+":"+port+"/NetServer_NFC_XT/acp_front_url.do");
		// 后台通知地址
//		data.put("backUrl", "http://"+returnIp+":"+port+"/NetServer_NFC_XT/acp_back_url.do");
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId", "898430341111002");
		// 商户订单号，8-40位数字字母
		data.put("orderId", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 订单发送时间，取系统时间
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额，单位分
		data.put("txnAmt", "00000100");
		// 交易币种
		data.put("currencyCode", "156");
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");
		// 订单描述，可不上送，上送时控件中会显示该信息
		// data.put("orderDesc", "订单描述");

		data = signData(data);

		// 交易请求url 从配置文件读取
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();

		Map<String, String> resmap = submitUrl(data, requestAppUrl);

		System.out.println("请求报文=["+data.toString()+"]");
		System.out.println("应答报文=["+resmap.toString()+"]");
		String resmsg = resmap.get("respMsg");
		String respCode = resmap.get("respCode");
		String tn = resmap.get("tn");
		System.out.println(resmsg);
		System.out.println(tn);
		System.out.println(respCode);
		//System.out.println("[{txnType=01, respCode=11, frontUrl=http://111.4.120.231:8082/NetServer_NFC_XT/acp_front_url.do, channelType=08, currencyCode=156, merId=898430341111002, txnSubType=01, version=5.0.0, txnAmt=1, signMethod=01, backUrl=http://111.4.120.231:8082/NetServer_NFC_XT/acp_back_url.do, certId=68759585097, encoding=UTF-8, bizType=000201, respMsg=[9100004]Signature verification failed, orderId=20160307144751, signature=U2DHPgi/AqSNQzmfXtkFBRhLpr7yh6Epj5aEh7KK2hg7LYVg9tZ1/XyChyqJ28D989IQ7hEjkZ3B/9Yh/x7/ILWkp5kNXuiyvfkknQWV8k/t5D9VyBEkNiPpMqQpYwr+I+3qgNzgKhw9hR2pd2qpJjFdFfZzCfFmucq74xVmqmmHwDoHPKUqftvnloLSxlDCT0w06nafZAhxxGRHnjROE5Qg28lSf7qfgbt4oaxLCRY/11TxnbDR9vL4nKCGVJQesGKPrYLrQQGLyQLvaxRk9l/q6MCvRRLjthb6neoZFqBRjh3MIXHp0QQ24e2c7ZHFn46BRJTPyTwmcO/sy4f7mg==, txnTime=20160307144751, accessType=0}]".length());
		
	}

	
	
	
}
