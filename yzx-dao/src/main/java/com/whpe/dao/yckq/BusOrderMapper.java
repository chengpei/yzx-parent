package com.whpe.dao.yckq;

import com.whpe.bean.BusOrder;
import com.whpe.bean.vo.BusOrderVO;

import java.util.List;

public interface BusOrderMapper {
    int deleteByPrimaryKey(String orderNo);

    int insert(BusOrder record);

    int insertSelective(BusOrder record);

    BusOrderVO selectByPrimaryKey(String orderNo);

    int updateByPrimaryKeySelective(BusOrder record);

    int updateByPrimaryKey(BusOrder record);

    BusOrder selectByVouchers(String vouchers);

    List<BusOrderVO> selectByCondition(BusOrder params);
}