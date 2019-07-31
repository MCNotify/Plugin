package org.mcnotify;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.authenticator.Authenticator;
import org.mcnotify.areas.AreaManager;
import org.mcnotify.commands.BaseCommandHandler;
import org.mcnotify.datastore.Database;
import org.mcnotify.config.EventRegistry;
import org.mcnotify.datastore.Datastore;
import org.mcnotify.subscriptions.SubscriptionManager;
import org.mcnotify.config.ConfigurationManager;
import org.mcnotify.authenticator.RequestManager;

import java.sql.SQLException;

public class MCNotify extends JavaPlugin {

    public static Authenticator auth;
    public static ConfigurationManager config;
    public static SubscriptionManager subscriptionManager;
    public static AreaManager areaManager;
    public static RequestManager requestManager;
    public static Datastore datastore;

    public static EventRegistry eventRegistry;

    @Override
    public void onEnable(){
        // Generate the configuration file if it doesn't exist. Otherwise, load the configuration file.
        config = new ConfigurationManager(this);

        // Initializes authentication to the MCNotify servers and checks if the users is authenticated.
        auth = new Authenticator();

        // Sets up/loads the datastore
        datastore = new Datastore(getDataFolder().getAbsolutePath() + "/data/data.mcn");

        // Load the player areas
        areaManager = new AreaManager();
        try {
            areaManager.loadDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load the event subscriptions
        try {
            subscriptionManager = new SubscriptionManager();
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
