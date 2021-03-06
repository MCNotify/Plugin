package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.zonex.ZoneX;
import org.zonex.subscriptions.Subscription;
import org.zonex.areas.Polygon;

import java.awt.*;

public class onBlockBreakEvent implements Listener {

    /**
     * Checks block break events to see if the event is being watched by a subscription. If it is, it triggers the event.
     * @param breakEvent The block break event
     */
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent breakEvent) {
        for(Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_BLOCK_BREAK)){
            // Determine if the break happened in a boundary
            Location blockLocation = breakEvent.getBlock().getLocation();
            Point blockPoint = new Point(blockLocation.getBlockX(), blockLocation.getBlockZ());

            String areaName = (String)subscription.getSubscriptionJson().get("areaName");

            Polygon poly = ZoneX.areaManager.getArea(subscription.getSubscriber().getUniqueId(), areaName).getPolygon();

            // Only trigger if the player moves into the boundary.
            if(poly.contains(blockPoint)) {
                // Player is inside the boundary.
                subscription.onEvent("A " + breakEvent.getBlock().getType().toString() + " has been broken in " + areaName + " at " + blockPoint.x + ", " + blockPoint.y);
            }
        }
    }

}
