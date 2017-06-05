package com.whpe.bean.vo;

import com.whpe.bean.OrderDetailT;
import com.whpe.bean.OrderPayT;
import com.whpe.bean.OrderT;

import java.util.List;

public class OrderVO extends OrderT{

    private List<OrderDetailT> orderDetailList;

    private List<OrderPayT> orderPayList;

    public List<OrderDetailT> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetailT> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public List<OrderPayT> getOrderPayList() {
        return orderPayList;
    }

    public void setOrderPayList(List<OrderPayT> orderPayList) {
        this.orderPayList = orderPayList;
    }
}
