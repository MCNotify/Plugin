package org.zonex.config;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.zonex.areas.protection.ProtectionEvents;
import org.zonex.commands.multipartcommand.multipartevents.MultiPartBlockPlaceEvent;
import org.zonex.commands.multipartcommand.multipartevents.MultiPartItemSpawnEvent;
import org.zonex.subscriptions.subscriptionevents.*;

import java.util.ArrayList;

public class EventRegistry {

    ArrayList<Listener> listeners = new ArrayList<>();
    JavaPlugin plugin;

    public EventRegistry(JavaPlugin plugin){

        this.plugin = plugin;

        System.out.println("[MCNotify] Registering events...");

        listeners.add(new onPlayerMove());
        listeners.add(new onPlayerJoin());
        listeners.add(new onEntityExplodeEvent());
        listeners.add(new onBlockBreakEvent());
        listeners.add(new onRedstoneActiveEvent());
        listeners.add(new onCropGrownEvent());
        listeners.add(new MultiPartItemSpawnEvent());
        listeners.add(new MultiPartBlockPlaceEvent());
        listeners.add(new ProtectionEvents());

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
