package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findAllByqId(int qId);
    Question findByqIdAndOrderId(int qId, int orderId);
}
