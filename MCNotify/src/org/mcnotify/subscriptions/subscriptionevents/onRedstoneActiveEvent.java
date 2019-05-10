package org.mcnotify.subscriptions.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;
import org.mcnotify.areas.Polygon;

import java.awt.*;

public class onRedstoneActiveEvent implements Listener {

    @EventHandler
    public void onRedstoneActiveEvent(BlockRedstoneEvent redstoneEvent){
        for(Subscription subscription : MCNotify.subscriptionManager.getSubscriptions(Events.ON_REDSTONE_ACTIVE)){
            // Determine if the explosion happened in a boundary
            Location blockLocation = redstoneEvent.getBlock().getLocation();
            Point blockPoint = new Point(blockLocation.getBlockX(), blockLocation.getBlockZ());

            String areaName = (String)subscription.getSubscriptionJson().get("areaName");

            Polygon poly = MCNotify.areaManager.getArea(subscription.getSubscriber().getUniqueId(), areaName).getPolygon();

            // Only trigger if the player moves into the boundary.
            if(poly.contains(blockPoint)) {
                // Player is inside the boundary.
                subscription.onEvent();
            }
        }
    }
}
