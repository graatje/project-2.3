package test;

import connection.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConnectionTest {
    public static void main(String[] args) throws IOException {
        Connection c = new Connection("localhost", 7789);

        try {
            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                c.getClient().sendCommandToServer(reader.readLine() + "\n");
            }
        } catch (IOException e) {

        }
    }
}
