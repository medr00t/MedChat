package com.chatApp.Main;

public class FriendManager {
    private static String friendName;

    public  FriendManager(String name) {
        friendName = name;
    }

    public static String getFriendName() {
        return friendName;
    }
}
