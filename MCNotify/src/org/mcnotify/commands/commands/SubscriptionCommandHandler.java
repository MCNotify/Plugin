package org.mcnotify.commands.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.subscriptionevents.Events;
import org.mcnotify.subscriptions.Subscription;

import java.util.ArrayList;

public class SubscriptionCommandHandler extends CommandHandler {

    @Override
    public void onCommand(Player player, Command command, String s, String[] args) {
        switch(args[1].toLowerCase()){
            case "list":
                ArrayList<Subscription> playerSubscriptions = new ArrayList<>();
                for(Subscription subscription : MCNotify.subscriptionManager.getSubscriptions()){
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
                for(Subscription subscription : MCNotify.subscriptionManager.getPlayerSubscriptions(player)){
                    if(subscription.getSubscriptionId() == subscriptionId){
                        MCNotify.subscriptionManager.removeSubscription(subscription);
                        return;
                    }
                }
                break;
            default:
                //Try to get an event from their event name
                Events eventType = Events.fromCommand(args[1].toLowerCase());

                if (eventType == null) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Event does not exist.");
                    return;
                } else {
                    // Determine how many arguments are required.
                    int argLength = eventType.getJsonKeys().length;

                    if (args.length == argLength + 2) {
                        int i = 0;
                        JSONObject subscriptionJson = new JSONObject();
                        // Generate JSON data for the command
                        for (String key : eventType.getJsonKeys()) {

                            switch(key){
                                case "areaName":
                                    subscriptionJson.put(key, MCNotify.areaManager.getArea(player.getUniqueId(), args[2 + i]).getAreaName());
                                    break;
                                case "watchedPlayer":
                                    Player targetPlayer = Bukkit.getPlayer(args[i + 2]);
                                    if(targetPlayer != null){
                                        subscriptionJson.put(key, targetPlayer.getUniqueId().toString());
                                    } else {
                                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " That player does not exist.");
                                        return;
                                    }
                                    break;
                                default:
                                    subscriptionJson.put(key, args[2 + i]);
                                    break;
                            }

                            i++;
                        }

                        if (MCNotify.subscriptionManager.addSubscription(new Subscription(player, eventType, subscriptionJson))) {
                            player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Successfully subscribed to " + eventType + " event.");
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Unable to subscribe to event.");
                        }
                        return;

                    } else {
                        String arguments = "<";
                        for (String string : eventType.getPlayerFriendlyKeyNames()) {
                            if(eventType.getPlayerFriendlyKeyNames().length > 1) {
                                arguments += string + ", ";
                            } else {
                                arguments += string;
                            }
                        }
                        arguments = arguments.trim();
                        arguments += ">";
                        player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " The following arguments are required for this command: " + arguments);
                    }
                }
                break;
        }

    }
}
