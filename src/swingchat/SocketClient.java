
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
    private JFileChooser fc;
    private JTextField inputText;
    private JTextArea chatArea;
    private Socket socket = null;
    
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private BufferedInputStream bis;
    private BufferedOutputStream bos;

    private String userName;
    private String fileName;
    private String filePath;
    private Long fileSize;
    File file;

    public SocketClient() {
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	this.setMinimumSize(new Dimension(640,480));

    	this.setLayout(new GridBagLayout());
    	GridBagConstraints gridLayout = new GridBagConstraints();
        
        chatArea = new JTextArea(12,40);
        inputText = new JTextField(25);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        sendButton = new JButton("Send");
        attachButton = new JButton( "Attach", new ImageIcon(SocketClient.class.getResource("Open16.gif")));
        fc = new JFileChooser();
        
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
        attachButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source == sendButton) {
        
            sendMessage(inputText.getText()); //Create the client message from input text
            inputText.setText(new String(""));
        } else if (source == attachButton) {
        	int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                sendMessage("Sending: " + file.getName());
                sendFile(file);
            }
        }
    }

    public void listenSocket(String userName, String ipAddress) {
    	this.userName = userName;
        //Create socket connection
        try {
            socket = new Socket(ipAddress, 4444);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            //Receive text from server
            sendMessage("[ Joined ]");
            new MessageListener();
        } catch (Exception ex) {ex.printStackTrace();}
    }
    
    // Build a string for the client message
    public void say(Message message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("(yyyy-MM-dd HH:mm)");
        chatArea.append(dateFormat.format(message.time) + " " + message.author + " : " + message.text + "\n");
    }
    
    public void sendMessage(String text) {
        Message message = new Message(this.userName, new Date(), text);
        try {
        	/**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             */
            out.writeObject(message); // Writes object to output stream
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }   
    
    /*send file method*/
	private void sendFile(File file) {
		try {
			bis = new BufferedInputStream(new FileInputStream(file)); 
	        byte[] buffer = new byte[65536];
	        int count;
	        
	        out.writeObject("file"); //send type-string
	        out.writeObject(file.getName()); //send file name
	        out.writeObject(file.length()); //send file's size
	        // Send file data
	        while ((count = bis.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
	        out.flush();
	        /*fileSize = file.length();
	        long current = 0;
	        while(current!=fileSize){ 
	            int size = 65536;
	            if(fileSize - current >= size)
	                current += size;    
	            else{ 
	                size = (int)(fileSize - current); 
	                current = fileSize;
	            }
	            bis.read(buffer, 0, size); 
	            out.write(buffer);
	        }*/
		} catch (FileNotFoundException e) {
			System.out.println("File not found!\n");
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O exception!\n");
	    }
	}
    
    public class MessageListener extends Thread {
        public MessageListener() {
            this.start();
        }
        
        @Override
        public void run() {
        	// Process all messages from server, according to the protocol.
            try {
                while (true) {
                	Object inputString = in.readObject();
                	// 
                	if(!inputString.toString().equals("file")) {
                		say((Message) inputString); //Reads object from input writes message
					} else {
						fileName = (String)in.readObject();
					    fileSize = (Long) in.readObject();
					    filePath = System.getProperty("user.home") + "\\Desktop\\" + fileName;

					    bos = new BufferedOutputStream( new FileOutputStream(filePath));
					    byte[] bytearray = new byte[65536];
					    int count = 0;
					    while ((count = in.read(bytearray)) > 0) {
					        bos.write(bytearray, 0 , count);
					    }
					    System.out.println("File: "+ file +" was sent transferred");
					}
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}