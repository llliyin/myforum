package com.qly.myforum.interceptor;

import com.qly.myforum.enums.NotificationStatusEnum;
import com.qly.myforum.mapper.NotificationMapper;
import com.qly.myforum.pojo.Notification;
import com.qly.myforum.pojo.NotificationExample;
import com.qly.myforum.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    NotificationMapper notificationMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入拦截器");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.getSession().setAttribute("unreadCount",0);;
            response.sendRedirect("/");
            return false;
        }
            NotificationExample notificationExample = new NotificationExample();
            notificationExample.createCriteria().andReceiverEqualTo(user.getId())
                    .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
            List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
            request.getSession().setAttribute("unreadCount",notifications.size());
        System.out.println("拦截器放行");
        return true;
    }
}
