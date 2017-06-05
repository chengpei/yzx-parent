package com.whpe.dao.yckq;

import com.whpe.bean.OrderT;
import com.whpe.bean.vo.OrderVO;

public interface OrderTMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderT record);

    int insertSelective(OrderT record);

    OrderT selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderT record);

    int updateByPrimaryKey(OrderT record);

    OrderVO selectOrderInfoByOrderId(String orderId);
}