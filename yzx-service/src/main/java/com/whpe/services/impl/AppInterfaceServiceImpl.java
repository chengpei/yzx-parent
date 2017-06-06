package com.whpe.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.whpe.bean.*;
import com.whpe.bean.Dictionary;
import com.whpe.bean.dto.SysPeopleDTO;
import com.whpe.bean.vo.OrderDetailVO;
import com.whpe.bean.vo.OrderVO;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.dao.ycbus.CardInfoMapper;
import com.whpe.dao.ycbus.TSmcardYhInfoMapper;
import com.whpe.dao.ycbus.YhIcKzMapper;
import com.whpe.dao.yckq.*;
import com.whpe.dao.yclyic.TCardInfoMapper;
import com.whpe.dao.yclyic.TYhInfoMapper;
import com.whpe.services.AppInterfaceService;
import com.whpe.services.CommonService;
import com.whpe.services.PayService;
import com.whpe.services.RechargeService;
import com.whpe.utils.DateUtils;
import com.whpe.utils.MessageAnalysisDevice;
import com.whpe.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AppInterfaceServiceImpl extends CommonService implements AppInterfaceService{

    @Resource
    private SysPeopleMapper sysPeopleMapper;

    @Resource
    private AppMycardMapper appMycardMapper;

    @Resource
    private NfcCardRechargeMapper nfcCardRechargeMapper;

    @Resource
    private PayService payService;

    @Resource
    private RechargeService rechargeService;

    @Resource
    private NhrequestresultMapper nhrequestresultMapper;

    @Resource
    private NshresresultMapper nshresresultMapper;

    @Resource
    private TSmcardYhInfoMapper tSmcardYhInfoMapper;

    @Resource
    private CardInfoMapper cardInfoMapper;

    @Resource
    private YhIcKzMapper yhIcKzMapper;

    @Resource
    private TCardInfoMapper tCardInfoMapper;

    @Resource
    private TYhInfoMapper tYhInfoMapper;

    @Resource
    private FeedbackMapper feedbackMapper;

    @Resource
    private DictionaryMapper dictionaryMapper;

    @Resource
    private OrderTMapper orderTMapper;

    @Resource
    private LeaseVouchersMapper leaseVouchersMapper;

    @Override
    public void updateSysPeople(JSONObject requestJson, JSONObject result, HttpSession session) {
        SysPeopleDTO sysPeopleDTO = requestJson.getObject("reqContent", SysPeopleDTO.class);
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        sysPeopleDTO.setuId(appUser.getuId());
        // 检查用户是否存在用户资料
        SysPeople sysPeople = sysPeopleMapper.selectSysPeopleByUid(appUser.getuId());
        if(sysPeople == null){
            sysPeopleDTO.setPuCreatedate(new Date());
            sysPeopleMapper.insertSelective(sysPeopleDTO);
            makeRetInfo("S0000", "修改成功", result);
        }else{
            if (sysPeopleMapper.updateSysPeopleByUid(sysPeopleDTO) > 0){
                makeRetInfo("S0000", "修改成功", result);
            }else{
                makeRetInfo("E0001", "修改失败", result);
            }
        }
    }

    @Override
    public void bindCard(JSONObject requestJson, JSONObject result, HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String cardNo = reqContent.getString("cardNo");
        String remark = reqContent.getString("remark");
        AppMycard appMycard = new AppMycard();
        appMycard.setAppBz(remark);
        appMycard.setAppFxkh(cardNo);
        appMycard.setAppUserid(appUser.getuId());
        appMycard.setAppCreatedate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        // 查询要绑定的卡号是否存在
        CardInfo cardInfo = cardInfoMapper.selectByPrimaryKey(cardNo);
        if(cardInfo == null){
            makeRetInfo("E0001", "该卡号不存在", result);
            return;
        }

        // 判断是否已经绑定过该卡
        List<AppMycard> mycardList = appMycardMapper.selectByCondition(appMycard);
        if(mycardList != null && mycardList.size() > 0){
            makeRetInfo("E0001", "已经绑定过该卡", result);
            return;
        }
        // 判断绑定的卡是否超过十张
        AppMycard param = new AppMycard();
        param.setAppUserid(appUser.getuId());
        List<AppMycard> mycards = appMycardMapper.selectByCondition(param);
        if(mycards != null && mycards.size() > 9){
            makeRetInfo("E0001", "绑定卡数量超过上线", result);
            return;
        }
        if(appMycardMapper.insertSelective(appMycard) > 0){
            makeRetInfo("S0000", "绑定成功", result);
        }else {
            makeRetInfo("E0001", "绑定失败", result);
        }
    }

    @Override
    public void queryMyCard(JSONObject requestJson, JSONObject result, HttpSession session){
        // 查询绑定的卡
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        AppMycard param = new AppMycard();
        param.setAppUserid(appUser.getuId());
        List<AppMycard> mycardList = appMycardMapper.selectByCondition(param);
        if(mycardList != null && mycardList.size() > 0){
            JSONArray retContent = JSONArray.parseArray(JSON.toJSONString(mycardList));
            putRetContent(retContent, result);
            makeRetInfo("S0000", "查询成功", result);
        }else{
            makeRetInfo("S0001", "未查询到数据", result);
            return;
        }
    }

    @Override
    public void commitOrder(JSONObject requestJson, JSONObject result, HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String orderNo = generateOrderNo(common.getString("txChan"));
        String fxkh = reqContent.getString("cardNo");
        String orderMount = reqContent.getString("orderMount");
        String tradeType = reqContent.getString("tradeType");
        if(StringUtils.isEmpty(fxkh) || StringUtils.isEmpty(orderMount)){
            makeRetInfo("E0001", "卡号和金额不能为空", result);
            return;
        }
        if(StringUtils.isEmpty(tradeType)){
            makeRetInfo("E0001", "交易类型", result);
            return;
        }
        if(Integer.parseInt(orderMount) > 800000){
            makeRetInfo("E0001", "订单金额不能大于800元", result);
            return;
        }
        NfcCardRecharge nfcCardRecharge = new NfcCardRecharge();
        nfcCardRecharge.setCardno(fxkh);
        nfcCardRecharge.setOrderseq(orderNo);
        nfcCardRecharge.setOrderno(orderNo);
        nfcCardRecharge.setOrdermount(orderMount);
        nfcCardRecharge.setPhoneno(appUser.getuPhone());
        nfcCardRecharge.setOrdertime(new Date());
        nfcCardRecharge.setSuccess("0");
        nfcCardRecharge.setChsj(new Date());
        nfcCardRecharge.setJylx("24");// 24-补登
        nfcCardRecharge.setImei("100000000001");
        nfcCardRecharge.setJylx(tradeType);
        if(nfcCardRechargeMapper.insertSelective(nfcCardRecharge) > 0){
            putRetContent("orderNo", orderNo, result);
            makeRetInfo("S0000", "提交成功", result);
        }else{
            makeRetInfo("E0001", "提交失败", result);
        }
    }

    @Override
    public void generateOrderNo(JSONObject requestJson, JSONObject result, HttpSession session){
        JSONObject common = requestJson.getJSONObject("common");
        String orderNo = generateOrderNo(common.getString("txChan"));
        putRetContent("orderNo", orderNo, result);
        makeRetInfo("S0000", "提交成功", result);
    }

    @Override
    public void applyPay(JSONObject requestJson, JSONObject result, HttpSession session) {
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String orderNo = reqContent.getString("orderNo");
        String payType = reqContent.getString("payType");
        // 根据订单号查询订单信息
        NfcCardRecharge nfcCardRecharge = nfcCardRechargeMapper.selectByPrimaryKey(orderNo);
        if(nfcCardRecharge == null){
            makeRetInfo("E0001", "订单不存在", result);
            return;
        }
        if("01".equals(nfcCardRecharge.getBackrcvresponse())){
            makeRetInfo("E0001", "订单已支付过", result);
            return;
        }
        if("08".equals(payType)){
            //银联支付
            String tn = payService.getUnionPayTN(orderNo, nfcCardRecharge.getOrdermount());
            if(StringUtils.isNotEmpty(tn)){
                putRetContent("tn", tn, result);
                makeRetInfo("S0000", "申请成功", result);
                return;
            }else {
                makeRetInfo("E0001", "申请失败", result);
                return;
            }
        }else if("06".equals(payType)){
            // 农行支付
            if (payService.generateAbcPayHtml(orderNo, nfcCardRecharge.getOrdermount())){
                HttpServletRequest request = (HttpServletRequest) session.getAttribute("javax.servlet.http.HttpServletRequest");
                String url = request.getScheme() + "://" + request.getServerName() + ":"
                        + request.getServerPort() + request.getContextPath() + "/abcPayHtml/" + orderNo + ".html";
                putRetContent("url", url, result);
                makeRetInfo("S0000", "申请成功", result);
                return;
            }else {
                makeRetInfo("E0001", "申请失败", result);
                return;
            }
        }else if("07".equals(payType)){
            // 农信支付
            if(payService.generateNxhPayHtml(orderNo, nfcCardRecharge.getOrdermount())){
                HttpServletRequest request = (HttpServletRequest) session.getAttribute("javax.servlet.http.HttpServletRequest");
                String url = request.getScheme() + "://" + request.getServerName() + ":"
                        + request.getServerPort() + request.getContextPath() + "/nxhPayHtml/" + orderNo + ".html";
                putRetContent("url", url, result);
                makeRetInfo("S0000", "申请成功", result);
                return;
            }else {
                makeRetInfo("E0001", "申请失败", result);
                return;
            }
        }else{
            makeRetInfo("E0001", "不支持的支付方式", result);
            return;
        }
    }

    @Override
    public void applyMallPay(JSONObject requestJson, JSONObject result, HttpSession session) {
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String orderId = reqContent.getString("orderId");
        String payType = reqContent.getString("payType");
        // 根据订单号查询订单信息
        OrderVO orderInfo = orderTMapper.selectOrderInfoByOrderId(orderId);
        if(orderInfo == null){
            makeRetInfo("E0001", "订单不存在", result);
            return;
        }
        if("1".equals(orderInfo.getPayState())){
            makeRetInfo("E0001", "订单已支付过", result);
            return;
        }
        if("08".equals(payType)){
            //银联支付
            String tn = payService.getUnionPayTN(orderInfo.getOrderId(), orderInfo.getRealGetMoney()+"");
            if (StringUtils.isNotEmpty(tn)){
                putRetContent("tn", tn, result);
                makeRetInfo("S0000", "申请成功", result);
                return;
            }else {
                makeRetInfo("E0001", "申请失败", result);
                return;
            }
        }else if("06".equals(payType)){
            // 农行支付
            if (payService.generateAbcPayHtml(orderInfo.getOrderId(), orderInfo.getRealGetMoney()+"")){
                HttpServletRequest request = (HttpServletRequest) session.getAttribute("javax.servlet.http.HttpServletRequest");
                String url = request.getScheme() + "://" + request.getServerName() + ":"
                        + request.getServerPort() + request.getContextPath() + "/abcPayHtml/" + orderId + ".html";
                putRetContent("url", url, result);
                makeRetInfo("S0000", "申请成功", result);
                return;
            }else {
                makeRetInfo("E0001", "申请失败", result);
                return;
            }
        }else if("07".equals(payType)){
            // 农信支付
            if(payService.generateNxhPayHtml(orderInfo.getOrderId(), orderInfo.getRealGetMoney()+"")){
                HttpServletRequest request = (HttpServletRequest) session.getAttribute("javax.servlet.http.HttpServletRequest");
                String url = request.getScheme() + "://" + request.getServerName() + ":"
                        + request.getServerPort() + request.getContextPath() + "/nxhPayHtml/" + orderId + ".html";
                putRetContent("url", url, result);
                makeRetInfo("S0000", "申请成功", result);
                return;
            }else {
                makeRetInfo("E0001", "申请失败", result);
                return;
            }
        }else{
            makeRetInfo("E0001", "不支持的支付方式", result);
            return;
        }
    }

    @Override
    public void payConfirm(JSONObject requestJson, JSONObject result, HttpSession session) {
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String orderNo = reqContent.getString("orderNo");
        String payType = reqContent.getString("payType");
        logger.info("订单号【"+orderNo+"】，支付类型【"+payType+"】，支付确认！");

        // 根据订单号查询订单信息
        if(orderNo.startsWith("M")){
            OrderVO orderInfo = orderTMapper.selectOrderInfoByOrderId(orderNo);
            if(orderInfo == null){
                makeRetInfo("E0001", "订单不存在", result);
                return;
            }
        }else {
            NfcCardRecharge nfcCardRecharge = nfcCardRechargeMapper.selectByPrimaryKey(orderNo);
            if(nfcCardRecharge == null){
                makeRetInfo("E0001", "订单不存在", result);
                return;
            }
        }

        // TODO 支付确认
        makeRetInfo("S0000", "正在开发中...", result);
        return;
    }

    @Override
    public void generateLeaseVouchers(JSONObject requestJson, JSONObject result, HttpSession session) {
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String orderId = reqContent.getString("orderId");
        String offerType = reqContent.getString("offerType");
        OrderVO orderInfo = orderTMapper.selectOrderInfoByOrderId(orderId);
        if(orderInfo == null){
            makeRetInfo("E0001", "订单不存在", result);
            return;
        }
        if("1".equals(orderInfo.getPayState())){
            makeRetInfo("E0001", "订单未支付", result);
            return;
        }
        if(!orderInfo.getUserId().equals(appUser.getuId())){
            makeRetInfo("E0001", "订单不属于当前用户", result);
            return;
        }

        // 查询该订单是否已经生成过凭证串码
        LeaseVouchers vouchers1 = leaseVouchersMapper.selectByOrderId(orderId);
        if(vouchers1 != null){
            putRetContent("leaseVouchers", vouchers1.getVouchers(), result);
            makeRetInfo("S0000", "查询成功", result);
            return;
        }

        // 判断该订单 商品类型是否可以生成凭证串码
        List<OrderDetailVO> orderDetailList = orderInfo.getOrderDetailList();
        if(orderDetailList == null || orderDetailList.size() == 0){
            makeRetInfo("E0001", "异常订单", result);
            return;
        }
        boolean canVouchers = false;
        for (OrderDetailVO orderDetail : orderDetailList){
            if("BUS_LEASE".equals(orderDetail.getProductOffer().getPriceType())){
                canVouchers = true;
            }
        }
        if(!canVouchers){
            // 如果订单下的所有商品都不包含需要凭证类的商品，那么订单不能生成凭证串码
            makeRetInfo("E0001", "该订单不能生成串码", result);
            return;
        }

        // 生成凭证串码
        String vouchers = generateVouchers();
        LeaseVouchers leaseVouchers = new LeaseVouchers();
        leaseVouchers.setOrderId(orderId);
        leaseVouchers.setCreateTime(new Date());
        leaseVouchers.setUserId(appUser.getuId());
        leaseVouchers.setVouchers(vouchers);
        leaseVouchersMapper.insertSelective(leaseVouchers);

        // TODO 发送短信


        putRetContent("leaseVouchers", vouchers, result);
        makeRetInfo("S0000", "查询成功", result);
        return;
    }

    @Override
    public void applyRecharge(JSONObject requestJson, JSONObject result, HttpSession session) {
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String initializeForLoad = reqContent.getString("initializeForLoad");
        String cardNo = reqContent.getString("cardNo");
        String orderNo = reqContent.getString("orderNo");
        String wlkh = reqContent.getString("wlkh");
        if(StringUtils.isEmpty(initializeForLoad) || StringUtils.isEmpty(cardNo) || StringUtils.isEmpty(orderNo)){
            makeRetInfo("E0001", "参数不能为空", result);
            return;
        }
        InitializeForLoadBean data8050Bean = analysisInitializeForLoad(initializeForLoad);
        NfcCardRecharge nfcCardRecharge = nfcCardRechargeMapper.selectByPrimaryKey(orderNo);
        if(nfcCardRecharge == null){
            makeRetInfo("E0001", "订单不存在", result);
            return;
        }
        if(!cardNo.equals(nfcCardRecharge.getCardno())){
            makeRetInfo("E0001", "补登订单不属于该卡", result);
            return;
        }
        if(!"01".equals(nfcCardRecharge.getBackrcvresponse())){
            makeRetInfo("E0001", "订单未支付", result);
            return;
        }
        int rechargeMoney = Integer.parseInt(nfcCardRecharge.getOrdermount());
        int oldBalanceInt = Integer.parseInt(data8050Bean.getOldBalance(), 16);
        if(rechargeMoney + oldBalanceInt > 80000){
            makeRetInfo("E0001", "卡余额不能超过800", result);
            return;
        }
        if(nfcCardRecharge.getJyxh() != null
                && nfcCardRecharge.getJyxh().longValue() != Long.parseLong(data8050Bean.getTradeSeq(), 16)){
            // 订单中充值计数器不为空，并且和圈存初始化 计数器不相等，说明此次充值已经完成
            // 更新订单为充值成功状态
            nfcCardRecharge.setSuccess("1");// 写卡成功
            nfcCardRechargeMapper.updateByPrimaryKeySelective(nfcCardRecharge);
            makeRetInfo("E0001", "请勿重复充值", result);
            return;
        }

        // 校验mac1
        if(!rechargeService.checkMac1(nfcCardRecharge, data8050Bean)){
            makeRetInfo("E0001", "MAC1校验失败", result);
            return;
        }

        // 计算mac2
        String mac2 = rechargeService.calculateMac2(nfcCardRecharge, data8050Bean);
        if(StringUtils.isEmpty(mac2)){
            makeRetInfo("E0001", "MAC2计算失败", result);
            return;
        }
        String creditForLoad = rechargeService.buildCreditForLoad(mac2);
        // 更新订单信息
        nfcCardRecharge.setCsn(wlkh);
        nfcCardRecharge.setSuccess("1");// 充值成功
        nfcCardRecharge.setYe(Long.parseLong(data8050Bean.getOldBalance(), 16));
        nfcCardRecharge.setJyxh(Long.parseLong(data8050Bean.getTradeSeq(), 16));
        nfcCardRecharge.setRandomdata(data8050Bean.getRandom());
        nfcCardRechargeMapper.updateByPrimaryKeySelective(nfcCardRecharge);

        putRetContent("creditForLoad", creditForLoad, result);
        makeRetInfo("S0000", "申请成功", result);
        return;
    }

    @Override
    public void rechargeConfirm(JSONObject requestJson, JSONObject result, HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String orderNo = reqContent.getString("orderNo");
        String success = reqContent.getString("success");
        if(StringUtils.isEmpty(success) || StringUtils.isEmpty(orderNo)){
            makeRetInfo("E0001", "参数不能为空", result);
            return;
        }
        NfcCardRecharge nfcCardRecharge = nfcCardRechargeMapper.selectByPrimaryKey(orderNo);
        if(nfcCardRecharge == null){
            makeRetInfo("E0001", "订单不存在", result);
            return;
        }
        nfcCardRecharge.setSuccess(success);
        if(nfcCardRechargeMapper.updateByPrimaryKeySelective(nfcCardRecharge) > 0){
            makeRetInfo("S0000", "确认成功", result);
        }else{
            makeRetInfo("E0001", "更新订单状态失败", result);
        }
    }

    @Override
    public void applyYearCardRenew(JSONObject requestJson, JSONObject result, HttpSession session) throws ParseException {
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String orderNo = reqContent.getString("orderNo");
        String file_1004_15 = reqContent.getString("file_1004_15");
        String random = reqContent.getString("random");
        NfcCardRecharge nfcCardRecharge = nfcCardRechargeMapper.selectByPrimaryKey(orderNo);
        if(nfcCardRecharge == null){
            makeRetInfo("E0001", "订单不存在", result);
            return;
        }
        Map<String, String> file15 = analysisFile15(file_1004_15);
        String cardNo = file15.get("cardNo");
        if(!cardNo.equals(nfcCardRecharge.getCardno())){
            makeRetInfo("E0001", "补登订单不属于该卡", result);
            return;
        }
        if(!"01".equals(nfcCardRecharge.getBackrcvresponse())){
            makeRetInfo("E0001", "订单未支付", result);
            return;
        }

        // 计算新的有效日期
        String effectiveDate = file15.get("effectiveDate");
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getDateForString(effectiveDate, "yyyyMMdd"));
        cal.add(Calendar.YEAR, 1);
        String newEffectiveDate = DateUtils.getFormatDate(cal.getTime(), "yyyyMMdd");

        // 04D6 + 文件标志(1字节) + 更新文件偏移量(1字节) + 长度(1字节，包含更新内容和mac) + 更新内容 + mac
        String apdu = "04D6951808" + newEffectiveDate;
        String mac = rechargeService.calculateMac(cardNo, random, apdu);

        putRetContent("apdu", apdu + mac, result);
        makeRetInfo("S0000", "申请成功", result);
        return;
    }

    @Override
    public void queryDictionary(JSONObject requestJson, JSONObject result, HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String code = reqContent.getString("code");
        if(StringUtils.isEmpty(code)){
            makeRetInfo("E0001", "参数不能为空", result);
            return;
        }
        List<Dictionary> dictionaryList = dictionaryMapper.selectListByCode(code.toUpperCase());
        if(dictionaryList == null || dictionaryList.size() == 0){
            makeRetInfo("S0001", "未查询到数据", result);
            return;
        }
        List<String> payMoneyList = new ArrayList<String>();
        for (Dictionary dic : dictionaryList){
            payMoneyList.add(dic.getValue());
        }
        putRetContent(JSONArray.parseArray(JSON.toJSONString(payMoneyList)), result);
        makeRetInfo("S0000", "查询成功", result);
        return;
    }

    @Override
    public int saveAbcRequestResult(Nhrequestresult nhrequestresult) {
        return nhrequestresultMapper.insertSelective(nhrequestresult);
    }

    @Override
    public int saveNshRequestResult(Nshresresult nshresresult) {
        return nshresresultMapper.insertSelective(nshresresult);
    }

    /**
     * 解析圈存初始化的返回
     * @param initializeForLoad
     * @return
     */
    private InitializeForLoadBean analysisInitializeForLoad(String initializeForLoad) {
        if(StringUtils.isEmpty(initializeForLoad)){
            return null;
        }
        MessageAnalysisDevice messageAnalysisDevice = new MessageAnalysisDevice();
        messageAnalysisDevice.putAnalysisLength("oldBalance", 4*2);
        messageAnalysisDevice.putAnalysisLength("tradeSeq",   2*2);
        messageAnalysisDevice.putAnalysisLength("keyVersion", 1*2);
        messageAnalysisDevice.putAnalysisLength("sfFlag",     1*2);
        messageAnalysisDevice.putAnalysisLength("random",     4*2);
        messageAnalysisDevice.putAnalysisLength("mac1",       4*2);
        messageAnalysisDevice.analysis(initializeForLoad);
        InitializeForLoadBean data8050Bean = new InitializeForLoadBean();
        data8050Bean.setOldBalance(messageAnalysisDevice.getValue("oldBalance"));
        data8050Bean.setTradeSeq(messageAnalysisDevice.getValue("tradeSeq"));
        data8050Bean.setKeyVersion(messageAnalysisDevice.getValue("keyVersion"));
        data8050Bean.setSfFlag(messageAnalysisDevice.getValue("sfFlag"));
        data8050Bean.setRandom(messageAnalysisDevice.getValue("random"));
        data8050Bean.setMac1(messageAnalysisDevice.getValue("mac1"));
        return data8050Bean;
    }

    /**
     * 解析15文件
     * @param file15
     * @return
     */
    public Map<String, String> analysisFile15(String file15){
        if(StringUtils.isEmpty(file15)){
            return null;
        }
        MessageAnalysisDevice messageAnalysisDevice = new MessageAnalysisDevice();
        messageAnalysisDevice.putAnalysisLength("code",     2*2);
        messageAnalysisDevice.putAnalysisLength("cityCode", 2*2);
        messageAnalysisDevice.putAnalysisLength("hydm",     2*2);
        messageAnalysisDevice.putAnalysisLength("reserved1",1*2);
        messageAnalysisDevice.putAnalysisLength("reserved2",1*2);
        messageAnalysisDevice.putAnalysisLength("enabled",  1*2);
        messageAnalysisDevice.putAnalysisLength("appVersion",1*2);
        messageAnalysisDevice.putAnalysisLength("reserved3", 2*2);
        messageAnalysisDevice.putAnalysisLength("cardNo",   8*2);
        messageAnalysisDevice.putAnalysisLength("openDate", 4*2);
        messageAnalysisDevice.putAnalysisLength("effectiveDate", 4*2);
        messageAnalysisDevice.putAnalysisLength("reserved4", 2*2);
        messageAnalysisDevice.putAnalysisLength("upperLimit",3*2);
        messageAnalysisDevice.putAnalysisLength("nsrq",      3*2);
        messageAnalysisDevice.putAnalysisLength("skrq",      4*2);
        messageAnalysisDevice.putAnalysisLength("lastCzrq",  4*2);
        messageAnalysisDevice.putAnalysisLength("keyVersion",1*2);
        messageAnalysisDevice.analysis(file15);
        return messageAnalysisDevice.getAnalysisResultMap();
    }

    @Override
    public boolean updateNfcCardRechargeOrder(NfcCardRecharge nfcCardRecharge) {
        return nfcCardRechargeMapper.updateByPrimaryKeySelective(nfcCardRecharge) > 0;
    }

    @Override
    public boolean updateMallOrder(OrderT order) {
        return orderTMapper.updateByPrimaryKeySelective(order) > 0;
    }

    @Override
    public void queryOrder(JSONObject requestJson, JSONObject result, HttpSession session) {
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject common = requestJson.getJSONObject("common");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String pageNo = common.getString("pageNo"); // 页码
        String pageSum = common.getString("pageSum"); // 每页记录数
        String success = reqContent.getString("success");
        String backrcvresponse = reqContent.getString("backrcvresponse");
        String phone = appUser.getuPhone();
        String cardNo = reqContent.getString("cardNo");
        NfcCardRecharge nfcCardRecharge = new NfcCardRecharge();
        nfcCardRecharge.setSuccess(success);
        nfcCardRecharge.setBackrcvresponse(backrcvresponse);
        nfcCardRecharge.setPhoneno(phone);
        nfcCardRecharge.setCardno(cardNo);
        if(StringUtils.isNotEmpty(pageNo)){
            // pageNo不为空分页查询
            PageHelper.startPage(Integer.parseInt(pageNo), Integer.parseInt(pageSum));
        }
        List<NfcCardRecharge> nfcCardRechargeList = nfcCardRechargeMapper.selectByCondition(nfcCardRecharge);
        if(nfcCardRechargeList == null && nfcCardRechargeList.size() == 0){
            makeRetInfo("S0001", "未查询到订单", result);
            return;
        }
        JSONArray nfcCardRechargeArray = JSONArray.parseArray(JSON.toJSONString(nfcCardRechargeList));
        putRetContent(nfcCardRechargeArray, result);
        makeRetInfo("S0000", "查询成功", result);
    }

    @Override
    public void queryCitzenCardNoByID(JSONObject requestJson, JSONObject result, HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String sfzhm = reqContent.getString("ID");
        if(StringUtils.isEmpty(sfzhm)){
            makeRetInfo("E0001", "身份证号不能为空", result);
            return;
        }
        TSmcardYhInfo param = new TSmcardYhInfo();
        param.setSfzhm(sfzhm);
        List<TSmcardYhInfo> tSmcardYhInfoList = tSmcardYhInfoMapper.selectByCondition(param);
        if(tSmcardYhInfoList != null && tSmcardYhInfoList.size() > 0){
            JSONArray retContent = new JSONArray();
            for (TSmcardYhInfo tSmcardYhInfo : tSmcardYhInfoList){
                CardInfo cardInfo = cardInfoMapper.selectByPrimaryKey(tSmcardYhInfo.getFxkh());
                YhIcKzKey yhIcKzKey = new YhIcKzKey();
                yhIcKzKey.setMklx(cardInfo.getMklx());
                yhIcKzKey.setKzbh(cardInfo.getZklx());
                YhIcKz yhIcKz = yhIcKzMapper.selectByPrimaryKey(yhIcKzKey);
                JSONObject obj = new JSONObject();
                obj.put("citzenCardNo", cardInfo.getFxkh());
                obj.put("cardTypeName", yhIcKz.getKmc());
                obj.put("cardStatus", cardInfo.getKzhzt());
                obj.put("name", tSmcardYhInfo.getXm());
                retContent.add(obj);
            }
            putRetContent(retContent, result);
            makeRetInfo("S0000", "查询成功", result);
        }else {
            makeRetInfo("S0001", "未查询到数据", result);
            return;
        }
    }

    @Override
    public void queryTourYearCardByCardNo(JSONObject requestJson, JSONObject result, HttpSession session){
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        String cardNo = reqContent.getString("cardNo");
        if(StringUtils.isEmpty(cardNo)){
            makeRetInfo("E0001", "卡号不能为空", result);
            return;
        }
        TCardInfo tCardInfo = tCardInfoMapper.selectByPrimaryKey(cardNo);
        if(tCardInfo != null && tCardInfo.getQyrq() != null){
            TYhInfo tYhInfo = tYhInfoMapper.selectByCardNo(cardNo);
            putRetContent("createCardTime", DateUtils.getFormatDate(tCardInfo.getQyrq(), "yyyy-MM-dd HH:mm:ss"), result);
            putRetContent("validTime", DateUtils.getFormatDate(tCardInfo.getYxrq(), "yyyy-MM-dd HH:mm:ss"), result);
            if(tYhInfo != null){
                putRetContent("name", tYhInfo.getName(), result);
            }
            makeRetInfo("S0000", "查询成功", result);
        }else {
            makeRetInfo("S0001", "未查询到数据", result);
            return;
        }
    }

    /**
     * 意见反馈
     * @param requestJson
     * @param result
     * @param session
     */
    @Override
    public void feedback(JSONObject requestJson, JSONObject result, HttpSession session) {
        SysAppUserVO appUser = (SysAppUserVO) session.getAttribute("user");
        JSONObject reqContent = requestJson.getJSONObject("reqContent");
        Feedback feedback = JSON.parseObject(reqContent.toJSONString(), Feedback.class);
        if(StringUtils.isEmpty(feedback.getContent()) && StringUtils.isEmpty(feedback.getPhoneModel())){
            makeRetInfo("E0001", "参数不能为空", result);
            return;
        }
        feedback.setPhone(appUser.getuPhone());
        feedback.setCreateTime(new Date());
        if(feedbackMapper.insertSelective(feedback) > 0){
            makeRetInfo("S0000", "反馈成功", result);
            return;
        }else {
            makeRetInfo("E0001", "反馈失败", result);
            return;
        }
    }

    /**
     * 生成订单号
     * @param txChan
     * @return
     */
    private String generateOrderNo(String txChan) {
        // 获取数据库序列 seq_orderno 下一个的值,作为订单序号
        int orderSeq = nfcCardRechargeMapper.generateOrderNo();
        StringBuilder orderNo = new StringBuilder(txChan);
        // Y10000000000100020170510123456
        orderNo.append("100000000001000");
        orderNo.append(DateUtils.getFormatDate(new Date(), "yyyyMMdd"));
        orderNo.append(String.format("%06d", orderSeq));
        return orderNo.toString();
    }

    /**
     * 生成凭证串码
     * @return
     */
    private synchronized String generateVouchers() {
        String vouchers = StringUtils.getRadomString2(16);
        // 检查凭证串码是否重复
        LeaseVouchers leaseVouchers = leaseVouchersMapper.selectByVouchers(vouchers);
        if(leaseVouchers != null){
            return generateVouchers();
        }
        return vouchers;
    }

    public static void makeRetInfo(String retCode,String retMsg , JSONObject result){
        Map<String,String> retComMap = new HashMap<String,String>();
        retComMap.put("retCode", retCode);
        retComMap.put("retMsg", retMsg);
        result.put("common", JSONObject.parseObject(JSON.toJSONString(retComMap)));
    }

    public static void putRetContent(String key, String value, JSONObject result){
        JSONObject retContent = result.getJSONObject("retContent");
        if(retContent == null){
            retContent = new JSONObject();
        }
        retContent.put(key, value);
        result.put("retContent", retContent);
    }

    public static void putRetContent(JSONArray jsonArray, JSONObject result){
        result.put("retContent", jsonArray);
    }

    public static void putRetContent(JSONObject jsonObject, JSONObject result){
        JSONObject retContent = result.getJSONObject("retContent");
        if(retContent == null){
            retContent = jsonObject;
        }else{
            Iterator<Map.Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Object> next = iterator.next();
                retContent.put(next.getKey(), next.getValue());
            }
        }
        result.put("retContent", retContent);
    }
}