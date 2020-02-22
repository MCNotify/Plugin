package org.zonex.communication.notifications;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.config.Configuration;
import sun.security.krb5.Config;

/**
 * A type of communication method that can registered to
 */
public class SmsNotification extends CommunicationMethod {

    /**
     * A type of communication method that sends text messages
     * @param player the player that subscribed to the communication method
     * @param target the player's phone number
     */
    public SmsNotification(OfflinePlayer player, String target){
        super(player, CommunicationProtocol.SMS);
        this.target = target;
    }

    /**
     * Creates a SmsNotification communication method from a database entry
     * @param player the player registered to the communication method
     * @param isVerified if the player has verified their phone number
     * @param verificationCode the player's verification code
     * @param target the player's phone number
     */
    public SmsNotification(OfflinePlayer player, boolean isVerified, String verificationCode, String target){
        super(player, CommunicationProtocol.SMS, isVerified, verificationCode, target);
    }

    /**
     * Sends a text message
     * @param message the message to send
     */
    @Override
    public void executeProtocol(String message) {
        if(Boolean.valueOf(Configuration.TWILIO_ENABLED.getValue())) {
            new SmsSender().sendSms(this.target, message);
        }
    }
}
