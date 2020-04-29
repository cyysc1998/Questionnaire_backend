package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.MultiChoices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiChoicesRepository extends JpaRepository<MultiChoices, Integer> {
}
