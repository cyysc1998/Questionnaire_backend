package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.TextCollect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextCollectRepository extends JpaRepository<TextCollect, Integer> {
}
