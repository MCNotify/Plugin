package org.zonex.subscriptions.subscriptionevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import org.zonex.ZoneX;
import org.zonex.areas.Area;
import org.zonex.subscriptions.Subscription;

import java.util.ArrayList;

public class onPlayerJoin implements Listener {

    /**
     * Checks login events to see if the event is being watched by a subscription. If it is, it triggers the event.
     * @param loginEvent The login event
     */
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent loginEvent){
        // Check subscriptions
        for(Subscription subscription : ZoneX.subscriptionManager.getSubscriptions(Events.ON_PLAYER_JOIN)){

            Player watchedPlayer = Bukkit.getPlayer((String)subscription.getSubscriptionJson().get("watchedPlayer"));

            if(watchedPlayer == loginEvent.getPlayer()) {
                subscription.onEvent(loginEvent.getPlayer().getDisplayName() + " has joined the server.");
            }
        }

        String uuid = loginEvent.getPlayer().getUniqueId().toString();
        String username = loginEvent.getPlayer().getDisplayName();
        String verificationCode = "";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("uuid", uuid);
        jsonBody.put("username", username);

        // Check if they are registered.
//        try {
//            RequestManager.sendRequest("POST", "users.php", jsonBody.toJSONString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ArrayList<Area> areaList = ZoneX.areaManager.getAreas(loginEvent.getPlayer().getUniqueId());
        if(areaList == null || areaList.size() == 0){
            loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + "Welcome to the server! Use ZoneX to protect your land with /zone add <AreaName>");
        }

//        if(ZoneX.auth.getSubscriptionLevel() == 0) {
//            loginEvent.getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX]" + ChatColor.GRAY + "This server is using a free version of ZoneX. If you would like SMS notifications, contact your server admins!");
//        }
    }
}
