package org.mcnotify.events.eventhandlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mcnotify.MCNotify;

public class onPlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuitEvet(PlayerQuitEvent quitEvent){
        MCNotify.eventSubscriptionManager.unloadSubscriptions(quitEvent.getPlayer());
    }

}
