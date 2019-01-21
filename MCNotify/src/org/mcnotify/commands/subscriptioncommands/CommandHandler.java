package org.mcnotify.commands.subscriptioncommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public abstract class CommandHandler {

    public abstract void onCommand(Player player, Command command, String s, String[] args);

}
