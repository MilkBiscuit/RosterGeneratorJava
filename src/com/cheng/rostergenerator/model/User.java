package com.cheng.rostergenerator.model;

public class User {
    
    public String name;
    public boolean isExperienced;
    public boolean assignSpeech;

    public User(String name, boolean isExperienced, boolean assignSpeech) {
        this.name = name;
        this.isExperienced = isExperienced;
        this.assignSpeech = assignSpeech;
    }

}
