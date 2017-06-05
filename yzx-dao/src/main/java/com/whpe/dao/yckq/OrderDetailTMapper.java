package com.whpe.dao.yckq;

import com.whpe.bean.OrderDetailT;

import java.util.List;

public interface OrderDetailTMapper {
    int deleteByPrimaryKey(String orderDetailId);

    int insert(OrderDetailT record);

    int insertSelective(OrderDetailT record);

    OrderDetailT selectByPrimaryKey(String orderDetailId);

    int updateByPrimaryKeySelective(OrderDetailT record);

    int updateByPrimaryKey(OrderDetailT record);

    List<OrderDetailT> selectByOrderId(String orderId);
}