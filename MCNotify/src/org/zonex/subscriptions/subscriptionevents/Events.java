package org.zonex.subscriptions.subscriptionevents;

public enum Events {
    ON_PLAYER_MOVE("ON_PLAYER_MOVE", "playerenter", new String[] {"areaName"}, new String[] {"areaName"}, "Sends notifications when an unallowed player enters the area."),
    ON_PLAYER_JOIN("ON_PLAYER_JOIN", "login", new String[] {"watchedPlayer"}, new String[] {"watchedPlayerName"}, "Sends a notification when the specified player joins the game."),
    ON_BLOCK_EXPLODE("ON_BLOCK_EXPLODE", "explosion", new String[] {"areaName"}, new String[] {"areaName"}, "Sends a notification when blocks explode in the area."),
    ON_REDSTONE_ACTIVE("ON_REDSTONE_ACTIVE", "redstone", new String[] {"areaName"}, new String[] {"areaName"}, "Sends a notification when redstone is activated in the area."),
    ON_BLOCK_BREAK("ON_BLOCK_BREAK", "blockbreak", new String[] {"areaName"}, new String[] {"areaName"}, "Sends a notification when a player breaks a block in the area."),
    ON_CROP_GROWN("ON_CROP_GROWN", "crop", new String[] {"areaName"}, new String[] {"areaName"}, "Sends a notification when crops have grown to their final stage in the area."),
    ON_HOPPER_FULL("ON_HOPPER_FULL", "hopper", new String[] {"areaName"}, new String[]{"areaName"}, "Sends a notification when hoppers are full in the area."),
    ON_MOB_LIMIT("ON_MOB_LIMIT", "mobCap", new String[] {"limit", "areaName"}, new String[]{"mobCap", "areaName"}, "Sends a notification if the mob limit is reached in an area."),
    ON_PLAYER_ENTER_NETHER("ON_PLAYER_ENTER_NETHER", "enterNether", new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"}, "Sends a notification if the specified player enters the nether."),
    ON_PLAYER_ENTER_END("ON_PLAYER_ENTER_END", "enderEnd", new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"}, "Sends a notification if the specified player enters the end."),
    ON_PLAYER_ENTER_WORLD("ON_PLAYER_ENTER_WORLD", "enterWorld", new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"}, "Sends a notification if the specified player enters the overworld"),
    ON_PLAYER_DEATH("ON_PLAYER_DEATH", "playerDies", new String[]{"watchedPlayer"}, new String[]{"watchedPlayerName"}, "Sends a notification if the specified player dies.");

    private String eventName;
    private String commandName;
    private String[] JsonKeys;
    private String[] playerFriendlyKeyNames;
    private String description;

    Events(String eventName, String commandName, String[] jsonKeys, String[] playerFriendlyKeyNames, String description){
        this.eventName = eventName;
        this.commandName = commandName;
        this.JsonKeys = jsonKeys;
        this.playerFriendlyKeyNames = playerFriendlyKeyNames;
        this.description = description;
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

    public static Events fromCommand(String commandName){
        for(Events e : Events.values()){
            if(e.getCommandName().equals(commandName)){
                return e;
            }
        }
        return null;
    }

    public static Events fromString(String eventName){
        for(Events e : Events.values()){
            if(e.getCommandName().equals(eventName)){
                return e;
            }
        }
        return null;
    }

    public String getDescription(){
        return this.description;
    }
}