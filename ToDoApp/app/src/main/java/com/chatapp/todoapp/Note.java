package com.chatapp.todoapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")//room ; to create sqlite objects //one table
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;// for data objects

    private String title;
    private String description; //we can also use column info ; name etc
    private int priority;
    private String date;
    private Boolean done;
    private String time;



    public Note(String title, String description, int priority, String date,String time, Boolean done) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.done = done;
        this.time =time;
    }

    public String getTime() {
        return time;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getDate() {
        return date;
    }

    public Boolean getDone() {
        return done;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }
}
