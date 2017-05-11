package com.whpe.dao.ycbus;

import com.whpe.bean.TSmcardYhInfo;

import java.util.List;

public interface TSmcardYhInfoMapper {
    int insert(TSmcardYhInfo record);

    int insertSelective(TSmcardYhInfo record);

    List<TSmcardYhInfo> selectByCondition(TSmcardYhInfo tSmcardYhInfo);
}