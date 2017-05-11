package com.whpe.dao.yclyic;

import com.whpe.bean.TYhInfo;

public interface TYhInfoMapper {
    int deleteByPrimaryKey(Long rybh);

    int insert(TYhInfo record);

    int insertSelective(TYhInfo record);

    TYhInfo selectByPrimaryKey(Long rybh);

    int updateByPrimaryKeySelective(TYhInfo record);

    int updateByPrimaryKey(TYhInfo record);

    TYhInfo selectByCardNo(String cardNo);
}