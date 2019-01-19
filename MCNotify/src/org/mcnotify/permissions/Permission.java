package org.mcnotify.permissions;

public enum Permission {
    MCNOTIFY_AREAS_ADD("mcnofity.areas.add"),
    MCNOTIFY_EVENTS_PLAYERJOIN("mcnofity.events.playerjoin"),
    MCNOTIFY_EVENTS_PLAYERMOVE("mcnofity.events.playermove");

    private String permissionNode;

    Permission(String perm){
        this.permissionNode = perm;
    }

    public String toString(){
        return this.permissionNode;
    }

}
