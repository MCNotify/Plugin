package org.zonex.communication.notifications;

import org.zonex.config.Configuration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Class to handle sending email.
 */
public class EmailSender {

    private final String SMTP_SERVER = Configuration.EMAIL_SMTP_SERVER.getValue();
    private final String USERNAME = Configuration.EMAIL_USERNAME.getValue();
    private final String PASSWORD = Configuration.EMAIL_PASSWORD.getValue();
    private final String EMAIL_FROM = Configuration.EMAIL_FROM_ADDRESS.getValue();
    private final boolean isEnabled = Boolean.valueOf(Configuration.EMAIL_ENABLED.getValue());

    public EmailSender(){

    }

    /**
     * Sends an email to the specified email
     * @param target the email to send a message to
     * @param notificationText The notification message
     */
    public void sendEmail(String target, String notificationText) {
        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(EMAIL_FROM));
            } catch (MessagingException e) {
                // Invalid email address
                System.out.println("[ZoneX] Invalid email address.");
                return;
            }
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(target));
            message.setSubject("[ZoneX] Notification in your area!");
            message.setText(notificationText);

            //send the message
            Transport.send(message);

        } catch (MessagingException e) {e.printStackTrace();}
    }

}
