package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.zonex.ZoneX;
import org.zonex.subscriptions.Subscription;

public class onPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent loginEvent){
        // Check subscriptions
        for(Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_PLAYER_JOIN)){

            Player watchedPlayer = Bukkit.getPlayer((String)subscription.getSubscriptionJson().get("watchedPlayer"));

            if(watchedPlayer == loginEvent.getPlayer()) {
                subscription.onEvent();
            }
        }

        loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Welcome to the server! MCNotify lets you receive push notifications on your mobile device.");
        loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Download the app here: " + ChatColor.GREEN + "MCNotify downloadLink");
        if(ZoneX.auth.getSubscriptionLevel() == 0) {
            loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "This server is using a free version of MCNotify. If you would like notifications on your mobile device, contact your admins!");
        } else {
            // TODO
            // Lookup the user's verification code.
            loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Your verification code is: <TODO>");
        }
    }
}
