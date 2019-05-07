package org.mcnotify.areas;

import net.minecraft.server.v1_14_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mcnotify.MCNotify;

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

    public boolean addArea(Area area){
        // Check if an area with that name already exists.
        for(Area a : this.areaList){
            if(a.getOwner() == area.getOwner() && a.getAreaName().toLowerCase().equals(area.getAreaName().toLowerCase())){
                return false;
            }
        }

        if(MCNotify.database.areaTable().insert(area)){
            areaList.add(area);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeArea(Area area){
        if(MCNotify.database.areaTable().delete(area)){
            areaList.remove(area);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAreaId(int id){
        for(Area area : this.areaList){
            if(area.getAreaId() == id){
                return this.removeArea(area);
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

    public Area getPlayerNamedArea(Player player, String areaName){
        for(Area area : this.areaList){
            if(area.getOwner() == player && area.getAreaName().toLowerCase().equals(areaName.toLowerCase())){
                return area;
            }
        }
        return null;
    }

}
