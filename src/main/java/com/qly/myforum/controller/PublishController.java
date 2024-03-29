package com.qly.myforum.controller;

import com.qly.myforum.cache.TagCache;
import com.qly.myforum.dto.QuestionDTO;
import com.qly.myforum.pojo.Question;
import com.qly.myforum.pojo.User;
import com.qly.myforum.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class PublishController {

    @Autowired
    QuestionService questionService;

    @GetMapping("publish")
    public String publishPage(Model model) {
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @PostMapping("publish")
    public String doPublish(
            @RequestParam(name = "title",required = false) String title,
            @RequestParam(name = "description",required = false) String description,
            @RequestParam(name = "tag",required = false) String tag ,
            @RequestParam(name = "id" ,required = false) Long id,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "您还没有登录哟，要不要先试试登录呢");
            return "publish";
        } else {
            model.addAttribute("title", title);
            model.addAttribute("description", description);
            model.addAttribute("tag", tag);
            if (title == null || title == "") {
                model.addAttribute("error", "标题不能为空");
                return "publish";
            }
            if (description == null || description == "") {
                model.addAttribute("error", "描述不能空不能为空");
                return "publish";
            }
            if (tag == null || tag == "") {
                model.addAttribute("error", "标签不能为空不能为空");
                return "publish";
            }

            String invalid = TagCache.filterInvalid(tag);
            if (StringUtils.isNotBlank(invalid)) {
                model.addAttribute("error", "输入非法标签:" + invalid);
                return "publish";
            }

            Question question = new Question();
            question.setTitle(title);
            question.setDescription(description);
            question.setTag(tag);
            question.setCreator(user.getId());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            question.setId(id);
            questionService.updateQuestion(question);

            model.addAttribute("tags",TagCache.get());
            return "redirect:/";
        }
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
    Model model) {
        QuestionDTO questionDTOById = questionService.getQuestionDTOById(id);
        model.addAttribute("title", questionDTOById.getTitle());
        model.addAttribute("description", questionDTOById.getDescription());
        model.addAttribute("tag", questionDTOById.getTag());
        model.addAttribute("id",questionDTOById.getId());
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }
}
