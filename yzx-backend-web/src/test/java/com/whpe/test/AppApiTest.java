package com.whpe.test;

import com.alibaba.fastjson.JSON;
import com.whpe.bean.*;
import com.whpe.bean.vo.OrderVO;
import com.whpe.dao.yckq.NfcCardRechargeMapper;
import com.whpe.dao.yckq.OrderTMapper;
import com.whpe.dao.yckq.SmsSendLogMapper;
import com.whpe.dao.yckq.SysAppUserMapper;
import com.whpe.services.AppInterfaceService;
import com.whpe.services.LoginRegisterService;
import com.whpe.services.SendSMSService;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;

/**
 * Created by chengpei on 2017/2/26.
 */
public class AppApiTest extends SpringTestContext{

    @Resource
    private SendSMSService sendSMSService;

    @Resource
    private LoginRegisterService loginRegisterService;

    @Resource
    private SmsSendLogMapper smsSendLogMapper;

    @Resource
    private SysAppUserMapper sysAppUserMapper;

    @Resource
    private NfcCardRechargeMapper nfcCardRechargeMapper;

    @Resource
    private AppInterfaceService appInterfaceService;

    @Resource
    private OrderTMapper orderTMapper;

    @Test
    public void sendSMSTest() throws Exception {
        SmsSendLog smsSendLog = new SmsSendLog();
        smsSendLog.setAcceptPhone("13476073978");
        smsSendLog.setSmsContent("尊敬的13476073978您本次验证码为123456");
        boolean b = sendSMSService.sendSms(smsSendLog);
        logger.info("发送结果 == " + b);
    }

    @Test
    public void test1() throws IOException {
        System.out.println(smsSendLogMapper.countCurrDaySMS("13476073978"));
    }

    @Test
    public void test2() throws IOException {
        SysAppUser sysAppUser = new SysAppUser();
        sysAppUser.setuPhone("13476073978");
        sysAppUser.setuEnabled("2");
        SysAppUser appUser = sysAppUserMapper.selectByCondition(sysAppUser);
        System.out.println(appUser);
    }

    @Test
    public void test3() throws IOException {
//        loginRegisterService.doRegister("13476073978", "123456");
        logger.info("发送次数 === " + loginRegisterService.countCurrDaySMS("15629169373"));
    }

    @Test
    public void loginTest() throws IOException {
        SysAppUser sysAppUser = loginRegisterService.doLogin("13476073978", "123456");
        System.out.println(sysAppUser);
    }

    @Test
    public void testSMSTemplate(){
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("phoneNumber", "123456");
        params.put("checkCode", "123456");
        String smsContentByParams = loginRegisterService.getSmsContentByParams("1", params);
        logger.info("短信为 === " + smsContentByParams);
    }

    @Test
    public void testCheckSmsOneMinutes(){
        NfcCardRecharge nfcCardRecharge = new NfcCardRecharge();
        nfcCardRecharge.setOrderno("Y20170503161504000000000000006");
        nfcCardRecharge.setBackrcvresponse("01");
        int i = nfcCardRechargeMapper.updateByPrimaryKeySelective(nfcCardRecharge);
        System.out.println(i);
    }

    @Test
    public void nhInsert() throws ParseException {
//        Nhrequestresult nhrequestresult = new Nhrequestresult();
//        nhrequestresult.setTrxtype("12");
//        nhrequestresult.setOrderno("Y20170507123456000000000000002");
//        nhrequestresult.setAmount(Double.valueOf("50.00"));
//        nhrequestresult.setBatchno("2");
//        nhrequestresult.setVoucherno("2");
//        nhrequestresult.setHostdatetime(DateUtils.getDateForString("2017-05-07 15:12:23", "yyyy-MM-dd HH:mm:ss"));
//        nhrequestresult.setMerchantremarks("12");
//        nhrequestresult.setPaytype("06");
//        nhrequestresult.setNotifytype("0");
//        nhrequestresult.setIrspref("0");
//
//        int saveAbcRequestResult = appInterfaceService.saveAbcRequestResult(nhrequestresult);
//        System.out.println(saveAbcRequestResult);

        Nshresresult nshresresult = new Nshresresult();
        nshresresult.setBranchid("1");
        nshresresult.setMercode("1");
        nshresresult.setOrdernum("1");
        nshresresult.setOrderamt("1");
        nshresresult.setCurtype("1");
        nshresresult.setOrderdate("1");
        nshresresult.setOrdertime("1");
        nshresresult.setTranserialno("1");
        nshresresult.setTranresult("1");
        nshresresult.setComment("1");
        nshresresult.setMerremarks("1");
        nshresresult.setSigndata("1");
        nshresresult.setPaytype("1");

        int saveNshRequestResult = appInterfaceService.saveNshRequestResult(nshresresult);
    }

    @Test
    public void testOrderQuery(){
        OrderVO orderVO = orderTMapper.selectOrderInfoByOrderId("M20170329111711000000000000012");
        System.out.println(JSON.toJSONString(orderVO));
    }

}
