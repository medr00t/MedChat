package com.chatApp.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class userLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public void submit() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);


        // Perform authentication logic here

        String query = "SELECT id, name, password FROM users";

        // db connection
        Myconnection checkConn = new Myconnection("check", query, username, password);
        ConnectionManager.setCheckConn(checkConn);

        if (checkConn.connectionStatus) {
            JOptionPane.showMessageDialog(null, "Login successful!");
            dispose();

            // Run the interface
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    discussions chatApp = null;
                    try {
                        chatApp = new discussions();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    chatApp.setSize(300, 500);
                    chatApp.setLocationRelativeTo(null);
                    chatApp.setVisible(true);
                }
            });

        } else {
            JOptionPane.showMessageDialog(null, "Login Unsuccessful *_*");
        }

        // Clear the fields after login attempt
        usernameField.setText("");
        passwordField.setText("");
    }

    public userLogin() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        ImageIcon icon = new ImageIcon("resources/chat.png");
        setIconImage(icon.getImage());

        JButton loginButton = new JButton("Login");
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Listen to the Enter key When it's pressed
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submit();
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                // Open registration window
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        userRegister register = new userRegister();
                        register.setSize(300, 200);
                        register.setLocationRelativeTo(null);
                        register.setVisible(true);
                    }
                });
            }
        });

        JPanel panel = new JPanel(new GridLayout(8, 4));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);

        pack();
        setLocationRelativeTo(null); // Center the window on the screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userLogin login = new userLogin();
                login.setVisible(true);
            }
        });
    }
}
