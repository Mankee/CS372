package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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
                        outgoingMessage.println("eof");
                    }
                    else {
                        if (clientCommand.equalsIgnoreCase("get") && clientCommand.length() > 0 && clientCommand != null) {
                            outgoingMessage.println("Server: Please specify the filename and path, e.g /Userfolder/Documents/filename.txt");
                            outgoingMessage.println("eof");
                            String filename = readBuffer(incomingMessage);
                            if (filename.length() > 0 && filename != null) {

                            }
                        } else if (clientCommand.equalsIgnoreCase("list") && clientCommand.length() > 0 && clientCommand != null) {
                            File[] listOfFiles = folder.listFiles();
                            for (int i = 0; i < listOfFiles.length; i++) {
                                if (listOfFiles[i].isFile()) {
                                    outgoingMessage.println("File " + listOfFiles[i].getName());
                                } else if (listOfFiles[i].isDirectory()) {
                                    outgoingMessage.println("Directory " + listOfFiles[i].getName());
                                }
                            }
                            outgoingMessage.println("eof");
                        }
                        else {
                            outgoingMessage.println("Server: Invalid command");
                            outgoingMessage.println("eof");
                        }
                    }
                }
                else {
                    outgoingMessage.println("Server: Invalid command");
                    outgoingMessage.println("eof");
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



//        System.out.println("The requested file is : "+s);
//        File f=new File(s);
//        if(f.exists())
//        {
//            BufferedInputStream d=new BufferedInputStream(new FileInputStream(s));
//            BufferedOutputStream outStream = new BufferedOutputStream(clientSocket.getOutputStream());
//            byte buffer[] = new byte[1024];
//            int read;
//            while((read = d.read(buffer))!=-1)
//            {
//                outStream.write(buffer, 0, read);
//                outStream.flush();
//            }
//            d.close();
//            System.out.println("File transfered boo boo");
//            clientSocket.close();
//            serverSocket.close();
//        } else {
//            outgoingMessage.println("file does not exist");
//        }
    }
}
