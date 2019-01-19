package org.mcnotify;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.commands.BaseCommandHandler;
import org.mcnotify.events.EventRegister;
import org.mcnotify.events.EventSubscriptionManager;
import org.mcnotify.config.ConfigurationManager;
import org.mcnotify.utility.RequestManager;

public class MCNotify extends JavaPlugin {

    public static ConfigurationManager config;
    public static EventSubscriptionManager eventSubscriptionManager;
    public static RequestManager requestManager;
    public static int server_id = -1;

    private EventRegister er;

    @Override
    public void onEnable(){
        // Generate the configuration file if it doesn't exist. Otherwise, load the configuration file.
        config = new ConfigurationManager(this);
        // Has to be executed after the configuration manager is created.
        config.checkConfig();

        requestManager = new RequestManager();

        // Load the event subscriptions
        eventSubscriptionManager = new EventSubscriptionManager();

        // Register events
        er = new EventRegister(this);

        this.getCommand("mcnotify").setExecutor(new BaseCommandHandler(this));
    }

    @Override
    public void onDisable(){

    }

}
