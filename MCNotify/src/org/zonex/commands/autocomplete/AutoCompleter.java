package org.zonex.commands.autocomplete;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleter {

    private ArrayList<AutoCompleteNode> commands = new ArrayList<>();
    private int depth;

    public AutoCompleter(){
        this.depth = 0;
    }

    public void addNode(AutoCompleteNode node){
        this.commands.add(node);
    }

    private AutoCompleter(ArrayList<AutoCompleteNode> autoCompleteTree, int depth){
        this.commands = autoCompleteTree;
        this.depth = depth;
    }

    public List<String> getSuggestions(Player player, String[] args){
        List<String> commands = new ArrayList<>();
        if(args.length - 1 > depth) {
            for (AutoCompleteNode commandNode : this.commands) {
                if (commandNode.matches(args[depth])){
                    if(commandNode.hasChildren()) {
                        commands.addAll(new AutoCompleter(commandNode.getChildren(), depth + 1).getSuggestions(player, args));
                    } else {
                        continue;
                    }
                }
            }
            return commands;
        } else {
            for (AutoCompleteNode commandNode : this.commands) {
                commands.addAll(commandNode.getSuggestedCommands(player, args[depth]));
            }
            return commands;
        }
    }
}
