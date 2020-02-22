package org.zonex.datastore.dbModels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zonex.datastore.baseModels.BaseSubscriptionModel;
import org.zonex.subscriptions.Subscription;
import org.zonex.subscriptions.subscriptionevents.Events;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Database storage for player subscription events
 */
public class DbSubscriptionTable extends BaseSubscriptionModel {

    private Connection connection;

    public DbSubscriptionTable(Connection connection){
        this.connection = connection;
    }

    public ArrayList<Subscription> selectAll(){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM subscriptions");
            ResultSet results = statement.executeQuery();

            if(results != null){
                ArrayList<Subscription> subscriptions = new ArrayList<>();
                while (results.next()) {
                    subscriptions.add(this.getSubscription(results));
                }
                return subscriptions;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Subscription> selectWhereUuid(String uuid) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM subscriptions WHERE uuid = ?");

            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();

            if(results != null){
                ArrayList<Subscription> subscriptions = new ArrayList<>();
                while (results.next()) {
                    subscriptions.add(this.getSubscription(results));
                }
                return subscriptions;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(Subscription subscription){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO subscriptions (uuid, event_name, event_properties) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, subscription.getSubscriber().getUniqueId().toString());
            statement.setString(2, subscription.getEventType().toString());
            statement.setString(3, subscription.getSubscriptionJson().toJSONString());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                return false;
            } else {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if(generatedKeys.next()){
                    subscription.setSubscriptionId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Subscription subscription){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM subscriptions WHERE id = ?");

            statement.setInt(1, subscription.getSubscriptionId());

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Subscription getSubscription(ResultSet resultSet) throws SQLException {
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
