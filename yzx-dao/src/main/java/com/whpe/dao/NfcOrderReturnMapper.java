package com.whpe.dao;

import com.whpe.bean.NfcOrderReturn;

public interface NfcOrderReturnMapper {
    int insert(NfcOrderReturn record);

    int insertSelective(NfcOrderReturn record);
}