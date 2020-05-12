package com.example.questionnaire_backend.service.impl;

import com.example.questionnaire_backend.domain.Answer;
import com.example.questionnaire_backend.domain.Question;
import com.example.questionnaire_backend.domain.Questionnaire;
import com.example.questionnaire_backend.domain.UserQuestionnaire;
import com.example.questionnaire_backend.repository.AnswerRepository;
import com.example.questionnaire_backend.repository.QuestionRepository;
import com.example.questionnaire_backend.repository.QuestionnaireRepository;
import com.example.questionnaire_backend.repository.UserQuestionnaireRepository;
import com.example.questionnaire_backend.service.ManageInfo;
import net.minidev.json.JSONObject;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Transactional
public class ManageInfoImpl implements ManageInfo {
    @Resource
    private UserQuestionnaireRepository userQuestionnaireRepository;
    @Resource
    private QuestionnaireRepository questionnaireRepository;
    @Resource
    private AnswerRepository answerRepository;
    @Resource
    private QuestionRepository questionRepository;

    @Override
    public JSONObject manage(HttpServletRequest request) {
        JSONObject questionnaire = new JSONObject();
        ArrayList<JSONObject> questions = new ArrayList<JSONObject>();

        HttpSession session = request.getSession();
        String userInfo = (String) session.getAttribute("userInfo");
        String[] info = userInfo.split("-");

        int uId = Integer.parseInt(info[0]);

        List<UserQuestionnaire> questionnaireList = userQuestionnaireRepository.findByuId(uId);

        for(int i=0; i<questionnaireList.size(); i++) {
            JSONObject question = new JSONObject();
            int qId = questionnaireList.get(i).getqId();
            Optional<Questionnaire> content = questionnaireRepository.findById(qId);

            String title = content.get().getTitle();
            String description = content.get().getIntroduction();
            if(description.length() > 10)
                description = description.substring(0, 9);
            description = description + "......";

            question.put("title", title);
            question.put("description", description);

            questions.add(question);
        }
        questionnaire.put("questions", questions);
        System.out.println(questionnaire);
        return questionnaire;
    }

    public Boolean submit(JSONObject answer, HttpServletRequest request) {
        int uId = -1;
        HttpSession session = request.getSession();
        String userInfo = (String) session.getAttribute("userInfo");
        if(userInfo != null) {
            String[] info = userInfo.split("-");
            uId = Integer.parseInt(info[0]);
        }

        int qId = (Integer) answer.get("qId");

        LinkedHashMap<String, ?> answers = (LinkedHashMap<String, ?>) answer.get("answer");

        Set<String> answersNumberSet = answers.keySet();
        List<Question> questions = questionRepository.findAllByqId(qId);
        for(String answerKey: answersNumberSet) {
            int orderId = Integer.parseInt(answerKey) + 1;
            Question question = questionRepository.findByqIdAndOrderId(qId, orderId);
            int type = question.getType();
            if(type != 1) {
                String answerContent = (String) answer.get(answerKey);
                Answer answerTuple = new Answer();
                answerTuple.setuID(uId);
                answerTuple.setqId(qId);
                answerTuple.setOrderId(orderId);
                answerTuple.setAnswer(answerContent);
            }
            else {
                ArrayList<String> answerContents = (ArrayList<String>) answer.get(answerKey);
                for(int i=0; i<answerContents.size(); i++) {
                    String answerContent = answerContents.get(i);
                    Answer answerTuple = new Answer();
                    answerTuple.setuID(uId);
                    answerTuple.setqId(qId);
                    answerTuple.setOrderId(orderId);
                    answerTuple.setAnswer(answerContent);
                }
            }
        }


        System.out.println(answers);
        return true;
    }
}
