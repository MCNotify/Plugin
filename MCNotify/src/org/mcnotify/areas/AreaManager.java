package org.mcnotify.areas;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.authenticator.Response;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AreaManager {

    ArrayList<Area> areaList = new ArrayList<>();

    public AreaManager(){

    }

    public void loadDatabase() throws SQLException {
        System.out.println("[MCNotify] Loading areas for online players...");
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            this.loadAreas(p);
        }
        System.out.println("[MCNotify] Areas loaded.");
    }

    public void loadAreas(Player player) throws SQLException {
        ResultSet results = MCNotify.database.areaTable().selectWhereUuid(player.getUniqueId().toString());
        if(results != null) {
            while (results.next()) {
                try {
                    int areaId = results.getInt("id");
                    String areaName = results.getString("area_name");
                    String jsonPoly = results.getString("polygon");
                    String world = results.getString("world");

                    areaList.add(new Area(areaId, player, new Polygon(jsonPoly), areaName, world));

                } catch (SQLException e) {
                    e.printStackTrace();
                    continue;
                }
            }
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

                // TODO: Generate delete function
                return false;
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

    public Area getArea(int areaId){
        for(Area area : this.areaList){
            if(area.getAreaId() == areaId){
                return area;
            }
        }
        return null;
    }

}
