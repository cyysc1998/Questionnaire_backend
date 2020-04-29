package com.example.questionnaire_backend.domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Questionnaire")
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "begin_time")
    private Date beginTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "need_register")
    private int needRegister;

    @Column(name = "times_per_day")
    private int timesPerDay;

    @Column(name = "times_total")
    private int timesTotal;

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

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getNeedRegister() {
        return needRegister;
    }

    public void setNeedRegister(int needRegister) {
        this.needRegister = needRegister;
    }

    public int getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(int timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public int getTimesTotal() {
        return timesTotal;
    }

    public void setTimesTotal(int timesTotal) {
        this.timesTotal = timesTotal;
    }
}
