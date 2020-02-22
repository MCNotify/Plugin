package org.zonex.datastore;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.zonex.config.Configuration;
import org.zonex.datastore.baseModels.BaseAreaModel;
import org.zonex.datastore.baseModels.BaseCommunicationModel;
import org.zonex.datastore.baseModels.BaseSubscriptionModel;

import java.io.File;
import java.io.IOException;

/**
 * The datastore class abstracts flat file and database systems and determines which one to query from based on
 * the plugin settings
 */
public class Datastore {

    /**
     * If the user has configured a database to be used
     */
    private boolean isUsingDatabase;

    /**
     * A reference to the database storage object (if used)
     */
    private Database database;

    /**
     * A refference to the flat file storage object (if used)
     */
    private FlatFile flatfile;

    /**
     * The flat file location as set in the config
     */
    private String FILE_LOCATION = "";

    /**
     * Creates a new datastore.
     * @param fileLocation The file location of the flat file. If database cannot connect, falls back to the file at this location.
     */
    public Datastore(String fileLocation) {
        this.FILE_LOCATION = fileLocation;

        // Check if the plugin is configured for database storage
        if(Configuration.DATABASE_ENABLED.getValue().equals("true")){
            // attempt to setup the database
            if(!this.setupDatabase()){
                // If database is unable to be setup, use flat file instead.
                this.setupFlatFile();
            }
        } else {
            this.isUsingDatabase = false;
            this.setupFlatFile();
        }
    }

    /**
     * Attempts to setup a database connection based on configuration parameters
     * @return
     */
    private boolean setupDatabase(){
        // Create a new database object
        this.database = new Database();

        // Check if the dadtabase was able to connect with configuration settings.
        if(!this.database.isConnected()){
            System.out.println("[ZoneX] Could not connect to database! Falling back to flat file");
            this.isUsingDatabase = false;
        } else {
            System.out.println("[ZoneX] Connected to database");
            this.isUsingDatabase = true;
        }
        return this.isUsingDatabase;
    }

    /**
     * Sets up a flat file storage system
     * @return
     */
    private boolean setupFlatFile() {
        File file = new File(this.FILE_LOCATION);
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        // If no storage file exists, create one.
        if(!file.exists()){
            yamlConfiguration.options().copyDefaults(true);
            yamlConfiguration.createSection("areas");
            yamlConfiguration.createSection("subscriptions");
            yamlConfiguration.createSection("communications");
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                yamlConfiguration.load(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        this.flatfile = new FlatFile(yamlConfiguration, file);
        System.out.println("[ZoneX] Using flat file");
        return true;
    }

    /**
     * Gets the area table
     * @return The area table
     */
    public BaseAreaModel areaTable(){
        if(this.isUsingDatabase){
            return this.database.areaTable();
        } else {
            return this.flatfile.areaTable();
        }
    }

    /**
     * Gets the subscription table
     * @return the subscription table
     */
    public BaseSubscriptionModel subscriptionTable(){
        if(this.isUsingDatabase){
            return this.database.subscriptionTable();
        } else {
            return this.flatfile.subscriptionTable();
        }
    }

    /**
     * Gets the communication table
     * @return the communication table
     */
    public BaseCommunicationModel communicationTable(){
        if(this.isUsingDatabase){
            return this.database.communicationTable();
        } else {
            return this.flatfile.communicationTable();
        }
    }

    /**
     * If a database connection is being used
     * @return If a database connection is being used
     */
    public boolean isUsingDatabase() {
        return isUsingDatabase;
    }
}
