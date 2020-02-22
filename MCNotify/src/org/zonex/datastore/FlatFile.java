package org.zonex.datastore;

import org.bukkit.configuration.file.YamlConfiguration;
import org.zonex.datastore.fileModels.FileAreaTable;
import org.zonex.datastore.fileModels.FileCommunicationTable;
import org.zonex.datastore.fileModels.FileSubscriptionTable;

import java.io.File;

/**
 * Flat file class to manage saving and loading flat file data
 */
public class FlatFile {

    private YamlConfiguration yamlConfiguration;
    private File file;

    /**
     * Creates an instance of a flat file storage
     * @param yamlFile the Yaml configuration file for the file
     * @param file The location of the flat file.
     */
    public FlatFile(YamlConfiguration yamlFile, File file) {
        this.yamlConfiguration = yamlFile;
        this.file = file;
    }

    /**
     * Gets the area table stored in flat file storage
     * @return the area table storing all player areas
     */
    public FileAreaTable areaTable(){
        return new FileAreaTable(yamlConfiguration, file);
    }

    /**
     * Gets the subscription table in flat file storage
     * @return The subscription table of all player subscriptions
     */
    public FileSubscriptionTable subscriptionTable(){
        return new FileSubscriptionTable(yamlConfiguration, file);
    }

    /**
     * The communication table in flat file storage
     * @return The communication table that stores all player registered communication methods
     */
    public FileCommunicationTable communicationTable(){
        return new FileCommunicationTable(yamlConfiguration, file);
    }

}
