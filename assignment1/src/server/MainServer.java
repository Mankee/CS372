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
//                        outgoingMessage.println("eof");
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
    //                            fileData.close();
    //                            outStream.close();
    //                            outgoingMessage.println("eof");
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
