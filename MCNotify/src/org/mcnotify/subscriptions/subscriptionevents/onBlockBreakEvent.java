package org.mcnotify.subscriptions.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;
import org.mcnotify.areas.Polygon;

import java.awt.*;

public class onBlockBreakEvent implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent breakEvent) {
        for(Subscription subscription : MCNotify.subscriptionManager.getSubscriptions(Events.ON_BLOCK_BREAK)){
            // Determine if the break happened in a boundary
            Location blockLocation = breakEvent.getBlock().getLocation();
            Point blockPoint = new Point(blockLocation.getBlockX(), blockLocation.getBlockZ());

            int areaId = ((Long)(subscription.getSubscriptionJson().get("areaId"))).intValue();

            Polygon poly = MCNotify.areaManager.getArea(areaId).getPolygon();

            // Only trigger if the player moves into the boundary.
            if(poly.contains(blockPoint)) {
                // Player is inside the boundary.
                subscription.onEvent();
            }
        }
    }

}
