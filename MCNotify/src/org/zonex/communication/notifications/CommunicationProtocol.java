package org.zonex.communication.notifications;

public enum CommunicationProtocol {

    EMAIL("email"),
    SMS("sms"),
    INGAME("ingame"),
    DISCORD("discord");

    private String protocol;

    CommunicationProtocol(String protocol){
        this.protocol = protocol;
    }

    public String getProtocolName(){
        return this.protocol;
    }

}
