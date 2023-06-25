package com.chatApp.Main;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ManageDiscussion {


    protected Boolean connectionStatus = false;
    String host = "aws.connect.psdb.cloud";
    int port = 3306;
    String database = "mydatabase";
    String dbusername = "eowgxqfakqs6lx3e8d3l";
    String dbpassword = "pscale_pw_MAQf41alYceD6lQHNNvHQv52L9XLl0zCY6zsXAmII05";
//    String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

    String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useCursorFetch=true";


    protected Myconnection checkConn = ConnectionManager.getCheckConn();
    protected int currentUserId = checkConn.id;
    protected int[] idDiscussionsArray;
    protected String[] nameFriendsArray;
    protected int discussionsNumber;

    public void setDiscussion(String username) throws SQLException {


        Connection conn = DriverManager.getConnection(this.url, this.dbusername, this.dbpassword);

        Statement stmt = conn.createStatement();

        String searchQuery = "SELECT id, COUNT(*) AS count FROM users WHERE name = '" + username + "'";
        ResultSet result = stmt.executeQuery(searchQuery);
        if (result.next()) {
            int count = result.getInt("count");
            int receiverId = result.getInt("id");

            String checkDiscussionQuery = "SELECT COUNT(*) AS count FROM discussions WHERE sender_id IN ("+currentUserId+","+receiverId+") AND receiver_id IN ("+currentUserId+","+receiverId+");";
            ResultSet checkingResult = stmt.executeQuery(checkDiscussionQuery);

            if (checkingResult.next()) {
                int checkCounter = checkingResult.getInt("count");
                if (checkCounter == 0) {
                    if (count > 0 && currentUserId != receiverId) {
                        String createDiscussionQuery = "INSERT INTO discussions (sender_id, receiver_id)\n" +
                                "VALUES (" + currentUserId + ", " + receiverId + ");\n";
                        Myconnection connAddDiscussion = new Myconnection("void", createDiscussionQuery, "", "");
//                        System.out.println("done with discussion !");
                        this.connectionStatus = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Other User with name " + username + " doesn't exist! ");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Chat with " + username + " already exist! ");
                }
            }
        }
        conn.close();
        stmt.close();

    }

    public void getDiscussions() throws SQLException {
        Connection conn = DriverManager.getConnection(this.url, this.dbusername, this.dbpassword);
        Statement stmt = conn.createStatement();

        String getDiscussions = "SELECT d.id, d.sender_id, d.receiver_id, u1.name AS sender_name, u2.name AS receiver_name, " +
                "(SELECT COUNT(*) FROM discussions) AS count " +
                "FROM discussions d " +
                "JOIN users u1 ON d.sender_id = u1.id " +
                "JOIN users u2 ON d.receiver_id = u2.id " +
                "WHERE (d.sender_id = '" + currentUserId + "' OR d.receiver_id = '" + currentUserId + "');";


        ResultSet discussions = stmt.executeQuery(getDiscussions);



        HashSet<String> nameFriendSet = new HashSet<>();
        ArrayList<Integer> idDiscussionList = new ArrayList<>();

        while (discussions.next()) {
            this.discussionsNumber = discussions.getInt("count");
            String senderName = discussions.getString("sender_name");
            String receiverName = discussions.getString("receiver_name");

            if (discussions.getInt("sender_id") == currentUserId) {
                nameFriendSet.add(receiverName);
            } else {
                nameFriendSet.add(senderName);
            }

            idDiscussionList.add(discussions.getInt("id"));
        }

        String[] nameFriendArray = nameFriendSet.toArray(new String[0]);
        int[] idDiscussionArray = idDiscussionList.stream().mapToInt(Integer::intValue).toArray();

        this.nameFriendsArray = nameFriendArray;

//        System.out.println(Arrays.toString(this.nameFriendsArray));
        this.idDiscussionsArray = idDiscussionArray;

        conn.close();
        stmt.close();
    }


}
