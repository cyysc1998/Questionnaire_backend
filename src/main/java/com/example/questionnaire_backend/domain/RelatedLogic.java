package com.example.questionnaire_backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "RelatedLogic")
public class RelatedLogic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "related_list")
    private String relatedList;

    public RelatedLogic() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelatedList() {
        return relatedList;
    }

    public void setRelatedList(String relatedList) {
        this.relatedList = relatedList;
    }
}
