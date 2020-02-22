package org.zonex.communication.notifications;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zonex.subscriptions.Subscription;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Base instance of a communication method
 */
public abstract class CommunicationMethod {

    String target;
    OfflinePlayer player;
    boolean isVerified = false;
    String verificationCode = null;
    CommunicationProtocol protocol;

    /**
     * Constructor
     * @param player the player to communicate with
     * @param protocol the protocol the player wants to use
     */
    public CommunicationMethod(OfflinePlayer player, CommunicationProtocol protocol){
        this.player = player;
        this.protocol = protocol;

        // Generate a verification code
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder( 6 );
        for( int i = 0; i < 6; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        this.verificationCode = sb.toString();
    }

    /**
     * Constructor for database loading
     * @param player the player to communicate with
     * @param protocol the protocol the player wants to communicate with
     * @param isVerified if the player has verified this communication method
     * @param verificationCode the player's verification code
     * @param target the player's username/email/phonenumber/username/etc. to target with the communication protocol
     */
    CommunicationMethod(OfflinePlayer player, CommunicationProtocol protocol, boolean isVerified, String verificationCode, String target){
        this.target = target;
        this.player = player;
        this.protocol = protocol;
        this.isVerified = isVerified;
        this.verificationCode = verificationCode;
    }

    /**
     * Executes communication to the player through this communication protocol. Method should never be called directly.
     * All communication protocols should be invoked from the CommunicationHandler.
     * @param message the message to send
     */
    public abstract void executeProtocol(String message);

    /**
     * Gets the verification code
     * @return the player's verification code
     */
    public String getVerificationCode(){
        return this.verificationCode;
    }

    /**
     * The protocol the player wants to use
     * @return A communication protocol
     */
    public CommunicationProtocol getProtocol(){
        return this.protocol;
    }

    /**
     * The target for the protocol
     * @return the players email/phone/discord/etc. to target
     */
    public String getTarget(){
        return this.target;
    }

    /**
     * Verifies the user
     */
    public void verify(){
        this.isVerified = true;
    }

    /**
     * Gets the player using this communication method.
     * @return
     */
    public OfflinePlayer getPlayer(){
        return this.player;
    }

    /**
     * Checks if the player is verified
     * @return if the player is verified.
     */
    public boolean isVerified(){
        return this.isVerified;
    }


    /**
     * Serialize this object for flat file storage
     * @return A JSON string for flat file storage.
     */
    public String serialize(){
        JSONObject result = new JSONObject();

        // Encrypt the target so that server owners cannot spy on a player's phone number, email address, discord account, etc.
        byte[] bytesEncoded = Base64.encodeBase64(target.getBytes());

        result.put("target", new String(bytesEncoded));
        result.put("subscriberUuid", player.getUniqueId().toString());
        result.put("verified", isVerified);
        result.put("verificationCode", verificationCode);
        result.put("protocol", protocol.toString());

        return result.toJSONString();
    }

    /**
     * Deserializes a string
     * @param jsonString the JSON string of a communication method
     * @return an instance of a CommunicationMethod object.
     * @throws ParseException if the JSON cannot be parsed an exception is thrown.
     */
    public static CommunicationMethod deserialize(String jsonString) throws ParseException {

        Object deserialized = new JSONParser().parse(jsonString);
        JSONObject deserializedJson = (JSONObject) deserialized;

        // Decode the player's personal information
        byte[] valueDecoded = Base64.decodeBase64((String)deserializedJson.get("target"));

        String target = new String(valueDecoded);
        OfflinePlayer subscriber = Bukkit.getOfflinePlayer(UUID.fromString((String)deserializedJson.get("subscriberUuid")));
        boolean isVerified = (boolean)deserializedJson.get("verified");
        String verificationCode = (String)deserializedJson.get("verificationCode");
        CommunicationProtocol protocol = CommunicationProtocol.valueOf((String)deserializedJson.get("protocol"));

        switch(protocol){
            case EMAIL:
                return new EmailNotification(subscriber, isVerified, verificationCode, target);
            case SMS:
                return new SmsNotification(subscriber, isVerified, verificationCode, target);
            case INGAME:
                return new IngameNotification(subscriber, isVerified, verificationCode, target);
            case DISCORD:
                return new DiscordNotification(subscriber, isVerified, verificationCode, target);
            default:
                return null;
        }
    }
}
