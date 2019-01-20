package org.mcnotify.events.subscriptions.subscriptiondata;

public class onPlayerMoveSubscriptionData extends SubscriptionData {
    @Override
    public SubscriptionData fromJSON(String json) {
        return this;
    }

    @Override
    public String toJSON() {
        return "";
    }
}
