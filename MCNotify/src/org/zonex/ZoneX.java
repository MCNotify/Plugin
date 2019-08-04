package org.zonex;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.zonex.areas.Area;
import org.zonex.authenticator.Authenticator;
import org.zonex.areas.AreaManager;
import org.zonex.commands.RegisterCommands;
import org.zonex.config.EventRegistry;
import org.zonex.datastore.Datastore;
import org.zonex.subscriptions.Subscription;
import org.zonex.subscriptions.SubscriptionManager;
import org.zonex.config.ConfigurationManager;
import org.zonex.authenticator.RequestManager;

import java.sql.SQLException;

public class ZoneX extends JavaPlugin {

    public static ZoneX plugin;
    public static Authenticator auth;
    public static ConfigurationManager config;
    public static SubscriptionManager subscriptionManager;
    public static AreaManager areaManager;
    public static RequestManager requestManager;
    public static Datastore datastore;

    public static EventRegistry eventRegistry;

    @Override
    public void onEnable(){
        plugin = this;
        // Register serialized classes
        ConfigurationSerialization.registerClass(Area.class, "Area");
        ConfigurationSerialization.registerClass(Subscription.class, "Subscription");


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
        eventRegistry = new EventRegistry(plugin);

        new RegisterCommands();
        System.out.println("[ZoneX] Plugin enabled.");
    }

    @Override
    public void onDisable(){

    }

}
