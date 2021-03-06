package org.zonex.communication.notifications;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.config.Configuration;

/**
 * A type of communication method. Handles in game notifications
 */
public class IngameNotification extends CommunicationMethod {
    public IngameNotification(OfflinePlayer player) {
        super(player, CommunicationProtocol.INGAME);
    }

    public IngameNotification(OfflinePlayer player, boolean isVerified, String verificationCode, String target){
        super(player, CommunicationProtocol.EMAIL, isVerified, verificationCode, target);
    }

    /**
     * Sends an ingame notification
     * @param message the message to send to the player
     */
    @Override
    public void executeProtocol(String message) {
        if(Boolean.valueOf(Configuration.INGAME_NOTIFICATIONS_ENABLED.getValue())) {
            if (player.isOnline()) {
                player.getPlayer().sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + message);
            }
        }
    }
}
