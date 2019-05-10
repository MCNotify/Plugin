package org.mcnotify.areas.protection;

import org.mcnotify.config.Configuration;

import java.util.ArrayList;

public enum Protection {

    PROTECT("Protect", "Prevents blocks from being placed and broken", "protect", Configuration.AREA_DEFAULT_PROTECT),
    MOB_LOCK("Mob Lock", "Prevents any mobs from entering the area", "mobLock", Configuration.AREA_DEFAULT_MOBLOCK),
    MOB_PROTECT("Mob Protect", "Prevents damage to peaceful mobs", "mobProtect", Configuration.AREA_DEFAULT_MOBPROTECT),
    STOP_LIQUID("Stop Liquid", "Prevents liquid from flowing into the area", "stopLiquid", Configuration.AREA_DEFAULT_STOPLIQUID),
    CHEST_LOCK("Chest Lock", "Prevents players from opening your chests", "chestLock", Configuration.AREA_DEFAULT_CHESTLOCK),
    NO_REDSTONE("No Redstone", "Prevents redstone from being activated", "noRedstone", Configuration.AREA_DEFAULT_NOREDSTONE),
    NO_FIRE("No Fire", "Prevents fire", "noFire", Configuration.AREA_DEFAULT_NOFIRE),
    DOOR_LOCK("Door Lock", "Prevents doors from being opened", "doorLock", Configuration.AREA_DEFAULT_DOORLOCK),
    NO_ENTER("No Enter", "Prevents players being able to walk in the area.", "noEnter", Configuration.AREA_DEFAULT_NOENTER),
    NO_INTERACT("No Interact", "Prevents players interacting with furnaces, hoppers, brewing stands, etc.", "noInteract", Configuration.AREA_DEFAULT_NOINTERACT);


    private String name;
    private String description;
    private String command;
    private Configuration defaultConfig;

    Protection(String name, String description, String command, Configuration defaultConfig){
        this.name = name;
        this.description = description;
        this.command = command;
        this.defaultConfig = defaultConfig;
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
            if(protectionString.toLowerCase().contains(p.command.toLowerCase())){
                protections.add(p);
            }
        }
        return protections;
    }

}
