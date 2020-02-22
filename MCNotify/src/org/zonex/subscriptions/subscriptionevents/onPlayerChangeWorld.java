package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.zonex.ZoneX;
import org.zonex.subscriptions.Subscription;

public class onPlayerChangeWorld implements Listener {
    /**
     * Checks teleport events to see if the event is being watched by a subscription. If it is, it triggers the event.
     * @param teleportEvent The teleport event
     */
    @EventHandler
    public void onPlayerChangeWorld(PlayerTeleportEvent teleportEvent){
        if(teleportEvent.getFrom().getWorld() != teleportEvent.getTo().getWorld()) {
            if (teleportEvent.getTo().getWorld().getEnvironment() == World.Environment.NETHER) {
                for (Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_PLAYER_ENTER_NETHER)) {
                        subscription.onEvent(teleportEvent.getPlayer().getName() + " entered the nether");
                    }
                }
            if (teleportEvent.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
                for (Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_PLAYER_ENTER_END)) {
                    subscription.onEvent(teleportEvent.getPlayer().getName() + " entered the end");
                }
            }
            if (teleportEvent.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
                for (Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_PLAYER_ENTER_WORLD)) {
                    subscription.onEvent(teleportEvent.getPlayer().getName() + " entered the overworld");
                }
            }
        }
    }
}
