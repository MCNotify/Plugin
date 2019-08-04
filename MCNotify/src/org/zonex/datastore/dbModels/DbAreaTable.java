package org.zonex.datastore.dbModels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.zonex.areas.Area;
import org.zonex.areas.Polygon;
import org.zonex.areas.protection.Protection;
import org.zonex.datastore.baseModels.BaseAreaModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class DbAreaTable extends BaseAreaModel {

    private Connection connection;

    public DbAreaTable(Connection connection){
        this.connection = connection;
    }

    public ArrayList<Area> selectAll(){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM areas");
            ResultSet results = statement.executeQuery();
                if(results != null) {

                    ArrayList<Area> areaList = new ArrayList<Area>();

                    while (results.next()) {
                        areaList.add(this.getArea(results));
                    }

                    return areaList;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Area> selectWhereUuid(String uuid) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM areas WHERE uuid = ?");

            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();

            if(results != null){
                ArrayList<Area> areaList = new ArrayList<Area>();

                while (results.next()) {
                    areaList.add(this.getArea(results));
                }

                return areaList;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Area selectWhereId(int id) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM areas WHERE id = ?");

            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            if(results != null){
                results.first();
                return this.getArea(results);
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
            statement = connection.prepareStatement("INSERT INTO areas (uuid, polygon, area_name, world, protections, whitelist) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, area.getOwner().getUniqueId().toString());
            statement.setString(2, area.getPolygon().getJson().toJSONString());
            statement.setString(3, area.getAreaName());
            statement.setString(4, area.getWorld());
            statement.setString(5, area.getProtectionsAsString());
            statement.setString(6, area.getWhitelistAsString());

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

    public boolean update(Area area){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("UPDATE areas SET protections = ?, whitelist = ? where id = ?");

            statement.setString(1, area.getProtectionsAsString());
            statement.setString(2, area.getWhitelistAsString());
            statement.setInt(3, area.getAreaId());

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Area area){
        PreparedStatement statement = null;
        try {
            //TODO: Remove subscriptions that are linked to an area when it is deleted.
            statement = connection.prepareStatement("DELETE FROM areas where id = ?");

            statement.setInt(1, area.getAreaId());

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Area getArea(ResultSet resultset) throws SQLException {
        int areaId = resultset.getInt("id");
        String ownerUuid = resultset.getString("uuid");
        String areaName = resultset.getString("area_name");
        String jsonPoly = resultset.getString("polygon");
        String world = resultset.getString("world");
        String protectionString = resultset.getString("protections");
        String whitelist = resultset.getString("whitelist");

        String[] playerStrings = whitelist.split(",");
        ArrayList<OfflinePlayer> playerList = new ArrayList<>();
        for (String s : playerStrings) {
            if (s != null && s != "") {
                UUID uuid = UUID.fromString(s);
                if (uuid != null) {
                    OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(s));
                    playerList.add(op);
                }
            }
        }

        OfflinePlayer owner = null;

        if (ownerUuid != null && ownerUuid != "") {
            UUID uuid = UUID.fromString(ownerUuid);
            if (uuid != null) {
                owner = Bukkit.getOfflinePlayer(UUID.fromString(ownerUuid));
            }
        }

        return new Area(areaId, owner, new Polygon(jsonPoly), areaName, world, Protection.fromString(protectionString), playerList);
    }

}
