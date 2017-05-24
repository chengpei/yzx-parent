package com.whpe.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.whpe.bean.*;
import com.whpe.bean.dto.SysPeopleDTO;
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
        if(StringUtils.isEmpty(fxkh) || StringUtils.isEmpty(orderMount)){
            makeRetInfo("E0001", "卡号和金额不能为空", result);
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

    @Override
    public boolean updateNfcCardRechargeOrder(NfcCardRecharge nfcCardRecharge) {
        return nfcCardRechargeMapper.updateByPrimaryKeySelective(nfcCardRecharge) > 0;
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
        makeRetInfo("E0001", "开发中...", result);
        return;
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
