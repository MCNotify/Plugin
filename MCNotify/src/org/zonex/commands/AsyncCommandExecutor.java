package org.zonex.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.zonex.ZoneX;
import org.zonex.communication.notifications.CommunicationProtocol;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public abstract class AsyncCommandExecutor implements CommandExecutor {

    public AsyncCommandExecutor(){

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Bukkit.getScheduler().runTaskAsynchronously(ZoneX.plugin, () -> {
            doAsyncCommand(commandSender, command, s, strings);
        });

        return true;
    }

    public abstract void doAsyncCommand(CommandSender commandSender, Command command, String s, String[] strings);
}
