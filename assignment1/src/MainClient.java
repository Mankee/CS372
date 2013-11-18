import java.io.IOException;
import java.net.Socket;
import java.io.*;


/**
 * Created with IntelliJ IDEA.
 * User: austin.dubina
 * Date: 11/14/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainClient {

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

