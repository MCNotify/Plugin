package org.zonex.areas;

import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.util.ArrayList;

/**
 * A polygon.
 */
public class Polygon {

    /**
     * A list of points
     */
    private ArrayList<Point> points = new ArrayList<>();

    /**
     * Polygon constructor
     */
    public Polygon(){
    }

    /**
     * Creates a polygon from a JSON string.
     * @param polygonJson A JSON string representing a polygon
     */
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

    /**
     * Gets the number of points in the polygon
     * @return how many points are in the polygon
     */
    public int getLength(){
        return this.points.size();
    }

    /**
     * Checks if a point is contained within the polygon
     * https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
     * @param test The point to test inside the polygon
     * @return if the point is within the polygon.
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

    /**
     * Checks if a location is in the polygon
     * @param location the location to check
     * @return if the location is inside the polygon
     */
    public boolean contains(Location location){
        Point testPoint = new Point(location.getBlockX(), location.getBlockZ());
        return this.contains(testPoint);
    }

    /**
     * Adds a point to the polygon
     * @param point the point to add to the polygon
     */
    public void addPoint(Point point){
        this.points.add(point);
    }

    /**
     * Gets a list of points within the polygon
     * @return a list of points in the polygon
     */
    public ArrayList<Point> getPoints(){
        return this.points;
    }

    /**
     * Converst the polygon to a json object.
     * @return a json object representing the polygon object
     */
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

    /**
     * Gets a player friendly representation of the polygon's points.
     * @return a player friendly string of the polygon
     */
    public String getPlayerFriendlyString(){
        String points = "";
        for(Point p : this.points){
            points += "(" + p.x + ", " + p.y + "), ";
        }
        points = points.substring(0, points.length() - 2);
        return points;
    }

    /**
     * Gets how much area the polygon covers
     * @return the area taken up by the polygon
     */
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
