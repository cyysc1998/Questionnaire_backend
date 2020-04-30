package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.ChoiceMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceMapRepository extends JpaRepository<ChoiceMap, Integer> {
}
