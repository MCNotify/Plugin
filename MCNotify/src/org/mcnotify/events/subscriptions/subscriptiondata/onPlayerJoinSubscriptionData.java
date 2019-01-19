package org.mcnotify.events.subscriptions.subscriptiondata;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.StringReader;
import java.util.UUID;

public class onPlayerJoinSubscriptionData extends SubscriptionData {

    Player watchedPlayer;

    public onPlayerJoinSubscriptionData(){

    }

    public onPlayerJoinSubscriptionData(Player watchedPlayer){
        this.watchedPlayer = watchedPlayer;
    }

    public Player getWatchedPlayer(){
        return this.watchedPlayer;
    }

    @Override
    public SubscriptionData fromJSON(String json) {

        try {
            Object obj = new JSONParser().parse(json);
            JSONObject jsonObject = (JSONObject) obj;
            String uuid = (String) jsonObject.get("watchedPlayer");
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            this.watchedPlayer = player;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public String toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("watchedPlayer", watchedPlayer.getUniqueId().toString());
        return obj.toJSONString();
    }
}
