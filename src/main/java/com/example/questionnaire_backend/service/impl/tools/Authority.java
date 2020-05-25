package com.example.questionnaire_backend.service.impl.tools;

import com.example.questionnaire_backend.domain.Answer;
import com.example.questionnaire_backend.domain.Questionnaire;
import com.example.questionnaire_backend.repository.AnswerRepository;
import com.example.questionnaire_backend.repository.QuestionnaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;


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
    @Resource
    private AnswerRepository answerRepository;

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

    public Boolean maxTimesRestrict(int qId, int uId, String ip) {
        Boolean permit = true;

        Questionnaire questionnaire = questionnaireRepository.findById(qId).get();
        if(questionnaire.getTimesTotal() == false)
            permit = true;
        else {
            int maxTimes = questionnaire.getMaxTimes();
            if(uId == ANONYMOUS) {
                List<Answer> answers = answerRepository.findAllByIp(ip);
                if(answers.size() >= maxTimes)
                    permit = false;
                else
                    permit = true;
            }
            else {
                List<Answer> answers = answerRepository.findAllByuID(uId);
                if(answers.size() >= maxTimes)
                    permit = false;
                else
                    permit = true;
            }
        }

        return permit;
    }

    public Boolean maxTimesPerDayRestrict(int qId, int uId, String ip) {
        Boolean permit = true;

        Questionnaire questionnaire = questionnaireRepository.findById(qId).get();
        if(questionnaire.getTimesPerDay() == false)
            permit = true;
        else {
            int maxTimesPerDay = questionnaire.getMaxTimes();
            LocalDate curDay = LocalDate.now();
            if(uId == ANONYMOUS) {
                List<Answer> answers = answerRepository.findAllByIpAndTime(ip, curDay);
                if(answers.size() >= maxTimesPerDay)
                    permit = false;
                else
                    permit = true;
            }
            else {
                List<Answer> answers = answerRepository.findAllByuIDAndTime(uId, curDay);
                if(answers.size() >= maxTimesPerDay)
                    permit = false;
                else
                    permit = true;
            }
        }
        return permit;
    }

    public Boolean outOfTime(int qId) {
        Boolean permit = true;
        Questionnaire questionnaire = questionnaireRepository.findById(qId).get();
        LocalDate finishTime = questionnaire.getEndTime();
        LocalDate curTime = LocalDate.now();
        if(finishTime.isBefore(curTime))
            permit = false;
        else
            permit = true;
        return permit;
    }


    public int authorityJudge(int qId, int uId, String ip) {
        Boolean outOfDate = outOfTime(qId);
        Boolean register = registerRestrict(qId, uId);
        Boolean maxTimes = maxTimesRestrict(qId, uId, ip);
        Boolean maxTimesPerDay = maxTimesPerDayRestrict(qId, uId, ip);

        if(outOfDate == false)
            return OUT_OF_DATE;
        else if(register == false)
            return NEED_REGISTER;
        else if(maxTimes == false)
            return REACH_MAX_TIMES_TOTAL;
        else if(maxTimesPerDay == false)
            return REACH_MAX_TIMES_PER_DAY;
        return SUCCESS;
    }

}
