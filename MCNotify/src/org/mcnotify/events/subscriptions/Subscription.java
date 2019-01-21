package org.mcnotify.events.subscriptions;

import org.bukkit.entity.Player;
import org.mcnotify.events.subscriptionevents.Events;
import org.mcnotify.events.subscriptions.subscriptiondata.SubscriptionData;

public class Subscription {

    int event_id;
    Events eventType;
    Player subscriber;
    SubscriptionData subscriptionData;

    public Subscription(Player subscriber, Events eventType, SubscriptionData subscriptionData){
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionData = subscriptionData;
    }

    public Subscription(int event_id, Player subscriber, Events eventType, SubscriptionData subscriptionData){
        this.event_id = event_id;
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionData = subscriptionData;
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

    public SubscriptionData getSubscriptionData(){
        return subscriptionData;
    }

    public int getEventId(){
        return event_id;
    }

    public void setEventId(int id){
        this.event_id = id;
    }
}
