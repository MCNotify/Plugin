package org.zonex.datastore;

import org.zonex.datastore.dbModels.DbAreaTable;
import org.zonex.config.Configuration;
import org.zonex.datastore.dbModels.DbCommunicationTable;
import org.zonex.datastore.dbModels.DbSubscriptionTable;

import java.sql.*;
import java.util.Scanner;

public class Database {

    private String host = Configuration.DATABASE_HOST.getValue();
    private String port = Configuration.DATABASE_PORT.getValue();
    private String username = Configuration.DATABSE_USERNAME.getValue();
    private String password = Configuration.DATABASE_PASSWORD.getValue();
    private String databaseName = Configuration.DATABASE_DATABASE.getValue();

    private Connection connection;
    private boolean isConnected = false;

    public Database() {
        System.out.println("[MCNotify] Attempting to connect to database...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/", this.username, this.password);

            System.out.println("[MCNotify] Successfully connected to database.");
            System.out.println("[MCNotify] Checking database tables...");

            // Attempt to create the MCNotify datastore
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `MCNotify`");

            connection.setCatalog("MCNotify");
            System.out.println("[MCNotify] Successfully linked with `MCNotify` database.");

            if(connection != null && !connection.isClosed()) {
                this.check_migrations();
            }

            this.isConnected = true;

        } catch (SQLException e) {
            System.out.println("[MCNotify] WARNING: Unable to connect to database! Falling back to flat file.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void check_migrations(){

        // List of migration scripts.
        String[] migrations = {"init.sql"};


        int start_id = 0;

        // Check if the migration table exists.
        System.out.println("[MCNotify] Checking migrations...");
        try {
            // Access the datastore's migration table to see if it exists.
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet results = meta.getTables(null, null, "Migrations", new String[]{"TABLE"});
            if(results.next()){
                if(results.getString("TABLE_NAME").equals("migrations")){
                    System.out.println("[MCNotify] Migration table exists.");
                }

                // If table exists, determine the last migration that was performed.

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Migrations ORDER BY id DESC");
                ResultSet migrationResult = statement.executeQuery();

                if(migrationResult.next()){
                    start_id = migrationResult.getInt("id");
                }
            } else {
                System.out.println("[MCNotify] Migration table does not exist.");
            }

            // From the last migration, loop through the migration scripts and execute them.
            for(int i = start_id; i < migrations.length; i++){
                this.runScript("migrations/" + migrations[i]);
            }


            System.out.println("[MCNotify] Migrations Completed.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return this.isConnected;
    }

    public Connection getConnection(){
        if(this.isConnected) {
            return connection;
        } else {
            return null;
        }
    }

    public DbAreaTable areaTable(){
        if(this.isConnected) {
            return new DbAreaTable(connection);
        } else {
            return null;
        }
    }

    public DbSubscriptionTable subscriptionTable(){
        if(this.isConnected) {
            return new DbSubscriptionTable(connection);
        } else {
            return null;
        }
    }

    public DbCommunicationTable communicationTable(){
        if(this.isConnected) {
            return new DbCommunicationTable(connection);
        } else {
            return null;
        }
    }

    private void runScript(String pathToScript){
        System.out.println("[MCNotify] Attempting to run " + pathToScript);
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
                System.out.println("[MCNotify] SQL Error when executing " + pathToScript + ". " + e.getMessage());
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
