package org.mcnotify.commands.subscriptioncommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;
import org.mcnotify.events.subscriptionevents.Events;
import org.mcnotify.events.subscriptions.Subscription;
import org.mcnotify.events.subscriptions.subscriptiondata.*;

import java.util.ArrayList;

public class SubscriptionCommandHandler extends CommandHandler{

    @Override
    public void onCommand(Player player, Command command, String s, String[] args) {
        switch(args[0].toLowerCase()){
            case "playerenters":
                if(args.length == 2){
                    String areaName = args[1];
                    ArrayList<Area> playerAreas = MCNotify.areaManager.getAreas(player);
                    for(Area area : playerAreas){
                        if(area.getAreaName().equals(areaName)){
                            // Create a new subscription
                            MCNotify.eventSubscriptionManager.addSubscription(new Subscription(player, Events.ON_PLAYER_MOVE, new onPlayerMoveSubscriptionData(area.getAreaId())));
                            return;
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You do not have an area with that name. Create an area with /mcnotify area add");
                }
                break;
            case "login":
                if(args.length == 2) {
                    String playername = args[1];
                    System.out.println(playername);
                    Player watchedPlayer = Bukkit.getServer().getPlayer(playername);

                    MCNotify.eventSubscriptionManager.addSubscription(new Subscription(player, Events.ON_PLAYER_JOIN, new onPlayerJoinSubscriptionData(watchedPlayer)));

                    player.sendMessage("Successfully subscribed to event");

                } else {
                    player.sendMessage("You need to specify a player name!");
                }
                break;
            case "explosionin":
                if(args.length == 2){
                    String areaName = args[1];
                    ArrayList<Area> playerAreas = MCNotify.areaManager.getAreas(player);
                    for(Area area : playerAreas){
                        if(area.getAreaName().equals(areaName)){
                            // Create a new subscription
                            MCNotify.eventSubscriptionManager.addSubscription(new Subscription(player, Events.ON_BLOCK_EXPLODE, new onBlockExplosionSubscriptionData(area.getAreaId())));
                            return;
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You do not have an area with that name. Create an area with /mcnotify area add");
                }
                break;
            case "blockbreak":
                if(args.length == 2){
                    String areaName = args[1];
                    ArrayList<Area> playerAreas = MCNotify.areaManager.getAreas(player);
                    for(Area area : playerAreas){
                        if(area.getAreaName().equals(areaName)){
                            // Create a new subscription
                            MCNotify.eventSubscriptionManager.addSubscription(new Subscription(player, Events.ON_BLOCK_BREAK, new onBlockBreakSubscriptionData(area.getAreaId())));
                            return;
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You do not have an area with that name. Create an area with /mcnotify area add");
                }
                break;
            case "redstoneactive":
                if(args.length == 2){
                    String areaName = args[1];
                    ArrayList<Area> playerAreas = MCNotify.areaManager.getAreas(player);
                    for(Area area : playerAreas){
                        if(area.getAreaName().equals(areaName)){
                            // Create a new subscription
                            MCNotify.eventSubscriptionManager.addSubscription(new Subscription(player, Events.ON_REDSTONE_ACTIVE, new onRedstoneActiveSubscriptionData(area.getAreaId())));
                            return;
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You do not have an area with that name. Create an area with /mcnotify area add");
                }
                break;
            case "cropgrow":
                if(args.length == 2){
                    String areaName = args[1];
                    ArrayList<Area> playerAreas = MCNotify.areaManager.getAreas(player);
                    for(Area area : playerAreas){
                        if(area.getAreaName().equals(areaName)){
                            // Create a new subscription
                            MCNotify.eventSubscriptionManager.addSubscription(new Subscription(player, Events.ON_CROP_GROWN, new onCropGrowSubscriptionData(area.getAreaId())));
                            return;
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " You do not have an area with that name. Create an area with /mcnotify area add");
                }
                break;
        }
    }
}
