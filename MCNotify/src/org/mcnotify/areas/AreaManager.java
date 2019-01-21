package org.mcnotify.areas;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.utility.Polygon;
import org.mcnotify.utility.Response;

import java.io.IOException;
import java.util.ArrayList;

public class AreaManager {

    ArrayList<Area> areaList = new ArrayList<>();

    public AreaManager(){

    }

    public void loadDatabase(){
        System.out.println("[MCNotify] Loading areas...");
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            this.loadAreas(p);
        }
        System.out.println("[MCNotify] Areas loaded.");
    }

    public void loadAreas(Player player){

        String endpoint = "areas.php?";
        endpoint += "uuid=" + player.getUniqueId().toString();

        try {
            Response response = MCNotify.requestManager.sendRequest("GET", endpoint, null);

            if(response.getResponseCode() == 200){

                JSONObject jsonResponse = response.getResponseBody();
                JSONArray areaArray = (JSONArray) jsonResponse.get("areas");

                for(Object obj : areaArray){
                    JSONObject areaJson = (JSONObject) obj;

                    // Get the data from the db
                    int areaId = Math.toIntExact((Long) areaJson.get("area_id"));
                    Polygon polygon = new Polygon((String) areaJson.get("polygon"));
                    String areaName = (String) areaJson.get("area_name");

                    this.areaList.add(new Area(areaId, player, polygon, areaName));

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void addArea(Area area){
        this.areaList.add(area);
    }

    public void removeArea(Area area){
        this.areaList.remove(area);
    }

    public boolean removeAreaId(int id){
        for(Area area : this.areaList){
            if(area.getAreaId() == id){

                JSONObject removeJson = new JSONObject();
                removeJson.put("area_id", id);

                Response deleteResponse = null;
                try {
                    deleteResponse = MCNotify.requestManager.sendRequest("DELETE", "areas.php", removeJson.toJSONString());
                    this.areaList.remove(area);
                    return deleteResponse.getResponseCode() == 200;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    public ArrayList<Area> getAreas(Player player){
        ArrayList<Area> playerAreas = new ArrayList<>();
        for(Area area : this.areaList){
            if(area.getOwner() == player){
                playerAreas.add(area);
            }
        }
        return playerAreas;
    }

}
