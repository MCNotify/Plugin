package org.mcnotify.commands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.Subscription;

import java.util.ArrayList;

public class EventCommandHandler extends CommandHandler {
    @Override
    public void onCommand(Player player, Command command, String s, String[] args) {
        switch(args[1].toLowerCase()){
            case "list":
                ArrayList<Subscription> playerSubscriptions = new ArrayList<>();
                for(Subscription subscription : MCNotify.eventSubscriptionManager.getSubscriptions()){
                    if(subscription.getSubscriber() == player){
                        playerSubscriptions.add(subscription);
                    }
                }

                if (playerSubscriptions.size() == 0) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You are not subscribed to any events.");
                } else {
                    player.sendMessage(ChatColor.GOLD + "=== Subscription Id : Subscription Name ===");

                    for(Subscription subscription : playerSubscriptions){
                        player.sendMessage("" + ChatColor.GRAY + subscription.getSubscriptionId() + " : " + subscription.getEventType().toString());
                    }
                }
                break;
            case "remove":
                if(args.length != 3){
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You need to specify the subscription id to remove.");
                    return;
                }

                int subscriptionId = Integer.valueOf(args[2]);
                for(Subscription subscription : MCNotify.eventSubscriptionManager.getPlayerSubscriptions(player)){
                    if(subscription.getSubscriptionId() == subscriptionId){
                        MCNotify.eventSubscriptionManager.removeSubscription(subscription);
                        return;
                    }
                }
                break;
        }
    }
}
