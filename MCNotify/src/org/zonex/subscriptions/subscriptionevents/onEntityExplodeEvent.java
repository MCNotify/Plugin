package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.zonex.ZoneX;
import org.zonex.subscriptions.Subscription;
import org.zonex.areas.Polygon;

import java.awt.*;

public class onEntityExplodeEvent implements Listener {

    /**
     * Checks explode events to see if the event is being watched by a subscription. If it is, it triggers the event.
     * @param explodeEvent The explode event
     */
    @EventHandler
    public void onEntityExplosionEvent(EntityExplodeEvent explodeEvent){
        for(Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_BLOCK_EXPLODE)){
            // Determine if the explosion happened in a boundary
            Location explodeLocation = explodeEvent.getLocation();
            Point explodePoint = new Point(explodeLocation.getBlockX(), explodeLocation.getBlockZ());

            String areaName = (String)subscription.getSubscriptionJson().get("areaName");

            Polygon poly = ZoneX.areaManager.getArea(subscription.getSubscriber().getUniqueId(), areaName).getPolygon();

            // Only trigger if the player moves into the boundary.
            if(poly.contains(explodePoint)) {
                // Player is inside the boundary.
                subscription.onEvent("A block has exploded in " + areaName + " at " + explodePoint.x + ", " + explodePoint.y + ".");
            }
        }
    }
}
