package com.whpe.dao;

import com.whpe.bean.NfcCardRecharge;

public interface NfcCardRechargeMapper {
    int deleteByPrimaryKey(String orderNo);

    int insert(NfcCardRecharge record);

    int insertSelective(NfcCardRecharge record);

    NfcCardRecharge selectByPrimaryKey(String orderNo);

    int updateByPrimaryKeySelective(NfcCardRecharge record);

    int updateByPrimaryKey(NfcCardRecharge record);

    int generateOrderNo();
}