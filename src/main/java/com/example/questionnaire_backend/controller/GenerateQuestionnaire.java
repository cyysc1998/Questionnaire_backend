package com.example.questionnaire_backend.controller;

import com.example.questionnaire_backend.service.CreateQuestionnaire;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class GenerateQuestionnaire {
    @Resource
    private CreateQuestionnaire createQuestionnaire;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/editor", produces = "application/json;charset=UTF-8")
    public int login(@RequestBody JSONObject data, HttpServletRequest request) {
        return createQuestionnaire.create(data, request);
    }
}
