package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.Choices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoicesRepository extends JpaRepository<Choices, Integer> {
}
