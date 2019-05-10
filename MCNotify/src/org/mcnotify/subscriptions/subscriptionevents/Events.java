package org.mcnotify.subscriptions.subscriptionevents;

public enum Events {
    ON_PLAYER_MOVE("ON_PLAYER_MOVE", "playerenter", new String[] {"areaName"}, new String[] {"areaName"}),
    ON_PLAYER_JOIN("ON_PLAYER_JOIN", "login", new String[] {"watchedPlayer"}, new String[] {"watchedPlayerName"}),
    ON_BLOCK_EXPLODE("ON_BLOCK_EXPLODE", "explosion", new String[] {"areaName"}, new String[] {"areaName"}),
    ON_REDSTONE_ACTIVE("ON_REDSTONE_ACTIVE", "redstone", new String[] {"areaName"}, new String[] {"areaName"}),
    ON_BLOCK_BREAK("ON_BLOCK_BREAK", "blockbreak", new String[] {"areaName"}, new String[] {"areaName"}),
    ON_CROP_GROWN("ON_CROP_GROWN", "crop", new String[] {"areaName"}, new String[] {"areaName"}),
    ON_HOPPER_FULL("ON_HOPPER_FULL", "hopper", new String[] {"areaName"}, new String[]{"areaName"}),
    ON_MOB_LIMIT("ON_MOB_LIMIT", "mobCap", new String[] {"limit", "areaName"}, new String[]{"mobCap", "areaName"}),
    ON_PLAYER_ENTER_NETHER("ON_PLAYER_ENTER_NETHER", "enterNether", new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"}),
    ON_PLAYER_ENTER_END("ON_PLAYER_ENTER_END", "enderEnd", new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"}),
    ON_PLAYER_ENTER_WORLD("ON_PLAYER_ENTER_WORLD", "enterWorld", new String[] {"watchedPlayer"}, new String[]{"watchedPlayerName"});

    private String eventName;
    private String commandName;
    private String[] JsonKeys;
    private String[] playerFriendlyKeyNames;

    Events(String eventName, String commandName, String[] jsonKeys, String[] playerFriendlyKeyNames){
        this.eventName = eventName;
        this.commandName = commandName;
        this.JsonKeys = jsonKeys;
        this.playerFriendlyKeyNames = playerFriendlyKeyNames;
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
}