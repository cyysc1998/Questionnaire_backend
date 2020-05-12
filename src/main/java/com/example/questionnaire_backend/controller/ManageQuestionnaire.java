package com.example.questionnaire_backend.controller;

import com.example.questionnaire_backend.service.ManageInfo;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ManageQuestionnaire {

    @Resource
    private ManageInfo manageInfo;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/manager", produces = "application/json;charset=UTF-8")
    public JSONObject manage(HttpServletRequest request) {
        return manageInfo.manage(request);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/submit", produces = "application/json;charset=UTF-8")
    public Boolean submit(@RequestBody JSONObject answer, HttpServletRequest request) {
        return  manageInfo.submit(answer, request);
    }

}
