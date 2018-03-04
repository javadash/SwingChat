package swingchat;

import java.io.*;
import java.net.Socket;

/**
 * @author Johnson Olayiwola
 */
public class ClientThread extends Thread {

    public Server server;
    private Socket socket;

    ObjectOutputStream serverOut;
    ObjectInputStream serverIn;

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
                server.broadcast((Message)serverIn.readObject());
            }

        } catch (Exception ex) {ex.printStackTrace();}
        finally {
            try {
                serverIn.close();
                serverOut.close();
                System.out.println("CLientThread's streams are closed");
            } catch (EOFException eofex) {
                System.out.println("EOFException: input stream is closed");
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    public void say(Message message) {
    	
        try {
            serverOut.writeObject(message);
        } catch (Exception ex) {ex.printStackTrace();}
    }

}
