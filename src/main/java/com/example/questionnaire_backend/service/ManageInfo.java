package com.example.questionnaire_backend.service;

import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface ManageInfo {
    JSONObject manage(HttpServletRequest request);
    Boolean submit(JSONObject answer, HttpServletRequest request);
}
