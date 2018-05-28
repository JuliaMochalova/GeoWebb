package models;

import play.db.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name="comments")
public class Comments extends Model {

    @Id
    public Integer id;

    @ManyToOne
    public Tracks tracks;

    public String comment;

    public static Finder<Integer, Comments> find = new Finder( Integer.class, Comments.class );



}
