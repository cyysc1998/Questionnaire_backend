package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.UserQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionnaireRepository extends JpaRepository<UserQuestionnaire, Integer> {
}
