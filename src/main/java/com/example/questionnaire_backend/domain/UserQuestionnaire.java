package com.example.questionnaire_backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "UserQuestionnaire")
public class UserQuestionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "u_id")
    private int uId;

    @Column(name = "q_id")
    private int qId;

    public UserQuestionnaire() {

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

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }
}
