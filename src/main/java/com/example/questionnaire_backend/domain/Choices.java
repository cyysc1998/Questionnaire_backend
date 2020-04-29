package com.example.questionnaire_backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "Choices")
public class Choices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "choice")
    private String choice;

    public Choices() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
