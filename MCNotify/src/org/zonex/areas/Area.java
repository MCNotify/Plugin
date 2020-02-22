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

/**
 * A player's area
 */
public class Area {

    /**
     * The owner of the area
     */
    private OfflinePlayer owner;

    /**
     * The area's polygon
     */
    private Polygon polygon;

    /**
     * The name of the area
     */
    private String areaName;

    /**
     * The id of the area
     */
    private int areaId;

    /**
     * What world the area is in
     */
    private String world;

    /**
     * A list of protections applied to the area
     */
    private ArrayList<Protection> protections;

    /**
     * A list of whitelisted players to allow access to
     */
    private ArrayList<OfflinePlayer> whitelist;

    /**
     * Loads an area from the database with an area id
     * @param areaId the area id
     * @param owner the area owner
     * @param area the area's polygon
     * @param areaName the name of the area
     * @param world the world name
     * @param protections the list of protections
     * @param whitelist a list of whitelisted players
     */
    public Area(int areaId, OfflinePlayer owner, Polygon area, String areaName, String world, ArrayList<Protection> protections, ArrayList<OfflinePlayer> whitelist){
        this.owner = owner;
        this.polygon = area;
        this.areaId = areaId;
        this.areaName = areaName;
        this.world = world;
        this.protections = protections;
        this.whitelist = whitelist;
    }

    /**
     * Creates a new area
     * @param owner the area's owner
     * @param area the polygon
     * @param areaName the name of the area
     * @param world the world name
     */
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

    /**
     * Get the area's owner
     * @return the area owner
     */
    public OfflinePlayer getOwner(){
        return this.owner;
    }

    /**
     * Gets the area's polygon
     * @return the area's polygon
     */
    public Polygon getPolygon(){
        return this.polygon;
    }

    /**
     * Gets the name of the area
     * @return the name of the area
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * Get the area id
     * @return the id of the area
     */
    public int getAreaId() {
        return areaId;
    }

    /**
     * Gets the name of the world
     * @return the world name
     */
    public String getWorld(){return world;}

    /**
     * Sets the area id
     * @param areaId the areaid to set
     */
    public void setAreaId(int areaId){
        this.areaId = areaId;
    }

    /**
     * Toggles a protection on the area.
     * @param protection the protection to toggle
     * @return if the protection was successfully saved
     */
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

    /**
     * Adds a player to the area's whitelist
     * @param newPlayer the player to add to the whitelist
     * @return if the whitelisted player was saved in storage
     */
    public boolean addWhitelist(OfflinePlayer newPlayer){
        for(OfflinePlayer p : whitelist){
            if(p.getUniqueId().equals(newPlayer.getUniqueId())){
                return true;
            }
        }
        whitelist.add(newPlayer);
        return this.updateDatabase();
    }

    /**
     * Removes a player from the whitelist
     * @param removePlayer the player to remove from the whitelist
     * @return if the whitelisted player was saved in storage
     */
    public boolean removeWhitelist(OfflinePlayer removePlayer){
        for(OfflinePlayer p : whitelist){
            if(p.getUniqueId().equals(removePlayer.getUniqueId())){
                whitelist.remove(removePlayer);
                return this.updateDatabase();
            }
        }
        return true;
    }

    /**
     * Gets the list of protections as a string
     * @return a string representation of the protections
     */
    public String getProtectionsAsString(){
        String protectionString = "";
        for(Protection p : this.protections){
            protectionString += p.getCommand() + ",";
        }
        return protectionString;
    }

    /**
     * Gets a player friendly version of the protections
     * @return a player friendly list of protections appled to their area
     */
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

    /**
     * Gets a player friendly list of whitelisted users
     * @return a player friendly whitelisted list
     */
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

    /**
     * Gets the whitelisted players as a string
     * @return a string of whitelisted players
     */
    public String getWhitelistAsString(){
        String whitelist = "";
        for(OfflinePlayer p : this.whitelist){
            whitelist += p.getUniqueId().toString() + ",";
        }
        return whitelist;
    }

    /**
     * Checks if a player is allowed in the area's whitelist
     * @param player the player to check permission for
     * @return if the player is allowed in the area
     */
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

    /**
     * Check if an area has the specified protection
     * @param protection the protection to check
     * @return if the area is protected by the specified protection
     */
    public boolean hasProtection(Protection protection){
        for(Protection p : this.protections){
            if(p == protection){
                return true;
            }
        }
        return false;
    }

    /**
     * Updates this area in the database
     * @return if the area was saved in storage
     */
    private boolean updateDatabase(){
        return ZoneX.datastore.areaTable().update(this);
    }


    /**
     * Serializes the area to save in flat file storage
     * @return a JSON string of the area
     */
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

    /**
     * Parses a JSON string of an area into an Area object.
     * @param serializedString the JSON string.
     * @return the Area instance.
     * @throws ParseException if the JSON was poorly formatted a ParseException is thrown.
     */
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
