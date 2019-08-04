package org.zonex.commands.zx;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.zonex.ZoneX;
import org.zonex.authenticator.Response;
import org.zonex.commands.AbstractCommand;
import org.zonex.commands.HelpFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZxCommandHandler extends AbstractCommand {

    public ZxCommandHandler(){
        super("zx", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length < 1)
            return;

        switch(args[0]){
            case "help":
                new HelpFactory().sendCommandList(sender);
                break;
            case "verify":
                if(!(sender instanceof Player)){
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY +" Only players may use this command.");
                    return;
                }

                Player player = (Player)sender;
                // Validate the user's MC account to the application and apply the datastore updates to register the user's device.
                // Set registered flag, allowing user to get notifications on their device.
                String endpoint = "users.php";
                endpoint += "?uuid=" + player.getUniqueId().toString();

                Response response = null;
                try {
                    response = ZoneX.requestManager.sendRequest("GET", endpoint, null);

                    // The server is not validated, or the secret key is wrong, or not connected to the internet.
                    if (response.getResponseCode() != 200) {
                        return;
                    }

                    JSONObject json = response.getResponseBody();
                    int userid = Math.toIntExact((Long) json.get("user_id"));
                    if (userid != -1) {
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
            default:
                if(args.length < 2)
                    return;

                switch(args[1]){

                }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();
        if(args.length == 0){
            list.add("help");
            list.add("about");
            list.add("verify");


            if(sender.hasPermission("zx.admin.*")){
               list.add("area");
               list.add("watch");
            }

            return list;
        }
        return null;
    }
}
