package org.mcnotify.events.subscriptionevents;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.events.subscriptions.Subscription;
import org.mcnotify.events.subscriptions.subscriptiondata.onPlayerJoinSubscriptionData;
import org.mcnotify.authenticator.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class onPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent loginEvent){
        // Check subscriptions
        for(Subscription subscription : MCNotify.eventSubscriptionManager.getSubscriptions()){
            if(subscription.getEventType() == Events.ON_PLAYER_JOIN){
                onPlayerJoinSubscriptionData subdata = (onPlayerJoinSubscriptionData) subscription.getSubscriptionData();
                if(subdata.getWatchedPlayer() == loginEvent.getPlayer()) {
                    subscription.onEvent();
                }
            }
        }

        loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Welcome to the server! MCNotify lets you receive push notifications on your mobile device.");
        loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Download the app here: " + ChatColor.GREEN + "MCNotify downloadLink");
        if(MCNotify.auth.getSubscriptionLevel() == 0) {
            loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "This server is using a free version of MCNotify. If you would like notifications on your mobile device, contact your admins!");
        } else {
            // TODO
            // Lookup the user's verification code.
            loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Your verification code is: <TODO>");
        }

        // Check if the player has any subscriptions and load them
        try {
            MCNotify.eventSubscriptionManager.loadSubscriptions(loginEvent.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Check if the player has any areas and load them
        try {
            MCNotify.areaManager.loadAreas(loginEvent.getPlayer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
