package org.zonex.communication.notifications;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.config.Configuration;

public class DiscordSender {

    private DiscordSRV discordSrv;
    private boolean enabled;

    public DiscordSender(){
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
