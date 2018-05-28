package models;


import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;

public class PointsList extends Model {



    public List<String[]> pointsList = new ArrayList<>();

    //public void add(String[] list){
        //PointsList.add(list);
    //}

    public String[] get(int i){
        return pointsList.get(i);
    }



}