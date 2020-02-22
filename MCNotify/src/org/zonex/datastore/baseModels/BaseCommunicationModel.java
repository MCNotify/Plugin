package org.zonex.datastore.baseModels;

import org.zonex.communication.notifications.CommunicationMethod;

import java.util.ArrayList;

/**
 * Abstract class to allow flat file and database methods implement the same methods
 */
public abstract class BaseCommunicationModel {

    /**
     * Gets a list of all communicaiton methods
     * @return a list of communication methods
     */
    public abstract ArrayList<CommunicationMethod> selectAll();

    /**
     * Gets a list of communications for the player with the given uuid
     * @param uuid The player's uuid to search for communication methods
     * @return A list of communication methods
     */
    public abstract ArrayList<CommunicationMethod> selectWhereUuid(String uuid);

    /**
     * Inserts a new communication method into storage
     * @param method the communication method to store
     * @return if the communication method was stored
     */
    public abstract boolean insert(CommunicationMethod method);

    /**
     * Updates a communication method in storage
     * @param method the communicaiton method to update
     * @return if the communication method was updadted
     */
    public abstract boolean update(CommunicationMethod method);

    /**
     * deletes a communication method from storage
     * @param method the communication method to delete
     * @return if the communication method was deleted
     */
    public abstract boolean delete(CommunicationMethod method);

}
