package org.mcnotify.commands.subscriptioncommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mcnotify.MCNotify;
import org.mcnotify.events.subscriptionevents.Events;
import org.mcnotify.events.subscriptions.Subscription;
import org.mcnotify.events.subscriptions.subscriptiondata.onPlayerJoinSubscriptionData;

public class SubscriptionCommandHandler extends CommandHandler{

    @Override
    public void onCommand(Player player, Command command, String s, String[] args) {
        switch(args[0].toLowerCase()){
            case "onplayermove":
                break;
            case "onplayerjoin":
                if(args.length == 2) {
                    String playername = args[1];
                    System.out.println(playername);
                    Player watchedPlayer = Bukkit.getServer().getPlayer(playername);

                    MCNotify.eventSubscriptionManager.addSubscription(new Subscription(player, Events.ON_PLAYER_JOIN, new onPlayerJoinSubscriptionData(watchedPlayer)));

                    player.sendMessage("Successfully subscribed to event");

                } else {
                    player.sendMessage("You need to specify a player name!");
                }
        }
    }
}
