package org.mcnotify.commands.SubscriptionCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpCommandHandler extends CommandHandler {

    @Override
    public void onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        // TODO: Generate help pages

        commandSender.sendMessage("-------------Page # of #-----------");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify help <page>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify verify");

        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify login <player>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify playerEnters <areaName>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify explosionIn <areaName>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify redstoneActiveIn <areaName>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify hopperFull <optional:hopperName>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify hopperOverCapacity <capacityPercentage> <optional:hopperName>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify hoppersFull <capacityPercentage> <areaName>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify hoppersOverCapacity <capacityPercentage> <areaName>");


        // Maybe not implement these if an ally is in a pvp arena it will notify when its not a real threat
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify attackAlly");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify attackFaction");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify attackAlliedFaction");

        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify allies");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify addAlly <player>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify removeAlly <player>");

        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify mail <player> <message>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify mail");

        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify event list");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify event <id>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify event remove <id>");

        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify area list");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify area <areaName>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify area add <areaName>");
        commandSender.sendMessage(ChatColor.GOLD + "/mcnotify area remove <areaName>");
    }
}
