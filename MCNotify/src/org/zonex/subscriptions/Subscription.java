package org.zonex.subscriptions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zonex.ZoneX;
import org.zonex.subscriptions.subscriptionevents.Events;


import java.util.ArrayList;
import java.util.UUID;

public class Subscription {

    int subscriptionId;
    Events eventType;
    OfflinePlayer subscriber;
    JSONObject subscriptionJson;

    public Subscription(OfflinePlayer subscriber, Events eventType, JSONObject subscriptionData){
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionJson = subscriptionData;

        // Determine the id of the subscription
        ArrayList<Subscription> playerSubscriptions = ZoneX.subscriptionManager.getPlayerSubscriptions(subscriber.getPlayer());
        if(playerSubscriptions.size() > 0) {
            int lastSubscriptionId = playerSubscriptions.get(playerSubscriptions.size() - 1).getSubscriptionId();
            this.subscriptionId = lastSubscriptionId + 1;
        } else {
            this.subscriptionId = 1;
        }
    }

    public Subscription(int event_id, OfflinePlayer subscriber, Events eventType, JSONObject subscriptionData){
        this.subscriptionId = event_id;
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionJson = subscriptionData;
    }

    public void onEvent(String eventMessage){
        if(this.subscriber.isOnline()) {
            ZoneX.communicationManager.sendIngameMessage(this.subscriber.getPlayer(), eventMessage);
            ZoneX.communicationManager.sendCommunications(this.subscriber.getPlayer(), eventMessage);
        } else {
            ZoneX.communicationManager.sendCommunications(this.subscriber.getPlayer(), eventMessage);
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

    public String getPlayerFriendlyDescription(){
        switch(this.eventType){
            case ON_BLOCK_BREAK:
            case ON_BLOCK_EXPLODE:
            case ON_CROP_GROWN:
            case ON_PLAYER_MOVE:
            case ON_REDSTONE_ACTIVE:
            // case ON_HOPPER_FULL:
            // case ON_MOB_LIMIT:
                String[] areaArgs = { (String)this.subscriptionJson.get("areaName") };
                return this.eventType.getPlayerFieldlyDescription(areaArgs);
            case ON_PLAYER_JOIN:
            // case ON_PLAYER_DEATH:
            // case ON_PLAYER_ENTER_END:
            // case ON_PLAYER_ENTER_NETHER:
            // case ON_PLAYER_ENTER_WORLD:
                String[] playerArgs = { (String)this.subscriptionJson.get("watchedPlayer") };
                return this.eventType.getPlayerFieldlyDescription(playerArgs);
        }
        return "";
    }

    public String serialize() {

        JSONObject result = new JSONObject();

        result.put("subscriptionId", String.valueOf(subscriptionId));
        result.put("eventType", eventType.toString());
        result.put("subscriber", subscriber.getUniqueId().toString());
        result.put("subscriptionJson", subscriptionJson.toJSONString());

        return result.toJSONString();
    }

    public static Subscription deserialize(String serialized) throws ParseException {
        Object deserialized = new JSONParser().parse(serialized);
        JSONObject deserializedJson = (JSONObject) deserialized;
        int subscriptionId = Integer.valueOf((String)deserializedJson.get("subscriptionId"));
        Events eventType = Events.fromString((String)deserializedJson.get("eventType"));
        OfflinePlayer subscriber = Bukkit.getOfflinePlayer(UUID.fromString(((String)deserializedJson.get("subscriber"))));

        Object jsonobj = null;
        try {
            jsonobj = new JSONParser().parse((String)deserializedJson.get("subscriptionJson"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) jsonobj;
        return new Subscription(subscriptionId, subscriber, eventType, jsonObject);
    }
}