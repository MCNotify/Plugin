package org.mcnotify.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcnotify.commands.subscriptioncommands.AreaCommandHandler;
import org.mcnotify.events.multipartcommandevents.MultiPartCommandManager;
import org.mcnotify.commands.subscriptioncommands.HelpCommandHandler;
import org.mcnotify.commands.subscriptioncommands.SubscriptionCommandHandler;

public class BaseCommandHandler extends AsyncCommandExecutor {

    public BaseCommandHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public static MultiPartCommandManager mutiPartManager = new MultiPartCommandManager();

    @Override
    public void doAsyncCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            // Execute different module functions based on the argument parameters
            switch(strings[0].toLowerCase()){
                case "verify":
                    // Validate the user's MC account to the application and apply the database updates to register the user's device.
                    // Set registered flag, allowing user to get notifications on their device.
                    break;
                case "help":
                    new HelpCommandHandler().onCommand(player, command, s, strings);
                    break;
                case "area":
                    new AreaCommandHandler().onCommand(player, command, s, strings);
                    break;
                default:
                    new SubscriptionCommandHandler().onCommand(player, command, s, strings);
                    break;
            }
        }
    }
}
