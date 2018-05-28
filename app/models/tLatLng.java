package models;


import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table(name="trackLatLng")
public class tLatLng extends Model{
    @Id
    public Integer id;

    @ManyToOne
    public Sectors sectors;

    public float latitude;
    public float longitude;


    public static Finder<Integer, tLatLng> find = new Finder( Integer.class, tLatLng.class );




}