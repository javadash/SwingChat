package swingchat;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author Johnson Olayiwola
 */
public class ClientThread extends Thread {
	
	private String fileName;
	private Long fileLength;
	private String filePath;

    public Server server;
    private Socket socket;

    private ObjectOutputStream serverOut;
    private ObjectInputStream serverIn;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;

    ClientThread(Socket socket) {
        this.socket = socket;
        this.start();
    }

    @Override
    public void run() {
        try {

            serverOut = new ObjectOutputStream(socket.getOutputStream());
            serverIn = new ObjectInputStream(socket.getInputStream());
 
            while (true) {
            	Object inputString = serverIn.readObject();
            	if(!inputString.toString().equals("file")) {
            		server.broadcast((Message) inputString);
				} else {
					receiveFile();
					server.broadcast(this.filePath);
				}
            }
            
        } catch (Exception ex) {ex.printStackTrace();}
        
    }

    public void sendMessage(Message message) {
    	
        try {
            serverOut.writeObject(message);
        } catch (Exception ex) {ex.printStackTrace();}
    }
    
    
    /*send file method*/
	public void sendFile(String filePath) {
		try {
			File file = new File(filePath);
			bis = new BufferedInputStream(new FileInputStream(file)); 
	        byte[] buffer = new byte[65536];
	        int count;
	        
	        serverOut.writeObject("file"); //send type-string
	        serverOut.writeObject(file.getName()); //send file name
	        serverOut.writeObject(file.length()); //send file's size
	        // Send file data
	        while ((count = bis.read(buffer)) > 0) {
                serverOut.write(buffer, 0, count);
            }
		} catch (FileNotFoundException e) {
			System.out.println("File not found!\n");
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O exception");
	    }
	}
    
	private void receiveFile(){
		try {
			fileName = (String) serverIn.readObject();
			fileLength = (Long) serverIn.readObject();
			byte[] buffer = new byte[65536];
			this.filePath = System.getProperty("user.home") + "\\Desktop\\" + fileName;
			System.out.println(filePath);
			bos = new BufferedOutputStream(new FileOutputStream(filePath));
			int count;
			
			/*while ((count = serverIn.read(buffer)) > 0) {
		        bos.write(buffer, 0 , count);
			}*/
			
			/*long current = 0;
			while (current < fileLength && (count = serverIn.read(buffer, 0, fileLength-current > buffer.length ? buffer.length : (int)(fileLength-current))) > 0)
			{
			  bos.write(buffer, 0, count);
			  current += count;
			}*/
			
			while ((count = serverIn.read(buffer)) > 0) {
				  bos.write(buffer, 0, count);
			}
			bos.flush();
		} catch (SocketException e1) {
			System.out.println("Socket Problems!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
    public void say(String message) {
        try {
            serverOut.writeObject(message);
        } catch (Exception ex) {ex.printStackTrace();}
    }
    
    protected void finalize() {
        //Clean up
        try {
            serverIn.close();
            serverOut.close();
        } catch (IOException e) {
            System.out.println("Could not close.");
            System.exit(-1);
        }
    }

}
