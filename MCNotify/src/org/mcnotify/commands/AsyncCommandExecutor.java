package org.mcnotify.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AsyncCommandExecutor implements CommandExecutor {

    JavaPlugin plugin;

    public AsyncCommandExecutor(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            doAsyncCommand(commandSender, command, s, strings);
        });

        return false;
    }

    public abstract void doAsyncCommand(CommandSender commandSender, Command command, String s, String[] strings);
}
