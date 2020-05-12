package com.example.questionnaire_backend.service.impl;

import com.example.questionnaire_backend.domain.*;
import com.example.questionnaire_backend.repository.*;
import com.example.questionnaire_backend.service.CreateQuestionnaire;
import net.minidev.json.JSONObject;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.GeneratedValue;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;


@Service
@Transactional
public class CreateQuestionnaireImpl implements CreateQuestionnaire {

    private final int SUCCEED = 1;
    private final int TITLE_BLANK_ERROR = -1;
    private final int INTRO_BLANK_ERROR = -2;
    private final int REGISTER_TIMES_ERROR = -3;
    private final int LOGIC_ERROR = -4;
    private final int DATA_PARSE_ERROR = -5;
    private final int DATA_LOGIC_ERROR = -6;

    @Resource
    private UserQuestionnaireRepository userQuestionnaireRepository;
    @Resource
    private QuestionnaireRepository questionnaireRepository;
    @Resource
    private IntegerCollectRepository integerCollectRepository;
    @Resource
    private FloatCollectRepository floatCollectRepository;
    @Resource
    private TextCollectRepository textCollectRepository;
    @Resource
    private RateCollectRepository rateCollectRepository;
    @Resource
    private ChoiceCollectRepository choiceCollectRepository;
    @Resource
    private ChoicesRepository choicesRepository;
    @Resource
    private ChoiceMapRepository choiceMapRepository;
    @Resource
    private ChoiceLogicRepository choiceLogicRepository;
    @Resource
    private QuestionRepository questionRepository;

    private class Metadata {
        String title;
        String intro;
    }

    private class Setting {
        Boolean setMaxTimes;
        Boolean needRegister;
        Boolean setMaxTimesPerDay;
        int restrictTimes;
        LocalDate beginTime;
        LocalDate finishTime;
    }

    public int create(JSONObject data) {
        System.out.println(data);

        int uId = (Integer) data.get("u_id");
        LinkedHashMap<String, String> metadataMap = (LinkedHashMap<String, String>) data.get("metadata");
        LinkedHashMap<String, ?> settingMap = (LinkedHashMap<String, ?>) data.get("setting");
        LinkedHashMap<String, ?> logicInfo = (LinkedHashMap<String, ?>) data.get("related");
        ArrayList<LinkedHashMap<String, ?>> questionsList = (ArrayList<LinkedHashMap<String, ?>>) data.get("content");

        Metadata metadata;
        Setting setting;

        try{
            metadata = getMetadata(metadataMap);
            setting = getSetting(settingMap);
        }
        catch (Exception e) {
            e.printStackTrace();
            return DATA_PARSE_ERROR;
        }

        int judgeMetadataResult = judgeMedaData(metadata);
        int juedeSettingResult = judgeSetting(setting);

        if(judgeMetadataResult < 0)
            return judgeMetadataResult;
        if(juedeSettingResult < 0)
            return juedeSettingResult;

        int qId = insertQuestionnaire(metadata, setting);

        for(int i=0; i<questionsList.size(); i++) {
            int type = (Integer) questionsList.get(i).get("type");
            int orderId = (Integer) questionsList.get(i).get("id");
            int questionId = 0;
            switch (type) {
                case 0:
                    questionId = parseSingleChoice(questionsList.get(i), logicInfo);
                    break;
                case 1:
                    questionId = parseMultiChoice(questionsList.get(i));
                    break;
                case 2:
                    questionId = parseIntegerCollect(questionsList.get(i));
                    break;
                case 3:
                    questionId = parseFloatCollect(questionsList.get(i));
                    break;
                case 4:
                    questionId = parseTextCollect(questionsList.get(i));
                    break;
                case 5:
                    questionId = parseRateCollect(questionsList.get(i));
                    break;
                default:
                    break;
            }
            insertQuestionMap(type, questionId, orderId, qId);
        }

        insertUserQuestionnaire(uId, qId);

        return qId;
    }


    private Metadata getMetadata(LinkedHashMap<String, String> metadata) {
        Metadata parseMataData = new Metadata();

        parseMataData.title = metadata.get("title");
        parseMataData.intro = metadata.get("intro");

        return parseMataData;
    }

    private Setting getSetting(LinkedHashMap<String, ?> setting) {
        Setting parseSettingData = new Setting();

        parseSettingData.setMaxTimes = setting.get("maxTimes").toString().equals("1");
        parseSettingData.needRegister = setting.get("needRegister").toString().equals("1");
        parseSettingData.setMaxTimesPerDay = setting.get("maxTimesPerDay").toString().equals("1");
        parseSettingData.restrictTimes = Integer.parseInt((String) setting.get("resistrictTimes"));
        parseSettingData.beginTime = LocalDate.now();
        parseSettingData.finishTime = LocalDate.parse((String) setting.get("finishTime"));

        return parseSettingData;
    }

    private int judgeMedaData(Metadata metadata) {
        if(metadata.title.equals(""))
            return TITLE_BLANK_ERROR;
        if(metadata.intro.equals(""))
            return INTRO_BLANK_ERROR;
        return SUCCEED;
    }

    private int judgeSetting(Setting setting) {
        if(setting.restrictTimes <= 0)
            return REGISTER_TIMES_ERROR;
        if(setting.beginTime.compareTo(setting.finishTime) >= 0)
            return DATA_LOGIC_ERROR;
        return SUCCEED;
    }

    private void insertQuestionMap(int type, int classifiedId, int orderId, int qId) {
        Question question = new Question();
        question.setType(type);
        question.setClassifiedId(classifiedId);
        question.setOrderId(orderId);
        question.setqId(qId);
        questionRepository.save(question);
    }

    private void insertUserQuestionnaire(int uId, int qId) {
        UserQuestionnaire userQuestionnaire = new UserQuestionnaire();
        userQuestionnaire.setqId(qId);
        userQuestionnaire.setuId(uId);
        userQuestionnaireRepository.save(userQuestionnaire);
    }

    private int insertQuestionnaire(Metadata metadata, Setting setting) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitle(metadata.title);
        questionnaire.setIntroduction(metadata.intro);
        questionnaire.setNeedRegister(setting.needRegister);
        questionnaire.setTimesPerDay(setting.setMaxTimesPerDay);
        questionnaire.setTimesTotal(setting.setMaxTimes);
        questionnaire.setMaxTimes(setting.restrictTimes);
        questionnaire.setBeginTime(setting.beginTime);
        questionnaire.setEndTime(setting.finishTime);
        Questionnaire savedEntity = questionnaireRepository.save(questionnaire);
        return savedEntity.getId();
    }

    private int parseIntegerCollect(LinkedHashMap<String, ?> integerCollectInfo) {
        IntegerCollect integerCollect = new IntegerCollect();
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) integerCollectInfo.get("q_content");
        String title = (String) content.get("intro");
        int min = Integer.parseInt ((String) content.get("min"));
        int max = Integer.parseInt ((String) content.get("max"));
        int step = Integer.parseInt ((String) content.get("step"));
        integerCollect.setTitle(title);
        integerCollect.setMin(min);
        integerCollect.setMax(max);
        integerCollect.setStep(step);
        integerCollect.setDisplay(1);
        IntegerCollect saved = integerCollectRepository.save(integerCollect);
        return saved.getId();
    }

    private int parseFloatCollect(LinkedHashMap<String, ?> floatCollectInfo) {
        FloatCollect floatCollect = new FloatCollect();
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) floatCollectInfo.get("q_content");
        String title = (String) content.get("intro");
        Double min = Double.parseDouble ((String) content.get("min"));
        Double max = Double.parseDouble ((String) content.get("max"));
        Double step = Double.parseDouble ((String) content.get("step"));
        Double precious = Double.parseDouble ( (String) content.get("precious"));
        floatCollect.setTitle(title);
        floatCollect.setMin(min);
        floatCollect.setMax(max);
        floatCollect.setStep(step);
        floatCollect.setPrecious(precious);
        floatCollect.setDisplay(1);
        FloatCollect saved = floatCollectRepository.save(floatCollect);
        return saved.getId();
    }

    private int parseTextCollect(LinkedHashMap<String, ?> textCollectInfo) {
        TextCollect textCollect = new TextCollect();
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) textCollectInfo.get("q_content");
        String title = (String) content.get("intro");
        textCollect.setTitle(title);
        textCollect.setDisplay(1);
        TextCollect saved = textCollectRepository.save(textCollect);
        return saved.getId();
    }

    private int parseRateCollect(LinkedHashMap<String, ?> rateCollectInfo) {
        RateCollect rateCollect = new RateCollect();
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) rateCollectInfo.get("q_content");
        String title = (String) content.get("intro");
        int max = Integer.parseInt((String) content.get("max"));
        rateCollect.setTitle(title);
        rateCollect.setMax(max);
        rateCollect.setDisplay(1);
        RateCollect saved = rateCollectRepository.save(rateCollect);
        return saved.getId();
    }

    private int parseMultiChoice(LinkedHashMap<String, ?> multiCollectInfo) {
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) multiCollectInfo.get("q_content");
        ArrayList<String> choicesInfo = (ArrayList<String>) ((LinkedHashMap<String, ?>)content.get("choices")).get("names");

        String title = (String) content.get("intro");
        ChoiceCollect choiceCollect = new ChoiceCollect();
        choiceCollect.setTitle(title);
        choiceCollect.setDisplay(1);
        ChoiceCollect saved = choiceCollectRepository.save(choiceCollect);

        int qId = saved.getId();

        for(int i=0; i<choicesInfo.size(); i++) {
            String choice = choicesInfo.get(i);
            Choices choices = new Choices();
            choices.setChoice(choice);
            Choices cSaved = choicesRepository.save(choices);
            int cId = cSaved.getId();

            ChoiceMap choiceMap = new ChoiceMap();
            choiceMap.setqId(qId);
            choiceMap.setcId(cId);
            choiceMapRepository.save(choiceMap);
        }

        return qId;
    }

    private int parseSingleChoice(LinkedHashMap<String, ?> singleCollectInfo, LinkedHashMap<String, ?> logicInfo) {
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) singleCollectInfo.get("q_content");
        ArrayList<String> choicesInfo = (ArrayList<String>) ((LinkedHashMap<String, ?>)content.get("choices")).get("names");
        int orderedId = (Integer) singleCollectInfo.get("id");


        String title = (String) content.get("intro");
        ChoiceCollect choiceCollect = new ChoiceCollect();
        choiceCollect.setTitle(title);
        choiceCollect.setDisplay(1);
        ChoiceCollect saved = choiceCollectRepository.save(choiceCollect);

        int qId = saved.getId();


        for(int i=0; i<choicesInfo.size(); i++) {
            String choice = choicesInfo.get(i);
            Choices choices = new Choices();
            choices.setChoice(choice);
            Choices cSaved = choicesRepository.save(choices);

            int cId = cSaved.getId();

            ChoiceMap choiceMap = new ChoiceMap();
            choiceMap.setqId(qId);
            choiceMap.setcId(cId);
            choiceMapRepository.save(choiceMap);

            Set<String> keySet = logicInfo.keySet();

            for(String choiceInfo: keySet) {
                String[] questionAndChoiceIndex = choiceInfo.split("-");
                int questionId = Integer.parseInt(questionAndChoiceIndex[0]);
                int choiceId = Integer.parseInt(questionAndChoiceIndex[1]);
                if(questionId != orderedId || choiceId != i )
                    continue;
                ArrayList<String> relatedQuestions = (ArrayList<String>) logicInfo.get(choiceInfo);

                for(String relatedQuestion: relatedQuestions) {
                    ChoiceLogic choiceLogic = new ChoiceLogic();
                    int relatedqId = Integer.parseInt(relatedQuestion);
                    choiceLogic.setChoiceId(cId);
                    choiceLogic.setLogicId(relatedqId);
                    choiceLogicRepository.save(choiceLogic);
                }
            }
        }
        return qId;
    }
}
