package org.zonex.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.zonex.ZoneX;
import org.zonex.commands.autocomplete.AutoCompleter;

import java.util.List;

public abstract class AbstractCommand extends AsyncCommandExecutor implements TabCompleter {

    private final String commandString;
    private final boolean canConsoleUse;
    private AutoCompleter autoCompleter;

    public AbstractCommand(String commandString, boolean canConsoleUse){
        this.commandString = commandString;
        this.canConsoleUse = canConsoleUse;
        ZoneX.plugin.getCommand(this.commandString).setExecutor(this);
        ZoneX.plugin.getCommand(this.commandString).setTabCompleter(this);
    }

    public void setAutoCompleter(AutoCompleter autoCompleter){
        this.autoCompleter = autoCompleter;
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String str, String[] args){
        if(this.autoCompleter != null) {
            return this.autoCompleter.getSuggestions((Player)sender, args);
        }
        return null;
    }

}
