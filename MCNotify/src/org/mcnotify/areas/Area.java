package org.mcnotify.areas;

import org.bukkit.entity.Player;
import org.mcnotify.utility.Polygon;

public class Area {

    Player owner;
    Polygon polygon;
    String areaName;
    int areaId;

    public Area(int areaId, Player owner, Polygon area, String areaName){
        this.owner = owner;
        this.polygon = area;
        this.areaId = areaId;
        this.areaName = areaName;
    }

    public Player getOwner(){
        return this.owner;
    }

    public Polygon getPolygon(){
        return this.polygon;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getAreaId() {
        return areaId;
    }

}
