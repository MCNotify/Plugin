package org.zonex.areas;

import org.bukkit.Location;
import org.zonex.ZoneX;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages adding, removing and updating areas.
 */
public class AreaManager {

    /**
     * A map of a player to their areas.
     */
    HashMap<UUID, ArrayList<Area>> areaList = new HashMap<>();

    /**
     * The number of created areas for BStats metrics
     */
    private int areaCount = 0;

    public AreaManager(){

    }

    /**
     * Loads all player areas from the database
     */
    public void loadDatabase() {

        System.out.println("[ZoneX] Loading areas...");


        ArrayList<Area> areaList = ZoneX.datastore.areaTable().selectAll();

        for(Area area : areaList){
            areaCount++;
            this.addOldArea(area);
        }

        System.out.println("[ZoneX] " + areaList.size() + " Areas loaded.");
    }

    /**
     * Adds an already existing area to the manager. Prevents doing database inserts when loading from the database.
     * @param area the area that already existed.
     * @return if the area was added
     */
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

    /**
     * Adds a new area. Stores it into the data store.
     * @param area the area to add to the data store.
     * @return if the area was successfully added
     */
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

    /**
     * Removes the selected area from a player's area list
     * @param uuid the uuid of the player to remove the area from
     * @param areaName the name of the area to remove
     * @return if the area was successfully removed
     */
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

    /**
     * Gets a list of the player's areas
     * @param uuid the uuid of the player
     * @return a list of the player's areas
     */
    public ArrayList<Area> getAreas(UUID uuid){
        ArrayList<Area> areas = this.areaList.get(uuid);
        if(areas == null){
            areas = new ArrayList<>();
        }
        return areas;
    }

    /**
     * Gets the player's area with the specified name
     * @param uuid the uuid of the player
     * @param areaName the name of the area to retrieve
     * @return the player's area. Null if none
     */
    public Area getArea(UUID uuid, String areaName){

        ArrayList<Area> playerAreas = this.areaList.get(uuid);

        for(Area area : playerAreas){
            if(area.getAreaName().equalsIgnoreCase(areaName)){
                return area;
            }
        }
        return null;
    }

    /**
     * Gets an area at the specified location.
     * @param location the location to check if an area exists in.
     * @return The area at the location. Null if none.
     */
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
