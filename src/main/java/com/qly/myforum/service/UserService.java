package com.qly.myforum.service;

import com.qly.myforum.mapper.UserMapper;
import com.qly.myforum.pojo.User;
import com.qly.myforum.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public int addUser(User user){
        return userMapper.insert(user);
    }

    public User findUserByAccount(String accountId){
        UserExample userExample=new UserExample();
        UserExample.Criteria criteria=userExample.createCriteria();
        criteria.andAccountIdEqualTo(accountId);
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size()==0){
            return null;
        }else {
            return users.get(0);
        }
    }
}
