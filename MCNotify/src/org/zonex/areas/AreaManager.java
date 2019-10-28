package org.zonex.areas;

import org.bukkit.Location;
import org.zonex.ZoneX;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AreaManager {

    HashMap<UUID, ArrayList<Area>> areaList = new HashMap<>();
    private int areaCount = 0;

    public AreaManager(){

    }

    public void loadDatabase() throws SQLException {

        System.out.println("[ZoneX] Loading areas...");


        ArrayList<Area> areaList = ZoneX.datastore.areaTable().selectAll();

        for(Area area : areaList){
            areaCount++;
            this.addOldArea(area);
        }

        System.out.println("[ZoneX] " + areaList.size() + " Areas loaded.");
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
            areaCount++;
        } else {
            playerAreas = this.areaList.get(area.getOwner().getUniqueId());
        }

        for(Area a : playerAreas){
            if(a.getOwner() == area.getOwner() && a.getAreaName().toLowerCase().equals(area.getAreaName().toLowerCase())){
                return false;
            }
        }

        if(ZoneX.datastore.areaTable().insert(area)) {
            playerAreas.add(area);
            return true;
        }

        return false;
    }

    public boolean removeArea(UUID uuid, String areaName){

        ArrayList<Area> playerAreas = this.areaList.get(uuid);

        for(Area area : playerAreas){
            if(area.getAreaName().toLowerCase().equals(areaName.toLowerCase())){

                if(ZoneX.datastore.areaTable().delete(area)){
                    playerAreas.remove(area);
                    areaCount--;
                    return true;
                }

                return false;
            }
        }
        return false;
    }

    public ArrayList<Area> getAreas(UUID uuid){
        ArrayList<Area> areas = this.areaList.get(uuid);
        if(areas == null){
            areas = new ArrayList<>();
        }
        return areas;
    }

    public Area getArea(UUID uuid, String areaName){

        ArrayList<Area> playerAreas = this.areaList.get(uuid);

        for(Area area : playerAreas){
            if(area.getAreaName().equalsIgnoreCase(areaName)){
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

    public int countZones(){
        return areaCount;
    }

}
