package org.zonex.communication.notifications;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.ZoneX;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles all communications for the plugin.
 */
public class CommunicationHandler {

    /**
     * A list of registered communication methods
     */
    HashMap<OfflinePlayer, ArrayList<CommunicationMethod>> communications = new HashMap<>();

    /**
     * Counter for how many communications have been sent for BStats metric tracking
     */
    private int totalCommunications = 0;

    /**
     * Creates a communcation handler and loads all communication methods from the database.
     */
    public CommunicationHandler(){
        this.loadCommunications();
    }

    /**
     * Loads communication methods from the database.
     */
    public void loadCommunications(){
        System.out.println("[ZoneX] Loading communication settings...");

        ArrayList<CommunicationMethod> communicationMethods = ZoneX.datastore.communicationTable().selectAll();

        for(CommunicationMethod method : communicationMethods){
            if(method != null) {
                ArrayList<CommunicationMethod> methods = communications.get(method.getPlayer());
                if (methods == null) {
                    methods = new ArrayList<>();
                }

                methods.add(method);
                communications.put(method.getPlayer(), methods);
                totalCommunications++;
            }
        }

        System.out.println("[ZoneX] " + String.valueOf(communicationMethods.size()) + " notification settings loaded.");
    }

    /**
     * Adds a communication method to the specified player
     * @param player the player to add the communication method to
     * @param method The communication method
     * @return if the communication method was saved
     */
    public boolean addCommunicationProtocol(Player player, CommunicationMethod method){
        // Check if the player is already recieving this type of communication.
        if(isRecievingCommunication(player, method.getProtocol())){
            //If they are, remove the old protocol and update with the new one.
            this.removeCommunicationProtocol(player, method.getProtocol());
        }

        ArrayList<CommunicationMethod> methods = communications.get(player);
        if(methods == null){
            methods = new ArrayList<>();
            communications.put(player, methods);
        }

        methods.add(method);
        ZoneX.datastore.communicationTable().insert(method);
        totalCommunications++;
        if(method.getProtocol() != CommunicationProtocol.DISCORD){
            method.executeProtocol("Welcome to ZoneX. " + method.getPlayer().getName() + " has requested to receive notifications at this location. Actions from the minecraft server: " + Bukkit.getServer().getName() + "(" + Bukkit.getIp() + ") will be sent after verification. You can verify this device with the verification code: " + method.getVerificationCode() + ". If this was not you, ignore this correspondence.");
        }
        return true;
    }

    /**
     * Gets the communication method with the spcified communication protocol for the player
     * @param player the player to get the communication method for
     * @param protocol the communication protocol to look for
     * @return the player's communication method with the given protocol. Null if none.
     */
    public CommunicationMethod getCommunicationMethod(Player player, CommunicationProtocol protocol){
        ArrayList<CommunicationMethod> methods = communications.get(player);
        if(methods != null){
            for(CommunicationMethod method : methods){
                if(method.getProtocol() == protocol){
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the player is registered to recieve communications with the specified protocol.
     * @param player the player to check communication protocols for
     * @param protocol the communication protocol to check for
     * @return if the player is recieving the specified communication protocol
     */
    public boolean isRecievingCommunication(Player player, CommunicationProtocol protocol){
        ArrayList<CommunicationMethod> methods = communications.get(player);
        if(methods != null){
            for(CommunicationMethod method : methods){
                if(method.getProtocol() == protocol){
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Removes a communication protocol from the player
     * @param player the player to remove the protocol from
     * @param protocol the type of protocol to remove
     * @return if the communication method was succesfully removed
     */
    public boolean removeCommunicationProtocol(Player player, CommunicationProtocol protocol){
        ArrayList<CommunicationMethod> methods = communications.get(player);
        if(methods != null){
            for(CommunicationMethod method : methods){
                if(method.getProtocol() == protocol){
                    ZoneX.datastore.communicationTable().delete(method);
                    methods.remove(method);
                    totalCommunications--;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sends an in game message to the player
     * @param player the player to send a message to
     * @param message the message to send
     */
    public void sendIngameMessage(OfflinePlayer player, String message){
        if(player != null) {
            new IngameNotification(player).executeProtocol(message);
        }
    }

    /**
     * Sends notifications to all valid, verified and registered communication methods for the specified player
     * @param player the player to send a message to
     * @param message the message to send
     */
    public void sendCommunications(OfflinePlayer player, String message){
        // If the player is in game, no external communication should be sent.
        if(player.isOnline()){
            this.sendIngameMessage(player, message);
            return;
        }

        // If the player is not in game, send external communications to all of their registered methods.
        ArrayList<CommunicationMethod> comms = communications.get(player);
        if(comms != null) {
            // Loop the player's registered communication protocols.
            for (CommunicationMethod method : comms) {
                // Check if the player has verified their communication method before sending
                if (method.isVerified()) {
                    // Send using external communication protocol.
                    method.executeProtocol(message);
                }
            }
        }
    }

    /**
     * Verifies a player's communication protocol
     * @param player the player to verify
     * @param protocol the communication protocol to verify
     * @param verificationCode the player entered verification code to ensure their email/sms/etc is their own
     * @return if the user successfully verified the communication method
     */
    public boolean verifyCommunicationProtocol(Player player, CommunicationProtocol protocol, String verificationCode){
        CommunicationMethod method = this.getCommunicationMethod(player, protocol);
        if(method != null) {
            // Check if the verificaion code matches
            if (method.getVerificationCode().toLowerCase().equals(verificationCode.toLowerCase())) {
                method.verify();
                ZoneX.datastore.communicationTable().update(method);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Get all communication methods for the player
     * @param player the player to get communication methods for
     * @return a list of the player's communication methods.
     */
    public ArrayList<CommunicationMethod> getCommunicationMethods(Player player){
        if(this.communications.containsKey(player)){
            return this.communications.get(player);
        } else {
            return null;
        }
    }

    public int getTotalCommunicationMethods() {
        return this.totalCommunications;
    }

}
