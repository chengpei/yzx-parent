package com.whpe.dao.ycbus;

import com.whpe.bean.YhIcKz;
import com.whpe.bean.YhIcKzKey;

public interface YhIcKzMapper {
    int deleteByPrimaryKey(YhIcKzKey key);

    int insert(YhIcKz record);

    int insertSelective(YhIcKz record);

    YhIcKz selectByPrimaryKey(YhIcKzKey key);

    int updateByPrimaryKeySelective(YhIcKz record);

    int updateByPrimaryKey(YhIcKz record);
}