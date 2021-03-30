package Connection;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Connection {
    private Socket clientSocket;
    private int port;
    private Client client;
    private String serverIP;

    public Connection(String serverIP, int port) {
        this.port = port;
        this.serverIP = serverIP;

        createClient();
    }

    public void createClient() {
        try {
            clientSocket = new Socket(serverIP, port);
            client = new Client(clientSocket, new CommunicationHandler());
            client.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
