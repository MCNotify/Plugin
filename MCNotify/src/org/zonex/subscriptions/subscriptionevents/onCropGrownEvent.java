package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.zonex.ZoneX;
import org.zonex.subscriptions.Subscription;
import org.zonex.areas.Polygon;

import java.awt.*;

public class onCropGrownEvent implements Listener {

    /**
     * Checks crop grow events to see if the event is being watched by a subscription. If it is, it triggers the event.
     * @param growEvent The crop grow event
     */
    @EventHandler
    public void onCropGrownEvent(BlockGrowEvent growEvent) {
        for (Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_CROP_GROWN)) {
            BlockData data = growEvent.getBlock().getBlockData();
            if (data instanceof Ageable) {
                Ageable age = (Ageable) data;
                if (age.getAge() == age.getMaximumAge() - 1) {
                    // Determine if the explosion happened in a boundary
                    Location blockLocation = growEvent.getBlock().getLocation();
                    Point blockPoint = new Point(blockLocation.getBlockX(), blockLocation.getBlockZ());

                    String areaName = (String)subscription.getSubscriptionJson().get("areaName");

                    Polygon poly = ZoneX.areaManager.getArea(subscription.getSubscriber().getUniqueId(), areaName).getPolygon();

                    // Only trigger if the player moves into the boundary.
                    if (poly.contains(blockPoint)) {
                        // Crop is inside the boundary.
                        subscription.onEvent(growEvent.getBlock().getType() + " is fully grown in " + areaName + ".");
                    }
                }
            }
        }
    }
}

