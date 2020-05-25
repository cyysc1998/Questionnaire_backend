package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByqIdAndOrderId(int qId, int orderId);
    List<Answer> findAllByuID(int uId);
    List<Answer> findAllByIp(String ip);
    List<Answer> findAllByuIDAndTime(int uId, LocalDate time);
    List<Answer> findAllByIpAndTime(String ip, LocalDate time);
}
