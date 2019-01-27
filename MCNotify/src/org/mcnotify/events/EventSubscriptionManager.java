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
import org.mcnotify.utility.Response;

import java.io.IOException;
import java.util.ArrayList;

public class EventSubscriptionManager {

    ArrayList<Subscription> subscriptions = new ArrayList<>();

    public EventSubscriptionManager(){
        // Get a list of the online players and add their subscribed events to the list.
        this.loadDatabase();
    }

    private void loadDatabase(){
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            // Get the player's UUID, get their subscriptions from the database.
            this.loadSubscriptions(player);
        }
    }

    public void loadSubscriptions(Player player){
        System.out.println("[MCNotify] Loading subscriptions for online players....");
        try {
            // Configure the GET endpoint
            String endpoint = "subscriptions.php?";
            endpoint += "uuid=" + player.getUniqueId().toString();

            Response subscriptionResponse = MCNotify.requestManager.sendRequest("GET", endpoint, null);

            JSONObject subscriptions = subscriptionResponse.getResponseBody();
            JSONArray array = (JSONArray) subscriptions.get("events");

            for(Object eventObject : array){
                JSONObject eventJson = (JSONObject) eventObject;

                // Parse the values of the json object
                Events eventName = Events.valueOf((String) eventJson.get("event_name"));
                int event_id = Math.toIntExact((Long)eventJson.get("subscription_id"));
                String subscriptionData = (String) eventJson.get("event_properties");

                // Determine the subscription data
                SubscriptionData subData = null;

                switch(eventName){
                    case ON_PLAYER_JOIN:
                        subData = new onPlayerJoinSubscriptionData().fromJSON(subscriptionData);
                        break;
                    case ON_PLAYER_MOVE:
                        subData = new onPlayerMoveSubscriptionData().fromJSON(subscriptionData);
                        break;
                }

                Subscription subscription = new Subscription(event_id, player, eventName, subData);

                this.subscriptions.add(subscription);

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        System.out.println("[MCNotify] Loaded subscriptions.");
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
