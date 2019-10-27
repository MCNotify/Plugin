package org.zonex.communication.notifications;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.config.Configuration;

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

    @Override
    public void executeProtocol(String message) {
        if(Boolean.valueOf(Configuration.DISCORD_SRV_ENABLED.getValue())){
            new DiscordSender().sendDiscordMessage((Player) this.player, message);
        }
    }

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
