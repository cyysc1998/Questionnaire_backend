package com.example.questionnaire_backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.questionnaire_backend.domain.*;
import com.example.questionnaire_backend.repository.*;
import com.example.questionnaire_backend.service.ManageInfo;
import com.example.questionnaire_backend.service.impl.tools.Statistic;
import net.minidev.json.JSONObject;
import org.apache.tomcat.jni.Local;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

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
    private UserRepository userRepository;
    @Resource
    private UserQuestionnaireRepository userQuestionnaireRepository;
    @Resource
    private QuestionnaireRepository questionnaireRepository;
    @Resource
    private AnswerRepository answerRepository;
    @Resource
    private QuestionRepository questionRepository;
    @Resource
    private ChoiceCollectRepository choiceCollectRepository;
    @Resource
    private TextCollectRepository textCollectRepository;
    @Resource
    private IntegerCollectRepository integerCollectRepository;
    @Resource
    private FloatCollectRepository floatCollectRepository;
    @Resource
    private RateCollectRepository rateCollectRepository;
    @Resource
    private ChoiceMapRepository choiceMapRepository;
    @Resource
    private ChoicesRepository choicesRepository;

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

        Questionnaire questionnaire = questionnaireRepository.findById(qId).get();
        int curAnswerNumber = questionnaire.getAnswerNumber();
        questionnaire.setAnswerNumber(curAnswerNumber + 1);
        questionnaireRepository.save(questionnaire);

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

    public JSONObject analysis(int qId, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        HttpSession session = request.getSession();
        String userInfo = (String) session.getAttribute("userInfo");
        String[] user = userInfo.split("-");
        int uId = Integer.parseInt(user[0]);

        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.findByqId(qId);
        if(userQuestionnaire == null || userQuestionnaire.getuId() != uId)
            result.put("verify", false);
        else {
            result.put("verify", true);
            System.out.println(qId);
            Questionnaire questionnaire = questionnaireRepository.findById(qId).get();

            JSONObject metadata = new JSONObject();

            LocalDate startTime = questionnaire.getBeginTime();
            LocalDate finishTime = questionnaire.getEndTime();
            Date date = new Date();
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate curDate = instant.atZone(zoneId).toLocalDate();

            if(curDate.isBefore(startTime))
                metadata.put("state", "未开始");
            else if(curDate.isBefore(finishTime))
                metadata.put("state", "进行中");
            else
                metadata.put("state", "已结束");

            metadata.put("title", questionnaire.getTitle());
            metadata.put("intro", questionnaire.getIntroduction());
            metadata.put("startTime", startTime);
            metadata.put("finishTime", finishTime);
            metadata.put("answerNumber", questionnaire.getAnswerNumber());

            result.put("metadata", metadata);


            LinkedList<JSONObject> answer = new LinkedList<>();
            List<Question> questions = questionRepository.findAllByqId(qId);

            for(int i = 0; i < questions.size(); i++) {
                JSONObject curAnswer = new JSONObject();

                Question curQuestion = questions.get(i);
                int key =  i + 1;
                int type = curQuestion.getType();
                int classifiedId = curQuestion.getClassifiedId();

                curAnswer.put("key", key);
                curAnswer.put("type", type);

                switch (type) {
                    case SINGLE_CHOICE_TYPE:
                        ChoiceCollect choiceCollectSingle = choiceCollectRepository.findById(classifiedId).get();
                        String questionSingle = choiceCollectSingle.getTitle();
                        curAnswer.put("type", SINGLE_CHOICE_TYPE);
                        curAnswer.put("question", questionSingle);
                        curAnswer.put("choices", analysisSingle(qId, key, classifiedId));
                        break;
                    case MULTI_CHOICES_TYPE:
                        ChoiceCollect choiceCollectMulti = choiceCollectRepository.findById(classifiedId).get();
                        String questionMulti = choiceCollectMulti.getTitle();
                        curAnswer.put("type", MULTI_CHOICES_TYPE);
                        curAnswer.put("question", questionMulti);
                        curAnswer.put("choices", analysisMulti(qId, key, classifiedId));
                        break;
                    case INTEGER_COLLECTION_TYPE:
                        IntegerCollect integerCollect = integerCollectRepository.findById(classifiedId).get();
                        String questionInteger = integerCollect.getTitle();
                        curAnswer.put("type", INTEGER_COLLECTION_TYPE);
                        curAnswer.put("question", questionInteger);
                        curAnswer.put("answerList", analysisCommonAnswer(qId, key));
                        statistic(qId, key, curAnswer);
                        break;

                    case FLOAT_COLLECTION_TYPE:
                        FloatCollect floatCollect = floatCollectRepository.findById(classifiedId).get();
                        String questionFloat = floatCollect.getTitle();
                        curAnswer.put("type", FLOAT_COLLECTION_TYPE);
                        curAnswer.put("question", questionFloat);
                        curAnswer.put("answerList", analysisCommonAnswer(qId, key));
                        statistic(qId, key, curAnswer);
                        break;

                    case TEXT_COLLECTION_TYPE:
                        TextCollect textCollect = textCollectRepository.findById(classifiedId).get();
                        String questionText = textCollect.getTitle();
                        curAnswer.put("type", TEXT_COLLECTION_TYPE);
                        curAnswer.put("question", questionText);
                        curAnswer.put("answerList", analysisCommonAnswer(qId, key));
                        break;

                    case RATE_COLLECTION_TYPE:
                        RateCollect rateCollect = rateCollectRepository.findById(classifiedId).get();
                        String questionRate = rateCollect.getTitle();
                        int count = rateCollect.getMax();
                        curAnswer.put("type", RATE_COLLECTION_TYPE);
                        curAnswer.put("question", questionRate);
                        curAnswer.put("answerList", analysisRateAnswer(qId, key, count));
                        curAnswer.put("count", count);
                        statistic(qId, key, curAnswer);
                }
                answer.add(curAnswer);
            }
            result.put("answer", answer);
        }



        return result;
    }

    public List<JSONObject> analysisSingle(int qId, int key, int classifiedId) {
        List<JSONObject> choices = new LinkedList<>();
        List<ChoiceMap> choiceMaps = choiceMapRepository.findAllByqId(classifiedId);

        LinkedList<String> choicesContent = new LinkedList<>();
        String[] users = new String[choiceMaps.size()];
        int[] number = new int[choiceMaps.size()];
        int sum = 0;

        for(int i=0; i<choiceMaps.size(); i++) {
            int choiceId = choiceMaps.get(i).getcId();
            Choices choiceTuple = choicesRepository.findById(choiceId).get();
            String choiceContent = choiceTuple.getChoice();
            choicesContent.add(choiceContent);
        }

        List<Answer> answers = answerRepository.findAllByqIdAndOrderId(qId, key);

        for(int i = 0; i < answers.size(); i++) {
            Answer curAnswer = answers.get(i);
            int answer = Integer.parseInt(curAnswer.getAnswer());
            int uId = curAnswer.getuID();

            String userName = getUserName(uId);

            if(users[answer] == null || users[answer] == "")
                users[answer] = userName;
            else
                users[answer] += ", " + userName;
            number[answer]++;
            sum++;
        }

        DecimalFormat df = new DecimalFormat("######0.00");
        for(int i = 0; i < choiceMaps.size(); i++) {
            JSONObject answer = new JSONObject();
            answer.put("key", i + 1);
            answer.put("order", "选项" + (i+1));
            answer.put("answer", choicesContent.get(i));
            answer.put("userNumber", number[i]);
            answer.put("percent", df.format(1.0 * number[i] / sum));
            answer.put("userList", users[i]);
            choices.add(answer);
        }

        return choices;
    }


    public List<JSONObject> analysisMulti(int qId, int key, int classifiedId) {
        List<JSONObject> choices = new LinkedList<>();
        List<ChoiceMap> choiceMaps = choiceMapRepository.findAllByqId(classifiedId);
        LinkedList<String> choicesContent = new LinkedList<>();
        String[] users = new String[choiceMaps.size()];
        int[] number = new int[choiceMaps.size()];
        int sum = 0;

        for(int i=0; i<choiceMaps.size(); i++) {
            int choiceId = choiceMaps.get(i).getcId();
            Choices choiceTuple = choicesRepository.findById(choiceId).get();
            String choiceContent = choiceTuple.getChoice();
            choicesContent.add(choiceContent);
        }

        List<Answer> answers = answerRepository.findAllByqIdAndOrderId(qId, key);

        for(int i = 0; i < answers.size(); i++) {
            Answer curAnswer = answers.get(i);
            String[] splitAnswer = curAnswer.getAnswer().split("-");
            for(int j=0; j<splitAnswer.length; j++) {
                int answer = Integer.parseInt(splitAnswer[j]);
                int uId = curAnswer.getuID();

                String userName = getUserName(uId);

                if(users[answer] == null || users[answer] == "")
                    users[answer] = userName;
                else
                    users[answer] += ", " + userName;
                number[answer]++;
                sum++;
            }
        }

        DecimalFormat df = new DecimalFormat("######0.00");
        for(int i = 0; i < choiceMaps.size(); i++) {
            JSONObject answer = new JSONObject();
            answer.put("key", i + 1);
            answer.put("order", "选项" + (i+1));
            answer.put("answer", choicesContent.get(i));
            answer.put("userNumber", number[i]);
            answer.put("percent", df.format(1.0 * number[i] / sum));
            answer.put("userList", users[i]);
            choices.add(answer);
        }
        return choices;
    }



    public List<JSONObject> analysisCommonAnswer(int qId, int key) {
        List<JSONObject> answerList = new LinkedList<>();
        List<Answer> answers = answerRepository.findAllByqIdAndOrderId(qId, key);
        for(int i=0; i<answers.size(); i++) {
            JSONObject answer = new JSONObject();
            Answer curAnswer = answers.get(i);
            answer.put("key", i+1);
            answer.put("answer", curAnswer.getAnswer());
            int uId = curAnswer.getuID();
            String userName = getUserName(uId);
            answer.put("user", userName);
            answerList.add(answer);
        }
        return answerList;
    }

    public List<JSONObject> analysisRateAnswer(int qId, int key, int count) {
        List<JSONObject> answerList = new LinkedList<>();
        List<Answer> answers = answerRepository.findAllByqIdAndOrderId(qId, key);
        for(int i=0; i<answers.size(); i++) {
            JSONObject answer = new JSONObject();
            Answer curAnswer = answers.get(i);
            answer.put("key", i+1);
            answer.put("answer", curAnswer.getAnswer() + "/" + count);
            int uId = curAnswer.getuID();
            String userName = getUserName(uId);
            answer.put("user", userName);
            answerList.add(answer);
        }
        return answerList;
    }


    public void statistic(int qId, int key, JSONObject curAnswer) {
        ArrayList<Double> numberList = new ArrayList<>();
        List<Answer> answers = answerRepository.findAllByqIdAndOrderId(qId, key);
        for(int j=0; j<answers.size(); j++) {
            numberList.add(Double.parseDouble(answers.get(j).getAnswer()));
        }
        curAnswer.put("min", Statistic.min(numberList));
        curAnswer.put("max", Statistic.max(numberList));
        curAnswer.put("sum", Statistic.sum(numberList));
        curAnswer.put("average", Statistic.average(numberList));
        curAnswer.put("median", Statistic.median(numberList));
        curAnswer.put("mode", Statistic.mode(numberList));
    }

    String getUserName(int uId) {
        if(uId < 0)
            return "匿名用户";
        User user = userRepository.findById(uId).get();
        String userName = user.getName();
        return userName;
    }

}
