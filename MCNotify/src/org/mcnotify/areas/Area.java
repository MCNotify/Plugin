package org.mcnotify.areas;

import org.bukkit.entity.Player;

public class Area {

    private Player owner;
    private Polygon polygon;
    private String areaName;
    private int areaId;
    private String world;

    public Area(int areaId, Player owner, Polygon area, String areaName, String world){
        this.owner = owner;
        this.polygon = area;
        this.areaId = areaId;
        this.areaName = areaName;
        this.world = world;
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

    public String getWorld(){return world;}

}
