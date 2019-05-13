package org.mcnotify.commands.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcnotify.areas.protection.Protection;
import org.mcnotify.subscriptions.subscriptionevents.Events;

public class HelpFactory {

    public void sendCommandList(Player player){
        // TODO: Generate help pages

        player.sendMessage("-------------Page # of #-----------");
        player.sendMessage(ChatColor.GOLD + "/mcnotify help <page>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify about"); //dev email, github link, and app download links
        player.sendMessage(ChatColor.GOLD + "/mcnotify verify");

        player.sendMessage(ChatColor.GOLD + "/mcnotify login <player>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify playerEnters <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify explosionIn <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify blockBreakIn <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify redstoneActiveIn <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify hopperFull <optional:hopperName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify hopperOverCapacity <capacityPercentage> <optional:hopperName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify hoppersFull <capacityPercentage> <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify hoppersOverCapacity <capacityPercentage> <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify cropGrown <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify mobsCapped <limit> <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify playerEnterNether <playerName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify playerEnterEnd <playerName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify playerEnterOverworld <playerName>");

        // Maybe not implement these if an ally is in a pvp arena it will notify when its not a real threat
        player.sendMessage(ChatColor.GOLD + "/mcnotify attackAlly");
        player.sendMessage(ChatColor.GOLD + "/mcnotify attackFaction");
        player.sendMessage(ChatColor.GOLD + "/mcnotify attackAlliedFaction");

        player.sendMessage(ChatColor.GOLD + "/mcnotify allies");
        player.sendMessage(ChatColor.GOLD + "/mcnotify addAlly <player>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify removeAlly <player>");

        player.sendMessage(ChatColor.GOLD + "/mcnotify mail <player> <message>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify mail");

        player.sendMessage(ChatColor.GOLD + "/mcnotify event list");
        player.sendMessage(ChatColor.GOLD + "/mcnotify event <id>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify event remove <id>");

        player.sendMessage(ChatColor.GOLD + "/mcnotify area list");
        player.sendMessage(ChatColor.GOLD + "/mcnotify area <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify area add <areaName>");
        player.sendMessage(ChatColor.GOLD + "/mcnotify area remove <areaName>");
    }

    public void sendProtectionList(Player player){
        player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   [MCNotify] ProtectionTypes   " + ChatColor.GOLD + "========");

        for (Protection p : Protection.values()) {
            player.sendMessage(ChatColor.GOLD + p.getCommand() + ":" + ChatColor.GRAY + " " + p.getDescription());
        }
    }

    public void sendEventList(Player player){
        player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   [MCNotify] EventTypes   " + ChatColor.GOLD + "========");

        for (Events e : Events.values()) {

            String commandUsage = e.getCommandName();
            for (String commandKey : e.getPlayerFriendlyKeyNames()) {
                commandUsage += " <" + commandKey + ">";
            }

            player.sendMessage(ChatColor.GOLD + commandUsage + ":" + ChatColor.GRAY + " " + e.getDescription());
        }
    }
}
