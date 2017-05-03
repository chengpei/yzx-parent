package com.whpe.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.whpe.utils.DateUtils;
import com.whpe.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppInterfaceServiceImpl extends CommonService implements AppInterfaceService{

    @Resource
    private SysPeopleMapper sysPeopleMapper;

    @Resource
    private AppMycardMapper appMycardMapper;

    @Resource
    private NfcCardRechargeMapper nfcCardRechargeMapper;

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
        String bz = reqContent.getString("bz");
        AppMycard appMycard = new AppMycard();
        appMycard.setAppBz(bz);
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
            JSONObject retContent =  new JSONObject();
            retContent.put("orderNo", orderNo);
            result.put("retContent", retContent);
            makeRetInfo("S0000", "提交成功", result);
        }else{
            makeRetInfo("E0001", "提交失败", result);
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
}
