package com.whpe.dao.yckq;

import com.whpe.bean.NfcCardRecharge;

import java.util.List;

public interface NfcCardRechargeMapper {
    int deleteByPrimaryKey(String orderNo);

    int insert(NfcCardRecharge record);

    int insertSelective(NfcCardRecharge record);

    NfcCardRecharge selectByPrimaryKey(String orderNo);

    int updateByPrimaryKeySelective(NfcCardRecharge record);

    int updateByPrimaryKey(NfcCardRecharge record);

    int generateOrderNo();

    List<NfcCardRecharge> selectByCondition(NfcCardRecharge nfcCardRecharge);
}