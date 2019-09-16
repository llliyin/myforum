package com.qly.myforum.controller;

import com.qly.myforum.dto.PaginationDTO;
import com.qly.myforum.mapper.QuestionMapper;
import com.qly.myforum.pojo.User;
import com.qly.myforum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;

@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profilePage(@PathVariable(name = "action") String action,
                              Model model,
                              HttpSession session,
                              @RequestParam(name = "page",defaultValue = "1") Integer page,
                              @RequestParam(name="size",defaultValue = "5") Integer size) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionService.selectMyQuestion(user.getId(), page, size);
            model.addAttribute("list",paginationDTO);
        }
        if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        return "profile";
    }
}
