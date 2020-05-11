package com.example.questionnaire_backend.controller;

import com.example.questionnaire_backend.service.ResolveQuestionnaire;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class GetQuestionnaire {

    @Resource
    private ResolveQuestionnaire resolveQuestionnaire;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/api/resolve", produces = "application/json;charset=UTF-8")
    public JSONObject resolve(@RequestParam int qId) {
        return resolveQuestionnaire.resolve(qId);
    }
}
