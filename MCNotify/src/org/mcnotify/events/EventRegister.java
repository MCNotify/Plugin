package org.mcnotify.events;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.events.eventhandlers.onPlayerJoin;
import org.mcnotify.events.eventhandlers.onPlayerMove;

import java.util.ArrayList;

public class EventRegister {

    ArrayList<Listener> listeners = new ArrayList<>();

    public EventRegister(JavaPlugin plugin){
        listeners.add(new onPlayerMove());
        listeners.add(new onPlayerJoin());

        for(Listener l : listeners){
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }
    }

}
