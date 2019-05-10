package org.mcnotify.areas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.protection.Protection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AreaManager {

    HashMap<UUID, ArrayList<Area>> areaList = new HashMap<>();

    public AreaManager(){

    }

    public void loadDatabase() throws SQLException {
        System.out.println("[MCNotify] Loading areas.");

        ResultSet results = MCNotify.database.areaTable().selectAll();

        if(results != null){
            while(results.next()){
                try {
                    int areaId = results.getInt("id");
                    String ownerUuid = results.getString("uuid");
                    String areaName = results.getString("area_name");
                    String jsonPoly = results.getString("polygon");
                    String world = results.getString("world");
                    String protectionString = results.getString("protections");
                    String whitelist = results.getString("whitelist");

                    String[] playerStrings = whitelist.split(",");
                    ArrayList<OfflinePlayer> playerList = new ArrayList<>();
                    for(String s : playerStrings){
                        if(s != null && s != "") {
                            UUID uuid = UUID.fromString(s);
                            if(uuid != null) {
                                OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(s));
                                playerList.add(op);
                            }
                        }
                    }

                    OfflinePlayer owner = null;

                    if(ownerUuid != null && ownerUuid != "") {
                        UUID uuid = UUID.fromString(ownerUuid);
                        if(uuid != null) {
                            owner = Bukkit.getOfflinePlayer(UUID.fromString(ownerUuid));
                        }
                    }

                    this.addOldArea(new Area(areaId, owner, new Polygon(jsonPoly), areaName, world, Protection.fromString(protectionString), playerList));

                } catch (SQLException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }

        System.out.println("[MCNotify] Areas loaded.");
    }

    public boolean addOldArea(Area area){

        if(this.areaList.get(area.getOwner().getUniqueId()) == null){
            this.areaList.put(area.getOwner().getUniqueId(), new ArrayList<Area>());
        }

        ArrayList<Area> playerAreas = this.areaList.get(area.getOwner().getUniqueId());
        for(Area a : playerAreas){
            if(a.getAreaName().equals(area.getAreaName())){
                return false;
            }
        }

        playerAreas.add(area);
        return true;
    }

    public boolean addNewArea(Area area){
        ArrayList<Area> playerAreas;
        if(this.areaList.get(area.getOwner().getUniqueId()) == null){
            playerAreas = new ArrayList<Area>();
            this.areaList.put(area.getOwner().getUniqueId(), playerAreas);
        } else {
            playerAreas = this.areaList.get(area.getOwner().getUniqueId());
        }


        for(Area a : playerAreas){
            if(a.getOwner() == area.getOwner() && a.getAreaName().toLowerCase().equals(area.getAreaName().toLowerCase())){
                return false;
            }
        }

        if(MCNotify.database.areaTable().insert(area)){
            playerAreas.add(area);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeArea(UUID uuid, String areaName){

        ArrayList<Area> playerAreas = this.areaList.get(uuid);

        for(Area area : playerAreas){
            if(area.getAreaName().toLowerCase().equals(areaName.toLowerCase())){
                if(MCNotify.database.areaTable().delete(area)){
                    playerAreas.remove(area);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public ArrayList<Area> getAreas(UUID uuid){
        return this.areaList.get(uuid);
    }

    public Area getArea(UUID uuid, String areaName){

        ArrayList<Area> playerAreas = this.areaList.get(uuid);

        for(Area area : playerAreas){
            if(area.getAreaName().equals(areaName)){
                return area;
            }
        }
        return null;
    }

    public Area getAreaAtLocation(Location location){
        //TODO: Access cache to check areas.
        for(Map.Entry<UUID, ArrayList<Area>> mapEntry : this.areaList.entrySet()){
            for(Area a : mapEntry.getValue()){
                if(a.getPolygon().contains(location)){
                    return a;
                }
            }
        }
        return null;
    }

}
