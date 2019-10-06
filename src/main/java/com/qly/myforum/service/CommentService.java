package com.qly.myforum.service;

import com.qly.myforum.dto.CommentDTO;
import com.qly.myforum.enums.ContentTypeEnum;
import com.qly.myforum.enums.NotificationStatusEnum;
import com.qly.myforum.enums.NotificationTypeEnum;
import com.qly.myforum.exception.CustomizeErrorCode;
import com.qly.myforum.exception.CustomizeException;
import com.qly.myforum.mapper.CommentMapper;
import com.qly.myforum.mapper.NotificationMapper;
import com.qly.myforum.mapper.QuestionMapper;
import com.qly.myforum.mapper.UserMapper;
import com.qly.myforum.pojo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionmapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void handleComment(Comment comment, User commentator) {

        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        //注意，第二个条件应该是当不是评论的类型的时候
        if (comment.getType() == null || !ContentTypeEnum.isExistType(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        //回复评论
        if (comment.getType() == ContentTypeEnum.COMMENT.getType()) {

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

            //添加问题的回复数
            dbComment.setCommentCount(comment.getCommentCount() + 1);
            commentMapper.updateByPrimaryKey(dbComment);

            //添加回复,并且将outId设置成最外层的问题的id
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());


        } else {
            //回复问题
            Question question = questionmapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);

            //添加问题的回复数
            question.setCommentCount(question.getCommentCount()+1);
            questionmapper.updateByPrimaryKey(question);

            //创建回复问题的评论
            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());

        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
//        if (receiver == comment.getCommentator()) {
//            return;
//        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
  System.out.println("完成通知操作");
    }

    public List<CommentDTO> commentListByid(Long id) {

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id)
                .andTypeEqualTo(ContentTypeEnum.QUESTION.getType());
        commentExample.setOrderByClause("gmt_create desc");

        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if(comments.size()==0){
            return new ArrayList<CommentDTO>();
        }
        User user;
        List<CommentDTO> list= new ArrayList<CommentDTO>();
        for (Comment comment: comments){
            CommentDTO commentDTO =new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            user = userMapper.selectByPrimaryKey(comment.getCommentator());
            commentDTO.setUser(user);
            list.add(commentDTO);
        }
        return list;
    }

    public List<CommentDTO> selectByTargetId(Long id, Integer type) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria()
                .andTypeEqualTo(type)
                .andParentIdEqualTo(id);
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        List<CommentDTO> list = new ArrayList<CommentDTO>();

        for (Comment comment : comments) {
            CommentDTO commentDTO=new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMapper.selectByPrimaryKey(comment.getCommentator()));
        list.add(commentDTO);
    }
        return list;

    }
}
