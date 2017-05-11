package com.whpe.dao.ycbus;

import com.whpe.bean.CardInfo;

public interface CardInfoMapper {
    int deleteByPrimaryKey(String fxkh);

    int insert(CardInfo record);

    int insertSelective(CardInfo record);

    CardInfo selectByPrimaryKey(String fxkh);

    int updateByPrimaryKeySelective(CardInfo record);

    int updateByPrimaryKey(CardInfo record);
}