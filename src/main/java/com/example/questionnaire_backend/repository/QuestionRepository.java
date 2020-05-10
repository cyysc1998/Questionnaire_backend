package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
