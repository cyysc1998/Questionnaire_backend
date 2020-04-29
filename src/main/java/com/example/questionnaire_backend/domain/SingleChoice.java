package com.example.questionnaire_backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "SingleChoice")
public class SingleChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    @Column(name = "title")
    private String title;

    @Column(name = "choices_list")
    private String choicesList;

    @Column(name = "logic_list")
    private String logicList;

    @Column(name = "display")
    private int display;

    public SingleChoice() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChoicesList() {
        return choicesList;
    }

    public void setChoicesList(String choicesList) {
        this.choicesList = choicesList;
    }

    public String getLogicList() {
        return logicList;
    }

    public void setLogicList(String logicList) {
        this.logicList = logicList;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
}

