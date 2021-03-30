package Connection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {
    private Socket clientSocket;
    private BufferedReader inputStream;
    private int availableData;
    private CommunicationHandler com;
    private PrintWriter outputStream;

    private Boolean running = true;
    private ArrayList<String> commandBuffer = new ArrayList<>();


    /**
     * @param clientSocket The clientsocket
     * @param com          Communication handler which handles the communicationprotocol.
     */
    public Client(Socket clientSocket, CommunicationHandler com) {
        this.clientSocket = clientSocket;
        this.com = com;

        try {
            this.inputStream = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.outputStream = new PrintWriter(this.clientSocket.getOutputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     * Send a command to the server
     *
     * @param command The command to send to the server
     */
    public void sendCommandToServer(String command) {
        outputStream.print(command);
        outputStream.flush();
    }


    /**
     * Reads server messages and sends them off to be handled
     */
    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                String input = inputStream.readLine();

                //There was input, handle it
                if (!input.equals("")) {
                    com.handleServerInput(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cleans up the client before closing down the thread.
     */
    public void dispose() {
        try {
            running = false;
            inputStream.close();
            outputStream.close();

            join();
        } catch (InterruptedException | IOException ie) {
            ie.printStackTrace();
        }
    }
}
