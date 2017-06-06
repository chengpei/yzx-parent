package com.whpe.dao.yckq;

import com.whpe.bean.ProductOfferT;

public interface ProductOfferTMapper {
    int deleteByPrimaryKey(String productOfferId);

    int insert(ProductOfferT record);

    int insertSelective(ProductOfferT record);

    ProductOfferT selectByPrimaryKey(String productOfferId);

    int updateByPrimaryKeySelective(ProductOfferT record);

    int updateByPrimaryKey(ProductOfferT record);
}