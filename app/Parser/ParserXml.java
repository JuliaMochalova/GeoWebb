package Parser;

/**
 * Created by 123 on 23.02.2018.
 */

import models.Location;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ParserXml{


    public String[] getPoints(File gpxFile){
        String info ="";

        List<Location> locationList = decodeGPX(gpxFile);
        String[] points = new String[locationList.size()];

        for(int i = 0; i < locationList.size(); i++){
            info = (locationList.get(i)).getLatitude() + ","
                    + (locationList.get(i)).getLongitude() +","+(locationList.get(i)).getTime()+ "\n";
            points[i]=info;
        }

        return points;
    }

    private List<Location> decodeGPX(File file){
        List<Location> list = new ArrayList<>();


        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            FileInputStream fileInputStream = new FileInputStream(file);
            Document document = documentBuilder.parse(fileInputStream);
            Element elementRoot = document.getDocumentElement();

            NodeList nodelist_trkpt = elementRoot.getElementsByTagName("trkpt");
            NodeList nodelist_time = elementRoot.getElementsByTagName("time");

            for(int i = 0; i < nodelist_trkpt.getLength(); i++){

                Node node = nodelist_trkpt.item(i);
                NamedNodeMap attributes = node.getAttributes();

                String  time = nodelist_time.item(i+1).getTextContent().split("T")[1].split("Z")[0];
                String[] t =time.split(":");

                String newLatitude = attributes.getNamedItem("lat").getTextContent();
                Double newLatitude_double = Double.parseDouble(newLatitude);

                String newLongitude = attributes.getNamedItem("lon").getTextContent();
                Double newLongitude_double = Double.parseDouble(newLongitude);

                String newLocationName = newLatitude + "," + newLongitude;

                Location newLocation = new Location();
                newLocation.setName(newLocationName);
                newLocation.setLatitude(newLatitude_double);
                newLocation.setLongitude(newLongitude_double);
                newLocation.setTime(new Integer(t[0])*3600+new Integer(t[1])*60+new Integer(t[2])+"");

                list.add(newLocation);

            }

            fileInputStream.close();

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;


    }


}