package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.entity.Player;
import org.zonex.config.Permission;

public enum Events {
    ON_PLAYER_MOVE("ON_PLAYER_MOVE", "playerEnter", Permission.WATCH_ZONE_ENTER, new String[] {"areaName"}, new String[] {"areaName"}, "Sends notifications when an unallowed player enters the area.", "Player enters {0}"),
    ON_PLAYER_JOIN("ON_PLAYER_JOIN", "login", Permission.WATCH_PLAYER_LOGIN, new String[] {"watchedPlayer"}, new String[] {"watchedPlayerName"}, "Sends a notification when the specified player joins the game.", "{0} joins server"),
    ON_BLOCK_EXPLODE("ON_BLOCK_EXPLODE", "explosion", Permission.WATCH_ZONE_EXPLOSION, new String[] {"areaName"}, new String[] {"areaName"}, "Sends a notification when blocks explode in the area.", "Explosion in {0}"),
    ON_REDSTONE_ACTIVE("ON_REDSTONE_ACTIVE", "redstone", Permission.WATCH_ZONE_REDSTONE, new String[] {"areaName"}, new String[] {"areaName"}, "Sends a notification when redstone is activated in the area.", "Redstone powered in {0}"),
    ON_BLOCK_BREAK("ON_BLOCK_BREAK", "blockbreak", Permission.WATCH_ZONE_BLOCK_BREAK, new String[] {"areaName"}, new String[] {"areaName"}, "Sends a notification when a player breaks a block in the area.", "Block breaks in {0}"),
    ON_CROP_GROWN("ON_CROP_GROWN", "crop", Permission.WATCH_ZONE_CROP_GROW, new String[] {"areaName"}, new String[] {"areaName"}, "Sends a notification when crops have grown to their final stage in the area.", "Crop grown in {0}"),
    // ON_HOPPER_FULL("ON_HOPPER_FULL", "hopper", Permission.WATCH_ZONE_HOPPER_FULL, new String[] {"areaName"}, new String[]{"areaName"}, "Sends a notification when hoppers are full in the area.", "Hopper full in {0}"),
    // ON_MOB_LIMIT("ON_MOB_LIMIT", "mobCap", Permission.WATCH_ZONE_MOB_LIMIT, new String[] {"limit", "areaName"}, new String[]{"mobCap", "areaName"}, "Sends a notification if the mob limit is reached in an area.", "Mob cap in {0}"),
    ON_PLAYER_ENTER_NETHER("ON_PLAYER_ENTER_NETHER", "enterNether", Permission.WATCH_PLAYER_ENTER_NETHER, new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"}, "Sends a notification if the specified player enters the nether.", "{0} enters Nether"),
    ON_PLAYER_ENTER_END("ON_PLAYER_ENTER_END", "enterEnd", Permission.WATCH_PLAYER_ENTER_END, new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"}, "Sends a notification if the specified player enters the end.", "{0} enters End"),
    ON_PLAYER_ENTER_WORLD("ON_PLAYER_ENTER_WORLD", "enterWorld", Permission.WATCH_PLAYER_ENTER_OVERWORLD, new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"}, "Sends a notification if the specified player enters the overworld", "{0} enters Overworld"),
    ON_PLAYER_DEATH("ON_PLAYER_DEATH", "playerDies", Permission.WATCH_PLAYER_DIE, new String[]{"watchedPlayer"}, new String[]{"watchedPlayerName"}, "Sends a notification if the specified player dies.", "{0} dies")
    ;

    private String eventName;
    private String commandName;
    private Permission permissionNode;
    private String[] JsonKeys;
    private String[] playerFriendlyKeyNames;
    private String description;
    private String playerFieldlyDescription;

    Events(String eventName, String commandName, Permission permissionNode, String[] jsonKeys, String[] playerFriendlyKeyNames, String description, String playerFriedlyDescription){
        this.eventName = eventName;
        this.commandName = commandName;
        this.permissionNode = permissionNode;
        this.JsonKeys = jsonKeys;
        this.playerFriendlyKeyNames = playerFriendlyKeyNames;
        this.description = description;
        this.playerFieldlyDescription = playerFriedlyDescription;
    }

    public String toString(){
        return this.eventName;
    }

    public String getCommandName(){
        return this.commandName;
    }

    public String[] getJsonKeys(){
        return this.JsonKeys;
    }

    public String[] getPlayerFriendlyKeyNames(){
        return this.playerFriendlyKeyNames;
    }

    public Permission getPermissionNode(){
        return this.permissionNode;
    }

    public boolean hasPermission(Player p){
        return this.hasPermission(p);
    }

    public static Events fromCommand(String commandName){
        for(Events e : Events.values()){
            if(e.getCommandName().toLowerCase().equals(commandName.toLowerCase())){
                return e;
            }
        }
        return null;
    }

    public static Events fromString(String eventName){
        for(Events e : Events.values()){
            if(e.getCommandName().toLowerCase().equals(eventName.toLowerCase())){
                return e;
            }
        }
        return null;
    }

    public String getDescription(){
        return this.description;
    }

    public String getPlayerFieldlyDescription(String[] args){
        int counter = 0;
        String replacement = this.playerFieldlyDescription;
        for(String s : args){
            replacement = replacement.replace("{" + counter + "}", s);
            counter++;
        }
        return replacement;
    }
}