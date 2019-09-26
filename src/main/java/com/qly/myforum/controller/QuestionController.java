package com.qly.myforum.controller;

import com.qly.myforum.dto.QuestionDTO;
import com.qly.myforum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @RequestMapping("question/{id}")
    public String questionPage(@PathVariable(value = "id") Long id,
                               Model model){
//        questionService.updateViewCount(id);
        QuestionDTO questionDTOById = questionService.getQuestionDTOById(id);
        model.addAttribute("question",questionDTOById);
        return "question";
    }
}
