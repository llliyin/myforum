package com.qly.myforum.controller;

import com.qly.myforum.dto.AccessTokenDTO;
import com.qly.myforum.dto.GithubUser;
import com.qly.myforum.enums.NotificationStatusEnum;
import com.qly.myforum.enums.NotificationTypeEnum;
import com.qly.myforum.mapper.NotificationMapper;
import com.qly.myforum.pojo.Notification;
import com.qly.myforum.pojo.NotificationExample;
import com.qly.myforum.pojo.User;
import com.qly.myforum.provider.GithubProvider;
import com.qly.myforum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.modeler.NotificationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.metadata.ReturnValueDescriptor;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    UserService userService;

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping("callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpSession session,
                           HttpServletRequest request){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(accessToken);
        GithubUser githubUser = githubProvider.getGithubUser(accessToken);
       if(githubUser!=null){
           User git = userService.findUserByAccount(String.valueOf(githubUser.getId()));
           if(git == null){
               User user = new User();
               String token= UUID.randomUUID().toString();
               user.setToken(token);
               user.setAvatarUrl(githubUser.getAvatarUrl());
               user.setBio(githubUser.getBio());
               user.setName(githubUser.getName());
               user.setGmtCreate(System.currentTimeMillis());
               user.setGmtModified(System.currentTimeMillis());
               user.setBio(githubUser.getBio());
               user.setAccountId(String.valueOf(githubUser.getId()));
               System.out.println(user.getName());
               userService.addUser(user);
               session.setAttribute("unreadCount",0);
               session.setAttribute("user",user);

           }else {
               NotificationExample notificationExample = new NotificationExample();
               notificationExample.createCriteria().andReceiverEqualTo(git.getId())
                       .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
               List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
               session.setAttribute("unreadCount",notifications.size());
               session.setAttribute("user",git);
           }

           return "redirect:/";
       }else {
           log.error("callback get github error,{}",githubUser);
            request.setAttribute("message","用户不存在，请重新登录");
           return "redirect:/";
       }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}
