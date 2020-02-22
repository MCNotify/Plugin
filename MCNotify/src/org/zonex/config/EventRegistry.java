package org.zonex.config;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.zonex.areas.protection.ProtectionEvents;
import org.zonex.commands.multipartcommand.multipartevents.MultiPartBlockPlaceEvent;
import org.zonex.commands.multipartcommand.multipartevents.MultiPartItemSpawnEvent;
import org.zonex.subscriptions.subscriptionevents.*;

import java.util.ArrayList;

/**
 * Registers events when the plugin loads.
 * Moved to this class to de-clutter the enable method.
 */
public class EventRegistry {

    ArrayList<Listener> listeners = new ArrayList<>();
    JavaPlugin plugin;

    /**
     * Creates and registers events for the plugin.
     * @param plugin the plugin to register events for.
     */
    public EventRegistry(JavaPlugin plugin){

        this.plugin = plugin;

        System.out.println("[ZoneX] Registering events...");

        listeners.add(new onPlayerMove());
        listeners.add(new onPlayerJoin());
        listeners.add(new onEntityExplodeEvent());
        listeners.add(new onBlockBreakEvent());
        listeners.add(new onRedstoneActiveEvent());
        listeners.add(new onCropGrownEvent());
        listeners.add(new MultiPartItemSpawnEvent());
        listeners.add(new MultiPartBlockPlaceEvent());
        listeners.add(new ProtectionEvents());
        listeners.add(new onPlayerDeath());
        listeners.add(new onPlayerChangeWorld());

        for(Listener l : listeners){
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }

        System.out.println("[ZoneX] Events registered.");
    }


    /**
     * Allows other plugins to link their events to the notification system by simply linking their listeners!
     * @param newListener
     */
    public void addEvent(Listener newListener){
        listeners.add(newListener);
        plugin.getServer().getPluginManager().registerEvents(newListener, plugin);
    }


}
