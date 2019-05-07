package org.mcnotify.subscriptions.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;
import org.mcnotify.areas.Polygon;

import java.awt.*;

public class onEntityExplodeEvent implements Listener {

    @EventHandler
    public void onEntityExplosionEvent(EntityExplodeEvent explodeEvent){
        for(Subscription subscription : MCNotify.subscriptionManager.getSubscriptions(Events.ON_BLOCK_EXPLODE)){
            // Determine if the explosion happened in a boundary
            Location explodeLocation = explodeEvent.getLocation();
            Point explodePoint = new Point(explodeLocation.getBlockX(), explodeLocation.getBlockZ());

            int areaId = ((Long)(subscription.getSubscriptionJson().get("areaId"))).intValue();

            Polygon poly = MCNotify.areaManager.getArea(areaId).getPolygon();

            // Only trigger if the player moves into the boundary.
            if(poly.contains(explodePoint)) {
                // Player is inside the boundary.
                subscription.onEvent();
            }
        }
    }
}
