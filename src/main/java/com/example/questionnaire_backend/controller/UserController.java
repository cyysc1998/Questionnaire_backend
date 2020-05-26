package com.example.questionnaire_backend.controller;

import com.example.questionnaire_backend.service.UserManage;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Resource
    private UserManage userManage;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/login", produces = "application/json;charset=UTF-8")
    public int login(@RequestBody JSONObject user, HttpServletRequest request) {
        return userManage.login(user, request);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/logout", produces = "application/json;charset=UTF-8")
    public Boolean logOut(HttpServletRequest request) {
        return userManage.loginOut(request);
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

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/islogin")
    public Boolean isLogin(HttpServletRequest request) {
        return userManage.loginCheck(request);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/getinfo")
    public JSONObject getUserInfo(HttpServletRequest request) {
        return userManage.getInfo(request);
    }
}

