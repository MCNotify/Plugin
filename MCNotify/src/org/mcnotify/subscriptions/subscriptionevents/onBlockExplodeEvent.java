package org.mcnotify.subscriptions.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;
import org.mcnotify.areas.Polygon;

import java.awt.*;

public class onBlockExplodeEvent implements Listener {

    @EventHandler
    public void onBlockExplosionEvent(BlockExplodeEvent explodeEvent){

        for(Subscription subscription : MCNotify.subscriptionManager.getSubscriptions()){
            if(subscription.getEventType() == Events.ON_BLOCK_EXPLODE){
                // Determine if the explosion happened in a boundary
                Location blockLocation = explodeEvent.getBlock().getLocation();
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
}
