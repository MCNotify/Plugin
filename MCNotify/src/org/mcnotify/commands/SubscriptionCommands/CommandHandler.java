package org.mcnotify.commands.SubscriptionCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class CommandHandler {

    public abstract void onCommand(CommandSender commandSender, Command command, String s, String[] args);

}
