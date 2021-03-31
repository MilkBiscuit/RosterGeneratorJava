package com.cheng.rostergenerator.model;

public class Member {
    
    public String name;
    public boolean isExperienced;
    public boolean assignSpeech;

    public Member(String name, boolean isExperienced, boolean assignSpeech) {
        this.name = name;
        this.isExperienced = isExperienced;
        this.assignSpeech = assignSpeech;
    }

}
