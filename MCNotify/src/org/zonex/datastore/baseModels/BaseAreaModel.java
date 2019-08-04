package org.zonex.datastore.baseModels;

import org.zonex.areas.Area;

import java.util.ArrayList;

public abstract class BaseAreaModel {

    public abstract ArrayList<Area> selectAll();

    public abstract ArrayList<Area> selectWhereUuid(String uuid);

    public abstract Area selectWhereId(int id);

    public abstract boolean insert(Area area);

    public abstract boolean update(Area area);

    public abstract boolean delete(Area area);

}
