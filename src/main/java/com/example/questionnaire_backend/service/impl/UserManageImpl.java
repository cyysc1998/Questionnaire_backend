package com.example.questionnaire_backend.service.impl;

import com.example.questionnaire_backend.domain.User;
import com.example.questionnaire_backend.repository.UserRepository;
import com.example.questionnaire_backend.service.UserManage;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Transactional
public class UserManageImpl implements UserManage {
    @Resource
    private UserRepository userRepository;

    private final int USERNAME_ERROR = -2;
    private final int PASSWORD_ERROR = -1;
    private final int LOGIN_SUCCEED = 0;


    public int login(JSONObject user, HttpServletRequest request) {
        User query = userRepository.findByName(user.get("username").toString());
        if(query == null)
            return USERNAME_ERROR;
        else if(!query.getPassword().equals(user.get("password").toString()))
            return PASSWORD_ERROR;
        else {
            String name = query.getName();
            Integer uId = query.getId();
            request.getSession().setAttribute("userInfo", uId + "-" + name);
            request.getSession().setMaxInactiveInterval(1800);
            return query.getId();
        }
    }

    @Override
    public Boolean loginOut(HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.removeAttribute("userInfo");
        Object userInfo = session.getAttribute("userInfo");

        if(userInfo == null)
            return true;
        else
            return false;
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
        List<User> name_duplicate_check = null;
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

    public Boolean loginCheck(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userInfo = session.getAttribute("userInfo");
        if(userInfo == null)
            return false;
        else
            return true;
    }
}
