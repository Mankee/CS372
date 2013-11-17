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
                while(!incomingMessage.ready()){}
                String message = incomingMessage.readLine();
                if (message.equalsIgnoreCase("list") && message.length() > 0) {
                    outgoingMessage.println("please specify the filename and path, e.g /Userfolder/Documents/filename.txt");
                }

                else {
                    outgoingMessage.println("invalid command, please try again");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
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
