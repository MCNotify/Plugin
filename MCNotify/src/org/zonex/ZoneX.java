package org.zonex;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.zonex.communication.auth.Authenticator;
import org.zonex.areas.AreaManager;
import org.zonex.commands.RegisterCommands;
import org.zonex.communication.notifications.CommunicationHandler;
import org.zonex.communication.notifications.CommunicationProtocol;
import org.zonex.config.EventRegistry;
import org.zonex.config.Metrics;
import org.zonex.datastore.Datastore;
import org.zonex.subscriptions.Subscription;
import org.zonex.subscriptions.SubscriptionManager;
import org.zonex.config.ConfigurationManager;
import org.zonex.communication.auth.RequestManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Filter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * Entry point for the plugin
 */
public class ZoneX extends JavaPlugin {

    /**
     * The plugin instance
     */
    public static ZoneX plugin;

    /**
     * The authenticator for network authentication (when implemented)
     */
    public static Authenticator auth;

    /**
     * ConfigurationManager that loads the user defined settings into the application. Allows retrieving the settings
     * to ensure the application behaves according to the selected settings
     */
    public static ConfigurationManager config;

    /**
     * Subscription manager to manage user notification subscriptions and to send out notifications to users
     */
    public static SubscriptionManager subscriptionManager;

    /**
     * Area manager that managers a player's areas and area protection
     */
    public static AreaManager areaManager;

    /**
     * Api module for networking (when implemented)
     */
    public static RequestManager requestManager;

    /**
     * Datastore element for easy access to data without worrying about if the user is using local storage or database
     */
    public static Datastore datastore;

    /**
     * Communication handler that handles sending out notifications to externally linked apps
     */
    public static CommunicationHandler communicationManager;

    /**
     * Event listener
     */
    public static EventRegistry eventRegistry;

    /**
     * OnEnable method that enables the plugin
     */
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

        // BStats tracking
        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SingleLineChart("zone", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return ZoneX.areaManager.countZones();
            }
        }));
        metrics.addCustomChart(new Metrics.SingleLineChart("subscriptions", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return ZoneX.subscriptionManager.getSubscriptions().size();
            }
        }));
        metrics.addCustomChart(new Metrics.SingleLineChart("externalCommunications", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return ZoneX.communicationManager.getTotalCommunicationMethods();
            }
        }));
        metrics.addCustomChart(new Metrics.SimplePie("storage_format", new Callable<String>() {
            @Override
            public String  call() throws Exception {
                return ZoneX.datastore.isUsingDatabase() ? "Database" : "Flat file";
            }
        }));

        System.out.println("[ZoneX] Plugin enabled.");
    }

    @Override
    public void onDisable(){

    }

}
