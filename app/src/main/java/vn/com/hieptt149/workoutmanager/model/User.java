package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/02/2018.
 */

public class User implements Serializable {

    private String id;
    private int age;
    private double height;
    private double weight;

    public User() {
    }

    public User(int age, double height, double weight) {
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public User(String id, int age, double height, double weight) {
        this.id = id;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("age")
    public int getAge() {
        return age;
    }

    @PropertyName("age")
    public void setAge(int age) {
        this.age = age;
    }

    @PropertyName("height")
    public double getHeight() {
        return height;
    }

    @PropertyName("height")
    public void setHeight(double height) {
        this.height = height;
    }

    @PropertyName("weight")
    public double getWeight() {
        return weight;
    }

    @PropertyName("weight")
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
