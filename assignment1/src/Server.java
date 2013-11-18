import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author: Austin Dubina
 * Date: 11/14/13
 * Readme: This is the server application that will connect and communicate with the client process. To compile this application simply type javac MainServer.java
 * (for jdk 1.6 or higher). Run this application by typing java MainServer in the terminal. This  server application must be running first or the connection
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
public class Server {
    private static void startService(Socket clientSocket, BufferedReader incomingMessage, PrintWriter outgoingMessage) {
        while (clientSocket.isConnected()) {
            try {
                String clientInputStream = readBuffer(incomingMessage);

                if (isValidCommand(clientInputStream) && clientInputStream != null) {
                        String[] clientArgs = parseInput(clientInputStream);
                        String clientCommand = clientArgs[0];
                        String pathname = clientArgs[1];
                        File folder = new File(pathname).getCanonicalFile();

                    if (!folder.exists()) {
                        outgoingMessage.println("Server: Invaild pathname");
                    }
                    else {
                        if (clientCommand.equalsIgnoreCase("get") && clientCommand.length() > 0 && clientCommand != null) {
                            if (folder.isFile()) {
                                outgoingMessage.println("sendingFile");
                                BufferedInputStream fileData = new BufferedInputStream(new FileInputStream(folder));
                                BufferedOutputStream outStream = new BufferedOutputStream(clientSocket.getOutputStream());
                                byte buffer[] = new byte[1024];
                                int read;
                                while((read = fileData.read(buffer))!=-1)
                                {
                                    outStream.write(buffer, 0, read);
                                    outStream.flush();
                                }
                            } else {
                                outgoingMessage.println("Server: Invalid filename");
                            }

                        }
                        else if (clientCommand.equalsIgnoreCase("list") && clientCommand.length() > 0 && clientCommand != null) {
                            if (folder.isDirectory()) {
                                File[] listOfFiles = folder.listFiles();
                                outgoingMessage.println("sendingList");
                                for (int i = 0; i < listOfFiles.length; i++) {
                                    if (listOfFiles[i].isFile()) {
                                        outgoingMessage.println("File " + listOfFiles[i].getName());
                                    } else if (listOfFiles[i].isDirectory()) {
                                        outgoingMessage.println("Directory " + listOfFiles[i].getName());
                                    }
                                }
                                outgoingMessage.println("eof");
                            } else {
                                outgoingMessage.println("Server: Invalid Directory");
                            }
                        }

                        else {
                            outgoingMessage.println("Server: Invalid command");
                        }
                    }
                }
                else if (clientInputStream.equalsIgnoreCase("close")) {
                    outgoingMessage.println("shuttingDown");
                    incomingMessage.close();
                    outgoingMessage.close();
                    clientSocket.close();
                    System.exit(0);
                }
                else {
                    outgoingMessage.println("Server: Invalid command");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readBuffer(BufferedReader message) throws IOException {
        try {
            while(!message.ready());
            return message.readLine();
        }
        catch (IOException e) {
            System.out.println("error reading buffer: " + e.getMessage());
        }
        return null;
    }

    private static boolean isValidCommand (String consoleInput) {
        if (consoleInput.trim().contains(" ")) {
            return true;
        }
        else {
            return false;
        }
    }

    private static String[] parseInput (String consoleInput) {
        String[] parsedArray = consoleInput.split("\\s+");
        return parsedArray;
    }

    public static void main(String args[])throws IOException
    {
        ServerSocket serverSocket = null;
        Socket clientSocket= null;
        try
        {
            serverSocket = new ServerSocket(30021);
            System.out.println("Server: Socket initialized, now listening on: " + serverSocket.toString());
            clientSocket = serverSocket.accept();
            PrintWriter outgoingStream = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader incomingStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            startService(clientSocket, incomingStream, outgoingStream);
        }
        catch(IOException e)
        {
            System.out.println("couldn't listen: " + e.getMessage());
            System.exit(0);
        }
    }
}
