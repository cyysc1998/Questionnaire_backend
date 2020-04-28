package com.example.questionnaire_backend.repository;

import com.example.questionnaire_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByName(String name);
    User findByEmail(String email);

    List<User> findAllByName(String name);

    List<User> findAllByEmail(String email);
}
