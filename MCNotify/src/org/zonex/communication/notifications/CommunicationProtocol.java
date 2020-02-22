package org.zonex.communication.notifications;

/**
 * An enumeration of all communication protocols
 */
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
