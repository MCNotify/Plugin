package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.zonex.ZoneX;
import org.zonex.subscriptions.Subscription;
import org.zonex.areas.Polygon;

import java.awt.*;


public class onPlayerMove implements Listener {

    /**
     * Checks move events to see if the event is being watched by a subscription. If it is, it triggers the event.
     * @param moveEvent The move event
     */
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent moveEvent){

        for(Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_PLAYER_MOVE)){
            // Determine if the movement happened across a boundary
            Point newPlayerPoint = new Point(moveEvent.getTo().getBlockX(), moveEvent.getTo().getBlockZ());
            Point oldPlayerPoint = new Point(moveEvent.getFrom().getBlockX(), moveEvent.getFrom().getBlockZ());

            String areaName = (String)subscription.getSubscriptionJson().get("areaName");

            Polygon poly = ZoneX.areaManager.getArea(subscription.getSubscriber().getUniqueId(), areaName).getPolygon();

            // Only trigger if the player moves into the boundary.
            if(!poly.contains(oldPlayerPoint) && poly.contains(newPlayerPoint)) {
                // Player is inside the boundary.
                subscription.onEvent(moveEvent.getPlayer().getDisplayName() + " has entered " + areaName + ".");
            }
        }
    }


}
