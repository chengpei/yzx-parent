package com.whpe.dao.yckq;

import com.whpe.bean.Dictionary;

import java.util.List;

public interface DictionaryMapper {
    int deleteByPrimaryKey(String gid);

    int insert(Dictionary record);

    int insertSelective(Dictionary record);

    Dictionary selectByPrimaryKey(String gid);

    int updateByPrimaryKeySelective(Dictionary record);

    int updateByPrimaryKey(Dictionary record);

    List<Dictionary> selectListByCode(String code);
}