package org.mcnotify.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.commands.subscriptioncommands.AreaCommandHandler;
import org.mcnotify.events.multipartcommandevents.MultiPartCommandManager;
import org.mcnotify.commands.subscriptioncommands.HelpCommandHandler;
import org.mcnotify.commands.subscriptioncommands.SubscriptionCommandHandler;
import org.mcnotify.utility.Response;

import java.io.IOException;

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
                    String endpoint = "users.php";
                    endpoint += "?uuid=" + player.getUniqueId().toString();

                    Response response = null;
                    try {
                        response = MCNotify.requestManager.sendRequest("GET", endpoint, null);

                        // The server is not validated, or the secret key is wrong, or not connected to the internet.
                        if(response.getResponseCode() != 200){
                            return;
                        }

                        JSONObject json = response.getResponseBody();
                        int userid = Math.toIntExact((Long) json.get("user_id"));
                        if(userid != -1) {
                            String verificationCode = (String) json.get("minecraft_verification_code");
                            player.sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + " Your verification code is: " + verificationCode);
                            return;
                        } else {
                            player.sendMessage("Could not locate your account. Contact server administractors, the plugin may not be configured properly.");
                            return;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
