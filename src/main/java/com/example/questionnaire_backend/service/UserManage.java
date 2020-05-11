package com.example.questionnaire_backend.service;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface UserManage {
    int login(JSONObject user, HttpServletRequest request);
    Boolean loginOut(HttpServletRequest request);
    int register(JSONObject user);
    int registerNameCheck(String name);
    int registerEmailCheck(String email);
    Boolean loginCheck(HttpServletRequest request);
}
