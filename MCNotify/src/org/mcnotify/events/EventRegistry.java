package org.mcnotify.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.events.multipartcommandevents.multipartevents.MultiPartBlockPlaceEvent;
import org.mcnotify.events.multipartcommandevents.multipartevents.MultiPartItemSpawnEvent;
import org.mcnotify.events.subscriptionevents.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EventRegistry {

    ArrayList<Listener> listeners = new ArrayList<>();
    JavaPlugin plugin;

    public EventRegistry(JavaPlugin plugin){

        this.plugin = plugin;

        System.out.println("[MCNotify] Registering events...");

        listeners.add(new onPlayerMove());
        listeners.add(new onPlayerJoin());
        listeners.add(new onBlockExplodeEvent());
        listeners.add(new onBlockBreakEvent());
        listeners.add(new onRedstoneActiveEvent());
        listeners.add(new onCropGrownEvent());
        listeners.add(new MultiPartItemSpawnEvent());
        listeners.add(new MultiPartBlockPlaceEvent());

        for(Listener l : listeners){
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }

        System.out.println("[MCNotify] Events registered.");
    }


    // Allows other plugins to link their events to the notification system by simply linking their listeners!
    public void addEvent(Listener newListener){
        listeners.add(newListener);
        plugin.getServer().getPluginManager().registerEvents(newListener, plugin);
    }


}
