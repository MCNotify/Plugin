package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.zonex.ZoneX;
import org.zonex.subscriptions.Subscription;

public class onPlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent deathEvent){
        for(Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_PLAYER_DEATH)){
            if(deathEvent.getEntity().getUniqueId().equals(subscription.getSubscriptionJson().get("watchedPlayer"))){
                subscription.onEvent(deathEvent.getEntity().getName() + " died");
            }
        }
    }

}
