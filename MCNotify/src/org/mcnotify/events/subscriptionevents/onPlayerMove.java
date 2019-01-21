package org.mcnotify.events.subscriptionevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.events.subscriptions.Subscription;

public class onPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent moveEvent){
        for(Subscription subscription : MCNotify.eventSubscriptionManager.getSubscriptions()){
            if(subscription.getEventType() == Events.ON_PLAYER_MOVE){
                subscription.onEvent();
            }
        }
    }


}
