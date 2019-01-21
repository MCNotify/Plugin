package org.mcnotify.events.multipartcommandevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.mcnotify.commands.BaseCommandHandler;

public abstract class MultiPartCommand implements Listener {

    protected Player player;

    public MultiPartCommand(Player player){
        BaseCommandHandler.mutiPartManager.addListener(player, this);
        this.player = player;
    }

    public abstract void onFinish(Event event);
    public abstract void onUpdateEvent(Event event);
    public abstract boolean checkUpdateEvent(Event event);
    public abstract boolean checkFinishEvent(Event event);
    public abstract void cleanup();

}
