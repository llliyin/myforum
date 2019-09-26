package com.qly.myforum.service;

import com.qly.myforum.enums.ContentTypeEnum;
import com.qly.myforum.exception.CustomizeErrorCode;
import com.qly.myforum.exception.CustomizeException;
import com.qly.myforum.mapper.CommentMapper;
import com.qly.myforum.mapper.QuestionMapper;
import com.qly.myforum.pojo.Comment;
import com.qly.myforum.pojo.Question;
import com.qly.myforum.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionmapper;

    @Transactional
    public void handleComment(Comment comment, User user) {

        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        //注意，第二个条件应该是当不是评论的类型的时候
        if (comment.getType() == null || !ContentTypeEnum.isExistType(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        //回复评论
        if (comment.getType() == ContentTypeEnum.COMMENT.getType()) {

   System.out.println("进入评论页面");
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());

            //查看评论是否存在
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //查看评论的文章是否存在
            Question question = questionmapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            comment.setCommentCount(comment.getCommentCount() + 1);
            commentMapper.updateByPrimaryKey(comment);

        } else {
            //回复问题
  System.out.println("进入回复问题页面");
            Question question = questionmapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setViewCount(question.getViewCount() + 1);
            questionmapper.updateByPrimaryKey(question);

        }
    }

}
