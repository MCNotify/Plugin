package org.mcnotify.commands.multipartcommand.multipartevents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mcnotify.commands.BaseCommandHandler;

public class MultiPartPlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(BaseCommandHandler.mutiPartManager.contains(player)){
            BaseCommandHandler.mutiPartManager.playerQuit(player);
        }

    }



}
