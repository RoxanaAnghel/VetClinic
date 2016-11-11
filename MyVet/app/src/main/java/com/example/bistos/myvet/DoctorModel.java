package com.example.bistos.myvet;

/**
 * Created by Bistos on 11/9/2016.
 */

public class DoctorModel {
    private String position;
    private String name;
    private String Surnme;

    public DoctorModel(String position, String name, String surnme) {
        this.position = position;
        this.name = name;
        Surnme = surnme;
        content=position+" "+name+" "+surnme+" "+position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnme() {
        return Surnme;
    }

    public void setSurnme(String surnme) {
        Surnme = surnme;
    }

    public String content;
}
