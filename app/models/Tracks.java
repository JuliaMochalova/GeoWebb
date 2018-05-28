package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tracks")
public class Tracks extends Model {

    @Id
    public Integer id;

    @ManyToOne
    public Sectors sectors;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Comments> commentsList = new ArrayList<>();

    public static int getCommentsList(int id) {
        return 5;
    }

    public static Finder<Integer, Tracks> find = new Finder( Integer.class, Tracks.class );



}
