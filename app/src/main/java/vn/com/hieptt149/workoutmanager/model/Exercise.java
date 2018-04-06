package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 03/28/2018.
 */

public class Exercise implements Serializable {

    @PropertyName("id")
    public int id;
    @PropertyName("name")
    public String name;
    @PropertyName("description")
    public String description;
    @PropertyName("url")
    public String url;
    @PropertyName("cadio_rate")
    public int cadioRate;
    @PropertyName("strength_rate")
    public int strengthRate;
    @PropertyName("mobility_rate")
    public int mobilityRate;
    public boolean isAdded;

    public Exercise() {
    }

    public Exercise(int id, String name, String description, String url, int cadioRate, int strengthRate, int mobilityRate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.cadioRate = cadioRate;
        this.strengthRate = strengthRate;
        this.mobilityRate = mobilityRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCadioRate() {
        return cadioRate;
    }

    public void setCadioRate(int cadio_rate) {
        this.cadioRate = cadio_rate;
    }

    public int getStrengthRate() {
        return strengthRate;
    }

    public void setStrengthRate(int strength_rate) {
        this.strengthRate = strength_rate;
    }

    public int getMobilityRate() {
        return mobilityRate;
    }

    public void setMobilityRate(int mobility_rate) {
        this.mobilityRate = mobility_rate;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
