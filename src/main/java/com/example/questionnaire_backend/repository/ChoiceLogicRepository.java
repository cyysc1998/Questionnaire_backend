package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.ChoiceLogic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;

public interface ChoiceLogicRepository extends JpaRepository<ChoiceLogic, Integer> {
    List<ChoiceLogic> findAllByChoiceId(int cId);
}
