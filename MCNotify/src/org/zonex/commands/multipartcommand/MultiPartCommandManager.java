package org.zonex.commands.multipartcommand;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;

/**
 * Handles multi-part commands
 */
public class MultiPartCommandManager {

    /**
     * A list of active multi-part commands for each player.
     */
    private HashMap<Player, MultiPartCommand> commandMap = new HashMap<>();

    /**
     * Adds a multi-part command for the specified player.
     * @param player the player starting a multi-part command
     * @param event the multipart command
     */
    public void addListener(Player player, MultiPartCommand event){
        this.commandMap.put(player, event);
    }

    /**
     * Removes a multi-part command from the player.
     * @param player the player to stop a multipart command for
     */
    public void removeListener(Player player){
        // Players can only be listening for one multi-part command at a time.
        if(this.commandMap.containsKey(player)){
            this.commandMap.remove(player);
        }
    }

    /**
     * Finalizes a multipart command
     * @param player the player finishing the command
     * @param event the event that triggered the finish
     */
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

    /**
     * Triggers a progression in a multipart event.
     * @param player the player triggering the event
     * @param event the event that was triggered
     */
    public void fireEvent(Player player, Event event){
        if(this.commandMap.containsKey(player)){
            MultiPartCommand multiPartCommand = this.commandMap.get(player);
            if(multiPartCommand.checkUpdateEvent(event)){
                multiPartCommand.onUpdateEvent(event);
            }
        }
    }

    /**
     * Check if the player is participating in a multi-part command
     * @param player the player to check
     * @return if the player is participating in a multi-part command
     */
    public boolean contains(Player player){
        return this.commandMap.containsKey(player);
    }

    /**
     * Stops the player from participating in a multi-part command
     * @param player the player to remove.
     */
    public void playerQuit(Player player){
        if(this.commandMap.containsKey(player)){
            this.commandMap.get(player).cleanup();
        }

        // Unregister them from the multipart command listeners.
        this.commandMap.remove(player);
    }

}
