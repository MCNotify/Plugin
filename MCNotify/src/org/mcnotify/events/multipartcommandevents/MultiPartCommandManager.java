package org.mcnotify.events.multipartcommandevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;

public class MultiPartCommandManager {

    private HashMap<Player, MultiPartCommand> commandMap = new HashMap<>();

    public void addListener(Player player, MultiPartCommand event){
        this.commandMap.put(player, event);
    }

    public void removeListener(Player player){
        // Players can only be listening for one multi-part command at a time.
        if(this.commandMap.containsKey(player)){
            this.commandMap.remove(player);
        }
    }

    public void finishEvent(Player player, Event event){
        // Players can only be listening for one multi-part command at a time.
        if(this.commandMap.containsKey(player)){
            MultiPartCommand command = this.commandMap.get(player);
            if(command.checkFinishEvent(event)) {
                this.commandMap.get(player).onFinish(event);
                this.commandMap.get(player).cleanup();
                this.commandMap.remove(player);
            }
        }
    }

    public void fireEvent(Player player, Event event){
        if(this.commandMap.containsKey(player)){
            MultiPartCommand multiPartCommand = this.commandMap.get(player);
            if(multiPartCommand.checkUpdateEvent(event)){
                multiPartCommand.onUpdateEvent(event);
            }
        }
    }

    public boolean contains(Player player){
        return this.commandMap.containsKey(player);
    }

    public void playerQuit(Player player){
        if(this.commandMap.containsKey(player)){
            this.commandMap.get(player).cleanup();
        }

        // Unregister them from the multipart command listeners.
        this.commandMap.remove(player);
    }

}
