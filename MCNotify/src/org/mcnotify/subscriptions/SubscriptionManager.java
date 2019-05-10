package org.mcnotify.subscriptions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.subscriptionevents.Events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SubscriptionManager {

    ArrayList<Subscription> subscriptions = new ArrayList<>();

    public SubscriptionManager() throws SQLException {
        // Get a list of the online players and add their subscribed events to the list.
        this.loadDatabase();
    }

    private void loadDatabase() throws SQLException {
        System.out.println("[MCNotify] Loading subscriptions for online players (if any).");

        ResultSet results = MCNotify.database.subscriptionTable().selectAll();
        if(results != null) {
            while (results.next()) {
                int subscriptionId = results.getInt("id");
                String subsciberUuid = results.getString("uuid");
                String eventName = results.getString("event_name");
                String eventProperties = results.getString("event_properties");


                Player subscriber = null;

                if(subsciberUuid != null && subsciberUuid != "") {
                    UUID uuid = UUID.fromString(subsciberUuid);
                    if(uuid != null) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
                        if (op != null) {
                            subscriber = (Player) op;
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

                subscriptions.add(new Subscription(subscriptionId, subscriber, Events.valueOf(eventName), new JSONObject(jsonObject)));
            }
        }

        System.out.println("[MCNotify] Subscriptions loaded.");
    }

    public ArrayList<Subscription> getPlayerSubscriptions(Player player){
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        for(Subscription subscription : this.subscriptions){
            if(subscription.getSubscriber() == player){
                subscriptions.add(subscription);
            }
        }
        return subscriptions;
    }

    public boolean addSubscription(Subscription subscription){
        if(MCNotify.database.subscriptionTable().insert(subscription)){
            subscriptions.add(subscription);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeSubscription(Subscription subscription){
        if(MCNotify.database.subscriptionTable().delete(subscription)){
            subscriptions.remove(subscription);
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Subscription> getSubscriptions(){
        return this.subscriptions;
    }

    public ArrayList<Subscription> getSubscriptions(Events eventType){
        ArrayList<Subscription> eventSubscriptions = new ArrayList<>();
        for(Subscription s : this.subscriptions){
            if(s.eventType == eventType){
                eventSubscriptions.add(s);
            }
        }
        return eventSubscriptions;
    }

}
