package org.mcnotify.database.dao;

import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SubscriptionTable {

    public ResultSet selectAll(){
        PreparedStatement statement = null;
        try {
            statement = MCNotify.database.getConnection().prepareStatement("SELECT * FROM subscriptions");
            ResultSet results = statement.executeQuery();

            if(results != null){
                return results;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet selectWhereUuid(String uuid) {
        PreparedStatement statement = null;
        try {
            statement = MCNotify.database.getConnection().prepareStatement("SELECT * FROM subscriptions WHERE uuid = ?");

            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();

            if(results != null){
                return results;
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
            statement = MCNotify.database.getConnection().prepareStatement("INSERT INTO subscriptions (uuid, event_name, event_properties) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

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
            statement = MCNotify.database.getConnection().prepareStatement("DELETE FROM subscriptions WHERE id = ?");

            statement.setInt(1, subscription.getSubscriptionId());

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
