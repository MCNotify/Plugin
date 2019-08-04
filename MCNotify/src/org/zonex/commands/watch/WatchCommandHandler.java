package org.zonex.commands.watch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.zonex.ZoneX;
import org.zonex.commands.AbstractCommand;
import org.zonex.subscriptions.Subscription;
import org.zonex.subscriptions.subscriptionevents.Events;

import java.util.ArrayList;
import java.util.List;

public class WatchCommandHandler extends AbstractCommand {
    
    public WatchCommandHandler(){
        super("watch", false);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        switch(args[0].toLowerCase()){
            case "list":
                ArrayList<Subscription> playerSubscriptions = new ArrayList<>();
                for(Subscription subscription : ZoneX.subscriptionManager.getSubscriptions()){
                    if(subscription.getSubscriber() == player){
                        playerSubscriptions.add(subscription);
                    }
                }

                if (playerSubscriptions.size() == 0) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " You are not subscribed to any events.");
                } else {
                    player.sendMessage(ChatColor.GOLD + "=== Subscription Id : Subscription Name ===");

                    for(Subscription subscription : playerSubscriptions){
                        player.sendMessage("" + ChatColor.GRAY + subscription.getSubscriptionId() + " : " + subscription.getEventType().toString());
                    }
                }
                break;
            case "help":
                player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   [ZoneX] /watch   " + ChatColor.GOLD + "========");
                player.sendMessage(ChatColor.GOLD + "<eventName>: " + ChatColor.GRAY + "Subscribes you to the event. See /watch events for a list of events you can watch.");
                player.sendMessage(ChatColor.GOLD + "list: " + ChatColor.GRAY + "Shows a list of all your subscriptions.");
                player.sendMessage(ChatColor.GOLD + "help: " + ChatColor.GRAY + "This information page.");
                return;
            case "remove":
                if(args.length != 2){
                    player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " You need to specify the subscription id to remove.");
                    return;
                }

                int subscriptionId;

                try {
                    subscriptionId = Integer.valueOf(args[1]);
                } catch (NumberFormatException e){

                    if(args[1].toLowerCase().equals("help")){
                        player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " This command stops a subscription that you are watching. Usage: /watch remove <subscriptionId>. This command requires a subcsriptionId (number) to remove. View your subscriptions with /zonex list to obtain the subscriptionId.");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Invalid subscriptionId. The subscriptionId must be a number relating to one of your subscriptions. See /zonex watch list");
                    }
                    return;
                }
                for(Subscription subscription : ZoneX.subscriptionManager.getPlayerSubscriptions(player)){
                    if(subscription.getSubscriptionId() == subscriptionId){
                        if(ZoneX.subscriptionManager.removeSubscription(subscription)){
                            player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " You have unsubscribed from watching " + subscription.getEventType().getDescription());
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " An error occured while try to unsubscribe.");
                        }
                        return;
                    }
                }
                break;
            default:
                //Try to get an event from their event name
                Events eventType = Events.fromCommand(args[0].toLowerCase());

                if (eventType == null) {
                    player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Event does not exist.");
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
                                case "areaName":
                                    subscriptionJson.put(key, ZoneX.areaManager.getArea(player.getUniqueId(), args[1 + i]).getAreaName());
                                    break;
                                case "watchedPlayer":
                                    Player targetPlayer = Bukkit.getPlayer(args[i + 1]);
                                    if(targetPlayer != null){
                                        subscriptionJson.put(key, targetPlayer.getUniqueId().toString());
                                    } else {
                                        player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " That player does not exist.");
                                        return;
                                    }
                                    break;
                                default:
                                    subscriptionJson.put(key, args[1 + i]);
                                    break;
                            }

                            i++;
                        }

                        if (ZoneX.subscriptionManager.addSubscription(new Subscription(player, eventType, subscriptionJson))) {
                            player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Successfully subscribed to " + eventType + " event.");
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Unable to subscribe to event.");
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
                        player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " The following arguments are required for this command: " + arguments);
                    }
                }
                break;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
