package org.zonex.communication.notifications;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.config.Configuration;
import org.zonex.subscriptions.Subscription;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailNotification extends CommunicationMethod {

    private final String SMTP_SERVER = Configuration.EMAIL_SMTP_SERVER.getValue();
    private final String USERNAME = Configuration.EMAIL_USERNAME.getValue();
    private final String PASSWORD = Configuration.EMAIL_PASSWORD.getValue();
    private final String EMAIL_FROM = Configuration.EMAIL_FROM_ADDRESS.getValue();
    private final boolean isEnabled = Boolean.valueOf(Configuration.EMAIL_ENABLED.getValue());

    public EmailNotification(OfflinePlayer player, String targetEmail){
        super(player, CommunicationProtocol.EMAIL);
        this.target = targetEmail;
    }

    public EmailNotification(OfflinePlayer player, boolean isVerified, String verificationCode, String target){
        super(player, CommunicationProtocol.EMAIL, isVerified, verificationCode, target);
    }


    @Override
    public void executeProtocol(String message) {
        if(Boolean.valueOf(Configuration.EMAIL_ENABLED.getValue())) {
            new EmailSender().sendEmail(this.target, message);
        }
    }

}
