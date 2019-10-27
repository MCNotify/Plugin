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

public abstract class CommunicationMethod {

    String target;
    OfflinePlayer player;
    boolean isVerified = false;
    String verificationCode = null;
    CommunicationProtocol protocol;

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

    CommunicationMethod(OfflinePlayer player, CommunicationProtocol protocol, boolean isVerified, String verificationCode, String target){
        this.target = target;
        this.player = player;
        this.protocol = protocol;
        this.isVerified = isVerified;
        this.verificationCode = verificationCode;
    }

    public abstract void executeProtocol(String message);

    public String getVerificationCode(){
        return this.verificationCode;
    }

    public CommunicationProtocol getProtocol(){
        return this.protocol;
    }

    public String getTarget(){
        return this.target;
    }

    public void verify(){
        this.isVerified = true;
    }

    public OfflinePlayer getPlayer(){
        return this.player;
    }

    public boolean isVerified(){
        return this.isVerified;
    }


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
