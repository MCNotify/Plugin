package org.mcnotify.commands.commands;

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
                    subscriptionJson.put(key, args[1 + i]);
                    i++;
                }

                if (MCNotify.eventSubscriptionManager.addSubscription(new Subscription(player, eventType, subscriptionJson))) {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Successfully subscribed to " + eventType + " event.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Unable to subscribe to event.");
                }
                return;

            } else {
                String arguments = "";
                for (String string : eventType.getJsonKeys()) {
                    arguments += string = " ";
                }
                player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " The following arguments are required for this command: " + arguments);
            }
        }
    }
}
