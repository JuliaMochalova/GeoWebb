package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="sectors")
public class Sectors extends Model{

    @Id
    public Integer id;

    public String name;
    public String firstPoint;
    public String endPoint;



    @OneToMany(cascade = CascadeType.ALL)
    public List<tLatLng> trackList = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.ALL)
    public List<Tracks> routeList = new ArrayList<>();

    public static Finder<Integer, Sectors> find = new Finder( Integer.class, Sectors.class );


}