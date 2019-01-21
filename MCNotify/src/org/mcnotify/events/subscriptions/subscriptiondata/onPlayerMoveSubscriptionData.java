package org.mcnotify.events.subscriptions.subscriptiondata;

import org.mcnotify.utility.Polygon;

public class onPlayerMoveSubscriptionData extends SubscriptionData {

    Polygon polygon;

    @Override
    public SubscriptionData fromJSON(String json) {
        return this;
    }

    @Override
    public String toJSON() {
        return "";
    }
}
