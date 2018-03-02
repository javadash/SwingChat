/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketclient;

/**
 *
 * @author Johnson Olayiwola
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;

public class SocketClient extends JFrame implements ActionListener {
    private JButton sendButton, attachButton;
    private JPanel panel, panel1;
    private JLabel label;
    private JTextField inputText;
    private JTextArea chatArea;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public SocketClient() {
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	this.setPreferredSize(new Dimension(640,480));
    	//this.setLocationRelativeTo(null);
    	this.setLayout(new GridBagLayout());
    	GridBagConstraints gridLayout = new GridBagConstraints();

    	label = new JLabel("Chat Application");
        panel = new JPanel();
        
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
    } //End Constructor

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source == sendButton) {
        //Send data over socket
            String text = inputText.getText();
            out.println(text);
            inputText.setText(new String(""));
            //Receive text from server
            try {
                String line = in.readLine();
                System.out.println("Text received :" + line);
            } catch (IOException e) {
                System.out.println("Read failed");
                System.exit(1);
            }
        }
    }

/*    public void listenSocket(String serverAddress) {
    	//serverAddress = getServerAddress();
        //Create socket connection
        try {
            socket = new Socket(serverAddress, 4444);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py.eng");
            System.exit(1);
        } catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }*/

/*    public static void main(String[] args) {
    	SocketClient frame = new SocketClient();
    	frame.setTitle("Client Program");
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };

        frame.addWindowListener(l);
        //frame.setMinimumSize(new Dimension(600,480));
        frame.pack();
        frame.setVisible(true);
        frame.listenSocket();
    }*/
}