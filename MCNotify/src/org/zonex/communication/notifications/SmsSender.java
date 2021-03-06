package org.zonex.communication.notifications;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.zonex.config.Configuration;

/**
 * Sends SMS messages through Twilio
 */
public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = Configuration.TWILIO_ACCOUNT_SID.getValue();
    public static final String AUTH_TOKEN = Configuration.TWILIO_AUTH_TOKEN.getValue();
    private boolean isEnabled = Boolean.valueOf(Configuration.TWILIO_ENABLED.getValue());

    /**
     * Creates an SmsSender
     */
    public SmsSender(){
        if(this.isEnabled) {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        }
    }

    /**
     * Sends an sms message
     * @param target The phone number to send a message to
     * @param body The message contents
     */
    public void sendSms(String target, String body){
        if(this.isEnabled) {
            Message message = Message
                    .creator(new PhoneNumber(target), // to
                            new PhoneNumber(Configuration.TWILIO_PHONE_NUMBER.getValue()), // from
                            body)
                    .create();
        }
    }
}