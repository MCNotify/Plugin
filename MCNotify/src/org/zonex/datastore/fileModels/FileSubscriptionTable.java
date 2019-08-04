package org.zonex.datastore.fileModels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zonex.datastore.baseModels.BaseSubscriptionModel;
import org.zonex.subscriptions.Subscription;
import org.zonex.subscriptions.subscriptionevents.Events;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

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
                    subscriptionList.add(((Subscription) (configurationSection.get(uuid + "." + key))));
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
                    Subscription subscription = ((Subscription) (configurationSection.get(uuid + "." + key)));
                    subscriptionList.add(subscription);
                }
            }
        }
        return subscriptionList;
    }

    @Override
    public boolean insert(Subscription subscription) {
        configurationSection.set(subscription.getSubscriber().getUniqueId().toString() + "." + String.valueOf(subscription.getSubscriptionId()), subscription);
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

    private Subscription getSubscription(ConfigurationSection resultSet) throws SQLException {
        int subscriptionId = resultSet.getInt("id");
        String subsciberUuid = resultSet.getString("uuid");
        String eventName = resultSet.getString("event_name");
        String eventProperties = resultSet.getString("event_properties");


        OfflinePlayer subscriber = null;

        if(subsciberUuid != null && subsciberUuid != "") {
            UUID uuid = UUID.fromString(subsciberUuid);
            if(uuid != null) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
                if (op != null) {
                    subscriber = op;
                }
            }
        }

        Object jsonobj = null;
        try {
            jsonobj = new JSONParser().parse(eventProperties);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) jsonobj;

        return new Subscription(subscriptionId, subscriber, Events.valueOf(eventName), new JSONObject(jsonObject));
    }
}
