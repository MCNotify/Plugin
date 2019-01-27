package org.mcnotify.events;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.events.multipartcommandevents.multipartevents.MultiPartBlockPlaceEvent;
import org.mcnotify.events.multipartcommandevents.multipartevents.MultiPartItemSpawnEvent;
import org.mcnotify.events.subscriptionevents.*;

import java.util.ArrayList;

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


}
