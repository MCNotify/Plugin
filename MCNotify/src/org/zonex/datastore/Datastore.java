package org.zonex.datastore;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.zonex.config.Configuration;
import org.zonex.datastore.baseModels.BaseAreaModel;
import org.zonex.datastore.baseModels.BaseSubscriptionModel;

import java.io.File;
import java.io.IOException;

public class Datastore {

    private boolean isUsingDatabase;
    private Database database;
    private FlatFile flatfile;
    private String FILE_LOCATION = "";

    public Datastore(String fileLocation) {
        this.FILE_LOCATION = fileLocation;
        if(Configuration.DATABASE_ENABLED.getValue() == "true"){
            if(!this.setupDatabase()){
                this.setupFlatFile();
            }
        } else {
            this.isUsingDatabase = false;
            this.setupFlatFile();
        }
    }

    private boolean setupDatabase(){
        this.database = new Database();
        if(!this.database.isConnected()){
            this.isUsingDatabase = false;
        } else {
            System.out.println("[ZoneX] Connected to database");
        }
        return this.isUsingDatabase;
    }

    private boolean setupFlatFile() {
        File file = new File(this.FILE_LOCATION);
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        if(!file.exists()){
            yamlConfiguration.options().copyDefaults(true);
            yamlConfiguration.createSection("areas");
            yamlConfiguration.createSection("subscriptions");
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

    public BaseAreaModel areaTable(){
        if(this.isUsingDatabase){
            return this.database.areaTable();
        } else {
            return this.flatfile.areaTable();
        }
    }

    public BaseSubscriptionModel subscriptionTable(){
        if(this.isUsingDatabase){
            return this.database.subscriptionTable();
        } else {
            return this.flatfile.subscriptionTable();
        }
    }

}
