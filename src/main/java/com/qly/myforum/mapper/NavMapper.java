package com.qly.myforum.mapper;

import com.qly.myforum.pojo.Nav;
import com.qly.myforum.pojo.NavExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NavMapper {
    long countByExample(NavExample example);

    int deleteByExample(NavExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Nav record);

    int insertSelective(Nav record);

    List<Nav> selectByExample(NavExample example);

    Nav selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Nav record, @Param("example") NavExample example);

    int updateByExample(@Param("record") Nav record, @Param("example") NavExample example);

    int updateByPrimaryKeySelective(Nav record);

    int updateByPrimaryKey(Nav record);
}