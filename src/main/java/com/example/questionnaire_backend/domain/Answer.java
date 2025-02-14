package com.example.questionnaire_backend.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "u_Id")
    private int userId;

    @Column(name = "q_Id")
    private int questionnaireId;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "answer")
    private String answer;

    @Column(name = "order_Id")
    private int orderId;

    @Column(name = "position")
    private String position;

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    @Column(name = "ip")
    private String ip;

    @Column(name = "time")
    private LocalDate time;

    public Answer() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getqQuestionnaireId() {
        return questionnaireId;
    }

    public void setqQuestionnaireId(int qId) {
        this.questionnaireId = qId;
    }

    public int getuUId() {
        return userId;
    }

    public void setuUId(int uID) {
        this.userId = uID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
