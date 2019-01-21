package org.mcnotify;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.areas.AreaManager;
import org.mcnotify.commands.BaseCommandHandler;
import org.mcnotify.events.EventRegistry;
import org.mcnotify.events.EventSubscriptionManager;
import org.mcnotify.config.ConfigurationManager;
import org.mcnotify.utility.RequestManager;

public class MCNotify extends JavaPlugin {

    public static ConfigurationManager config;
    public static EventSubscriptionManager eventSubscriptionManager;
    public static AreaManager areaManager;
    public static RequestManager requestManager;
    public static int server_id = -1;

    public static EventRegistry eventRegistry;

    @Override
    public void onEnable(){
        // Generate the configuration file if it doesn't exist. Otherwise, load the configuration file.
        config = new ConfigurationManager(this);
        // Has to be executed after the configuration manager is created.
        boolean isConfigured = config.isConfigured();

        if(!isConfigured){
            // Shutdown the plugin if it is not properly configured.
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Create a new request manager to send requests to the MCNotify servers.
        requestManager = new RequestManager(this);
        requestManager.init();

        // Load the player areas
        areaManager = new AreaManager();
        areaManager.loadDatabase();

        // Load the event subscriptions
        eventSubscriptionManager = new EventSubscriptionManager();

        // Register events
        eventRegistry = new EventRegistry(this);

        this.getCommand("mcnotify").setExecutor(new BaseCommandHandler(this));
        System.out.println("[MCNotify] Plugin enabled.");
    }

    @Override
    public void onDisable(){

    }

}
