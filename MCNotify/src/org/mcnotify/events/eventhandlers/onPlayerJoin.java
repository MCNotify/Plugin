package org.mcnotify.events.eventhandlers;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.events.Events;
import org.mcnotify.events.subscriptions.Subscription;
import org.mcnotify.events.subscriptions.subscriptiondata.onPlayerJoinSubscriptionData;
import org.mcnotify.utility.Response;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class onPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent loginEvent){
        // Check subscriptions
        for(Subscription subscription : MCNotify.eventSubscriptionManager.getSubscriptions()){
            if(subscription.getEventType() == Events.ON_PLAYER_JOIN){
                onPlayerJoinSubscriptionData subdata = (onPlayerJoinSubscriptionData) subscription.getSubscriptionData();
                if(subdata.getWatchedPlayer() == loginEvent.getPlayer()) {
                    subscription.onEvent();
                }
            }
        }


        try {
            // Check if the player exists in the database
            JSONObject checkUserJson = new JSONObject();
            checkUserJson.put("uuid", loginEvent.getPlayer().getUniqueId().toString());
            checkUserJson.put("server_id", MCNotify.server_id);
            Response response = MCNotify.requestManager.sendRequest("users.php", "GET", checkUserJson.toJSONString());

            // The server is not validated, or the secret key is wrong, or not connected to the internet.
            if(response.getResponseCode() != 200){
                return;
            }

            boolean isVerified = false;
            String verificationCode = "";

            // extract the user's id
            JSONObject json = response.getResponseBody();
            int userid = (Integer) json.get("user_id");

            // If the user is not in the database, add them to the database.
            if(userid == -1){
                // The user does not exist. Add them to the database.
                JSONObject newUserJson = new JSONObject();
                newUserJson.put("uuid", loginEvent.getPlayer().getUniqueId().toString());
                newUserJson.put("server_id", MCNotify.server_id);
                newUserJson.put("username", loginEvent.getPlayer().getName());

                // Generate a minecraft token to validate in the app
                Random random = new Random();
                String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
                StringBuilder token = new StringBuilder(6);
                for (int i = 0; i < 6; i++) {
                    token.append(CHARS.charAt(random.nextInt(CHARS.length())));
                }

                newUserJson.put("minecraft_verification_code", token.toString());

                Response newUserResponse = MCNotify.requestManager.sendRequest("users.php", "POST", checkUserJson.toJSONString());
                verificationCode = token.toString();
                isVerified = false;
            } else {
                isVerified = (boolean) json.get("verified_minecraft");
                verificationCode = (String) json.get("minecraft_verification_code");
            }

            if(!isVerified){
                loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Welcome to the server! MCNotify lets you receive push notifications on your mobile device.");
                loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Download the app here: " + ChatColor.GREEN + "MCNotify downloadLink");
                loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[MCNotify]" + ChatColor.GRAY + "Your verification code is: " + verificationCode);
            }

        } catch (ParseException e1) {
        e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Check if the player has any subscriptions and load them
        MCNotify.eventSubscriptionManager.loadSubscriptions(loginEvent.getPlayer());
    }
}
