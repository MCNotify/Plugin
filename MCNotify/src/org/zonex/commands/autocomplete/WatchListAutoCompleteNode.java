package org.zonex.commands.autocomplete;

import org.bukkit.entity.Player;
import org.zonex.ZoneX;
import org.zonex.areas.Area;
import org.zonex.subscriptions.Subscription;

import java.util.ArrayList;
import java.util.List;

public class WatchListAutoCompleteNode extends AutoCompleteNode {
    @Override
    public boolean matches(String s) {
        return true;
    }

    @Override
    public List<String> getSuggestedCommands(Player player, String s) {
        List<String> subscriptions = new ArrayList<>();
        for(Subscription subscription : ZoneX.subscriptionManager.getPlayerSubscriptions(player)){
            if(String.valueOf(subscription.getSubscriptionId()).startsWith(s)){
                subscriptions.add(String.valueOf(subscription.getSubscriptionId()));
            }
        }
        return subscriptions;
    }
}
