package com.example.questionnaire_backend.service.impl;


import com.alibaba.fastjson.JSON;
import com.example.questionnaire_backend.domain.*;
import com.example.questionnaire_backend.repository.*;
import com.example.questionnaire_backend.service.ResolveQuestionnaire;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResolveQuestionnaireImpl  implements ResolveQuestionnaire {

    private final int SINGLE_CHOICE_TYPE = 0;
    private final int MULTI_CHOICES_TYPE = 1;
    private final int INTEGER_COLLECTION_TYPE = 2;
    private final int FLOAT_COLLECTION_TYPE = 3;
    private final int TEXT_COLLECTION_TYPE = 4;
    private final int RATE_COLLECTION_TYPE = 5;

    @Resource
    private QuestionRepository questionRepository;
    @Resource
    private QuestionnaireRepository questionnaireRepository;
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
    @Resource
    private ChoiceLogicRepository choiceLogicRepository;

    public JSONObject resolve(int qId) {
        JSONObject questionnaire = new JSONObject();

        List<Question> questions = questionRepository.findAllByqId(qId);
        Optional<Questionnaire> questionnaireInfo = questionnaireRepository.findById(qId);
        questionnaire.put("title", questionnaireInfo.get().getTitle());
        questionnaire.put("intro", questionnaireInfo.get().getIntroduction());

        ArrayList<JSONObject> resolvedQuestions = new ArrayList<JSONObject>();

        for(int i=0; i<questions.size(); i++) {
            Question question =  questions.get(i);
            switch(question.getType()) {
                case SINGLE_CHOICE_TYPE:
                    resolvedQuestions.add(resolveSingleChoices(question));
                    break;
                case MULTI_CHOICES_TYPE:
                    resolvedQuestions.add(resolveMultiChoices(question));
                    break;
                case TEXT_COLLECTION_TYPE:
                    resolvedQuestions.add(resolveTextCollect(question));
                    break;
                case INTEGER_COLLECTION_TYPE:
                    resolvedQuestions.add(resolveIntegerCollect(question));
                    break;
                case FLOAT_COLLECTION_TYPE:
                    resolvedQuestions.add(resolveFloatCollect(question));
                    break;
                case RATE_COLLECTION_TYPE:
                    resolvedQuestions.add(resolveReteCollect(question));
                    break;
                default:
                    break;
            }
        }

        questionnaire.put("questions", resolvedQuestions);

        return questionnaire;
    }

    /*
     *  TextCollection 解析
     */
    public JSONObject resolveTextCollect(Question info) {
        JSONObject question = new JSONObject();

        int classifiedId = info.getClassifiedId();
        Optional<TextCollect> textQuestion = textCollectRepository.findById(classifiedId);
        String title = textQuestion.get().getTitle();
        Boolean display = textQuestion.get().getDisplay() == 1;

        question.put("type", TEXT_COLLECTION_TYPE);
        question.put("display", display);
        question.put("intro", title);

        return question;
    }

    /*
     *  IntegerCollection解析
     */
    public JSONObject resolveIntegerCollect(Question info) {
        JSONObject question = new JSONObject();

        int classifiedId = info.getClassifiedId();
        Optional<IntegerCollect> integerQuestion = integerCollectRepository.findById(classifiedId);

        String title = integerQuestion.get().getTitle();
        int min = integerQuestion.get().getMin();
        int max = integerQuestion.get().getMax();
        int step = integerQuestion.get().getStep();
        Boolean display = integerQuestion.get().getDisplay() == 1;

        question.put("type", INTEGER_COLLECTION_TYPE);
        question.put("display", display);
        question.put("intro", title);
        question.put("min", min);
        question.put("max", max);
        question.put("step", step);

        return question;
    }

    /*
     *  FloatCollection 解析
     */
    public JSONObject resolveFloatCollect(Question info) {
        JSONObject question = new JSONObject();

        int classifiedId = info.getClassifiedId();
        Optional<FloatCollect> floatQuestion = floatCollectRepository.findById(classifiedId);

        String title = floatQuestion.get().getTitle();
        Boolean display = floatQuestion.get().getDisplay() == 1;
        double min = floatQuestion.get().getMin();
        double max = floatQuestion.get().getMax();
        double step = floatQuestion.get().getStep();
        double precious = floatQuestion.get().getPrecious();

        question.put("type", FLOAT_COLLECTION_TYPE);
        question.put("intro", title);
        question.put("display", display);
        question.put("min", min);
        question.put("max", max);
        question.put("step", step);
        question.put("precious", precious);


        return question;
    }

    /*
     *  RateCollection 解析
     */
    public JSONObject resolveReteCollect(Question info) {
        JSONObject question = new JSONObject();

        int classifiedId = info.getClassifiedId();
        Optional<RateCollect> rateQuestion = rateCollectRepository.findById(classifiedId);

        String title = rateQuestion.get().getTitle();
        Boolean display = rateQuestion.get().getDisplay() == 1;
        double max = rateQuestion.get().getMax();

        question.put("type", RATE_COLLECTION_TYPE);
        question.put("intro", title);
        question.put("display", display);
        question.put("max", max);

        return question;
    }

    /*
     *  MultiChoice 解析
     */
    public JSONObject resolveMultiChoices(Question info) {
        JSONObject question = new JSONObject();

        int classifiedId = info.getClassifiedId();
        Optional<ChoiceCollect> multiQuestion = choiceCollectRepository.findById(classifiedId);

        String title = multiQuestion.get().getTitle();
        Boolean display = multiQuestion.get().getDisplay() == 1;

        ArrayList<String> resolvedChoices = new ArrayList<String>();

        List<ChoiceMap> choices = choiceMapRepository.findAllByqId(classifiedId);

        for(int i=0; i<choices.size(); i++) {
            int choiceId = choices.get(i).getcId();
            Optional<Choices> stringChoice = choicesRepository.findById(choiceId);
            resolvedChoices.add(stringChoice.get().getChoice());
        }



        question.put("type", MULTI_CHOICES_TYPE);
        question.put("intro", title);
        question.put("display", display);
        question.put("choices", resolvedChoices);

        return question;
    }

    /*
     *  SingleChoice 解析
     */
    public JSONObject resolveSingleChoices(Question info) {
        JSONObject question = new JSONObject();

        int classifiedId = info.getClassifiedId();
        Optional<ChoiceCollect> singleQuestion = choiceCollectRepository.findById(classifiedId);

        String title = singleQuestion.get().getTitle();
        Boolean display = singleQuestion.get().getDisplay() == 1;

        ArrayList<String> resolvedChoices = new ArrayList<String>();
        ArrayList<ArrayList<Integer>> resolvedLogic = new ArrayList<ArrayList<Integer>>();

        List<ChoiceMap> choices = choiceMapRepository.findAllByqId(classifiedId);

        for(int i=0; i<choices.size(); i++) {
            int choiceId = choices.get(i).getcId();
            ArrayList<Integer> relatedQuestion = new ArrayList<Integer>();
            Optional<Choices> stringChoice = choicesRepository.findById(choiceId);
            resolvedChoices.add(stringChoice.get().getChoice());

            List<ChoiceLogic> logic = choiceLogicRepository.findAllByChoiceId(choiceId);
            for(int j=0; j<logic.size(); j++) {
                int relatedQuestionId = logic.get(j).getLogicId();
                relatedQuestion.add(relatedQuestionId - 1);
            }
            if(relatedQuestion.isEmpty())
                relatedQuestion.add(-1);
            resolvedLogic.add(relatedQuestion);
        }

        question.put("type", SINGLE_CHOICE_TYPE);
        question.put("intro", title);
        question.put("display", display);
        question.put("choices", resolvedChoices);
        question.put("logic", resolvedLogic);

        return question;
    }

}
