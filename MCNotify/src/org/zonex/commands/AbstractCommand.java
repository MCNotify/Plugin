package org.zonex.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.zonex.ZoneX;

import java.util.List;

public abstract class AbstractCommand extends AsyncCommandExecutor implements TabCompleter {

    private final String commandString;
    private final boolean canConsoleUse;

    public AbstractCommand(String commandString, boolean canConsoleUse){
        this.commandString = commandString;
        this.canConsoleUse = canConsoleUse;
        ZoneX.plugin.getCommand(this.commandString).setExecutor(this);
        ZoneX.plugin.getCommand(this.commandString).setTabCompleter(this);
    }

    @Override
    public void doAsyncCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if(!cmd.getLabel().equalsIgnoreCase(this.commandString))
            return;
        if(!canConsoleUse && !(sender instanceof Player)){
            sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY +" Only players may use this command.");
            return;
        }
        this.execute(sender, args);
    }

    public abstract void execute(CommandSender sender, String[] args);
    public abstract List<String> tabComplete(CommandSender sender, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String str, String[] args){
        if(!cmd.getLabel().equalsIgnoreCase(this.commandString))
            return null;
        if(sender instanceof Player)
            return this.tabComplete(sender, args);
        else
            return null;
    }

}
