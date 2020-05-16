package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAllByqIdAndOrderId(int qId, int orderId);
}
