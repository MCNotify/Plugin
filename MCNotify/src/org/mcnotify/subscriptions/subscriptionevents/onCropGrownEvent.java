package org.mcnotify.subscriptions.subscriptionevents;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;
import org.mcnotify.areas.Polygon;

import java.awt.*;

public class onCropGrownEvent implements Listener {

    @EventHandler
    public void onCropGrownEvent(BlockGrowEvent growEvent){
        for(Subscription subscription : MCNotify.eventSubscriptionManager.getSubscriptions()){
            if(subscription.getEventType() == Events.ON_BLOCK_EXPLODE){
                if(growEvent.getBlock().getState().equals(CropState.RIPE) || growEvent.getBlock().getState().equals(CropState.VERY_TALL)){
                    // Determine if the explosion happened in a boundary
                    Location blockLocation = growEvent.getBlock().getLocation();
                    Point blockPoint = new Point(blockLocation.getBlockX(), blockLocation.getBlockZ());

                    Polygon poly = MCNotify.areaManager.getArea(((int)(subscription.getSubscriptionJson().get("areaid")))).getPolygon();
                    // Only trigger if the player moves into the boundary.
                    if(poly.contains(blockPoint)) {
                        // Player is inside the boundary.
                        subscription.onEvent();
                    }
                }
            }
        }
    }
}