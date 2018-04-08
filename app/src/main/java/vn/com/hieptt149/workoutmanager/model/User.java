package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/02/2018.
 */

public class User implements Serializable {

    private String id;
    private String name;
    private float height;
    private float weight;

    public User() {
    }

    public User(String id, String name, float height, float weight) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("height")
    public float getHeight() {
        return height;
    }

    @PropertyName("height")
    public void setHeight(float height) {
        this.height = height;
    }

    @PropertyName("weight")
    public float getWeight() {
        return weight;
    }

    @PropertyName("weight")
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
