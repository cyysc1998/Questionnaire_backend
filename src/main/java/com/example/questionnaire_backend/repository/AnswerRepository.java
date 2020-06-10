package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByQuestionnaireIdAndOrderId(int qId, int orderId);
    List<Answer> findAllByQuestionnaireIdAndTimeAndOrderId(int qId, LocalDate curDay, int orderId);
    List<Answer> findAllByQuestionnaireIdAndUserIdAndOrderId(int qId, int uId, int orderId);
    List<Answer> findAllByQuestionnaireIdAndIpAndUserIdAndOrderId(int qId, String ip, int uId, int orderId);
    List<Answer> findAllByQuestionnaireIdAndUserIdAndTimeAndOrderId(int qId, int uId, LocalDate time, int orderId);
    List<Answer> findAllByQuestionnaireIdAndIpAndUserIdAndTimeAndOrderId(int qId, String ip, int uId, LocalDate time, int orderId);
}
