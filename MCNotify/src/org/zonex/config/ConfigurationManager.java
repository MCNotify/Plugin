package org.zonex.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

import static java.lang.System.exit;

/**
 * Manages the configuration file and plugin config settings
 */
public class ConfigurationManager {

    // config files
    private File customConfigFile;
    private FileConfiguration customConfig;

    /**
     * Creates a new configuration manager
     * @param plugin the plugin to configure
     */
    public ConfigurationManager(JavaPlugin plugin){
        this.createConfigFile(plugin);
    }

    /**
     * Attempts to create a new configuration file.
     * @param plugin the plugin to create the configuration for
     */
    private void createConfigFile(JavaPlugin plugin){
        // Creates a new yml file in the plugin's folder.
        customConfigFile = new File(plugin.getDataFolder(), "ZoneX.yml");

        // If the file didn't exist, creates a new config file
        if (!customConfigFile.exists()) {
            System.out.println("[ZoneX] Creating new configuration file.");
            System.out.println("[ZoneX] WARNING: Configure the plugin in /plugins/ZoneX/ZoneX.yml");
            customConfigFile.getParentFile().mkdirs();
            try {
                // Attempts to create the config file.
                customConfigFile.createNewFile();
                customConfig= new YamlConfiguration();
                for(Configuration configuration : Configuration.values()){
                    customConfig.set(configuration.getYamlName(), configuration.getDefaultValue());
                }
                customConfig.save(customConfigFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("[ZoneX] WARNING: Plugin disabled until configuring /plugins/ZoneX/ZoneX.yml");
        } else {
            // Loads the yaml config.
            customConfig= new YamlConfiguration();
            try {
                customConfig.load(customConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            // Check if there are any additional configuration values that have been added that need to be placed in the config file.
            for(Configuration configuration : Configuration.values()){
                if(customConfig.get(configuration.getYamlName()) == null){
                    customConfig.set(configuration.getYamlName(), configuration.getDefaultValue());
                }
                try {
                    customConfig.save(customConfigFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets a value from the config file.
     * @param key the key in the config file to retrieve
     * @return the value set in the config file
     */
    public String getConfigValue(String key){
        return this.customConfig.getString(key);
    }

    /**
     * Sets a value in the config file. Used to set defaults.
     * @param key the key to set
     * @param value the value to set
     */
    void setConfigValue(String key, String value){
        this.customConfig.set(key, value);
        try {
            this.customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
