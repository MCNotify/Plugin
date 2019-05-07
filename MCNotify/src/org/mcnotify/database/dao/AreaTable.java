package org.mcnotify.database.dao;

import org.mcnotify.MCNotify;

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

}
