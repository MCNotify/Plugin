package org.mcnotify.datastore.baseModels;

import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class BaseAreaModel {

    public abstract ArrayList<Area> selectAll();

    public abstract Area selectWhereUuid(String uuid);

    public abstract Area selectWhereId(int id);

    public abstract boolean insert(Area area);

    public abstract boolean update(Area area);

    public abstract boolean delete(Area area);

}
