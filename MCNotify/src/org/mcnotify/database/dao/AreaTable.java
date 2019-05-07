package org.mcnotify.database.dao;

import org.mcnotify.MCNotify;
import org.mcnotify.areas.Area;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AreaTable {

    public ResultSet selectWhereUuid(String uuid) {
        PreparedStatement statement = null;
        try {
            statement = MCNotify.database.getConnection().prepareStatement("SELECT * FROM areas WHERE uuid = ?");

            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();

            if(results.next()){
                return results;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet selectWhereId(int id) {
        PreparedStatement statement = null;
        try {
            statement = MCNotify.database.getConnection().prepareStatement("SELECT * FROM areas WHERE id = ?");

            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            if(results.next()){
                return results;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(Area area){
        PreparedStatement statement = null;
        try {
            statement = MCNotify.database.getConnection().prepareStatement("INSERT INTO areas (uuid, polygon, area_name, world) VALUES (?, ?, ?, ?)");

            statement.setString(1, area.getOwner().getUniqueId().toString());
            statement.setString(2, area.getPolygon().getJson().toJSONString());
            statement.setString(3, area.getAreaName());
            statement.setString(4, area.getWorld());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                return false;
            } else {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if(generatedKeys.next()){
                    area.setAreaId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Area area){
        PreparedStatement statement = null;
        try {
            statement = MCNotify.database.getConnection().prepareStatement("DELETE FROM areas where id = ?");

            statement.setInt(1, area.getAreaId());

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}