package com.chatApp.Main;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Arrays;
import java.util.Objects;

public class ManageMessages {
    protected String  friendName = FriendManager.getFriendName();
    protected int discussionId;
    protected Myconnection checkConn = ConnectionManager.getCheckConn();
    protected int currentUserId = checkConn.id;
    protected int receiverId;
    protected Boolean connectionStatus = false;
    String host = "aws.connect.psdb.cloud";
    int port = 3306;
    String database = "mydatabase";
    String dbusername = "eowgxqfakqs6lx3e8d3l";
    String dbpassword = "pscale_pw_MAQf41alYceD6lQHNNvHQv52L9XLl0zCY6zsXAmII05";
    String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
    protected String[] names;

    public ManageMessages() throws SQLException {
//        System.out.println("this is "+ friendName);

        ManageDiscussion discussion = new ManageDiscussion();
        discussion.getDiscussions();
        int[] discussionsIDs = discussion.idDiscussionsArray;
        this.names = discussion.nameFriendsArray;



        Connection conn = DriverManager.getConnection(this.url, this.dbusername, this.dbpassword);

        Statement stmt = conn.createStatement();

        String query = "\n" +
                "SELECT d.id AS discussionId, u1.name AS sender_name, u2.name AS receiver_name\n" +
                "FROM discussions d\n" +
                "JOIN users u1 ON d.sender_id = u1.id\n" +
                "JOIN users u2 ON d.receiver_id = u2.id\n" +
                "WHERE (d.sender_id = "+this.currentUserId+" OR d.receiver_id = "+this.currentUserId+")\n" +
                "  AND (u1.name = '"+this.friendName+"' OR u2.name = '"+this.friendName+"');";

        ResultSet result = stmt.executeQuery(query);

        while(result.next()){
            this.discussionId = result.getInt("discussionId");
        }



    }

    private void getReceiverId() throws SQLException {
        Connection conn = DriverManager.getConnection(this.url, this.dbusername, this.dbpassword);

        Statement stmt = conn.createStatement();
        String username = this.friendName;
        String getIdQuery = "SELECT id FROM users WHERE name = '"+ username +"'";
        ResultSet result = stmt.executeQuery(getIdQuery);

        while(result.next()){
            this.receiverId = result.getInt("id");
        }


    }




    public void setMessages(String content) throws SQLException {
        getReceiverId();
        Connection conn = DriverManager.getConnection(this.url, this.dbusername, this.dbpassword);

        Statement stmt = conn.createStatement();



        String setMessage = "INSERT INTO messages (discussion_id, sender_id, receiver_id, content)\n" +
                "VALUES (" + this.discussionId + ", " + this.currentUserId + "," + this.receiverId + ", '" + content + "');";
        stmt.executeUpdate(setMessage);
//        System.out.println(discussionId);
//        System.out.println(currentUserId);
//        System.out.println(receiverId);
//        System.out.println("me");

    }







    public ResultSet getMessages() throws SQLException {
        Connection conn = DriverManager.getConnection(this.url, this.dbusername, this.dbpassword);

        Statement stmt = conn.createStatement();

        String searchQuery = "SELECT content, sender_id, timestamp\n" +
                "FROM messages\n" +
                "WHERE discussion_id = " + this.discussionId + "\n" +
                "ORDER BY timestamp ASC;";


        ResultSet result = stmt.executeQuery(searchQuery);

    return result;
    }

}
