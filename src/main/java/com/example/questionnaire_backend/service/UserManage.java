package com.example.questionnaire_backend.service;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface UserManage {
    int login(JSONObject user);
    int register(JSONObject user);
    int registerNameCheck(String name);
    int registerEmailCheck(String email);
}
