package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire,Integer> {

}
