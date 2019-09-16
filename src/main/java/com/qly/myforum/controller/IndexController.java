package com.qly.myforum.controller;

import com.qly.myforum.dto.PaginationDTO;
import com.qly.myforum.dto.QuestionDTO;
import com.qly.myforum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    QuestionService questionService;

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page" ,defaultValue = "1") Integer page,
                        @RequestParam(name = "size" ,defaultValue = "5") Integer size){
        PaginationDTO paginationDTO = questionService.findQuestions(page,size);
        model.addAttribute("list",paginationDTO);
        return "index";
    }
}
