package org.mcnotify.subscriptions.subscriptionevents;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;
import org.mcnotify.areas.Polygon;

import java.awt.*;

public class onCropGrownEvent implements Listener {

    @EventHandler
    public void onCropGrownEvent(BlockGrowEvent growEvent) {
        for (Subscription subscription : MCNotify.subscriptionManager.getSubscriptions(Events.ON_CROP_GROWN)) {
            BlockData data = growEvent.getBlock().getBlockData();
            if (data instanceof Ageable) {
                Ageable age = (Ageable) data;
                if (age.getAge() == age.getMaximumAge() - 1) {
                    // Determine if the explosion happened in a boundary
                    Location blockLocation = growEvent.getBlock().getLocation();
                    Point blockPoint = new Point(blockLocation.getBlockX(), blockLocation.getBlockZ());

                    int areaId = ((Long) subscription.getSubscriptionJson().get("areaId")).intValue();

                    Polygon poly = MCNotify.areaManager.getArea(areaId).getPolygon();

                    // Only trigger if the player moves into the boundary.
                    if (poly.contains(blockPoint)) {
                        // Crop is inside the boundary.
                        subscription.onEvent();
                    }
                }
            }
        }
    }
}

