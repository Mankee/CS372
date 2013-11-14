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
    public static void main(String args[])throws IOException
    {
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket();
        }
        catch(IOException e)
        {
            System.out.println("couldn't listen");
            System.exit(0);
        }
        Socket clientSocket = null;
        try
        {
            clientSocket = serverSocket.accept();
            System.out.println("Connection established" + clientSocket);
        }
        catch(Exception e)
        {
            System.out.println("Accept failed");
            System.exit(1);
        }
        PrintWriter put = new PrintWriter(clientSocket.getOutputStream(),true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String fileName = bufferedReader.readLine();
        System.out.println("The requested file is : "+ fileName);
        File file = new File(fileName);

        if(file.exists())
        {
            BufferedInputStream d=new BufferedInputStream(new FileInputStream(fileName));
            BufferedOutputStream outStream = new BufferedOutputStream(clientSocket.getOutputStream());
            byte buffer[] = new byte[1024];
            int read;
            while((read = d.read(buffer))!=-1)
            {
                outStream.write(buffer, 0, read);
                outStream.flush();
            }
            d.close();
            System.out.println("File transfered");
            clientSocket.close();
            serverSocket.close();
        } else {
        //TODO sendback error message stating file not found
        }

    }
}
