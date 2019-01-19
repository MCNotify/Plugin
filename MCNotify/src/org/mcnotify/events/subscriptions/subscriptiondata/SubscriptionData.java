package org.mcnotify.events.subscriptions.subscriptiondata;

public abstract class SubscriptionData {

    public abstract SubscriptionData fromJSON(String json);

    public abstract String toJSON();

}
