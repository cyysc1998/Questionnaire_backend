package com.example.questionnaire_backend.service.impl.tools;

import com.example.questionnaire_backend.domain.Questionnaire;
import com.example.questionnaire_backend.repository.QuestionnaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class Authority {

    public final int OUT_OF_DATE = -1;
    public final int NEED_REGISTER = -2;
    public final int REACH_MAX_TIMES_TOTAL = -3;
    public final int REACH_MAX_TIMES_PER_DAY = -4;
    public final int SUCCESS = 1;

    public final int ANONYMOUS = -5;

    @Resource
    private QuestionnaireRepository questionnaireRepository;

    public Boolean registerRestrict(int qId, int uId) {
        Boolean hasRegistered = true;

        if(uId == ANONYMOUS) {
            Questionnaire questionnaire = questionnaireRepository.findById(qId).get();
            if(questionnaire.getNeedRegister() == true)
                hasRegistered = false;
            else
                hasRegistered = true;
        }
        else if(uId > 0){
            hasRegistered = true;
        }
        return hasRegistered;
    }
}
