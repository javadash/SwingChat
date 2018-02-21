/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package socketclient;

/**
 *
 * @author james.murphy
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

class SocketClient extends JFrame implements ActionListener {
    private JLabel text, clicked;
    private JButton button;
    private JPanel panel;
    private JTextField textArea;
    private JTextArea chatDisplay;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public SocketClient() { //Begin Constructor
    	chatDisplay = new JTextArea();
    	chatDisplay.setEditable(false);
    	chatDisplay.setLineWrap(true);
    	textArea = new JTextField(20);
        button = new JButton("Send");
        text = new JLabel("Text to send over socket:");
        button.addActionListener(this);
    	
    	panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	panel.setBackground(Color.white);
    	getContentPane().add(panel);
    	//GridBagConstraints ourGrid = new GridBagConstraints();
    	GridBagConstraints ourGrid = new GridBagConstraints(0,0,2,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    	JScrollPane chatScroller = new JScrollPane(chatDisplay);
		chatScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		chatScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
/*    	ourGrid.gridx = 0;
		ourGrid.gridy = 0;
		ourGrid.weightx = 1;
		ourGrid.weighty = 1;
		ourGrid.gridwidth = 1;
		ourGrid.fill = GridBagConstraints.BOTH;
		ourGrid.insets = new Insets(5, 5, 5, 5);*/
    	panel.add(chatScroller, ourGrid);
		
		
    	
		ourGrid.gridwidth = 1;
		ourGrid.gridy = 1; 
		ourGrid.weighty = 0.25;
    	panel.add(textArea, ourGrid);
    	
    	ourGrid.gridx = 1;
    	ourGrid.weightx = 0.2;
        panel.add(button, ourGrid);
    	
    	//panel.setMinimumSize(new Dimension(600,480));
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    	
        
        
        
        
        
        
        

        
        
        /*panel.add("North", text);
        panel.add("Center", textArea);
        panel.add("South", button);*/
        
        
    } //End Constructor

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source == button) {
        //Send data over socket
            String text = textArea.getText();
            out.println(text);
            textArea.setText(new String(""));
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

    public void listenSocket() {
        //Create socket connection
        try {
            socket = new Socket("10.53.146.173", 4444);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: kq6py.eng");
            System.exit(1);
        } catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SocketClient frame = new SocketClient();
	frame.setTitle("Client Program");
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };

        frame.addWindowListener(l);
        frame.setMinimumSize(new Dimension(600,480));
        frame.pack();
        frame.setVisible(true);
	frame.listenSocket();
    }
}