package org.zonex.config;

import org.bukkit.entity.Player;

/**
 * An enumeration of permission nodes for the plugin
 */
public enum Permission {
    // General
    ADMIN("zx.admin.*"),
    MEMBER("zx.member.*"),

    // ZONES
    ZONE_ADD("zx.member.zone.add"),
    ZONE_LIST("zx.member.zone.list"),
    ZONE_REMOVE("zx.member.zone.remove"),
    ZONE_DENY_PLAYERS("zx.member.zone.deny"),
    ZONE_ALLOW_PLAYERS("zx.member.zone.allow"),
    ZONE_VIEW("zx.member.zone.view"),
    ZONE_INFO("zx.member.zone.info"),

    // Admin Zone Commands
    ZONE_ADMIN_LIST_ANY("zx.admin.zone.listAny"),
    ZONE_ADMIN_ADD_ANY("zx.admin.zone.addAny"),
    ZONE_ADMIN_REMOVE_ANY("zx.admin.zone.removeAny"),
    ZONE_ADMIN_DENY_ANY("zx.admin.zone.denyAny"),
    ZONE_ADMIN_ALLOW_ANY("zx.admin.zone.allowAny"),
    ZONE_ADMIN_VIEW_ANY("zx.admin.zone.viewAny"),
    ZONE_ADMIN_INFO_ANY("zx.admin.zone.infoAny"),
    ZONE_ADMIN_PROTECT_ANY("zx.admin.zone.protectAny"),

    // Protections
    PROTECTION_PROTECT("zx.member.protect.protect"),
    PROTECTION_MOB_PROTECT("zx.member.protect.mobProtect"),
    PROTECTION_STOP_LIQUID("zx.member.protect.stopLiquid"),
    PROTECTION_CHEST_LOCK("zx.member.protect.chestLock"),
    PROTECTION_NO_REDSTONE("zx.member.protect.noRedstone"),
    PROTECTION_NO_FIRE("zx.member.protect.noFire"),
    PROTECTION_DOOR_LOCK("zx.member.protect.doorLock"),
    PROTECTION_NO_ENTER("zx.member.protect.noEnter"),
    PROTECTION_NO_INTERACT("zx.member.protect.noInteract"),
    PROTECTION_NO_PVP("zx.member.protect.noPvp"),


    // EVENTS
    WATCH_PLAYER_LOGIN("zx.member.notify.player.login"),
    WATCH_PLAYER_ENTER_NETHER("zx.member.notify.player.enterNether"),
    WATCH_PLAYER_ENTER_END("zx.member.notify.player.enterEnd"),
    WATCH_PLAYER_ENTER_OVERWORLD("zx.member.notify.player.enterWorld"),
    WATCH_PLAYER_DIE("zx.member.notify.player.playerDies"),
    WATCH_ZONE_ENTER("zx.member.notify.zone.enter"),
    WATCH_ZONE_EXPLOSION("zx.member.notify.zone.explosion"),
    WATCH_ZONE_REDSTONE("zx.member.notify.zone.redstone"),
    WATCH_ZONE_BLOCK_BREAK("zx.member.notify.zone.blockbreak"),
    WATCH_ZONE_CROP_GROW("zx.member.notify.zone.crop"),
    WATCH_ZONE_HOPPER_FULL("zx.member.notify.zone.hopper"),
    WATCH_ZONE_MOB_LIMIT("zx.member.notify.zone.mobLimit");


    private String permissionNode;

    Permission(String perm){
        this.permissionNode = perm;
    }

    /**
     * Returns the string representation of the permission node
     * @return String representation of the permission node
     */
    public String getPermissionNode(){
        return this.permissionNode;
    }

    /**
     * Determines if a player has permissions to access the node.
     * @param p the player to check access for
     * @return if the player has access to the permission node
     */
    public boolean hasPermission(Player p){
        return p.hasPermission(this.permissionNode);
    }

}
