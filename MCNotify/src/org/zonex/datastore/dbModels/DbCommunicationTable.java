package org.zonex.datastore.dbModels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.zonex.communication.notifications.*;
import org.zonex.datastore.baseModels.BaseCommunicationModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class DbCommunicationTable extends BaseCommunicationModel {

    private Connection connection;

    public DbCommunicationTable(Connection connection){
        this.connection = connection;
    }

    @Override
    public ArrayList<CommunicationMethod> selectAll() {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM communications");
            ResultSet results = statement.executeQuery();
            if(results != null) {

                ArrayList<CommunicationMethod> communicationList = new ArrayList<CommunicationMethod>();

                while (results.next()) {
                    communicationList.add(this.getCommunicationMethod(results));
                }

                return communicationList;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<CommunicationMethod> selectWhereUuid(String uuid) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM communications WHERE subscriberUuid = ?");

            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();

            if(results != null){
                ArrayList<CommunicationMethod> subscriptions = new ArrayList<>();
                while (results.next()) {
                    subscriptions.add(this.getCommunicationMethod(results));
                }
                return subscriptions;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(CommunicationMethod method) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO communications (subscriberUuid, target, verified, verificationCode, protocol) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, method.getPlayer().getUniqueId().toString());
            statement.setBoolean(3, method.isVerified());
            statement.setString(2, method.getTarget());
            statement.setString(4, method.getVerificationCode());
            statement.setString(5, method.getProtocol().toString());

            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(CommunicationMethod method) {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("UPDATE communications SET verified = ?, target = ? where subscriberUuid = ? AND protocol = ?");

            statement.setBoolean(1, method.isVerified());
            statement.setString(2, method.getTarget());
            statement.setString(3, method.getPlayer().getUniqueId().toString());
            statement.setString(4, method.getProtocol().toString());

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(CommunicationMethod method){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM communications WHERE subscriberUuid = ? AND protocol = ?");

            statement.setString(1, method.getPlayer().getUniqueId().toString());
            statement.setString(2, method.getProtocol().toString());

            int affectedRows = statement.executeUpdate();

            return affectedRows != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CommunicationMethod getCommunicationMethod(ResultSet resultSet) throws SQLException {

        String subsciberUuid = resultSet.getString("subscriberUuid");
        CommunicationProtocol protocol = CommunicationProtocol.valueOf(resultSet.getString("protocol"));
        String target = resultSet.getString("target");
        String verificationCode = resultSet.getString("verificationCode");
        boolean verified = resultSet.getBoolean("verified");


        OfflinePlayer subscriber = null;

        if(subsciberUuid != null && subsciberUuid != "") {
            UUID uuid = UUID.fromString(subsciberUuid);
            if(uuid != null) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
                if (op != null) {
                    subscriber = op;
                }
            }
        }

        switch(protocol){
            case EMAIL:
                return new EmailNotification(subscriber, verified, verificationCode, target);
            case SMS:
                return new SmsNotification(subscriber, verified, verificationCode, target);
            case INGAME:
                return new IngameNotification(subscriber, verified, verificationCode, target);
            case DISCORD:
                return new DiscordNotification(subscriber, verified, verificationCode, target);
            default:
                return null;
        }
    }
}
