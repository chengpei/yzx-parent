package com.whpe.dao;

import com.whpe.bean.AppMycard;

import java.util.List;

public interface AppMycardMapper {
    int insert(AppMycard record);

    int insertSelective(AppMycard record);

    List<AppMycard> selectByCondition(AppMycard appMycard);
}