package com.example.questionnaire_backend.service;

import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface ManageInfo {
    JSONObject manage(HttpServletRequest request);
    Boolean submit(JSONObject answer, HttpServletRequest request);
    Boolean modified(JSONObject info, HttpServletRequest request);
    JSONObject analysis(int qId, HttpServletRequest request);
    JSONObject delete(int qId, HttpServletRequest request);
}
