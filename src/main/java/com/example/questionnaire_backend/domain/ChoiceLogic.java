package com.example.questionnaire_backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "ChoiceLogic")
public class ChoiceLogic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "choice_id")
    private int choiceId;

    @Column(name = "logic_id")
    private int logicId;

    public ChoiceLogic() {

    }

    public int getId() {
        return id;
    }

    public void setId(int logicId) {
        this.logicId = logicId;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public int getLogicId() {
        return logicId;
    }

    public void setLogicId(int logicId) {
        this.logicId = logicId;
    }
}
