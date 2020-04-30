package com.example.questionnaire_backend.service.impl;

import com.example.questionnaire_backend.domain.*;
import com.example.questionnaire_backend.repository.*;
import com.example.questionnaire_backend.service.CreateQuestionnaire;
import net.minidev.json.JSONObject;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;



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
            switch (type) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    parseIntegerCollect(questionsList.get(i));
                    break;
                case 3:
                    parseFloatCollect(questionsList.get(i));
                    break;
                case 4:
                    parseTextCollect(questionsList.get(i));
                    break;
                case 5:
                    parseRateCollect(questionsList.get(i));
                    break;
                default:
                    break;
            }
        }

        return SUCCEED;
    }


    private Metadata getMetadata(LinkedHashMap<String, String> metadata) {
        Metadata parseMataData = new Metadata();

        parseMataData.title = metadata.get("title");
        parseMataData.intro = metadata.get("intro");

        return parseMataData;
    }

    private Setting getSetting(LinkedHashMap<String, ?> setting) {
        Setting parseSettingData = new Setting();

        parseSettingData.setMaxTimes = (Boolean) setting.get("maxTimes");
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
        int orderId = (Integer) integerCollectInfo.get("id");
        String title = (String) content.get("intro");
        int min = Integer.parseInt ((String) content.get("min"));
        int max = Integer.parseInt ((String) content.get("max"));
        int step = Integer.parseInt ((String) content.get("step"));
        integerCollect.setTitle(title);
        integerCollect.setId(orderId);
        integerCollect.setMin(min);
        integerCollect.setMax(max);
        integerCollect.setStep(step);
        integerCollect.setDisplay(0);
        IntegerCollect saved = integerCollectRepository.save(integerCollect);
        return saved.getId();
    }

    private int parseFloatCollect(LinkedHashMap<String, ?> floatCollectInfo) {
        FloatCollect floatCollect = new FloatCollect();
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) floatCollectInfo.get("q_content");
        int orderId = (Integer) floatCollectInfo.get("id");
        String title = (String) content.get("intro");
        Double min = Double.parseDouble ((String) content.get("min"));
        Double max = Double.parseDouble ((String) content.get("max"));
        Double step = Double.parseDouble ((String) content.get("step"));
        Double precious = Double.parseDouble ( (String) content.get("precious"));
        floatCollect.setTitle(title);
        floatCollect.setId(orderId);
        floatCollect.setMin(min);
        floatCollect.setMax(max);
        floatCollect.setStep(step);
        floatCollect.setPrecious(precious);
        floatCollect.setDisplay(0);
        FloatCollect saved = floatCollectRepository.save(floatCollect);
        return saved.getId();
    }

    private int parseTextCollect(LinkedHashMap<String, ?> textCollectInfo) {
        TextCollect textCollect = new TextCollect();
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) textCollectInfo.get("q_content");
        int orderId = (Integer) textCollectInfo.get("id");
        String title = (String) content.get("intro");
        textCollect.setId(orderId);
        textCollect.setTitle(title);
        textCollect.setDisplay(0);
        TextCollect saved = textCollectRepository.save(textCollect);
        return saved.getId();
    }

    private int parseRateCollect(LinkedHashMap<String, ?> rateCollectInfo) {
        RateCollect rateCollect = new RateCollect();
        LinkedHashMap<String, ?> content = (LinkedHashMap<String, ?>) rateCollectInfo.get("q_content");
        int orderId = (Integer) rateCollectInfo.get("id");
        String title = (String) content.get("intro");
        int max = Integer.parseInt((String) content.get("max"));
        rateCollect.setId(orderId);
        rateCollect.setTitle(title);
        rateCollect.setMax(max);
        rateCollect.setDisplay(0);
        RateCollect saved = rateCollectRepository.save(rateCollect);
        return saved.getId();
    }
}
