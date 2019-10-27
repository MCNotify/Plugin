package org.zonex.communication.notifications;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.zonex.ZoneX;
import org.zonex.communication.auth.RequestManager;
import org.zonex.communication.auth.Response;
import org.zonex.subscriptions.Subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CommunicationHandler {

    HashMap<OfflinePlayer, ArrayList<CommunicationMethod>> communications = new HashMap<>();

    public CommunicationHandler(){
        this.loadCommunications();
    }

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
            }
        }

        System.out.println("[ZoneX] " + String.valueOf(communicationMethods.size()) + " notification settings loaded.");
    }

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
        if(method.getProtocol() != CommunicationProtocol.DISCORD){
            method.executeProtocol("Welcome to ZoneX. " + method.getPlayer().getName() + " has requested to receive notifications at this location. Actions from the minecraft server: " + Bukkit.getServer().getName() + "(" + Bukkit.getIp() + ") will be sent after verification. You can verify this device with the verification code: " + method.getVerificationCode() + ". If this was not you, ignore this correspondence.");
        }
        return true;
    }

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

    public boolean removeCommunicationProtocol(Player player, CommunicationProtocol protocol){
        ArrayList<CommunicationMethod> methods = communications.get(player);
        if(methods != null){
            for(CommunicationMethod method : methods){
                if(method.getProtocol() == protocol){
                    ZoneX.datastore.communicationTable().delete(method);
                    methods.remove(method);
                    return true;
                }
            }
        }
        return false;
    }

    public void sendIngameMessage(OfflinePlayer player, String message){
        if(player != null) {
            // Check if the player is subscribed to recieve in-game messages.
            for (CommunicationMethod method : communications.get(player)) {
                if (method.getProtocol() == CommunicationProtocol.INGAME) {
                    method.executeProtocol(message);
                } else {
                    // Check if they have verified their other protocols. Send a warning if not verified.
                    if (!method.isVerified()) {
                        player.getPlayer().sendMessage("[ZoneX] You have not verified this method of communication. You need to verify this method before recieving notifications. /zx verify " + method.getProtocol().toString() + "<code>");
                    }
                }
            }
        }
    }

    public void sendCommunications(OfflinePlayer player, String message){
//        if(player.isOnline()){
//            this.sendIngameMessage(player, message);
//            return;
//        }
        for(CommunicationMethod method : communications.get(player)){
            if(method.isVerified()){
                method.executeProtocol(message);
            }
        }
    }

    public boolean verifyCommunicationProtocol(Player player, CommunicationProtocol protocol, String verificationCode){
        CommunicationMethod method = this.getCommunicationMethod(player, protocol);
        if(method != null) {
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

    public ArrayList<CommunicationMethod> getCommunicationMethods(Player player){
        if(this.communications.containsKey(player)){
            return this.communications.get(player);
        } else {
            return null;
        }
    }

}
