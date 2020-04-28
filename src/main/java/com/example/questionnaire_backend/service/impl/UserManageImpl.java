package com.example.questionnaire_backend.service.impl;

import com.example.questionnaire_backend.domain.User;
import com.example.questionnaire_backend.repository.UserRepository;
import com.example.questionnaire_backend.service.UserManage;
import net.minidev.json.JSONObject;

import javax.annotation.Resource;
import java.util.List;

public class UserManageImpl implements UserManage {
    @Resource
    private UserRepository userRepository;

    public int login(JSONObject user) {
        User query = userRepository.findByName(user.get("username").toString());
        if(query == null)
            return -2;
        else if(!query.getPassword().equals(user.get("password").toString()))
            return -1;
        return query.getId();
    }

    public int register(JSONObject user) {
        try {
            List<User> name_duplicate_check = userRepository.findAllByName(user.get("name").toString());
            List<User> email_duplicate_check = userRepository.findAllByEmail(user.get("email").toString());
            if(name_duplicate_check.size() != 0)
                return -1;
            if(email_duplicate_check.size() != 0)
                return -2;
            User newUser = new User();
            newUser.setName(user.get("name").toString());
            newUser.setPassword(user.get("password").toString());
            newUser.setEmail(user.get("email").toString());
            userRepository.save(newUser);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public int registerNameCheck(String name) {
        System.out.println(name);
        List<User> name_duplicate_check = null;
        if(userRepository == null) System.out.println("zz");
        try {
            name_duplicate_check = userRepository.findAllByName(name);
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return name_duplicate_check.size() == 0 ? 1 : 0;
    }

    public int registerEmailCheck(String email) {
        List<User> email_duplicate_check = userRepository.findAllByEmail(email);
        return email_duplicate_check.size()==0 ? 1:0;
    }
}
