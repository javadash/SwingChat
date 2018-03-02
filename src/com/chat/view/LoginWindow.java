package com.chat.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import socketclient.SocketClient;

public class LoginWindow extends JFrame implements ActionListener {
    private JPanel panel;
    private JButton button;
    private JTextField textFieldIP, textFieldName ;
    private JLabel labelMain, labelIP, labelName ;
    private String userName,IP;

    //constructor for login
    public LoginWindow(){
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        

        panel = new JPanel(grid);
        panel.setBackground(Color.GRAY);
        this.getContentPane().add(panel);

        labelMain = new JLabel("Login");
        textFieldIP = new JTextField();
        textFieldName = new JTextField(20);
        labelIP = new JLabel("IP Address");
        labelName = new JLabel("User Name");
        button = new JButton("Submit");

        c.insets = new Insets(3,3,3,3);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1 ;
        panel.add(labelMain,c);
        
        c.gridx = GridBagConstraints.HORIZONTAL;
        c.gridx = 0 ;
        c.gridy = 1 ;
        c.gridwidth = 1 ;
        c.weightx =  0 ;
        c.ipadx = 0 ;
        panel.add(labelName,c);

        c.gridx = GridBagConstraints.HORIZONTAL;
        c.gridx = 1 ;
        c.gridy = 1 ;
        c.gridwidth = 4 ;
        c.weightx = 0.1 ;
        c.weightx =  0.1 ;
        panel.add(textFieldName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0 ;
        c.gridy = 2 ;
        c.gridwidth = 1 ;
        c.weightx =  0 ;
        panel.add(labelIP,c);

        c.gridx = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 4;
        c.weightx =  0.1 ;
        panel.add(textFieldIP,c);

        /*c.gridx = GridBagConstraints.HORIZONTAL;
        c.gridx = 0 ;
        c.gridy = 2 ;
        c.gridwidth = 1 ;
        c.weightx =  0 ;
        c.ipadx = 0 ;
        panel.add(labelName,c);

        c.gridx = GridBagConstraints.HORIZONTAL;
        c.gridx = 1 ;
        c.gridy = 2 ;
        c.gridwidth = 4 ;
        c.weightx = 0.1 ;

        panel.add(textFieldName,c);*/

        c.gridx = GridBagConstraints.HORIZONTAL;
        c.gridx = 1 ;
        c.gridy = 3 ;
        c.gridwidth = 1 ;
        c.weightx = 0 ;
        c.ipadx =0 ; 
        panel.add(button,c);
        
        this.setSize(225,175);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        button.addActionListener(this);

        WindowAdapter winListener =  new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        
        this.addWindowListener(winListener);
        	
            /*client initial = new client(getUserName());
            initial.go();*/
    }
    
    private Boolean IpValidation(String ipInput)
	{
		
    	Pattern inetPattern = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})(:(\\d{1,5}))?$");
    	Matcher ipMatcher = inetPattern.matcher(ipInput);
		if(!ipMatcher.matches()) {
			JOptionPane.showMessageDialog(null,"Please eneter a Valid IP Address","Error", JOptionPane.ERROR_MESSAGE);
			this.textFieldIP.requestFocusInWindow(); 
			return false;
		}
		return true;
	}
    
    public void actionPerformed (ActionEvent e){
    	if(IpValidation(textFieldIP.getText())){
    		userName = textFieldName.getText();
    		IP = textFieldIP.getText();
    		dispose();
    		SocketClient frame = new SocketClient();
        	frame.setTitle("Chat Window");
        	frame.setVisible(true);
            WindowListener l = new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            };
            frame.addWindowListener(l);
            //frame.setMinimumSize(new Dimension(600,480));
            frame.pack();
            //frame.listenSocket(IP);
    	}
	}

    public String getUserName(){
        return this.userName ;
    }

    public static void main (String[] args) throws Exception {
        LoginWindow frame = new LoginWindow();
        frame.setTitle("Login Window");
        
    }




}