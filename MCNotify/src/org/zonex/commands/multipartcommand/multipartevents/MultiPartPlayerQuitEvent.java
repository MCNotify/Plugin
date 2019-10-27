package org.zonex.commands.multipartcommand.multipartevents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.zonex.ZoneX;
import org.zonex.commands.RegisterCommands;
import org.zonex.particles.ParticleManager;

public class MultiPartPlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        // Cleaup events when a player leaves the server.
        Player player = event.getPlayer();
        if(RegisterCommands.mutiPartManager.contains(player)){
            RegisterCommands.mutiPartManager.playerQuit(player);
        }
        if(RegisterCommands.particleManager.isViewingArea(player)){
            RegisterCommands.particleManager.stopAreaViewParticleThread(player);
        }

    }


}
