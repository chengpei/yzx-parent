package com.whpe.bean.vo;

import com.whpe.bean.OrderDetailT;
import com.whpe.bean.ProductOfferT;

public class OrderDetailVO extends OrderDetailT{

    private ProductOfferT productOffer;

    public ProductOfferT getProductOffer() {
        return productOffer;
    }

    public void setProductOffer(ProductOfferT productOffer) {
        this.productOffer = productOffer;
    }
}
