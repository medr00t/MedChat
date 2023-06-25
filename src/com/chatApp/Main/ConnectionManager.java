package com.chatApp.Main;

public class ConnectionManager {
    private static Myconnection checkConn;

    public static void setCheckConn(Myconnection connection) {
        checkConn = connection;
    }

    public static Myconnection getCheckConn() {
        return checkConn;
    }
}
