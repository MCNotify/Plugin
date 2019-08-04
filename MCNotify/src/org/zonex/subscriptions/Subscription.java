package org.zonex.subscriptions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zonex.subscriptions.subscriptionevents.Events;

import java.util.LinkedHashMap;
import java.util.Map;

public class Subscription implements ConfigurationSerializable {

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

    @Override
    public Map<String, Object> serialize() {

        LinkedHashMap result = new LinkedHashMap();
        result.put("subscriptionId", String.valueOf(subscriptionId));
        result.put("eventType", eventType.toString());
        result.put("subscriber", subscriber.getUniqueId().toString());
        result.put("subscriptionJson", subscriptionJson.toJSONString());

        return result;
    }

    public static Subscription deserialize(Map<String, Object> args){
        int subscriptionId = Integer.valueOf((String)args.get("subscriptionId"));
        Events eventType = Events.fromString((String)args.get("eventType"));
        OfflinePlayer subscriber = Bukkit.getOfflinePlayer((String)args.get("subscriber"));

        Object jsonobj = null;
        try {
            jsonobj = new JSONParser().parse((String)args.get("subscriptionJson"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) jsonobj;
        return new Subscription(subscriptionId, subscriber, eventType, jsonObject);
    }
}