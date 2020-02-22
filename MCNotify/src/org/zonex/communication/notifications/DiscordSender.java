package org.zonex.communication.notifications;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.config.Configuration;

/**
 * Handles sending messages to discord
 */
public class DiscordSender {

    private DiscordSRV discordSrv;
    private boolean enabled;

    /**
     * Creates a new discord sender.
     */
    public DiscordSender(){
        // Checks if discord is configured
        if(Boolean.valueOf(Configuration.DISCORD_SRV_ENABLED.getValue())) {
            DiscordSRV discord = DiscordSRV.getPlugin();
            if (discord != null) {
                this.enabled = true;
            } else {
                this.enabled = false;
            }
        } else {
            this.enabled = false;
        }
    }

    /**
     * Sends a discord message to the player
     * @param target the player to send the discord message to
     * @param message the discord message to send
     */
    public void sendDiscordMessage(Player target, String message){
        if(this.enabled) {
            if(DiscordSRV.getPlugin().getAccountLinkManager() != null) {
                String discordUUID = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(target.getUniqueId());
                if(discordUUID != null) {
                    DiscordUtil.getJda().getUserById(discordUUID).openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(message).queue();
                    });
                }
            }
        }
    }

    /**
     * Checks if the player has verified their discord
     * @param player the player to check is verified
     * @return if the player is verified
     */
    public boolean playerIsVerified(OfflinePlayer player){
        if(this.enabled){
            if(DiscordSRV.getPlugin().getAccountLinkManager() != null) {
                String discordUUID = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
                if (discordUUID != null) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Gets the player's discord name
     * @param player the player to get the discord name of
     * @return the player's discord name
     */
    public String getDiscordName(OfflinePlayer player){
        if(this.enabled){
            if(DiscordSRV.getPlugin().getAccountLinkManager() != null) {
                String discordUUID = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
                return DiscordUtil.getJda().getUserById(discordUUID).getName();
            }
        }
        return "";
    }
}
