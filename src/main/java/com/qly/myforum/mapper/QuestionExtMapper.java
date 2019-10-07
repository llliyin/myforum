package com.qly.myforum.mapper;

import com.qly.myforum.dto.QuestionQueryDTO;
import com.qly.myforum.pojo.Question;
import com.qly.myforum.pojo.QuestionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionExtMapper {
    List<Question> selectRelated(Question question);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}