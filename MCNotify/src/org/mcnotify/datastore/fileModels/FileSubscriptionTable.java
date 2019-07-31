package org.mcnotify.datastore.fileModels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mcnotify.datastore.baseModels.BaseSubscriptionModel;
import org.mcnotify.subscriptions.Subscription;
import org.mcnotify.subscriptions.subscriptionevents.Events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class FileSubscriptionTable extends BaseSubscriptionModel {

    private ConfigurationSection configurationSection;

    public FileSubscriptionTable(ConfigurationSection configurationSection){
        this.configurationSection = configurationSection;
    }

    @Override
    public ArrayList<Subscription> selectAll() {
        return null;
    }

    @Override
    public Subscription selectWhereUuid(String uuid) {
        return null;
    }

    @Override
    public boolean insert(Subscription subscription) {
        return false;
    }

    @Override
    public boolean delete(Subscription subscription) {
        return false;
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
