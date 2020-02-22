package org.zonex.datastore;

import org.zonex.datastore.dbModels.DbAreaTable;
import org.zonex.config.Configuration;
import org.zonex.datastore.dbModels.DbCommunicationTable;
import org.zonex.datastore.dbModels.DbSubscriptionTable;

import java.sql.*;
import java.util.Scanner;

/**
 * Jdbc database connector
 */
public class Database {

    // Uses the configuration files settings when created.
    private String host = Configuration.DATABASE_HOST.getValue();
    private String port = Configuration.DATABASE_PORT.getValue();
    private String username = Configuration.DATABSE_USERNAME.getValue();
    private String password = Configuration.DATABASE_PASSWORD.getValue();
    private String databaseName = Configuration.DATABASE_DATABASE.getValue();

    private Connection connection;
    private boolean isConnected = false;

    /**
     * Creates a new database connection using configuration file parameters.
     */
    public Database() {
        System.out.println("[ZoneX] Attempting to connect to database...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/", this.username, this.password);

            System.out.println("[ZoneX] Successfully connected to database.");
            System.out.println("[ZoneX] Checking database tables...");

            // Attempt to create the MCNotify datastore
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `ZoneX`");

            connection.setCatalog("ZoneX");
            System.out.println("[ZoneX] Successfully linked with `ZoneX` database.");

            if(connection != null && !connection.isClosed()) {
                this.check_migrations();
            }

            this.isConnected = true;

        } catch (SQLException e) {
            System.out.println("[ZoneX] WARNING: Unable to connect to database! Falling back to flat file.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if any database migrations need to be applied. If migrations need to be applied it will apply them
     */
    private void check_migrations(){

        // List of migration scripts.
        String[] migrations = {"init.sql"};


        int start_id = 0;

        // Check if the migration table exists.
        System.out.println("[ZoneX] Checking migrations...");
        try {
            // Access the datastore's migration table to see if it exists.
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet results = meta.getTables(null, null, "Migrations", new String[]{"TABLE"});
            if(results.next()){
                if(results.getString("TABLE_NAME").equals("migrations")){
                    System.out.println("[ZoneX] Migration table exists.");
                }

                // If table exists, determine the last migration that was performed.

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Migrations ORDER BY id DESC");
                ResultSet migrationResult = statement.executeQuery();

                if(migrationResult.next()){
                    start_id = migrationResult.getInt("id");
                }
            } else {
                System.out.println("[ZoneX] Migration table does not exist.");
            }

            // From the last migration, loop through the migration scripts and execute them.
            for(int i = start_id; i < migrations.length; i++){
                this.runScript("migrations/" + migrations[i]);
            }


            System.out.println("[ZoneX] Migrations Completed.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the database connection was successfully connected
     * @return if the database is connected
     */
    public boolean isConnected(){
        return this.isConnected;
    }

    /**
     * Gets the database connection
     * @return the database connection
     */
    public Connection getConnection(){
        if(this.isConnected) {
            return connection;
        } else {
            return null;
        }
    }

    /**
     * Returns the area table
     * @return the area table
     */
    public DbAreaTable areaTable(){
        if(this.isConnected) {
            return new DbAreaTable(connection);
        } else {
            return null;
        }
    }

    /**
     * Returns the subscription table
     * @return the subscription table
     */
    public DbSubscriptionTable subscriptionTable(){
        if(this.isConnected) {
            return new DbSubscriptionTable(connection);
        } else {
            return null;
        }
    }

    /**
     * Returns the communication table
     * @return the communication table
     */
    public DbCommunicationTable communicationTable(){
        if(this.isConnected) {
            return new DbCommunicationTable(connection);
        } else {
            return null;
        }
    }

    /**
     * Executes an .sql script on the database. This method is used for migrations
     * @param pathToScript The path to the script to execute
     */
    private void runScript(String pathToScript){
        System.out.println("[ZoneX] Attempting to run " + pathToScript);
        Scanner s = null;
        s = new Scanner(getClass().getResourceAsStream(pathToScript));
        s.useDelimiter("(;(\r)?\n)|(--\n)");
            Statement st = null;
            try
            {
                st = connection.createStatement();
                while (s.hasNext())
                {
                    String line = s.next();
                    if (line.startsWith("/*!") && line.endsWith("*/"))
                    {
                        int i = line.indexOf(' ');
                        line = line.substring(i + 1, line.length() - " */".length());
                    }

                    if (line.trim().length() > 0)
                    {
                        st.execute(line);
                    }
                }
            } catch (SQLException e) {
                System.out.println("[ZoneX] SQL Error when executing " + pathToScript + ". " + e.getMessage());
            } finally
            {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
}
