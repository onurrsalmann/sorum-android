package com.sorum.sorum.model;

import java.io.Serializable;

public class ExamAnswer implements Serializable {
    private String soruId;
    private String userAnswer;

    public ExamAnswer(String soruId, String userAnswer) {
        this.soruId = soruId;
        this.userAnswer = userAnswer;
    }

    public void setSoruId(String soruId) {
        this.soruId = soruId;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getSoruId() {
        return soruId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }
}
