package com.example.questionnaire_backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.questionnaire_backend.service.CreateQuestionnaire;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateQuestionnaireImpl implements CreateQuestionnaire {


    public int create(JSONObject data) {
        System.out.println(data);
        System.out.println(data.get("metadata"));
//        JSONObject metaData = (JSONObject) data.get("metadata");
//        JSONObject[] questions = (JSONObject[]) data.get("content");
//        JSONObject setting = (JSONObject) data.get("setting");

//        System.out.println(metaData);
//        for(int i=0; i<questions.length; i++)
//            System.out.println(questions[i]);
//        System.out.println(setting);

        return 1;
    }

}
