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

/**
 * A subscription defines an in game event that a player is listening to and would like to be informed about.
 */
public class Subscription {

    /**
     * The id of the subscription
     */
    private int subscriptionId;

    /**
     * The type of event the player is listening for
     */
    private Events eventType;

    /**
     * The player listening to the event
     */
    OfflinePlayer subscriber;

    /**
     * Details about the event that the player is listening to
     */
    JSONObject subscriptionJson;

    /**
     * Constructor to create a new subscription to an ingame event.
     * @param subscriber The player subscribing to the event
     * @param eventType The type of event the player is subscribing to
     * @param subscriptionData Additional information about the subscription
     */
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

    /**
     * Creates a subscription from a database entry with a specified id.
     * @param event_id The id of the subscription
     * @param subscriber The player subscribing to the event
     * @param eventType The type of event that the player is listening to
     * @param subscriptionData Additional information about the event
     */
    public Subscription(int event_id, OfflinePlayer subscriber, Events eventType, JSONObject subscriptionData){
        this.subscriptionId = event_id;
        this.subscriber = subscriber;
        this.eventType = eventType;
        this.subscriptionJson = subscriptionData;
    }

    /**
     * Called when the event the player is listening to gets triggered. This method invokes notifications to be sent.
     * @param eventMessage A player friendly message that details the event that has occured.
     */
    public void onEvent(String eventMessage){
        if(this.subscriber.isOnline()) {
            ZoneX.communicationManager.sendIngameMessage(this.subscriber.getPlayer(), eventMessage);
            ZoneX.communicationManager.sendCommunications(this.subscriber.getPlayer(), eventMessage);
        } else {
            ZoneX.communicationManager.sendCommunications(this.subscriber.getPlayer(), eventMessage);
        }
    }

    /**
     * Gets the type of event being watched for
     * @return The type of event being watched for
     */
    public Events getEventType() {
        return eventType;
    }

    /**
     * Gets the player watching the event
     * @return Gets the player watching the event
     */
    public OfflinePlayer getSubscriber() {
        return subscriber;
    }

    /**
     * Gets additional details about the subscription
     * @return Additional details about the subscribed event
     */
    public JSONObject getSubscriptionJson(){
        return subscriptionJson;
    }

    /**
     * The subscription id for the event.
     * @return The id of the subscription
     */
    public int getSubscriptionId(){
        return subscriptionId;
    }

    /**
     * Sets the id for the subscription. Called after the row is inserted to a datastore.
     * @param id The id of the subscription
     */
    public void setSubscriptionId(int id){
        this.subscriptionId = id;
    }

    /**
     * Gets a player friendly description of the event that was triggered.
     * @return A player friendly string detailing the event they are watching for
     */
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
            case ON_PLAYER_DEATH:
            case ON_PLAYER_ENTER_END:
            case ON_PLAYER_ENTER_NETHER:
            case ON_PLAYER_ENTER_WORLD:
                String[] playerArgs = { (String)this.subscriptionJson.get("watchedPlayer") };
                return this.eventType.getPlayerFieldlyDescription(playerArgs);
        }
        return "";
    }

    /**
     * Serializes the object into a JSON string for flat file storage.
     * @return A JSON string for flat file storage.
     */
    public String serialize() {

        JSONObject result = new JSONObject();

        result.put("subscriptionId", String.valueOf(subscriptionId));
        result.put("eventType", eventType.toString());
        result.put("subscriber", subscriber.getUniqueId().toString());
        result.put("subscriptionJson", subscriptionJson.toJSONString());

        return result.toJSONString();
    }

    /**
     * Decodes a JSON string into an instance of a subscription.
     * @param serialized JSON serialized string representing a subscription
     * @return The represented Subscription object.
     * @throws ParseException If the JSON cannot be parsed and stored, a ParseException is thrown.
     */
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