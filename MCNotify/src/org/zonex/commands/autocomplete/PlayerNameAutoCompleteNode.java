package org.zonex.commands.autocomplete;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerNameAutoCompleteNode extends AutoCompleteNode {


    @Override
    public boolean matches(String s) {
        return true;
    }

    @Override
    public List<String> getSuggestedCommands(Player player, String s) {
        // Get potential list of players.
        List<String> players = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getName().startsWith(s)){
                players.add(p.getName());
            }
        }
        for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
            if(p.getName().startsWith(s)){
                players.add(p.getName());
            }
        }
        return players;
    }
}
