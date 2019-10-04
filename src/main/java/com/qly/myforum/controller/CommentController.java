package com.qly.myforum.controller;


import com.qly.myforum.dto.CommentCreateDTO;
import com.qly.myforum.dto.CommentDTO;
import com.qly.myforum.dto.ResultDTO;
import com.qly.myforum.enums.ContentTypeEnum;
import com.qly.myforum.exception.CustomizeErrorCode;
import com.qly.myforum.mapper.CommentMapper;
import com.qly.myforum.pojo.Comment;
import com.qly.myforum.pojo.User;
import com.qly.myforum.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CommentService commentService;

    @ResponseBody
    @PostMapping("/comment")
    public Object comment(@RequestBody CommentCreateDTO commentCreateDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isEmpty(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setCommentator(user.getId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setLikeCount(1L);
        comment.setCommentCount(0);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        commentService.handleComment(comment, user);
        return ResultDTO.okOf();
    }

    @GetMapping(value = "/comment/{id}")
    @ResponseBody
    public ResultDTO<List<CommentDTO>> secondComment(@PathVariable("id") Long id){
        List<CommentDTO> comments = commentService.selectByTargetId(id, ContentTypeEnum.COMMENT.getType());
        return ResultDTO.okOf(comments);
    }
}
