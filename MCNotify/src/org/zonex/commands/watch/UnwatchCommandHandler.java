package org.zonex.commands.watch;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zonex.ZoneX;
import org.zonex.commands.AbstractCommand;
import org.zonex.subscriptions.Subscription;

import java.util.List;

public class UnwatchCommandHandler extends AbstractCommand {

    public UnwatchCommandHandler(){
        super("unwatch", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length != 1){
            player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " You need to specify the subscription id to remove.");
            return;
        }

        int subscriptionId;

        try {
            subscriptionId = Integer.valueOf(args[0]);
        } catch (NumberFormatException e){

            if(args[0].toLowerCase().equals("help")){
                player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " This command stops a subscription that you are watching. Usage: /unwatch <subscriptionId>. This command requires a subcsriptionId (number) to remove. View your subscriptions with /watch list to obtain the subscriptionId.");
            } else {
                player.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + " Invalid subscriptionId. The subscriptionId must be a number relating to one of your subscriptions. See /watch list");
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
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
