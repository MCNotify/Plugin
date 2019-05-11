package org.mcnotify.areas;

import org.bukkit.OfflinePlayer;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.protection.Protection;

import java.util.ArrayList;

public class Area {

    private OfflinePlayer owner;
    private Polygon polygon;
    private String areaName;
    private int areaId;
    private String world;
    private ArrayList<Protection> protections;
    private ArrayList<OfflinePlayer> whitelist;

    public Area(int areaId, OfflinePlayer owner, Polygon area, String areaName, String world, ArrayList<Protection> protections, ArrayList<OfflinePlayer> whitelist){
        this.owner = owner;
        this.polygon = area;
        this.areaId = areaId;
        this.areaName = areaName;
        this.world = world;
        this.protections = protections;
        this.whitelist = whitelist;
    }

    public Area(OfflinePlayer owner, Polygon area, String areaName, String world){
        this.owner = owner;
        this.polygon = area;
        this.areaName = areaName;
        this.world = world;
        this.protections = Protection.getDefaultProtections();
        this.whitelist = new ArrayList<>();
    }

    public OfflinePlayer getOwner(){
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

    public void setAreaId(int areaId){
        this.areaId = areaId;
    }

    public boolean toggleProtection(Protection protection){
        for(Protection p : this.protections){
            if(p == protection){
                this.protections.remove(p);
                if(MCNotify.database.areaTable().update(this)){
                    return true;
                } else {
                    return false;
                }
            }
        }
        this.protections.add(protection);
        if(MCNotify.database.areaTable().update(this)){
            return true;
        } else {
            return false;
        }
    }

    public boolean addWhitelist(OfflinePlayer newPlayer){
        for(OfflinePlayer p : whitelist){
            if(p == newPlayer){
                return true;
            }
        }
        whitelist.add(newPlayer);
        if(MCNotify.database.areaTable().update(this)){
            return true;
        } else {
            return false;
        }
    }

    public boolean removeWhitelist(OfflinePlayer removePlayer){
        for(OfflinePlayer p : whitelist){
            if(p == removePlayer){
                whitelist.remove(removePlayer);
                if(MCNotify.database.areaTable().update(this)){
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public String getProtectionsAsString(){
        String protectionString = "";
        for(Protection p : this.protections){
            protectionString += p.getCommand() + ",";
        }
        return protectionString;
    }

    public String getWhitelistAsString(){
        String whitelist = "";
        for(OfflinePlayer p : this.whitelist){
            whitelist += p.getUniqueId().toString() + ",";
        }
        return whitelist;
    }

    public boolean isAllowed(OfflinePlayer player){
        if(player == this.owner){
            return true;
        }

        for(OfflinePlayer p : this.whitelist){
            if(p == player){
                return true;
            }
        }
        return false;
    }

    public boolean hasProtection(Protection protection){
        for(Protection p : this.protections){
            if(p == protection){
                return true;
            }
        }
        return false;
    }


}
