package com.example.questionnaire_backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "ChoiceMap")
public class ChoiceMap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "question_id")
    private int qId;

    @Column(name = "choice_id")
    private int cId;

    public ChoiceMap() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }
}
