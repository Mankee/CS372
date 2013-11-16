package customClasses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: austin.dubina
 * Date: 11/16/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Connection extends Thread {
        DataInputStream input;
        DataOutputStream output;
        Socket clientSocket;

        public Connection(Socket aClientSocket) {
            try {
                clientSocket = aClientSocket;
                input = new DataInputStream(clientSocket.getInputStream());
                output = new DataOutputStream(clientSocket.getOutputStream());
            }
            catch (IOException e) {
                System.out.println("Error instantiating connection object: " + e.getMessage());
            }

        }
}

