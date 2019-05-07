package org.mcnotify;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.authenticator.Authenticator;
import org.mcnotify.areas.AreaManager;
import org.mcnotify.commands.BaseCommandHandler;
import org.mcnotify.database.Database;
import org.mcnotify.config.EventRegistry;
import org.mcnotify.subscriptions.EventSubscriptionManager;
import org.mcnotify.config.ConfigurationManager;
import org.mcnotify.authenticator.RequestManager;

import java.sql.SQLException;

public class MCNotify extends JavaPlugin {

    public static Authenticator auth;
    public static ConfigurationManager config;
    public static EventSubscriptionManager eventSubscriptionManager;
    public static AreaManager areaManager;
    public static RequestManager requestManager;
    public static Database database;

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

        // Initializes authentication to the MCNotify servers and checks if the users is authenticated.
        auth = new Authenticator();

        // Sets up/loads the local databases to keep track of areas
        database = new Database();

        // Load the player areas
        areaManager = new AreaManager();
        try {
            areaManager.loadDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load the event subscriptions
        try {
            eventSubscriptionManager = new EventSubscriptionManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Register events
        eventRegistry = new EventRegistry(this);

        this.getCommand("mcnotify").setExecutor(new BaseCommandHandler(this));
        System.out.println("[MCNotify] Plugin enabled.");
    }

    @Override
    public void onDisable(){

    }

}
