package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.zonex.ZoneX;
import org.zonex.subscriptions.Subscription;
import org.zonex.areas.Polygon;

import java.awt.*;

public class onRedstoneActiveEvent implements Listener {

    /**
     * Checks redstone events to see if the event is being watched by a subscription. If it is, it triggers the event.
     * @param redstoneEvent The redstone event
     */
    @EventHandler
    public void onRedstoneActiveEvent(BlockRedstoneEvent redstoneEvent){
        for(Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_REDSTONE_ACTIVE)){
            // Determine if the event happened in a boundary
            Location blockLocation = redstoneEvent.getBlock().getLocation();
            Point blockPoint = new Point(blockLocation.getBlockX(), blockLocation.getBlockZ());

            String areaName = (String)subscription.getSubscriptionJson().get("areaName");

            Polygon poly = ZoneX.areaManager.getArea(subscription.getSubscriber().getUniqueId(), areaName).getPolygon();

            // Only trigger if the player moves into the boundary.
            if(poly.contains(blockPoint)) {
                // Player is inside the boundary.
                subscription.onEvent("Redstone was activated in " + areaName + " at " + blockPoint.x + ", " + blockPoint.y + ".");
            }
        }
    }
}
