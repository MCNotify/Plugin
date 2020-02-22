package org.zonex.commands.multipartcommand;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.zonex.commands.RegisterCommands;

/**
 * Base multi-part command.
 */
public abstract class MultiPartCommand implements Listener {

    protected Player player;

    public MultiPartCommand(Player player){
        RegisterCommands.mutiPartManager.addListener(player, this);
        this.player = player;
    }

    public abstract void onFinish(Event event);
    public abstract void onUpdateEvent(Event event);
    public abstract boolean checkUpdateEvent(Event event);
    public abstract boolean checkFinishEvent(Event event);
    public abstract void cleanup();

}
