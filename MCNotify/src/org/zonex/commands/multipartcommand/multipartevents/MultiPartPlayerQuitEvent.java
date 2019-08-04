package org.zonex.commands.multipartcommand.multipartevents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.zonex.commands.RegisterCommands;

public class MultiPartPlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(RegisterCommands.mutiPartManager.contains(player)){
            RegisterCommands.mutiPartManager.playerQuit(player);
        }

    }



}
