package com.whpe.bean.vo;

import com.whpe.bean.BusOrder;
import com.whpe.bean.ProductOfferT;


public class BusOrderVO extends BusOrder{

    private ProductOfferT productOffer;

    public ProductOfferT getProductOffer() {
        return productOffer;
    }

    public void setProductOffer(ProductOfferT productOffer) {
        this.productOffer = productOffer;
    }
}
