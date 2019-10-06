package com.qly.myforum.controller;


import com.qly.myforum.dto.NotificationDTO;
import com.qly.myforum.enums.NotificationTypeEnum;
import com.qly.myforum.mapper.NotificationMapper;
import com.qly.myforum.pojo.Notification;
import com.qly.myforum.pojo.User;
import com.qly.myforum.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class NotificationController {

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    NotificationService notificationService;

    @GetMapping("notification/{id}")
    public String readNotification(@PathVariable("id") Long notificationId,
                                   HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        Notification notification=notificationService.read(notificationId,user.getId());
        if(notification.getType()== NotificationTypeEnum.REPLY_COMMENT.getType()||notification.getType()==NotificationTypeEnum.REPLY_QUESTION.getType()){
            return "redirect:/question/"+notification.getOuterid();
        }else {
            return "redirect:/";
        }
    }
}
