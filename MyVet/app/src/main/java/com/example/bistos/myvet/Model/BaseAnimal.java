package com.example.bistos.myvet.Model;

/**
 * Created by Bistos12 on 1/17/2017.
 */

public class BaseAnimal {
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

    public BaseAnimal(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public BaseAnimal() {
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
