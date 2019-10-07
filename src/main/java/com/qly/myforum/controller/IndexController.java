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
                        @RequestParam(name = "size" ,defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String search){
        PaginationDTO paginationDTO = questionService.findQuestions(page,size,search);
        model.addAttribute("list",paginationDTO);
        model.addAttribute("search",search);
        return "index";
    }
}
