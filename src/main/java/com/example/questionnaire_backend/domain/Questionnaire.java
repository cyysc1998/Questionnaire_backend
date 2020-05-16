package com.example.questionnaire_backend.domain;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "Questionnaire")
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "begin_time")
    private LocalDate beginTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @Column(name = "need_register")
    private Boolean needRegister;

    @Column(name = "times_per_day")
    private Boolean timesPerDay;

    @Column(name = "times_total")
    private Boolean timesTotal;

    @Column(name = "max_times")
    private int maxTimes;

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    @Column(name = "answer_number")
    private int answerNumber;

    public Questionnaire() {

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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public LocalDate getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDate beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public Boolean getNeedRegister() {
        return needRegister;
    }

    public void setNeedRegister(Boolean needRegister) {
        this.needRegister = needRegister;
    }

    public Boolean getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(Boolean timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public Boolean getTimesTotal() {
        return timesTotal;
    }

    public void setTimesTotal(Boolean timesTotal) {
        this.timesTotal = timesTotal;
    }

    public int getMaxTimes() {
        return maxTimes;
    }

    public void setMaxTimes(int maxTimes) {
        this.maxTimes = maxTimes;
    }
}
