package org.zonex.datastore.baseModels;

import org.zonex.communication.notifications.CommunicationMethod;

import java.util.ArrayList;

public abstract class BaseCommunicationModel {

    public abstract ArrayList<CommunicationMethod> selectAll();

    public abstract ArrayList<CommunicationMethod> selectWhereUuid(String uuid);

    public abstract boolean insert(CommunicationMethod area);

    public abstract boolean update(CommunicationMethod area);

    public abstract boolean delete(CommunicationMethod area);

}
