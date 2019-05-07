package org.mcnotify.subscriptions.subscriptionevents;

public enum Events {
    ON_PLAYER_MOVE("ON_PLAYER_MOVE", "playerenters", new String[] {"areaId"}, new String[] {"areaName"}),
    ON_PLAYER_JOIN("ON_PLAYER_JOIN", "login", new String[] {"watchedPlayer"}, new String[] {"watchedPlayerName"}),
    ON_BLOCK_EXPLODE("ON_BLOCK_EXPLODE", "explosionin", new String[] {"areaId"}, new String[] {"areaName"}),
    ON_REDSTONE_ACTIVE("ON_REDSTONE_ACTIVE", "redstoneactivein", new String[] {"areaId"}, new String[] {"areaName"}),
    ON_BLOCK_BREAK("ON_BLOCK_BREAK", "blockbreakin", new String[] {"areaId"}, new String[] {"areaName"}),
    ON_CROP_GROWN("ON_CROP_GROWN", "cropgrownin", new String[] {"areaId"}, new String[] {"areaName"});

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