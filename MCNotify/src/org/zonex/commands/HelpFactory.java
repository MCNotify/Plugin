package org.zonex.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zonex.areas.protection.Protection;
import org.zonex.subscriptions.subscriptionevents.Events;

public class HelpFactory {

    public void sendCommandList(CommandSender player){
        // TODO: Generate help pages

        player.sendMessage("-------------Page # of #-----------");
        player.sendMessage(ChatColor.GOLD + "/zx help <page>");
        player.sendMessage(ChatColor.GOLD + "/zx about"); //dev email, github link, and app download links
        player.sendMessage(ChatColor.GOLD + "/zx verify");

        player.sendMessage(ChatColor.GOLD + "/watch login <player>");
        player.sendMessage(ChatColor.GOLD + "/watch playerEnters <areaName>");
        player.sendMessage(ChatColor.GOLD + "/watch explosionIn <areaName>");
        player.sendMessage(ChatColor.GOLD + "/watch blockBreakIn <areaName>");
        player.sendMessage(ChatColor.GOLD + "/watch redstoneActiveIn <areaName>");
        player.sendMessage(ChatColor.GOLD + "/watch hopperFull <optional:hopperName>");
        player.sendMessage(ChatColor.GOLD + "/watch hopperOverCapacity <capacityPercentage> <optional:hopperName>");
        player.sendMessage(ChatColor.GOLD + "/watch hoppersFull <capacityPercentage> <areaName>");
        player.sendMessage(ChatColor.GOLD + "/watch hoppersOverCapacity <capacityPercentage> <areaName>");
        player.sendMessage(ChatColor.GOLD + "/watch cropGrown <areaName>");
        player.sendMessage(ChatColor.GOLD + "/watch mobsCapped <limit> <areaName>");
        player.sendMessage(ChatColor.GOLD + "/watch playerEnterNether <playerName>");
        player.sendMessage(ChatColor.GOLD + "/watch playerEnterEnd <playerName>");
        player.sendMessage(ChatColor.GOLD + "/watch playerEnterOverworld <playerName>");

        // Maybe not implement these if an ally is in a pvp arena it will notify when its not a real threat
        player.sendMessage(ChatColor.GOLD + "/watch attackAlly");
        player.sendMessage(ChatColor.GOLD + "/watch attackFaction");
        player.sendMessage(ChatColor.GOLD + "/watch attackAlliedFaction");

        player.sendMessage(ChatColor.GOLD + "/watch allies");
        player.sendMessage(ChatColor.GOLD + "/watch addAlly <player>");
        player.sendMessage(ChatColor.GOLD + "/watch removeAlly <player>");

        player.sendMessage(ChatColor.GOLD + "/watch mail <player> <message>");
        player.sendMessage(ChatColor.GOLD + "/watch mail");

        player.sendMessage(ChatColor.GOLD + "/watch event list");
        player.sendMessage(ChatColor.GOLD + "/watch event <id>");
        player.sendMessage(ChatColor.GOLD + "/watch event remove <id>");

        player.sendMessage(ChatColor.GOLD + "/zone");
        player.sendMessage(ChatColor.GOLD + "/zone add <areaName>");
        player.sendMessage(ChatColor.GOLD + "/zone list");
        player.sendMessage(ChatColor.GOLD + "/zone info <areaName>");
        player.sendMessage(ChatColor.GOLD + "/zone view");
        player.sendMessage(ChatColor.GOLD + "/zone remove <areaName>");
    }

    public void sendProtectionList(CommandSender player){
        player.sendMessage(ChatColor.GOLD + "========" + ChatColor.GREEN + "   [MCNotify] ProtectionTypes   " + ChatColor.GOLD + "========");

        for (Protection p : Protection.values()) {
            player.sendMessage(ChatColor.GOLD + p.getCommand() + ":" + ChatColor.GRAY + " " + p.getDescription());
        }
    }

    public void sendEventList(CommandSender player){
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
