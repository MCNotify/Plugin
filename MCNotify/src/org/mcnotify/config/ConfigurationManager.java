package org.mcnotify.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    private File customConfigFile;
    private FileConfiguration customConfig;

    public ConfigurationManager(JavaPlugin plugin){
        this.createConfigFile(plugin);
    }

    private void createConfigFile(JavaPlugin plugin){

        customConfigFile = new File(plugin.getDataFolder(), "MCNotify.yml");
        if (!customConfigFile.exists()) {
            System.out.println("[MCNotify] Creating new configuration file.");
            System.out.println("[MCNotify] WARNING: Configure the plugin in /plugins/MCNotify/MCNotify.yml");
            customConfigFile.getParentFile().mkdirs();
            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            plugin.saveResource(plugin.getDataFolder() + "MCNotify.yml", false);
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    String getConfigValue(String key){
        return this.customConfig.getString(key);
    }

    void setConfigValue(String key, String value){
        this.customConfig.set(key, value);
        try {
            this.customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkConfig(){
        System.out.println("[MCNotify] Checking configuration files...");
        for(Configuration configuration : Configuration.values()){
            // If the configuration value does not exist in the file,
            // Set the configuration to the default value.
            if(configuration.getValue() == null){
                configuration.setDefaultValue();
            }
        }
    }
}
