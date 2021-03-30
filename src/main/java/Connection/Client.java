package Connection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client extends Thread {
    Socket clientSocket;
    InputStream inputStream;
    int availableData;

    CommunicationHandler com;


    public Client(Socket clientSocket, CommunicationHandler com) {
        this.clientSocket = clientSocket;
        this.com = com;
    }

    @Override
    public void run() {
        byte[] data = new byte[8192];

        try {
            inputStream = new BufferedInputStream(this.clientSocket.getInputStream(), 65536);
            String content = "";

            while ((availableData = inputStream.read(data)) != -1) {
                content = new String(data, 0, availableData);
                com.readServerInput(content);
            }
            inputStream.close();

            dispose();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void dispose() {
        try {
            join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
