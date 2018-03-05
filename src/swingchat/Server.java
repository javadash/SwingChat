package swingchat;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Johnson Olayiwola
 */
public class Server {
	private static final int PORT = 4444;
    public ArrayList<ClientThread> clients = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        try {
            ServerSocket socketListener = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket client = null;
                while (client == null) {
                    client = socketListener.accept();
                }
                ClientThread ct = new ClientThread(client);
                ct.server = this;
                clients.add(ct);

            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void broadcast(Message message) {
        for (ClientThread ct : clients) {
            ct.sendMessage(message);
        }
    }
    
    public void broadcast(String fileName) {
        for (ClientThread ct : clients) {
            ct.sendFile(fileName);
        }
    }
}
