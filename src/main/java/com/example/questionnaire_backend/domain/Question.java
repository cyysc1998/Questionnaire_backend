package com.example.questionnaire_backend.domain;

import javax.persistence.*;

@Entity
@Table(name="Question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "type")
    private int type;

    @Column(name = "classified_id")
    private int classifiedId;

    public Question() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getClassifiedId() {
        return classifiedId;
    }

    public void setClassifiedId(int classifiedId) {
        this.classifiedId = classifiedId;
    }
}
