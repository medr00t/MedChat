package com.chatApp.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class userRegister extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public void register() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        // Validate required fields
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a username and password.");
            return;
        }

        // Perform registration logic here

        String query = "INSERT INTO users (name, password) VALUES ('" + username + "', '" + password + "')";

        // db connection
        Myconnection checkConn = new Myconnection("void", query, username, password);
        ConnectionManager.setCheckConn(checkConn);

        if (checkConn.connectionStatus) {
            JOptionPane.showMessageDialog(null, "Registration successful!");
            dispose();

            // Open login window after successful registration
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    userLogin login = new userLogin();
                    login.setSize(300, 200);
                    login.setLocationRelativeTo(null);
                    login.setVisible(true);
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Registration Unsuccessful *_*\n" + "Try another Username!");
        }

        // Clear the fields after registration attempt
        usernameField.setText("");
        passwordField.setText("");
    }


    public userRegister() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        ImageIcon icon = new ImageIcon("resources/chat.png");
        setIconImage(icon.getImage());


        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                userLogin login = new userLogin();
                login.setSize(300, 200);
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            }
        });

        JPanel panel = new JPanel(new GridLayout(8, 4));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(backButton);

        add(panel);

        pack();
        setLocationRelativeTo(null); // Center the window on the screen
    }
}