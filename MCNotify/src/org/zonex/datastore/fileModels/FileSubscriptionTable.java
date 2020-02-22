package org.zonex.datastore.fileModels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zonex.ZoneX;
import org.zonex.datastore.baseModels.BaseSubscriptionModel;
import org.zonex.subscriptions.Subscription;
import org.zonex.subscriptions.SubscriptionManager;
import org.zonex.subscriptions.subscriptionevents.Events;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Flat file storage for player subscription events
 */
public class FileSubscriptionTable extends BaseSubscriptionModel {

    private ConfigurationSection configurationSection;
    private YamlConfiguration yamlConfiguration;
    private File file;

    public FileSubscriptionTable(YamlConfiguration yaml, File file){
        this.configurationSection = yaml.getConfigurationSection("subscriptions");
        this.yamlConfiguration = yaml;
        this.file = file;
    }

    @Override
    public ArrayList<Subscription> selectAll() {
        ArrayList<Subscription> subscriptionList = new ArrayList<>();
        if(configurationSection != null) {
            for (String uuid : configurationSection.getKeys(false)) {
                for(String key : configurationSection.getConfigurationSection(uuid).getKeys(false)) {
                    try {
                        subscriptionList.add((Subscription.deserialize((String) configurationSection.get(uuid + "." + key))));
                    } catch (ParseException e) {
                        // Unable to parse the subscription
                        System.out.println("[ZoneX] unable to parse Subscription JSON at:" + uuid + "." + key);
                        return subscriptionList;
                    }
                }
            }
        }
        return subscriptionList;
    }

    @Override
    public ArrayList<Subscription> selectWhereUuid(String uuid) {
        ArrayList<Subscription> subscriptionList = new ArrayList<>();
        if(configurationSection != null) {
            if (configurationSection.getConfigurationSection(uuid) != null) {
                for (String key : configurationSection.getConfigurationSection(uuid).getKeys(false)) {
                    Subscription subscription = null;
                    try {
                        subscription = (Subscription.deserialize((String)configurationSection.get(uuid + "." + key)));
                    } catch (ParseException e) {
                        // Unable to parse the subscription
                        System.out.println("[ZoneX] unable to parse Subscription JSON at:" + uuid + "." + key);
                        return subscriptionList;
                    }
                    subscriptionList.add(subscription);
                }
            }
        }
        return subscriptionList;
    }

    @Override
    public boolean insert(Subscription subscription) {
        configurationSection.set(subscription.getSubscriber().getUniqueId().toString() + "." + String.valueOf(subscription.getSubscriptionId()), subscription.serialize());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean delete(Subscription subscription) {
        configurationSection.set(subscription.getSubscriber().getUniqueId().toString() + "." + String.valueOf(subscription.getSubscriptionId()), null);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
