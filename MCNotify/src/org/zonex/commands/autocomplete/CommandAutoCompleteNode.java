package org.zonex.commands.autocomplete;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandAutoCompleteNode extends AutoCompleteNode {

    private String nodeName;

    public CommandAutoCompleteNode(String s){
        this.nodeName = s;
    }

    public boolean matches(String s){
        return this.nodeName.startsWith(s.toLowerCase());
    }

    public List<String> getSuggestedCommands(Player player, String s){
        ArrayList<String> commands = new ArrayList<>();
        if(hasPermission(player)) {
            if(this.matches(s)) {
                commands.add(this.nodeName);
                return commands;
            }
        }
        return commands;
    }

}
