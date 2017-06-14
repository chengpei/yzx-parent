package com.whpe.dao.yckq;

import com.whpe.bean.ParamT;

import java.util.List;

public interface ParamTMapper {
    int deleteByPrimaryKey(String paramId);

    int insert(ParamT record);

    int insertSelective(ParamT record);

    ParamT selectByPrimaryKey(String paramId);

    int updateByPrimaryKeySelective(ParamT record);

    int updateByPrimaryKey(ParamT record);

    List<ParamT> selectListByCode(String paramCode);
}