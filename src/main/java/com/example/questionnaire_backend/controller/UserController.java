package com.example.questionnaire_backend.controller;

import com.example.questionnaire_backend.domain.ReceiveLoginInfo;
import com.example.questionnaire_backend.domain.ReceiveRegisterInfo;
import com.example.questionnaire_backend.domain.User;
import com.example.questionnaire_backend.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;



    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/login", produces = "application/json;charset=UTF-8")
    public int login(@RequestBody ReceiveLoginInfo user) {
        User query = userRepository.findByName(user.getUsername());
        if(query == null)
            return -2;
        else if(!query.getPassword().equals(user.getPassword()))
            return -1;
        return query.getId();
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/register", produces = "application/json;charset=UTF-8")
    public int register(@RequestBody ReceiveRegisterInfo user) {
        try {
            List<User> name_duplicate_check = userRepository.findAllByName(user.getName());
            List<User> email_duplicate_check = userRepository.findAllByEmail(user.getEmail());
            if(name_duplicate_check.size() != 0)
                return -1;
            if(email_duplicate_check.size() != 0)
                return -2;
            User newUser = new User();
            newUser.setName(user.getName());
            newUser.setPassword(user.getPassword());
            newUser.setEmail(user.getEmail());
            userRepository.save(newUser);
            System.out.println(user.getEmail() + user.getName() + user.getPassword());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/register/emailcheck")
    public int registerEmailCheck(@RequestParam String email) {
        List<User> email_duplicate_check = userRepository.findAllByEmail(email);
        return email_duplicate_check.size()==0 ? 1:0;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/register/namecheck")
    public int registerNameCheck(@RequestParam String name) {
        List<User> name_duplicate_check = userRepository.findAllByName(name);
        return name_duplicate_check.size()==0 ? 1:0;
    }
}

