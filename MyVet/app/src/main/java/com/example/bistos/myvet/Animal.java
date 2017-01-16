package com.example.bistos.myvet;

import java.util.UUID;

import io.realm.RealmObject;

public class Animal extends RealmObject {
    private String type;
    private String name;


    //NEW
    private String id;
    //NEW


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Animal(String type, String name) {
        this.type = type;
        this.name = name;
        this.id= UUID.randomUUID().toString();
    }

    public Animal() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
