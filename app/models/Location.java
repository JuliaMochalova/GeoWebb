package models;

import play.data.validation.Constraints;

/**
 * Created by 123 on 23.02.2018.
 */
public class Location {


    public Integer id;
    public String name;


    private double longitude;
    private double latitude;
    private String time;

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLongitude(double longtitude){
        this.longitude = longtitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getName(){
        return name;
    }
}
