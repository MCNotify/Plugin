package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.entity.Player;
import org.zonex.config.Permission;

/**
 * An enumeration of potential event types that the player can subscribe to watch.
 */
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

    /**
     * Gets the name of the event type as a string
     * @return The name of the event
     */
    public String toString(){
        return this.eventName;
    }

    /**
     * Gets the name of the event that is recognized as a command
     * @return The command name for the event type
     */
    public String getCommandName(){
        return this.commandName;
    }

    /**
     * JSON keys that define additional information about the event
     * @return An array of strings listing possible JSON keys for this event type.
     */
    public String[] getJsonKeys(){
        return this.JsonKeys;
    }

    /**
     * Returns a list of player friendly attribute names
     * @return An array of strings that are player friendly
     */
    public String[] getPlayerFriendlyKeyNames(){
        return this.playerFriendlyKeyNames;
    }

    /**
     * The permission node required to watch this event.
     * @return The permission required to watch this event.
     */
    public Permission getPermissionNode(){
        return this.permissionNode;
    }


    /**
     * Parses an event type from a command string.
     * @param commandName the command string to parse the event from
     * @return The event type of the command string. Null if no match.
     */
    public static Events fromCommand(String commandName){
        for(Events e : Events.values()){
            if(e.getCommandName().toLowerCase().equals(commandName.toLowerCase())){
                return e;
            }
        }
        return null;
    }

    /**
     * Creates an event type from a string of the event name
     * @param eventName the name of the event
     * @return The event type of the string. Null if no match.
     */
    public static Events fromString(String eventName){
        for(Events e : Events.values()){
            if(e.getCommandName().toLowerCase().equals(eventName.toLowerCase())){
                return e;
            }
        }
        return null;
    }

    /**
     * Gets a player friendly description of when this event type is triggered.
     * @return A description of when the event is triggered
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Gets a player friendly description of the watched event
     * @param args Arguments to replace in the description
     * @return A player friendly description of the event.
     */
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