package com.example.questionnaire_backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "MultiChoices")
public class MultiChoices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "choices_list")
    private String choicesList;

    @Column(name = "display")
    private int display;

    public MultiChoices() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
}
