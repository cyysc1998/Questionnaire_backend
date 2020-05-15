package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.UserQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuestionnaireRepository extends JpaRepository<UserQuestionnaire, Integer> {
    List<UserQuestionnaire> findByuId(int uId);
    UserQuestionnaire findByqId(int qId);
}
