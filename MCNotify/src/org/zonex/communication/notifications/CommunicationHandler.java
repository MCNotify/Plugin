package org.zonex.communication.notifications;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.zonex.ZoneX;
import java.util.ArrayList;
import java.util.HashMap;

public class CommunicationHandler {

    HashMap<OfflinePlayer, ArrayList<CommunicationMethod>> communications = new HashMap<>();
    private int totalCommunications = 0;

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
                totalCommunications++;
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
        totalCommunications++;
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
                    totalCommunications--;
                    return true;
                }
            }
        }
        return false;
    }

    public void sendIngameMessage(OfflinePlayer player, String message){
        if(player != null) {
            new IngameNotification(player).executeProtocol(message);
        }
    }

    public void sendCommunications(OfflinePlayer player, String message){
        if(player.isOnline()){
            this.sendIngameMessage(player, message);
            return;
        }
        ArrayList<CommunicationMethod> comms = communications.get(player);
        if(comms != null) {
            for (CommunicationMethod method : comms) {
                if (method.isVerified()) {
                    method.executeProtocol(message);
                }
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

    public int getTotalCommunicationMethods() {
        return this.totalCommunications;
    }

}
