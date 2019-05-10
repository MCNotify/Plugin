package org.mcnotify.subscriptions;

import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.mcnotify.subscriptions.subscriptionevents.Events;

public class Subscription {

    int subscriptionId;
    Events eventType;
    OfflinePlayer subscriber;
    JSONObject subscriptionJson;

    public Subscription(OfflinePlayer subscriber, Events eventType, JSONObject subscriptionData){
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionJson = subscriptionData;
    }

    public Subscription(int event_id, OfflinePlayer subscriber, Events eventType, JSONObject subscriptionData){
        this.subscriptionId = event_id;
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionJson = subscriptionData;
    }

    private void pushnotification(){
        // Send a push notification to the player
    }

    public void onEvent(){
        if(this.subscriber.isOnline()) {
            this.subscriber.getPlayer().sendMessage(this.eventType.toString() + " event triggered!");
        } else {
            this.pushnotification();
        }
    }

    public Events getEventType() {
        return eventType;
    }

    public OfflinePlayer getSubscriber() {
        return subscriber;
    }

    public JSONObject getSubscriptionJson(){
        return subscriptionJson;
    }

    public int getSubscriptionId(){
        return subscriptionId;
    }

    public void setSubscriptionId(int id){
        this.subscriptionId = id;
    }
}