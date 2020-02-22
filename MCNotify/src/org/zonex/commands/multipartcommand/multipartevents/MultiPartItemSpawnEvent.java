package org.zonex.commands.multipartcommand.multipartevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.zonex.commands.RegisterCommands;

/**
 * Handles item drop events for multi-part commands.
 */
public class MultiPartItemSpawnEvent implements Listener {

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event){
        if(RegisterCommands.mutiPartManager.contains(event.getPlayer())){
                RegisterCommands.mutiPartManager.finishEvent(event.getPlayer(), event);
            }
        }
}
