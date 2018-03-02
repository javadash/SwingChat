/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ignore;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import swingchat.Message;

public class SocketServer{
	private static final int PORT = 4444;
	private ArrayList<Handler> threads = new ArrayList<>();
    private ServerSocket server = null;
    private Socket socket = null;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public SocketServer() {
		try {
			server = new ServerSocket(PORT);
			while(true){
				while (socket == null) {
					socket = server.accept();
				}
				Handler handlerThread = new Handler(socket);
				threads.add(handlerThread);
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
    }

    public void broadcast(Message message) {
        for (Handler clientThread : threads) {
            clientThread.say(message);
        }
    }
    
    public class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
            this.start();
        }

        @Override
        public void run() {
            try {
            	reader = new ObjectInputStream(socket.getInputStream());
            	writer = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    broadcast((Message)reader.readObject());
                }
            } catch (Exception ex) {ex.printStackTrace();}
            finally {
                try {
                    reader.close();
                    writer.close();
                    System.out.println("ClientThread's streams are closed");
                } catch (EOFException eofex) {
                    System.out.println("EOFException: input stream is closed");
                } catch (Exception ex) {ex.printStackTrace();}
            }
        }

        public void say(Message message) {
            try {
                writer.writeObject(message);
            } catch (Exception ex) {ex.printStackTrace();}
        }

    }
 
/*    protected void finalize() {
        //Clean up
        try {
            reader.close();
            writer.close();
            server.close();
        } catch (IOException e) {
            System.out.println("Could not close.");
            System.exit(-1);
        }
    }
*/
    /**
     * The appplication main method, which just listens on a port and
     * spawns handler threads.
     */
//    public static void main(String[] args) {
//    	System.out.println("The chat server is running.");
//        SocketServer chatServer = new SocketServer();
//    }
}