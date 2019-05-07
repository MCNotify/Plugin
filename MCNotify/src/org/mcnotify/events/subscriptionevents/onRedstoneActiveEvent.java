package org.mcnotify.events.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.events.subscriptions.Subscription;
import org.mcnotify.events.subscriptions.subscriptiondata.onBlockExplosionSubscriptionData;
import org.mcnotify.areas.Polygon;

import java.awt.*;

public class onRedstoneActiveEvent implements Listener {

    @EventHandler
    public void onRedstoneActiveEvent(BlockRedstoneEvent redstoneEvent){
        for(Subscription subscription : MCNotify.eventSubscriptionManager.getSubscriptions()){
            if(subscription.getEventType() == Events.ON_BLOCK_EXPLODE){
                // Determine if the explosion happened in a boundary
                Location blockLocation = redstoneEvent.getBlock().getLocation();
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
