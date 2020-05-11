package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.ChoiceMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface ChoiceMapRepository extends JpaRepository<ChoiceMap, Integer> {
    List<ChoiceMap> findAllByqId(int qId);
}
