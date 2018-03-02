package swingchat;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by aldartron on 02.02.17.
 */
public class Server {

    public ArrayList<ClientThread> clients = new ArrayList<>();

    public static void main(String[] args) {

        Server server = new Server();
        server.start();
    }

    private void start() {
        try {
            ServerSocket socketListener = new ServerSocket(4444);

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
        // Рассылка сообщений
        for (ClientThread ct : clients) {
            ct.say(message);
        }
    }
}
