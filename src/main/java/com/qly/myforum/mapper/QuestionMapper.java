package com.qly.myforum.mapper;

import com.qly.myforum.dto.QuestionQueryDTO;
import com.qly.myforum.pojo.Question;
import com.qly.myforum.pojo.QuestionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuestionMapper {
    long countByExample(QuestionExample example);

    int deleteByExample(QuestionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Question record);

    int insertSelective(Question record);

    List<Question> selectByExampleWithBLOBs(QuestionExample example);

    List<Question> selectByExample(QuestionExample example);

    Question selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Question record, @Param("example") QuestionExample example);

    int updateByExampleWithBLOBs(@Param("record") Question record, @Param("example") QuestionExample example);

    int updateByExample(@Param("record") Question record, @Param("example") QuestionExample example);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);

    List<Question> selectByPage(@Param("offset") Integer offset, @Param("size") Integer size);

    Integer countOfQuestions();

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    Integer countOfQuestionsByUser(@Param("userId") Long userId);

    List<Question> selectByPageAndId(@Param("userId")Long userId,@Param("offset") Integer offset, @Param("size") Integer size);
}