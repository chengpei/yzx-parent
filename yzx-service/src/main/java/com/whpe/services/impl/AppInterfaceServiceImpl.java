package com.whpe.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.whpe.bean.AppMycard;
import com.whpe.bean.NfcCardRecharge;
import com.whpe.bean.SysPeople;
import com.whpe.bean.dto.SysPeopleDTO;
import com.whpe.bean.vo.SysAppUserVO;
import com.whpe.dao.AppMycardMapper;
import com.whpe.dao.NfcCardRechargeMapper;
import com.whpe.dao.SysPeopleMapper;
import com.whpe.services.AppInterfaceService;
import com.whpe.services.CommonService;
import com.whpe.services.PayService;
import com.whpe.utils.DateUtils;
import com.whpe.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        // 判断是否已经绑定过该卡
        List<AppMycard> mycardList = appMycardMapper.selectByCondition(appMycard);
        if(mycardList != null && mycardList.size() > 0){
            makeRetInfo("E0001", "已经绑定过该卡", result);
            return;
        }
        if(appMycardMapper.insertSelective(appMycard) > 0){
            makeRetInfo("S0000", "绑定成功", result);
        }else {
            makeRetInfo("E0001", "绑定失败", result);
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
        if(nfcCardRechargeMapper.insertSelective(nfcCardRecharge) > 0){
            putRetContent("orderNo", orderNo, result);
            makeRetInfo("S0000", "提交成功", result);
        }else{
            makeRetInfo("E0001", "提交失败", result);
        }
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
        }else{
            makeRetInfo("E0001", "不支持的支付方式", result);
            return;
        }
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

    /**
     * 生成订单号
     * @param txChan
     * @return
     */
    private String generateOrderNo(String txChan) {
        // 获取数据库序列 seq_orderno 下一个的值,作为订单序号
        int orderSeq = nfcCardRechargeMapper.generateOrderNo();
        StringBuilder orderNo = new StringBuilder(txChan);
        orderNo.append(DateUtils.getFormatDate(new Date(), "yyyyMMddHHmmss"));
        orderNo.append(String.format("%015d", orderSeq));
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
