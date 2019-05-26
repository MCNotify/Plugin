package org.mcnotify.areas;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Polygon {

    private ArrayList<Point> points = new ArrayList<>();

    public Polygon(){
    }

    public Polygon(String polygonJson){
        try {
            Object obj = new JSONParser().parse(polygonJson);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray pointArray = (JSONArray) jsonObject.get("points");
            for(Object object : pointArray){
                JSONObject json = (JSONObject) object;
                int x = Math.toIntExact((Long) json.get("x"));
                int y = Math.toIntExact((Long) json.get("y"));
                this.points.add(new Point(x, y));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getLength(){
        return this.points.size();
    }

    /**
     * https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
     * @param test The point to test inside the polygon
     * @return
     */
    public boolean contains(Point test) {
        int i;
        int j;
        boolean result = false;

        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {

            if ((points.get(i).y > test.y) != (points.get(j).y > test.y) &&
                    (test.x < (points.get(j).x - points.get(i).x) * (test.y - points.get(i).y) / (points.get(j).y-points.get(i).y) + points.get(i).x)) {
                result = !result;
            }
        }
        return result;
    }

    public boolean contains(Location location){
        Point testPoint = new Point(location.getBlockX(), location.getBlockZ());
        return this.contains(testPoint);
    }

    public void addPoint(Point point){
        this.points.add(point);
    }

    public ArrayList<Point> getPoints(){
        return this.points;
    }

    public JSONObject getJson(){
        JSONObject json = new JSONObject();
        JSONArray pointArray = new JSONArray();
        for(Point p : this.points){
            JSONObject pointJson = new JSONObject();
            pointJson.put("x", p.x);
            pointJson.put("y", p.y);
            pointArray.add(pointJson);
        }
        json.put("points", pointArray);
        return json;
    }

    public String getPlayerFriendlyString(){
        String points = "";
        for(Point p : this.points){
            points += "(" + p.x + ", " + p.y + "), ";
        }
        points = points.substring(0, points.length() - 2);
        return points;
    }

    public float getArea(){

        float yArea = 0;
        float xArea = 0;

        for(int i=0; i<this.points.size(); i++){
            Point thisPoint = this.points.get(i);
            Point nextPoint;
            if((i + 1) == this.points.size()){
                nextPoint = this.points.get(0);
            } else {
                nextPoint = this.points.get(i + 1);
            }

            xArea += thisPoint.x * nextPoint.y;
            yArea += thisPoint.y * nextPoint.x;
        }

        return Math.abs((xArea - yArea)/2);
    }
}
