package org.zonex.communication.notifications;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.config.Configuration;

/**
 * A type of communication method. Allows players to get discord notifications
 */
public class DiscordNotification extends CommunicationMethod {

    public DiscordNotification(OfflinePlayer player) {
        super(player, CommunicationProtocol.DISCORD);

        // Get the target
        DiscordSender discord = new DiscordSender();
        if(discord.playerIsVerified(player)){
            // Get their discord username
            this.target = discord.getDiscordName(player);
        } else {
            this.target = "";
        }
    }

    public DiscordNotification(OfflinePlayer player, boolean isVerified, String verificationCode, String target) {
        super(player, CommunicationProtocol.DISCORD, isVerified, verificationCode, target);
    }

    /**
     * Sends the player a discord message
     * @param message the message to send
     */
    @Override
    public void executeProtocol(String message) {
        if(Boolean.valueOf(Configuration.DISCORD_SRV_ENABLED.getValue())){
            new DiscordSender().sendDiscordMessage((Player) this.player, message);
        }
    }

    /**
     * Checks if the player has verified their discord. Requires an override because checking verification with
     * discord is slightly different than other methods of communication as it is built into the DiscordSRV library
     * @return if the player has verified their discord.
     */
    @Override
    public boolean isVerified(){
        DiscordSender discord = new DiscordSender();
        if(discord.playerIsVerified(this.player)){
            this.target = discord.getDiscordName(player);
            return true;
        }
        return false;
    }

}
