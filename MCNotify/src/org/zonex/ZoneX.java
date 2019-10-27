package org.zonex;

import org.bukkit.plugin.java.JavaPlugin;
import org.zonex.communication.auth.Authenticator;
import org.zonex.areas.AreaManager;
import org.zonex.commands.RegisterCommands;
import org.zonex.communication.notifications.CommunicationHandler;
import org.zonex.config.EventRegistry;
import org.zonex.config.Metrics;
import org.zonex.datastore.Datastore;
import org.zonex.subscriptions.SubscriptionManager;
import org.zonex.config.ConfigurationManager;
import org.zonex.communication.auth.RequestManager;

import java.sql.SQLException;

public class ZoneX extends JavaPlugin {

    public static ZoneX plugin;
    public static Authenticator auth;
    public static ConfigurationManager config;
    public static SubscriptionManager subscriptionManager;
    public static AreaManager areaManager;
    public static RequestManager requestManager;
    public static Datastore datastore;
    public static CommunicationHandler communicationManager;

    public static EventRegistry eventRegistry;

    @Override
    public void onEnable(){
        plugin = this;

        // Initializes authentication to the MCNotify servers and checks if the server is authenticated.

        // Disabling server authentication for initial release.
        // Users should not have to register with my severs to use the plugin while it is free!
        // auth = new Authenticator();
        // auth.init();

        // Generate the configuration file if it doesn't exist. Otherwise, load the configuration file.
        config = new ConfigurationManager(this);

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
        eventRegistry = new EventRegistry(plugin);

        // Setup the communication protocol
        communicationManager = new CommunicationHandler();

        new RegisterCommands();

        Metrics metrics = new Metrics(this);

        System.out.println("[ZoneX] Plugin enabled.");
    }

    @Override
    public void onDisable(){

    }

}
