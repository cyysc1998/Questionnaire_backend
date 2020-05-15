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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class ManageInfoImpl implements ManageInfo {

    private final int SINGLE_CHOICE_TYPE = 0;
    private final int MULTI_CHOICES_TYPE = 1;
    private final int INTEGER_COLLECTION_TYPE = 2;
    private final int FLOAT_COLLECTION_TYPE = 3;
    private final int TEXT_COLLECTION_TYPE = 4;
    private final int RATE_COLLECTION_TYPE = 5;

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

            question.put("qId", qId);
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

        for(String answerKey: answersNumberSet) {
            int orderId = Integer.parseInt(answerKey) + 1;
            Question question = questionRepository.findByqIdAndOrderId(qId, orderId);
            int type = question.getType();

            Answer answerTuple = new Answer();

            if(type == MULTI_CHOICES_TYPE){
                ArrayList<Integer> answerContents = (ArrayList<Integer>) answers.get(answerKey);
                String answerContent = null;
                Boolean is_first = true;
                for(int i=0; i<answerContents.size(); i++) {
                    String s = answerContents.get(i).toString();
                    if(is_first) {
                        answerContent = s;
                        is_first = false;
                    }
                    else
                answerContent = answerContent + "-" + s;
            }
                answerTuple.setuID(uId);
                answerTuple.setqId(qId);
                answerTuple.setOrderId(orderId);
                answerTuple.setAnswer(answerContent);
            }
            else if(type == FLOAT_COLLECTION_TYPE || type == RATE_COLLECTION_TYPE) {
                String answerContent = null;
                if(answers.get(answerKey) instanceof Double)
                    answerContent = ((Double) answers.get(answerKey)).toString();
                else
                    answerContent = ((Integer) answers.get(answerKey)).toString();
                answerTuple.setuID(uId);
                answerTuple.setqId(qId);
                answerTuple.setOrderId(orderId);
                answerTuple.setAnswer(answerContent);
            }
            else if(type == TEXT_COLLECTION_TYPE) {
                String answerContent = (String) answers.get(answerKey);
                answerTuple.setuID(uId);
                answerTuple.setqId(qId);
                answerTuple.setOrderId(orderId);
                answerTuple.setAnswer(answerContent);
            }
            else if(type == SINGLE_CHOICE_TYPE || type == INTEGER_COLLECTION_TYPE){
                String answerContent = ((Integer) answers.get(answerKey)).toString();
                answerTuple.setuID(uId);
                answerTuple.setqId(qId);
                answerTuple.setOrderId(orderId);
                answerTuple.setAnswer(answerContent);
            }
            answerRepository.save(answerTuple);
        }


        System.out.println(answers);
        return true;
    }

    public Boolean modified(@RequestBody JSONObject info, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userInfo = (String) session.getAttribute("userInfo");
        String[] user = userInfo.split("-");
        int uId = Integer.parseInt(user[0]);

        int questionnaireId = (Integer) info.get("questionnaireId");
        String title = (String) info.get("title");
        String intro = (String) info.get("intro");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate finishTime = LocalDate.parse((String) info.get("finishTime"), formatter);


        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.findByqId(questionnaireId);
        if(userQuestionnaire == null || userQuestionnaire.getuId() != uId)
            return false;
        else {
            Optional<Questionnaire> questionnaireById = questionnaireRepository.findById(questionnaireId);
            Questionnaire questionnaire = questionnaireById.get();
            questionnaire.setTitle(title);
            questionnaire.setIntroduction(intro);
            questionnaire.setEndTime(finishTime);
            questionnaireRepository.save(questionnaire);
        }
        return true;
    }
}
