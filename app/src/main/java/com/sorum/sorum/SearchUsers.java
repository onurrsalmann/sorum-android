package com.sorum.sorum;

public class SearchUsers {
    public String username, exam;
    public SearchUsers(){}

    public SearchUsers(String username, String exam) {
        this.username = username;
        this.exam = exam;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }
}
