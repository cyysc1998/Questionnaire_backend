package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.ChoiceCollect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceCollectRepository extends JpaRepository<ChoiceCollect, Integer> {
}
