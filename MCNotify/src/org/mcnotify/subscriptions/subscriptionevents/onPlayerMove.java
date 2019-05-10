package org.mcnotify.subscriptions.subscriptionevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;
import org.mcnotify.areas.Polygon;

import java.awt.*;


public class onPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent moveEvent){

        for(Subscription subscription : MCNotify.subscriptionManager.getSubscriptions(Events.ON_PLAYER_MOVE)){
            // Determine if the movement happened across a boundary
            Point newPlayerPoint = new Point(moveEvent.getTo().getBlockX(), moveEvent.getTo().getBlockZ());
            Point oldPlayerPoint = new Point(moveEvent.getFrom().getBlockX(), moveEvent.getFrom().getBlockZ());

            String areaName = (String)subscription.getSubscriptionJson().get("areaName");

            Polygon poly = MCNotify.areaManager.getArea(subscription.getSubscriber().getUniqueId(), areaName).getPolygon();

            // Only trigger if the player moves into the boundary.
            if(!poly.contains(oldPlayerPoint) && poly.contains(newPlayerPoint)) {
                // Player is inside the boundary.
                subscription.onEvent();
            }
        }
    }


}
