package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: austin.dubina
 * Date: 11/14/13
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainServer {
    public static void main(String args[]) {

        try {
            int serverPortNumber = 30021;
            ServerSocket listenSocket = new ServerSocket(serverPortNumber);
            System.out.println("Listening on port: " + serverPortNumber);

            while(true) {
                Socket clientSocket = listenSocket.accept();
                Connection connection = new Connection(clientSocket);
                System.out.println(connection.clientConnection.getInetAddress().getHostAddress());
            }
        }
        catch(IOException e) {
            System.out.println("Error intializing server: " + e.getMessage());
            System.exit(0);
        }
    }
}
