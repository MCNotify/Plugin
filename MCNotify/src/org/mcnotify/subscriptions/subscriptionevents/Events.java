package org.mcnotify.subscriptions.subscriptionevents;

public enum Events {
    ON_PLAYER_MOVE("ON_PLAYER_MOVE", "playerenters", new String[] {"areaId"}),
    ON_PLAYER_JOIN("ON_PLAYER_JOIN", "login", new String[] {"watchedPlayer"}),
    ON_BLOCK_EXPLODE("ON_BLOCK_EXPLODE", "explosionin", new String[] {"areaId"}),
    ON_REDSTONE_ACTIVE("ON_REDSTONE_ACTIVE", "redstoneactivein", new String[] {"areaId"}),
    ON_BLOCK_BREAK("ON_BLOCK_BREAK", "blockbreakin", new String[] {"areaId"}),
    ON_CROP_GROWN("ON_CROP_GROWN", "cropgrownin", new String[] {"areaId"});

    private String eventName;
    private String commandName;
    private String[] JsonKeys;

    Events(String eventName, String commandName, String[] jsonKeys){
        this.eventName = eventName;
        this.commandName = commandName;
        this.JsonKeys = jsonKeys;
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

    public static Events fromCommand(String commandName){
        for(Events e : Events.values()){
            if(e.getCommandName().equals(commandName)){
                return e;
            }
        }
        return null;
    }
}