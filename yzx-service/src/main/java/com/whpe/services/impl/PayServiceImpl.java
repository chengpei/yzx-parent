package com.whpe.services.impl;

import com.unionpay.acp.demo.DemoBase;
import com.unionpay.acp.sdk.SDKConfig;
import com.whpe.services.PayService;
import com.whpe.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService{

    @Value("${unionpay.frontUrl}")
    private String frontUrl;

    @Value("${unionpay.backUrl}")
    private String backUrl;

    @Value("${unionpay.merchantId}")
    private String merchantId;

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
        data.put("merId", "141525048160002");
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
}
