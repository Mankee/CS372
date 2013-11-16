package server;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: austin.dubina
 * Date: 11/16/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Connection extends Thread {
        public DataInputStream input;
        public DataOutputStream output;
        public Socket clientConnection;

        public Connection(Socket aclientConnection) {
            try {
                clientConnection = aclientConnection;
                input = new DataInputStream(aclientConnection.getInputStream());
                output = new DataOutputStream(aclientConnection.getOutputStream());
            }
            catch (IOException e) {
                System.out.println("Error instantiating connection object: " + e.getMessage());
            }
        }

        public void run() {
            try {
                System.out.println(clientConnection.getInetAddress().getHostName());
                PrintWriter put = new PrintWriter(clientConnection.getOutputStream(),true);
                BufferedReader clientCommand = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
                String string = clientCommand.readLine();
                System.out.println("The client command is: " + string);


            } catch(IOException e) {
                System.out.println("Failed to extablish connection: " + e.getMessage());
                System.exit(1);
            }
        }
}

