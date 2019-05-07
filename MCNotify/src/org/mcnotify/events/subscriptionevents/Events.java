package org.mcnotify.events.subscriptionevents;

import org.mcnotify.events.subscriptions.subscriptiondata.*;

public enum Events {
    ON_PLAYER_MOVE("ON_PLAYER_MOVE"),
    ON_PLAYER_JOIN("ON_PLAYER_JOIN"),
    ON_BLOCK_EXPLODE("ON_BLOCK_EXPLODE"),
    ON_REDSTONE_ACTIVE("ON_REDSTONE_ACTIVE"),
    ON_BLOCK_BREAK("ON_BLOCK_BREAK"),
    ON_CROP_GROWN("ON_CROP_GROWN");

    private String eventName;

    Events(String eventName){
        this.eventName = eventName;
    }

    public String toString(){
        return this.eventName;
    }

    public SubscriptionData getSubscriptionData(){
        switch(this.eventName){
            case "ON_PLAYER_MOVE":
                return new onPlayerMoveSubscriptionData();
            case "ON_PLAYER_JOIN":
                return new onPlayerJoinSubscriptionData();
            case "ON_BLOCK_EXPLODE":
                return new onBlockExplosionSubscriptionData();
            case "ON_REDSTONE_ACTIVE":
                return new onRedstoneActiveSubscriptionData();
            case "ON_BLOCK_BREAK":
                return new onBlockBreakSubscriptionData();
            case "ON_CROP_GROWN":
                return new onCropGrowSubscriptionData();
            default:
                return null;

        }
    }
}