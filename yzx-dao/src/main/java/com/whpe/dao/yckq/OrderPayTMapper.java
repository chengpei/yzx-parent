package com.whpe.dao.yckq;

import com.whpe.bean.OrderPayT;

import java.util.List;

public interface OrderPayTMapper {
    int deleteByPrimaryKey(String payId);

    int insert(OrderPayT record);

    int insertSelective(OrderPayT record);

    OrderPayT selectByPrimaryKey(String payId);

    int updateByPrimaryKeySelective(OrderPayT record);

    int updateByPrimaryKey(OrderPayT record);

    List<OrderPayT> selectByOrderId(String orderId);
}