package com.example.questionnaire_backend.controller;

import com.example.questionnaire_backend.domain.User;
import com.example.questionnaire_backend.repository.UserRepository;
import com.example.questionnaire_backend.service.UserManage;
import com.example.questionnaire_backend.service.impl.UserManageImpl;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserManage userManage;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/login", produces = "application/json;charset=UTF-8")
    public int login(@RequestBody JSONObject user) {
        return userManage.login(user);
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/register", produces = "application/json;charset=UTF-8")
    public int register(@RequestBody JSONObject user) {
        return userManage.register(user);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/register/emailcheck")
    public int registerEmailCheck(@RequestParam String email) {
        return userManage.registerEmailCheck(email);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/register/namecheck")
    public int registerNameCheck(@RequestParam String name) {
        return userManage.registerNameCheck(name);
    }
}

