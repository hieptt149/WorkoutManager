package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 03/28/2018.
 */

public class Exercise implements Serializable {

    private int id;
    private String name;
    private String description;
    private String url;
    private int cardioRate;
    private int strengthRate;
    private int mobilityRate;
    private double metsRate;
    private boolean isAdded;

    public Exercise() {
    }

    public Exercise(String name) {
        this.name = name;
    }

    public Exercise(int id, String name, String description, String url, int cardioRate, int strengthRate, int mobilityRate, double metsRate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.cardioRate = cardioRate;
        this.strengthRate = strengthRate;
        this.mobilityRate = mobilityRate;
        this.metsRate = metsRate;
    }

    @PropertyName("id")
    public int getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(int id) {
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

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("url")
    public String getUrl() {
        return url;
    }

    @PropertyName("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @PropertyName("cardio_rate")
    public int getCardioRate() {
        return cardioRate;
    }

    @PropertyName("cardio_rate")
    public void setCardioRate(int cardio_rate) {
        this.cardioRate = cardio_rate;
    }

    @PropertyName("strength_rate")
    public int getStrengthRate() {
        return strengthRate;
    }

    @PropertyName("strength_rate")
    public void setStrengthRate(int strength_rate) {
        this.strengthRate = strength_rate;
    }

    @PropertyName("mobility_rate")
    public int getMobilityRate() {
        return mobilityRate;
    }

    @PropertyName("mobility_rate")
    public void setMobilityRate(int mobility_rate) {
        this.mobilityRate = mobility_rate;
    }

    @PropertyName("mets_rate")
    public double getMetsRate() {
        return metsRate;
    }

    @PropertyName("mets_rate")
    public void setMetsRate(double metsRate) {
        this.metsRate = metsRate;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
