package com.example.fifth_class.task;

import java.util.Date;

public class Task {

    public final long id;
    private String content;
    private Date date;
    private boolean activate;

    public Task(long id) {
        this.id = id;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }
}
