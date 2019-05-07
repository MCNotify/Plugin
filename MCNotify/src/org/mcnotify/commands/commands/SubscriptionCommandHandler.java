package org.mcnotify.commands.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.mcnotify.MCNotify;
import org.mcnotify.subscriptions.subscriptionevents.Events;
import org.mcnotify.subscriptions.Subscription;

public class SubscriptionCommandHandler extends CommandHandler {

    @Override
    public void onCommand(Player player, Command command, String s, String[] args) {

        //Try to get an event from their event name
        Events eventType = Events.fromCommand(args[0].toLowerCase());

        if (eventType == null) {
            player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Event does not exist.");
            return;
        } else {
            // Determine how many arguments are required.
            int argLength = eventType.getJsonKeys().length;

            if (args.length == argLength + 1) {
                int i = 0;
                JSONObject subscriptionJson = new JSONObject();
                // Generate JSON data for the command
                for (String key : eventType.getJsonKeys()) {

                    switch(key){
                        case "areaId":
                            subscriptionJson.put(key, MCNotify.areaManager.getPlayerNamedArea(player, args[1 + i]).getAreaId());
                            break;
                        case "watchedPlayer":
                            Player targetPlayer = Bukkit.getPlayer(args[i + 1]);
                            if(targetPlayer != null){
                                subscriptionJson.put(key, targetPlayer.getUniqueId().toString());
                            } else {
                                player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " That player does not exist.");
                                return;
                            }
                            break;
                        default:
                            subscriptionJson.put(key, args[1 + i]);
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
    }
}
