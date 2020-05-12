package com.example.questionnaire_backend.service;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface CreateQuestionnaire {
    int create(JSONObject data, HttpServletRequest request);
}
