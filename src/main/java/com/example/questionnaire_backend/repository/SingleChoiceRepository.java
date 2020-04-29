package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.SingleChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleChoiceRepository extends JpaRepository<SingleChoice, Integer> {
}
