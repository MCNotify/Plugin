package org.mcnotify.events.multipartcommandevents.multipartevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.mcnotify.commands.BaseCommandHandler;

public class MultiPartItemSpawnEvent implements Listener {

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event){
        if(BaseCommandHandler.mutiPartManager.contains(event.getPlayer())){
                BaseCommandHandler.mutiPartManager.finishEvent(event.getPlayer(), event);
            }
        }
}
