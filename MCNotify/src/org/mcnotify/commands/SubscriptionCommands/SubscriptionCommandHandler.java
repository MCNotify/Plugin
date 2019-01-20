package org.mcnotify.commands.SubscriptionCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcnotify.MCNotify;
import org.mcnotify.events.Events;
import org.mcnotify.events.subscriptions.Subscription;
import org.mcnotify.events.subscriptions.subscriptiondata.onPlayerJoinSubscriptionData;
import org.mcnotify.events.subscriptions.subscriptiondata.onPlayerMoveSubscriptionData;

public class SubscriptionCommandHandler extends CommandHandler{

    @Override
    public void onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        switch(args[0].toLowerCase()){
            case "onplayermove":
                MCNotify.eventSubscriptionManager.addSubscription(new Subscription((Player)commandSender, Events.ON_PLAYER_MOVE, new onPlayerMoveSubscriptionData()));
                break;
            case "onplayerjoin":
                if(args.length == 2) {
                    String playername = args[1];
                    System.out.println(playername);
                    Player watchedPlayer = Bukkit.getServer().getPlayer(playername);

                    MCNotify.eventSubscriptionManager.addSubscription(new Subscription((Player) commandSender, Events.ON_PLAYER_JOIN, new onPlayerJoinSubscriptionData(watchedPlayer)));

                    commandSender.sendMessage("Successfully subscribed to event");

                } else {
                    commandSender.sendMessage("You need to specify a player name!");
                }
        }
    }
}
