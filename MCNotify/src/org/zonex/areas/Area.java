package org.zonex.areas;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zonex.ZoneX;
import org.zonex.areas.protection.Protection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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

        // Determine the id of the subscription
        ArrayList<Area> playerAreas = ZoneX.areaManager.getAreas(owner.getUniqueId());
        if(playerAreas.size() > 0) {
            int lastAreaId = playerAreas.get(playerAreas.size() - 1).getAreaId();
            this.areaId = lastAreaId + 1;
        } else {
            this.areaId = 1;
        }
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
                return this.updateDatabase();
            }
        }
        this.protections.add(protection);
        return this.updateDatabase();
    }

    public boolean addWhitelist(OfflinePlayer newPlayer){
        for(OfflinePlayer p : whitelist){
            if(p.getUniqueId().equals(newPlayer.getUniqueId())){
                return true;
            }
        }
        whitelist.add(newPlayer);
        return this.updateDatabase();
    }

    public boolean removeWhitelist(OfflinePlayer removePlayer){
        for(OfflinePlayer p : whitelist){
            if(p.getUniqueId().equals(removePlayer.getUniqueId())){
                whitelist.remove(removePlayer);
                return this.updateDatabase();
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

    public String getPlayerFriendlyProtectionString(){
        String protectionString = "";
        for(Protection p : this.protections){
            protectionString += p.getCommand() + ", ";
        }
        if(protectionString.length() != 0) {
            protectionString = protectionString.substring(0, protectionString.length() - 2);
        } else {
            protectionString = "Not protected.";
        }
        return protectionString;

    }

    public String getPlayerFriendlyWhitelistString(){
        String whitelist = "";
        for(OfflinePlayer p : this.whitelist){
            whitelist += p.getName() + ", ";
        }
        if(whitelist.length() != 0) {
            whitelist = whitelist.substring(0, whitelist.length() - 2);
        } else {
            whitelist = "No allowed players.";
        }
        return whitelist;
    }

    public String getWhitelistAsString(){
        String whitelist = "";
        for(OfflinePlayer p : this.whitelist){
            whitelist += p.getUniqueId().toString() + ",";
        }
        return whitelist;
    }

    public boolean isAllowed(OfflinePlayer player){
        if(player.getUniqueId().equals(this.owner.getUniqueId())){
            return true;
        }

        for(OfflinePlayer p : this.whitelist){
            if(p.getUniqueId().equals(player.getUniqueId())){
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

    private boolean updateDatabase(){
        return ZoneX.datastore.areaTable().update(this);
    }



    public String serialize() {

        JSONObject json = new JSONObject();
        json.put("name", areaName);
        json.put("protections", this.getProtectionsAsString());
        json.put("whitelist", this.getWhitelistAsString());
        json.put("polygon", polygon.getJson().toJSONString());
        json.put("areaId", String.valueOf(areaId));
        json.put("world", world);
        json.put("owner", owner.getUniqueId().toString());
        return json.toJSONString();
    }

    public static Area deserialize(String serializedString) throws ParseException {

        Object deserialized = new JSONParser().parse(serializedString);
        JSONObject deserializedJson = (JSONObject) deserialized;

        OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(((String)deserializedJson.get("owner"))));
        Polygon poly = new Polygon((String)deserializedJson.get("polygon"));
        int areaId = Integer.valueOf((String)deserializedJson.get("areaId"));
        String areaName = (String)deserializedJson.get("name");
        String world = (String)deserializedJson.get("world");
        ArrayList<Protection> protections = Protection.fromString((String)deserializedJson.get("protections"));

        String whitelistString = (String)deserializedJson.get("whitelist");
        ArrayList<OfflinePlayer> playerList = new ArrayList<>();
        if(whitelistString.length() > 0) {
            for (String s : whitelistString.split(",")) {
                if (s != null && !s.equals("")) {
                    UUID uuid = UUID.fromString(s);
                    if (uuid != null) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(s));
                        playerList.add(op);
                    }
                }
            }
        }

        return new Area(areaId, owner, poly, areaName, world, protections, playerList);
    }

}
