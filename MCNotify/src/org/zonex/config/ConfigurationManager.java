package org.zonex.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

import static java.lang.System.exit;

public class ConfigurationManager {

    private File customConfigFile;
    private FileConfiguration customConfig;

    public ConfigurationManager(JavaPlugin plugin){
        this.createConfigFile(plugin);
    }

    private void createConfigFile(JavaPlugin plugin){

        customConfigFile = new File(plugin.getDataFolder(), "ZoneX.yml");
        if (!customConfigFile.exists()) {
            System.out.println("[ZoneX] Creating new configuration file.");
            System.out.println("[ZoneX] WARNING: Configure the plugin in /plugins/ZoneX/ZoneX.yml");
            customConfigFile.getParentFile().mkdirs();
            try {
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
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getConfigValue(String key){
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
}
