package com.example.questionnaire_backend.domain;

import jdk.jfr.Enabled;

import javax.persistence.*;

@Entity
@Table(name = "RateCollect")
public class RateCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "max")
    private int max;

    @Column(name = "display")
    private int display;

    public RateCollect() {

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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
}
