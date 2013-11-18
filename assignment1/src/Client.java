import java.io.IOException;
import java.net.Socket;
import java.io.*;


/**
 * Author: Austin Dubina
 * Date: 11/14/13
 * Readme: This is the client application that will connect and communicate with the server processt. To compile this application simply type javac MainClient.java
 * (for jdk 1.6 or higher). Run this application by typing java MainClient in the terminal. The server application must be running first or the connection
 * will be refused.
 *
 * List of available commands get <file path> | list <directory path>  | close
 *
 * get <file pathname>: retrieves a .txt file up to 1024 bytes in size. eg file pathname "/users/austin.dubina/test.txt"
 * list <directory pathname>: retrieves a list of all files and sub-directories within the specified pathname. eg directory pathname "/users/austin.dubina"
 * close: terminates both the server and client process but closes all input and output streams and performing a system exit with code "0"
 *
 * Note: please insure that both the server and client application are running on the same host machine. IE local hostname matches both shells. To check this simply type
 * "hostname" into the shell.
 */
public class Client {

    public static void main(String srgs[]) {
        Socket socket = null;
        BufferedReader serverInputStream = null;
        PrintWriter serverOutputStream = null;

        try
        {
            socket = new Socket("localhost",30021);
            serverInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverOutputStream = new PrintWriter(socket.getOutputStream(),true);

        }
        catch(Exception e)
        {
            System.out.println("Failed to connect to server: " + e.getMessage());
            System.exit(0);
        }
        BufferedReader consoleInputStream = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Client: Please enter a command");
            String consoleInput = null;

            try {
                consoleInput = consoleInputStream.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                serverOutputStream.println(consoleInput);
                while(!serverInputStream.ready()){}
                String message = serverInputStream.readLine();
                if (message.equalsIgnoreCase("sendingList")) {
                    while(!serverInputStream.ready()){}
                    message = serverInputStream.readLine();
                    while (!message.equalsIgnoreCase("eof")) {
                        System.out.println(message);
                        message = serverInputStream.readLine();
                    }
                }
                else if (message.equalsIgnoreCase("sendingFile")) {
                    File file = new File("output_file");

                    BufferedInputStream fileInputStream = new BufferedInputStream(socket.getInputStream());
                    BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(file));
                    byte buffer[] = new byte[1024];
                    int read = fileInputStream.read(buffer);
                        outStream.write(buffer, 0, read);
                        outStream.flush();
                }
                else if (message.equalsIgnoreCase("shuttingDown")) {
                    System.out.println("Client: Server is shutting down, So am I... Goodbye!");
                    serverInputStream.close();
                    serverInputStream.close();
                    socket.close();
                    System.exit(0);
                }
                else {
                    System.out.println(message);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

