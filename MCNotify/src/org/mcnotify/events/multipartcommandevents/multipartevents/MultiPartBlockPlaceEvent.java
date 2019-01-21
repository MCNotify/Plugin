package org.mcnotify.events.multipartcommandevents.multipartevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mcnotify.commands.BaseCommandHandler;

public class MultiPartBlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event){
        if(BaseCommandHandler.mutiPartManager.contains(event.getPlayer())){
            BaseCommandHandler.mutiPartManager.fireEvent(event.getPlayer(), event);
        }
    }

}
