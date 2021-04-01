package Connection;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    private Socket clientSocket;
    private int port;
    private Client client;
    private String serverIP;


    /**
     * Sets up the connection and creates a client
     *
     * @param serverIP Ip-Address of the server
     * @param port     Port on which the server is listening
     */
    public Connection(String serverIP, int port) throws IOException {
        this.port = port;
        this.serverIP = serverIP;

        createClient();
    }


    /**
     * Creates a Client to start handling communication
     */
    private void createClient() throws IOException {
        clientSocket = new Socket(serverIP, port);
        client = new Client(clientSocket, new CommunicationHandler());
        client.setDaemon(true);
        client.start();
        client.setDaemon(true);
    }

    public Client getClient() {
        return client;
    }
}
