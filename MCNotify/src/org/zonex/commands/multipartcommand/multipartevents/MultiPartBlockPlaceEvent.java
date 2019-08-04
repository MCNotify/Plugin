package org.zonex.commands.multipartcommand.multipartevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.zonex.commands.RegisterCommands;

public class MultiPartBlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event){
        if(RegisterCommands.mutiPartManager.contains(event.getPlayer())){
            RegisterCommands.mutiPartManager.fireEvent(event.getPlayer(), event);
        }
    }

}
