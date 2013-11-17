package client;

import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;


/**
 * Created with IntelliJ IDEA.
 * User: austin.dubina
 * Date: 11/14/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainClient {

    public static void main(String srgs[]) {
        Socket socket;
        BufferedReader serverInputStream = null;
        PrintWriter serverOutputStream = null;

        try
        {
            socket = new Socket("127.0.0.1",30021);
            serverInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataInputStream fileInputStream = new DataInputStream(socket.getInputStream());
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
                while (!message.equalsIgnoreCase("eof")) {
                    System.out.println(message);
                    message = serverInputStream.readLine();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
//        File f1 = new File("outputfile");
//
//        System.out.println(input.readLine());
//        FileOutputStream  fs=new FileOutputStream(f1);
//
//        BufferedInputStream d=new BufferedInputStream(socket.getInputStream());
//        BufferedOutputStream outStream = new BufferedOutputStream(new             FileOutputStream(f1));
//        byte buffer[] = new byte[1024];
//        int read;
//        while((read = d.read(buffer))!=-1)
//        {
//            outStream.write(buffer, 0, read);
//            outStream.flush();
//        }
//        fs.close();
//        System.out.println("File received");
//        socket.close();
    }


}

