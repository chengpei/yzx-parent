package com.whpe.dao.yclyic;

import com.whpe.bean.TCardInfo;

public interface TCardInfoMapper {
    int deleteByPrimaryKey(String fxkh);

    int insert(TCardInfo record);

    int insertSelective(TCardInfo record);

    TCardInfo selectByPrimaryKey(String fxkh);

    int updateByPrimaryKeySelective(TCardInfo record);

    int updateByPrimaryKey(TCardInfo record);
}