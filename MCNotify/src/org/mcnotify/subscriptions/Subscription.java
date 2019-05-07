package org.mcnotify.subscriptions;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.mcnotify.subscriptions.subscriptionevents.Events;

public class Subscription {

    int subscriptionId;
    Events eventType;
    Player subscriber;
    JSONObject subscriptionJson;

    public Subscription(Player subscriber, Events eventType, JSONObject subscriptionData){
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionJson = subscriptionData;
    }

    public Subscription(int event_id, Player subscriber, Events eventType, JSONObject subscriptionData){
        this.subscriptionId = event_id;
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionJson = subscriptionData;
    }

    protected void pushnotification(){
        // Send a push notification to the player
    }

    public void onEvent(){
        this.pushnotification();
        this.subscriber.sendMessage(this.eventType.toString() + " event triggered!");
    }

    public Events getEventType() {
        return eventType;
    }

    public Player getSubscriber() {
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