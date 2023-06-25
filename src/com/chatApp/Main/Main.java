package com.chatApp.Main;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userLogin userLogin = new userLogin();
                userLogin.setVisible(true);
                userLogin.setLocationRelativeTo(null);

            }
        });
    }
}
