package org.zonex.areas.protection;

import org.zonex.config.Configuration;
import org.zonex.config.Permission;

import java.util.ArrayList;

public enum Protection {

    PROTECT("Protect", "Players cannot place or destroy blocks", "protect", Configuration.AREA_DEFAULT_PROTECT, "Players cannot build or place blocks unless allowed", "Players can place and destroy blocks", Permission.PROTECTION_PROTECT),
    MOB_PROTECT("Mob Protect", "Players cannot damage peaceful mobs", "mobProtect", Configuration.AREA_DEFAULT_MOBPROTECT, "Players cannot harm peaceful mobs unless allowed", "Players can harm peaceful mobs", Permission.PROTECTION_MOB_PROTECT),
    STOP_LIQUID("Stop Liquid", "Liquid cannot flow into area", "stopLiquid", Configuration.AREA_DEFAULT_STOPLIQUID, "Water and lava cannot flow into the area", "Water and lava can flow into the area", Permission.PROTECTION_STOP_LIQUID),
    CHEST_LOCK("Chest Lock", "Prevents players from opening your chests, shulkers, hoppers, etc.", "chestLock", Configuration.AREA_DEFAULT_CHESTLOCK, "Players cannot access inventories unless allowed", "Players can access inventories", Permission.PROTECTION_CHEST_LOCK),
    NO_REDSTONE("No Redstone", "Prevents players using redstone devices (buttons, levers, pressure plates)", "noRedstone", Configuration.AREA_DEFAULT_NOREDSTONE, "Players cannot use redstone devices unless allowed", "Players can use redstone devices", Permission.PROTECTION_NO_REDSTONE),
    NO_FIRE("No Fire", "Prevents fire from starting", "noFire", Configuration.AREA_DEFAULT_NOFIRE, "Fire cannot be started unless allowed", "Fire can be started", Permission.PROTECTION_NO_FIRE),
    DOOR_LOCK("Door Lock", "Prevents doors and trapdoors from being opened", "doorLock", Configuration.AREA_DEFAULT_DOORLOCK, "Doors cannot be used unless allowed", "Doors can be used", Permission.PROTECTION_DOOR_LOCK),
    NO_ENTER("No Enter", "Prevents players being able to walk into the area", "noEnter", Configuration.AREA_DEFAULT_NOENTER, "Players cannot enter the area unless allowed", "Players can enter the area", Permission.PROTECTION_NO_ENTER),
    NO_INTERACT("No Interact", "Prevents players interacting with furnaces, brewing stands, etc.", "noInteract", Configuration.AREA_DEFAULT_NOINTERACT, "Players cannot interact with furnaces, etc. unless allowed", "Players can interact with furnaces, etc.", Permission.PROTECTION_NO_INTERACT),
    NO_PVP("No PvP", "Prevents other players from being able to attack players.", "noPvP", Configuration.AREA_DEFAULT_NOPVP, "Players cannot attack other players in the area.", "Players can attack players in the area.", Permission.PROTECTION_NO_PVP);


    private String name;
    private String description;
    private String command;
    private Configuration defaultConfig;
    private String enabledMessage;
    private String disabledMessage;
    private Permission permissionNode;

    Protection(String name, String description, String command, Configuration defaultConfig, String enabledMessage, String disabledMessage, Permission permissionString){
        this.name = name;
        this.description = description;
        this.command = command;
        this.defaultConfig = defaultConfig;
        this.enabledMessage = enabledMessage;
        this.disabledMessage = disabledMessage;
        this.permissionNode = permissionString;
    }

    public String toString(){
        return this.name;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getCommand(){
        return this.command;
    }

    public Configuration getDefaultConfig(){
        return this.defaultConfig;
    }

    public Permission getPermissionNode(){
        return this.permissionNode;
    }

    public static ArrayList<Protection> getDefaultProtections(){
        ArrayList<Protection> protections = new ArrayList<>();
        for(Protection p : Protection.values()){
            if(p.defaultConfig.getValue().toLowerCase().equals("true")){
                protections.add(p);
            }
        }
        return protections;
    }

    public static ArrayList<Protection> fromString(String protectionString){
        ArrayList<Protection> protections = new ArrayList<>();
        for(Protection p : Protection.values()){
            if(protectionString.toLowerCase().equals(p.command.toLowerCase())){
                protections.add(p);
            }
        }
        return protections;
    }

    public static Protection fromCommand(String commandName){
        for(Protection p : Protection.values()){
            if(commandName.toLowerCase().equals(p.command.toLowerCase())){
                return p;
            }
        }
        return null;
    }

    public String getEnabledMessage(){
        return this.enabledMessage;
    }

    public String getDisabledMessage(){
        return this.disabledMessage;
    }

}
