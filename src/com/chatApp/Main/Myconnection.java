package com.chatApp.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class Myconnection {
    public int id;
    protected String queryType;
    protected String query;
    protected Boolean connectionStatus = false;



    public Myconnection(String queryType, String query , String username, String password) {
        this.queryType = queryType;
        this.query = query;
        String host = "aws.connect.psdb.cloud";
        int port = 3306;
        String database = "mydatabase";
        String dbusername = "eowgxqfakqs6lx3e8d3l";
        String dbpassword = "pscale_pw_MAQf41alYceD6lQHNNvHQv52L9XLl0zCY6zsXAmII05";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        // delete * update * create
        if (this.queryType.equals("void")) {

            try {
                // Establish the database connection
                Connection conn = DriverManager.getConnection(url, dbusername, dbpassword);

                Statement stmt = conn.createStatement();

                stmt.executeUpdate(this.query);

                stmt.close();
                conn.close();
                this.connectionStatus = true;
//                System.out.println("Successfully done.");

            } catch (SQLException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        } else if (this.queryType.equals("check")) {        //  ( select) !
            try {
                // Establish the database connection
                Connection conn = DriverManager.getConnection(url, dbusername, dbpassword);

                Statement stmt = conn.createStatement();

                ResultSet resultSet = stmt.executeQuery(this.query);

                while (resultSet.next()) {
                    String RowUsername = resultSet.getString("name");
                    String RowPassword = resultSet.getString("password");
                    this.id = resultSet.getInt("id");

                    if (RowUsername.equals(username)){
                        if(RowPassword.equals(password)){
                            this.connectionStatus = true;
                            break;
                        }
                    }
                }
                stmt.close();
                conn.close();

            } catch (SQLException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }

        }

    }

}
