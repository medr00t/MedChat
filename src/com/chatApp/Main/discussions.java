package com.chatApp.Main;


import org.w3c.dom.ls.LSOutput;

import javax.naming.Name;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class discussions extends JFrame {

    protected JPanel gridPanel;
    protected String buttonName;
    protected String[] names;
    protected int[] discussionsIDs;


    protected void attachButtonListener(JButton button) {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource();
                discussions.this.buttonName = clickedButton.getText(); // Use discussions.this.friendName
                FriendManager friend = new FriendManager(discussions.this.buttonName);

//                System.out.println(discussions.this.buttonName);


                dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Conversation chatApp = null;
                        try {
                            chatApp = new Conversation();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        chatApp.setSize(300, 500);
                        chatApp.setLocationRelativeTo(null);
                        chatApp.setVisible(true);
                    }
                });
            }
        });
    }

    public discussions() throws SQLException {
        setTitle("Chats");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Use null layout for absolute positioning

        // Create the grid panel
        gridPanel = new JPanel(new GridLayout(0, 1)); // 0 rows, 1 column

        JLabel head = new JLabel("Chats");
        head.setFont(new Font("Arial", Font.BOLD, 32)); // Set font size
        head.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
        gridPanel.add(head);
        ImageIcon icon = new ImageIcon("resources/chat.png");
        setIconImage(icon.getImage());


        JButton logoutButton = new JButton("Log Out");
        logoutButton.setBounds(220, 10, 80, 30); // Set position and size of the button

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform log out logic here
                // For example, dispose the current frame and open the login window
                dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        userLogin login = new userLogin();
                        login.setSize(300, 200);
                        login.setLocationRelativeTo(null);
                        login.setVisible(true);
                    }
                });
            }
        });

        add(logoutButton);

        JScrollPane scrollPane = new JScrollPane(gridPanel); // Wrap the grid panel in a scroll pane
        scrollPane.setBounds(10, 10, 280, 400); // Set position and size of the scroll pane
        add(scrollPane);


        //
        ManageDiscussion discussion = new ManageDiscussion();
        discussion.getDiscussions();
        this.discussionsIDs = discussion.idDiscussionsArray;

        int discussionsNumber = discussion.discussionsNumber;
        String[] Names = discussion.nameFriendsArray;
        this.names = Names;


        for (int i = 0; i < discussionsNumber; i++) {
            if (i < Names.length) {
                JButton button = new JButton(String.valueOf(Names[i]));
                button.setFont(new Font("Arial", Font.BOLD, 16)); // Set font size
                button.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
                gridPanel.add(button);
                attachButtonListener(button); // Attach listener to the new button

            }
        }

        gridPanel.revalidate();


        // Add "New Message" button
        JButton newMessageButton = new JButton("New Message");
        newMessageButton.setBounds(10, 420, 150, 30); // Set position and size of the button

        newMessageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input dialog for username
                String username = JOptionPane.showInputDialog(discussions.this, "Enter username:");
                if (username != null && !username.isEmpty()) {
                    try {
                        ManageDiscussion discussion = new ManageDiscussion();
                        discussion.setDiscussion(username);
                        if (discussion.connectionStatus) {
                            JButton button = new JButton(username);
                            button.setFont(new Font("Arial", Font.BOLD, 16)); // Set font size
                            button.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
                            gridPanel.add(button);
                            attachButtonListener(button); // Attach listener to the new button
                            gridPanel.revalidate(); // Update the layout of the panel

                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        add(newMessageButton);

        setSize(300, 500);
        setLocationRelativeTo(null); // Center the window on the screen

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                discussions discussion = null;
                try {
                    discussion = new discussions();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                discussion.setVisible(true);
            }
        });
    }
}
