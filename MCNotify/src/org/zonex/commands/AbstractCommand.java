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

/**
 * A command using the async command executor and tab completion.
 */
public abstract class AbstractCommand extends AsyncCommandExecutor implements TabCompleter {

    private final String commandString;
    private final boolean canConsoleUse;
    private AutoCompleter autoCompleter;

    /**
     * Constructor for a command
     * @param commandString the base command string
     * @param canConsoleUse if the console can use the command.
     */
    public AbstractCommand(String commandString, boolean canConsoleUse){
        this.commandString = commandString;
        this.canConsoleUse = canConsoleUse;
        ZoneX.plugin.getCommand(this.commandString).setExecutor(this);
        ZoneX.plugin.getCommand(this.commandString).setTabCompleter(this);
    }

    /**
     * Sets the autocompleter for the command
     * @param autoCompleter An autocompleter instance to auto complete commands
     */
    public void setAutoCompleter(AutoCompleter autoCompleter){
        this.autoCompleter = autoCompleter;
    }

    /**
     * Executes a command asynchronously.
     * @param sender the command sender
     * @param cmd the command
     * @param str the base command string
     * @param args command arguments
     */
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

    /**
     * Executes the given command
     * @param sender the command sender
     * @param args command arguments.
     */
    public abstract void execute(CommandSender sender, String[] args);

    /**
     * Tab completion method to suggest
     * @param sender the command sender
     * @param cmd the comand
     * @param str the base command string
     * @param args command arguments
     * @return a list of potential command suggestions based on current inputs.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String str, String[] args){
        if(this.autoCompleter != null) {
            return this.autoCompleter.getSuggestions((Player)sender, args);
        }
        return null;
    }

}
