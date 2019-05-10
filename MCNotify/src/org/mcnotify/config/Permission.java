package org.mcnotify.config;

public enum Permission {
    MCNOTIFY_AREAS_ADD("mcnofity.areas.add"),
    MCNOTIFY_EVENTS_PLAYER_LOGIN("mcnofity.events.player.login"),
    MCNOTIFY_EVENTS_PLAYER_ENTER("mcnofity.events.player.enter");

    private String permissionNode;

    Permission(String perm){
        this.permissionNode = perm;
    }

    public String getPermissionNode(){
        return this.permissionNode;
    }

}
