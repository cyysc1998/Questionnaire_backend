package com.example.questionnaire_backend.service;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface ResolveQuestionnaire {
    JSONObject resolve(int uId);
}
