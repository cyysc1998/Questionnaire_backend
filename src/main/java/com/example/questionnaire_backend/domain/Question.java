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

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "questionnaire_id")
    private int qId;

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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }
}
