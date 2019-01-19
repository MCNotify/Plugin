package org.mcnotify.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.commands.SubscriptionCommands.HelpCommandHandler;
import org.mcnotify.commands.SubscriptionCommands.SubscriptionCommandHandler;

public class BaseCommandHandler extends AsyncCommandExecutor {

    public BaseCommandHandler(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void doAsyncCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            // Execute different module functions based on the argument parameters
            switch(strings[0].toLowerCase()){
                case "verify":
                    // Validate the user's MC account to the application and apply the database updates to register the user's device.
                    // Set registered flag, allowing user to get notifications on their device.
                    break;
                case "help":
                    new HelpCommandHandler().onCommand(commandSender, command, s, strings);
                    break;
                default:
                    new SubscriptionCommandHandler().onCommand(commandSender, command, s, strings);
            }
        }
    }
}
