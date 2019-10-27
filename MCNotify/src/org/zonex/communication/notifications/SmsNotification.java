package org.zonex.communication.notifications;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.config.Configuration;
import sun.security.krb5.Config;

public class SmsNotification extends CommunicationMethod {

    public SmsNotification(OfflinePlayer player, String target){
        super(player, CommunicationProtocol.SMS);
        this.target = target;
    }

    public SmsNotification(OfflinePlayer player, boolean isVerified, String verificationCode, String target){
        super(player, CommunicationProtocol.SMS, isVerified, verificationCode, target);
    }

    @Override
    public void executeProtocol(String message) {
        if(Boolean.valueOf(Configuration.TWILIO_ENABLED.getValue())) {
            new SmsSender().sendSms(this.target, message);
        }
    }
}
