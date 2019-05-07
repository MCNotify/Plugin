package org.mcnotify.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.events.subscriptionevents.Events;
import org.mcnotify.events.subscriptions.Subscription;
import org.mcnotify.events.subscriptions.subscriptiondata.SubscriptionData;
import org.mcnotify.events.subscriptions.subscriptiondata.onPlayerJoinSubscriptionData;
import org.mcnotify.events.subscriptions.subscriptiondata.onPlayerMoveSubscriptionData;
import org.mcnotify.authenticator.Response;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventSubscriptionManager {

    ArrayList<Subscription> subscriptions = new ArrayList<>();

    public EventSubscriptionManager() throws SQLException {
        // Get a list of the online players and add their subscribed events to the list.
        this.loadDatabase();
    }

    private void loadDatabase() throws SQLException {
        System.out.println("[MCNotify] Loading subscriptions for online players (if any).");
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            // Get the player's UUID, get their subscriptions from the database.
            this.loadSubscriptions(player);
        }
        System.out.println("[MCNotify] Subscriptions loaded.");
    }

    public void loadSubscriptions(Player player) throws SQLException {
        ResultSet results = MCNotify.database.subscriptionTable().SelectWhereUuid(player.getUniqueId().toString());
        if(results != null) {
            while (results.next()) {
                int subscriptionId = results.getInt("subscription_id");
                String eventName = results.getString("event_name");
                String eventProperties = results.getString("event_properties");

                Events eventType = Events.valueOf(eventName);
                subscriptions.add(new Subscription(subscriptionId, player, eventType, eventProperties));
            }
        }
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

    public void addSubscription(Subscription subscription){
        int user_id = -1;


        //TODO Redo this

        try {
            // Insert a new subscription to the table
            JSONObject subscriptionRequestJson = new JSONObject();
            subscriptionRequestJson.put("uuid", subscription.getSubscriber().getUniqueId().toString());
            subscriptionRequestJson.put("event_name", subscription.getEventType().toString());
            subscriptionRequestJson.put("event_properties", subscription.getSubscriptionData().toJSON());

            // Send request
            Response subscriptionResponse = MCNotify.requestManager.sendRequest("POST", "subscriptions.php", subscriptionRequestJson.toJSONString());

            if(subscriptionResponse.getResponseCode() != 200){
                // Something went wrong.
                subscription.getSubscriber().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Error adding subscription.");
                return;
            }

            JSONObject responsejson = subscriptionResponse.getResponseBody();
            int subscription_id = Math.toIntExact((Long)responsejson.get("subscription_id"));
            subscription.setEventId(subscription_id);
            subscriptions.add(subscription);
            subscription.getSubscriber().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Successfully subscribed to event!");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void removeSubscription(Subscription subscription){

        //TODO Redo this

        try {
            // Insert a new subscription to the table
            JSONObject subscriptionRequestJson = new JSONObject();
            subscriptionRequestJson.put("subscription_id", subscription.getEventId());

            // Send request
            Response deleteResponse = MCNotify.requestManager.sendRequest("DELETE", "subscriptions.php", subscriptionRequestJson.toJSONString());

            if(deleteResponse.getResponseCode() != 200){
                subscription.getSubscriber().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Unable to unsubscribe from the event!");
            }

            if(subscriptions.contains(subscription)) {
                subscriptions.remove(subscription);
            }

            subscription.getSubscriber().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Successfully unsubscribed from event!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Subscription> getSubscriptions(){
        return this.subscriptions;
    }

}
