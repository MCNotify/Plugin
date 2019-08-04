package org.zonex.subscriptions;

import org.bukkit.entity.Player;
import org.zonex.ZoneX;
import org.zonex.subscriptions.subscriptionevents.Events;

import java.sql.SQLException;
import java.util.ArrayList;

public class SubscriptionManager {

    ArrayList<Subscription> subscriptions = new ArrayList<>();

    public SubscriptionManager() throws SQLException {
        // Get a list of the online players and add their subscribed events to the list.
        this.loadDatabase();
    }

    private void loadDatabase() throws SQLException {

        System.out.println("[MCNotify] Loading subscriptions...");

        this.subscriptions = ZoneX.datastore.subscriptionTable().selectAll();

        System.out.println("[MCNotify] " + String.valueOf(subscriptions.size()) + " Subscriptions loaded.");
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
        if (ZoneX.datastore.subscriptionTable().insert(subscription)) {
            subscriptions.add(subscription);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeSubscription(Subscription subscription){
        if (ZoneX.datastore.subscriptionTable().delete(subscription)) {
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
