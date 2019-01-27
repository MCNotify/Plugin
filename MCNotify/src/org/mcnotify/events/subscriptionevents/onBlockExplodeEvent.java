package org.mcnotify.events.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.events.subscriptions.Subscription;
import org.mcnotify.events.subscriptions.subscriptiondata.onBlockExplosionSubscriptionData;
import org.mcnotify.events.subscriptions.subscriptiondata.onPlayerMoveSubscriptionData;
import org.mcnotify.utility.Polygon;

import java.awt.*;

public class onBlockExplodeEvent implements Listener {

    @EventHandler
    public void onBlockExplosionEvent(BlockExplodeEvent explodeEvent){

        for(Subscription subscription : MCNotify.eventSubscriptionManager.getSubscriptions()){
            if(subscription.getEventType() == Events.ON_BLOCK_EXPLODE){
                // Determine if the explosion happened in a boundary
                Location blockLocation = explodeEvent.getBlock().getLocation();
                Point blockPoint = new Point(blockLocation.getBlockX(), blockLocation.getBlockZ());

                Polygon poly =((onBlockExplosionSubscriptionData)subscription.getSubscriptionData()).getArea();

                // Only trigger if the player moves into the boundary.
                if(poly.contains(blockPoint)) {
                    // Player is inside the boundary.
                    subscription.onEvent();
                }
            }
        }
    }
}
