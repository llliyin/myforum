package com.qly.myforum.service;

import com.qly.myforum.dto.PaginationDTO;
import com.qly.myforum.dto.QuestionDTO;
import com.qly.myforum.dto.QuestionQueryDTO;
import com.qly.myforum.mapper.QuestionExtMapper;
import com.qly.myforum.mapper.QuestionMapper;
import com.qly.myforum.mapper.UserMapper;
import com.qly.myforum.pojo.Question;
import com.qly.myforum.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionExtMapper questionExtMapper;

    public int addQuestion(Question question) {
        return questionMapper.insert(question);
    }

    public Integer getTotalPage(Integer totalSize, Integer page, Integer size) {
        int totalPage = 0;
        if (totalSize % size == 0) {
            totalPage = totalSize / size;
        } else {
            totalPage = totalSize / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        return totalPage;
    }

    public PaginationDTO getPaginationDto(List<Question> questions, Integer totalPage, Integer page) {
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        QuestionDTO target = null;
        //查询出该业需要的数据（questionDTOS）
        for (Question question : questions) {
            target = new QuestionDTO();
            BeanUtils.copyProperties(question, target);
            target.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
            questionDTOS.add(target);
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(totalPage, page);
        paginationDTO.setData(questionDTOS);
        return paginationDTO;
    }

    public PaginationDTO findQuestions(Integer page, Integer size, String search) {
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalSize = questionMapper.countBySearch(questionQueryDTO);
        Integer totalPage = getTotalPage(totalSize, page, size);
        Integer offset = size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
//        List<Question> questions = questionMapper.selectByPage(offset, size);
        return getPaginationDto(questions, totalPage, page);
    }

    public PaginationDTO selectMyQuestion(Long userId, Integer page, Integer size) {

        Integer totalSize = questionMapper.countOfQuestionsByUser(userId);
        Integer totalPage = getTotalPage(totalSize, page, size);
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.selectByPageAndId(userId, offset, size);
        return getPaginationDto(questions, totalPage, page);
    }

    public QuestionDTO getQuestionDTOById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }


    public void updateQuestion(Question question) {
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            question.setViewCount(1);
            addQuestion(question);
        } else {
            question.setGmtModified(System.currentTimeMillis());
            if (question.getViewCount() == null) {
                question.setViewCount(1);
            }
            question.setViewCount(question.getViewCount() + 1);
            questionMapper.updateByPrimaryKey(question);
        }
    }

    public void updateViewCount(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setViewCount(question.getViewCount()+1);
        questionMapper.updateByPrimaryKey(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays
                .stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
