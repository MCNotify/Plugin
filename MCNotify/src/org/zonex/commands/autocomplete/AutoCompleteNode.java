package org.zonex.commands.autocomplete;

import org.bukkit.entity.Player;
import org.zonex.config.Permission;

import java.util.ArrayList;
import java.util.List;

public abstract class AutoCompleteNode {

    protected Permission permissionNode = null;
    protected ArrayList<AutoCompleteNode> children = new ArrayList<>();

    public abstract boolean matches(String s);


    public AutoCompleteNode addChild(AutoCompleteNode node){
        this.children.add(node);
        return this;
    }

    public ArrayList<AutoCompleteNode> getChildren(){
        return this.children;
    }

    public boolean hasChildren(){
        return this.children.size() > 0;
    }

    public AutoCompleteNode requiresPermissionNode(Permission permissionNode){
        this.permissionNode = permissionNode;
        return this;
    }

    public boolean hasPermission(Player player){
        if(this.permissionNode == null){
            return true;
        } else {
            return this.permissionNode.hasPermission(player);
        }
    }

    public abstract List<String> getSuggestedCommands(Player player, String s);
}
