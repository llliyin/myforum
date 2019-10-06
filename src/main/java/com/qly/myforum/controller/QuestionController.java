package com.qly.myforum.controller;

import com.qly.myforum.dto.CommentDTO;
import com.qly.myforum.dto.QuestionDTO;
import com.qly.myforum.service.CommentService;
import com.qly.myforum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @RequestMapping("question/{id}")
    public String questionPage(@PathVariable(value = "id") Long id,
                               Model model,
                               HttpSession session){

        //添加阅读数
        questionService.updateViewCount(id);
        QuestionDTO questionDTOById = questionService.getQuestionDTOById(id);
        //寻找commentList

        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTOById);
        List<CommentDTO> parentComment = commentService.commentListByid(id);
        model.addAttribute("relatedQuestions",relatedQuestions);
        model.addAttribute("question",questionDTOById);
        model.addAttribute("comments",parentComment);
        return "question";
    }
}
