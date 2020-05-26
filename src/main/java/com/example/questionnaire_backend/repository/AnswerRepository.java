package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByQuestionnaireIdAndOrderId(int qId, int orderId);
    List<Answer> findAllByQuestionnaireIdAndUserId(int qId, int uId);
    List<Answer> findAllByQuestionnaireIdAndIp(int qId, String ip);
    List<Answer> findAllByQuestionnaireIdAndUserIdAndTime(int qId, int uId, LocalDate time);
    List<Answer> findAllByQuestionnaireIdAndIpAndTime(int qId, String ip, LocalDate time);
}
