package org.zonex.subscriptions;

import org.bukkit.entity.Player;
import org.zonex.ZoneX;
import org.zonex.subscriptions.subscriptionevents.Events;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Subscription manager class manages a player's subscriptions to in game events.
 */
public class SubscriptionManager {

    ArrayList<Subscription> subscriptions = new ArrayList<>();

    /**
     * Loads all of the subscriptions from the datastore.
     */
    public SubscriptionManager() {
        // Get a list of the online players and add their subscribed events to the list.
        this.loadDatabase();
    }

    /**
     * Loads all of the subscriptions that have been saved to the user's storage.
     */
    private void loadDatabase() {

        System.out.println("[ZoneX] Loading subscriptions...");

        this.subscriptions = ZoneX.datastore.subscriptionTable().selectAll();

        System.out.println("[ZoneX] " + String.valueOf(subscriptions.size()) + " Subscriptions loaded.");
    }

    /**
     * Gets a list of all of the player's subscriptions.
     * @param player The player to get subscriptions for
     * @return A list of the player's subscriptions
     */
    public ArrayList<Subscription> getPlayerSubscriptions(Player player){
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        for(Subscription subscription : this.subscriptions){
            if(subscription.getSubscriber().getUniqueId().equals(player.getUniqueId())){
                subscriptions.add(subscription);
            }
        }
        return subscriptions;
    }

    /**
     * Adds a subscription to the subscription manager and saves the subscription to the datastore
     * @param subscription The subscription to add to the subscription manager
     * @return If the subscription was successfully saved to the datastore
     */
    public boolean addSubscription(Subscription subscription){
        if (ZoneX.datastore.subscriptionTable().insert(subscription)) {
            subscriptions.add(subscription);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a subscription from the subscription manager and datastore
     * @param subscription The subscription to remove
     * @return If the subscription was successfully removed.
     */
    public boolean removeSubscription(Subscription subscription){
        if (ZoneX.datastore.subscriptionTable().delete(subscription)) {
            subscriptions.remove(subscription);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a list of all subscribed events
     * @return A list of all subscribed events
     */
    public ArrayList<Subscription> getSubscriptions(){
        return this.subscriptions;
    }

    /**
     * Returns a list of all subscriptions of the given event type.
     * @param eventType The type of event to find subscriptions for
     * @return A list of all subscriptions of the given event type.
     */
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
