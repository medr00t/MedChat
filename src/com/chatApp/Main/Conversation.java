package com.chatApp.Main;


import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conversation extends JFrame {
    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton backButton;
    private Timer refreshTimer;

    public Conversation() throws SQLException {
        setTitle("ChatApp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("resources/chat.png");
        setIconImage(icon.getImage());

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setContentType("text/html");

        messageField = new JTextField(30);
        sendButton = new JButton("Send");
        backButton = new JButton("Back");

        ManageMessages messages = new ManageMessages();
        int currentUserId = messages.currentUserId;
        String friendName = messages.friendName;

        refreshChatArea();

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMsg();
                    refreshChatArea();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMsg();
                    refreshChatArea();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
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
            }
        });

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center the window on the screen

        // Start the timer to refresh the chat area every 30 seconds
        refreshTimer = new Timer(19000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refreshChatArea();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        refreshTimer.start();
    }

    private void refreshChatArea() throws SQLException {
        StyledDocument doc = chatArea.getStyledDocument();
        SimpleAttributeSet userStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(userStyle, Color.RED);
        StyleConstants.setBold(userStyle, true);
        SimpleAttributeSet friendStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(friendStyle, Color.BLACK);
        StyleConstants.setBold(friendStyle, true);

        try {

            ManageMessages messages = new ManageMessages();
            int currentUserId = messages.currentUserId;
            String friendName = messages.friendName;
            ResultSet result = messages.getMessages();
            doc.remove(0, doc.getLength()); // Clear the chat area
            while (result.next()) {
                String msgContent = result.getString("content");
                String timestampString = result.getString("timestamp");

                int msgSenderId = result.getInt("sender_id");

                if (msgSenderId == currentUserId) {
                    doc.insertString(doc.getLength(), "You: " + msgContent + "\n", userStyle);
                } else {
                    doc.insertString(doc.getLength(), friendName + ": " + msgContent + "\n", friendStyle);
                }
                doc.insertString(doc.getLength(), timestampString + "\n", null);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void sendMsg() throws SQLException {
        if (!messageField.getText().equals("")) {
            String message = messageField.getText().trim();
            chatArea.setText(chatArea.getText() + "You: " + message + "\n");
            // server process here !!!

            ManageMessages newMessage = new ManageMessages();
            newMessage.setMessages(message);
            // done
        }
        messageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Conversation conversation = null;
                try {
                    conversation = new Conversation();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                conversation.setSize(300, 500);
                conversation.setLocationRelativeTo(null);
                conversation.setVisible(true);
            }
        });
    }
}
