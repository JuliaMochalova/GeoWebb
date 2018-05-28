package controllers;

import Parser.ParserXml;
import models.*;
import models.Tracks;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static play.data.Form.form;

public class Application extends Controller {


    static Form<tLatLng> gpsForm = form(tLatLng.class);
    static Form<Sectors> gpsListForm = form(Sectors.class);
    static String[] nul = new String[0];


    public static Result index() {

        return ok(views.html.index.render(nul, gpsForm,"",allPoints()));
    }

    public static Result main() {
        PointsList allPoints = new PointsList();
        for (int i = 0; i<Sectors.find.all().size(); i++){
            Sectors.find.all().clear();
        }

        return ok(views.html.index.render(nul, gpsForm,"",allPoints));
    }

    public static Result insertGPS() {

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart RequestFile = body.getFile("GpsFile");
        if (RequestFile != null) {
            if (RequestFile.getContentType().equals("application/octet-stream")) {


                ParserXml parser= new ParserXml();

                //JOptionPane.showMessageDialog(null,Arrays.toString(parser.getPoints(RequestFile.getFile())));
                return ok(views.html.index.render(parser.getPoints(RequestFile.getFile()), gpsForm,"",allPoints()));


            } else {
                flash("error", "Wrong type " + RequestFile.getContentType());
                return badRequest();
            }
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

    public static Result DownloadSubmit(){
        return null;
    }

    public static Result saveGPS(){
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        String GpsName;
        String GpsStart;
        String[] GpsPoints;
        String[] GpsDots;
        String GpsEnd;
        int[] time=new int[2];

        float[] oneLatLng = new float[2];
        float[] twoLatLng= new float[2];
        String[] splits = new String[2];
        int length=0;

        DynamicForm RequestFile = Form.form().bindFromRequest();
        if (RequestFile != null) {
            String request = RequestFile.get("points");
            String comment = RequestFile.get("comment");
            String name = RequestFile.get("name");


            GpsDots = request.split("!");
            if (GpsDots.length>1){
                int id=0;
                Tracks tracks = new Tracks();
                Comments comments = new Comments();
                comments.comment = comment;
                tracks.commentsList.add(comments);

                for (int j=0;j<GpsDots.length-1;j++){
                    GpsPoints = (GpsDots[j]).split(";");
                    GpsStart = GpsPoints[0];
                    GpsEnd = GpsPoints[GpsPoints.length-1];

                    Sectors sectors = new Sectors();

                    sectors.name = name;
                    sectors.firstPoint = (GpsStart);
                    sectors.endPoint = (GpsEnd);
                    sectors.routeList.add(tracks);

                    //JOptionPane.showMessageDialog(null,GpsDots[0]);
                    for (int i=0;i<GpsPoints.length;i++){
                        tLatLng gps = new tLatLng();
                        splits = GpsPoints[i].split(",");
                        //removeCharAt(removeCharAt(GpsPoints[i], 0))
                        gps.latitude =new BigDecimal(new BigDecimal(removeCharAt(splits[0],0).trim()).setScale(5, RoundingMode.HALF_UP).floatValue()).setScale(4, RoundingMode.HALF_UP).floatValue();
                        gps.longitude =new BigDecimal(new BigDecimal(removeCharAt(splits[1]).trim()).setScale(5, RoundingMode.HALF_UP).floatValue()).setScale(4, RoundingMode.HALF_UP).floatValue();
                        sectors.trackList.add(sectors.trackList.size(),gps);
                    }
                    sectors.save();
                    //запоминаем id последнего кусочка, то есть общей части
                    id = sectors.id;
                    }
                splits = (GpsDots[GpsDots.length - 1].split(";")[0]).split(",");
                oneLatLng[0] = Float.parseFloat(removeCharAt(splits[0],0).trim());
                oneLatLng[1] = Float.parseFloat(removeCharAt(splits[1]).trim());
                time[0] = Integer.parseInt(splits[2]);

                splits = (GpsDots[GpsDots.length - 1].split(";")[1]).split(",");
                twoLatLng[0] = Float.parseFloat(removeCharAt(splits[0],0).trim());
                twoLatLng[1] = Float.parseFloat(removeCharAt(splits[1]).trim());
                time[1] = Integer.parseInt(splits[2]);
                //JOptionPane.showMessageDialog(null,oneLatLng[0]+" "+oneLatLng[1]+" "+twoLatLng[0]+" "+twoLatLng[1]);

                List<Sectors> allTracks = Sectors.find.all();
                List<tLatLng> list = allTracks.get(Integer.parseInt(GpsDots[GpsDots.length - 1].split(";")[2])).trackList;
                String[] index = new String[list.size()];

                double[] min=new double[2];
                int[] ind=new int[2];
                min[0]=min[1]=5;
                for(int i=0;i<list.size();i++){
                    index[i]= list.get(i).latitude+","+list.get(i).longitude;
                    if (Math.abs(list.get(i).latitude-oneLatLng[0])+Math.abs(list.get(i).longitude-oneLatLng[1])<min[0]){
                        min[0]=Math.abs(list.get(i).latitude-oneLatLng[0])+Math.abs(list.get(i).longitude-oneLatLng[1]);
                        ind[0]=i;
                    }
                    if (Math.abs(list.get(i).latitude-twoLatLng[0])+Math.abs(list.get(i).longitude-twoLatLng[1])<min[1]){
                        min[1]=Math.abs(list.get(i).latitude-twoLatLng[0])+Math.abs(list.get(i).longitude-twoLatLng[1]);
                        ind[1]=i;
                    }
                }

                if (ind[0]<ind[1]){


                }else{

                    List<Tracks> route2 = (Sectors.find.byId(Integer.parseInt(GpsDots[GpsDots.length - 1].split(";")[2]) + 1).routeList);

                    Sectors track2 = Sectors.find.byId(id);
                    track2.routeList.addAll(route2);
                    track2.save();
                    Sectors sectors = new Sectors();
                    tLatLng gps = new tLatLng();
                    gps.latitude =new BigDecimal(new BigDecimal(oneLatLng[0]).setScale(5, RoundingMode.HALF_UP).floatValue()).setScale(4, RoundingMode.HALF_UP).floatValue();
                    gps.longitude =new BigDecimal(new BigDecimal(oneLatLng[1]).setScale(5, RoundingMode.HALF_UP).floatValue()).setScale(4, RoundingMode.HALF_UP).floatValue();
                    sectors.trackList.add(gps);

                    //первая часть перед общей
                    for (int i=ind[0];i<list.size();i++){
                        gps = new tLatLng();
                        gps.latitude =list.get(i).latitude;
                        gps.longitude =list.get(i).longitude;
                        sectors.trackList.add(gps);
                    }


                    sectors.routeList.addAll(route2);
                    sectors.save();


                    //вторая часть перед общей
                    sectors = new Sectors();
                    gps = new tLatLng();
                    for (int i=0;i<ind[1]+1;i++){
                        gps = new tLatLng();
                        gps.latitude =list.get(i).latitude;
                        gps.longitude =list.get(i).longitude;
                        sectors.trackList.add(gps);

                    }
                    gps.latitude =new BigDecimal(new BigDecimal(twoLatLng[0]).setScale(5, RoundingMode.HALF_UP).floatValue()).setScale(4, RoundingMode.HALF_UP).floatValue();
                    gps.longitude =new BigDecimal(new BigDecimal(twoLatLng[1]).setScale(5, RoundingMode.HALF_UP).floatValue()).setScale(4, RoundingMode.HALF_UP).floatValue();
                    sectors.trackList.add(gps);
                    sectors.routeList.addAll(route2);

                    sectors.save();
                    Sectors.find.byId(Integer.parseInt(GpsDots[GpsDots.length - 1].split(";")[2])+1).delete();

                }
            }
                else {

                Tracks track = new Tracks();
                Comments comments = new Comments();
                comments.comment = comment;
                track.commentsList.add(comments);

                GpsPoints = (GpsDots[0]).split(";");
                GpsStart = GpsPoints[0];
                GpsEnd = GpsPoints[GpsPoints.length-1];

                Sectors sectors = new Sectors();
                sectors.name = name;
                sectors.firstPoint = (GpsStart);
                sectors.endPoint = (GpsEnd);
                sectors.routeList.add(track);

                //JOptionPane.showMessageDialog(null,GpsDots[0]);
                for (int i=0;i<GpsPoints.length;i++){
                    tLatLng gps = new tLatLng();
                    splits = GpsPoints[i].split(",");
                    gps.latitude =new BigDecimal(new BigDecimal(removeCharAt(splits[0],0).trim()).setScale(5, RoundingMode.HALF_UP).floatValue()).setScale(4, RoundingMode.HALF_UP).floatValue();
                    gps.longitude =new BigDecimal(new BigDecimal(removeCharAt(splits[1]).trim()).setScale(5, RoundingMode.HALF_UP).floatValue()).setScale(4, RoundingMode.HALF_UP).floatValue();
                    sectors.trackList.add(sectors.trackList.size(),gps);
                }
                sectors.save();



            }
// записываем треки в базу



            //JOptionPane.showMessageDialog(null,Sectors.find.byId(id).routeList.size());
//разбиваем трек в базе на новые части

            return ok(views.html.index.render(new String[] {null}, gpsForm,"Трек успешно сохранен", allPoints()));

        } else {
            flash("error", "Missing file");
            return badRequest();
        }

    }

    public static Result all() {
        List<Sectors> allSectors = Sectors.find.all();
        //allTracks.get(0).trackList.get(0);
        //JOptionPane.showMessageDialog(null,Tracks.find.all().size())
        //JOptionPane.showMessageDialog(null,Tracks.find.all().get(0).commentsList.get(0).comment);
        return ok(views.html.all.render(allSectors, Tracks.find.all(),Comments.find.all()));
    }


    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);  }
    public static String removeCharAt(String s) {
        return s.substring(0, s.length()-1);  }

    public static PointsList allPoints(){
            List<Sectors> allTracks = Sectors.find.all();
            PointsList allPoints = new PointsList();

            for (int i=0;i<allTracks.size();i++){
                String[] points = new String[allTracks.get(i).trackList.size()];
                for(int j=0;j<allTracks.get(i).trackList.size();j++){
                    points[j]=allTracks.get(i).trackList.get(j).latitude + ","
                            + allTracks.get(i).trackList.get(j).longitude + ","+ "\n";
                }
                allPoints.pointsList.add(points);
            }

        return allPoints;
        }

}
