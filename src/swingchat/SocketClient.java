/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swingchat;

/**
 *
 * @author Johnson Olayiwola
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketClient extends JFrame implements ActionListener {
    private JButton sendButton, attachButton;
    //private JLabel label;
    private JTextField inputText;
    private JTextArea chatArea;
    private Socket socket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    private String userName;
//    private String ipAddress;

    public SocketClient() {
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	this.setMinimumSize(new Dimension(640,480));
    	//this.setVisible(true);
    	//this.setTitle("Chat Window");

    	this.setLayout(new GridBagLayout());
    	GridBagConstraints gridLayout = new GridBagConstraints();
        
        chatArea = new JTextArea(12,40);
        inputText = new JTextField(25);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        sendButton = new JButton("Send");
        attachButton = new JButton( "Attach");
        
        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        
        gridLayout.gridx = 0;
        gridLayout.gridy = 0;
        gridLayout.weightx = 1;
        gridLayout.weighty = 1;
        gridLayout.gridwidth = 4;
        gridLayout.fill = GridBagConstraints.BOTH;
        gridLayout.insets = new Insets(5, 5, 5, 5);
		this.add(scroll, gridLayout);
        
        
		gridLayout.gridx = 0;
		gridLayout.gridy = 4;
		gridLayout.weighty = 0;
		gridLayout.gridwidth = 2;
		this.add(inputText, gridLayout);
		
		gridLayout.gridx = 2;
		gridLayout.gridy = 4;
		gridLayout.weightx = 0;
		gridLayout.gridwidth = 1;
		this.add(attachButton, gridLayout);
		
		gridLayout.gridx = 3;
		gridLayout.gridy = 4;
		gridLayout.weightx = 0;
		gridLayout.gridwidth = 1;
		this.add(sendButton, gridLayout);
		
        sendButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
    	send(inputText.getText());
        inputText.setText(new String(""));
        /*Object source = event.getSource();
        if(source == sendButton) {
        //Create the client message from input text
            send(inputText.getText());
            inputText.setText(new String(""));
        }*/
    }

    public void listenSocket(String userName, String ipAddress) {
    	this.userName = userName;
        //Create socket connection
        try {
            socket = new Socket(ipAddress, 4444);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            //Receive text from server
            send("[ Joined ]");
            new MessageListener();
        } catch (Exception ex) {ex.printStackTrace();}
    }
    
    // Build a string for the client message
    public void say(Message message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("(yyyy-MM-dd HH:mm)");
        chatArea.append(dateFormat.format(message.time) + " " + message.author + " : " + message.text + "\n");
    }
    
    public void send(String text) {
        Message message = new Message(this.userName, new Date(), text);
        try {
            out.writeObject(message);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
    
    public class MessageListener extends Thread {

        public MessageListener() {
            this.start();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    say((Message) in.readObject());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}