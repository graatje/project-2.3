package Connection;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Connection {
    private Socket clientSocket;
    private int port;
    private Client client;
    private String serverIP;


    /**
     * Sets up the connection and creates a client
     * @param serverIP Ip-Address of the server
     * @param port     Port on which the server is listening
     */
    public Connection(String serverIP, int port) {
        this.port = port;
        this.serverIP = serverIP;

        createClient();
    }


    /**
     * Creates a Client to start handling communication
     */
    private void createClient() {
        try {
            clientSocket = new Socket(serverIP, port);
            client = new Client(clientSocket, new CommunicationHandler());
            client.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Client getClient() {
        return client;
    }
}
