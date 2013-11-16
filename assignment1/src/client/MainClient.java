package client;

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
    public static void main(String srgs[])throws IOException
    {
        Socket socket = null;
        BufferedReader input = null;
        PrintWriter output = null;
        try
        {
            socket = new Socket("127.0.0.1",30021);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(),true);
        }
        catch(Exception e)
        {
            System.exit(0);
        }
        String f;
        System.out.println("Enter the file name to transfer from server:");
        DataInputStream dis=new DataInputStream(System.in);
        f = dis.readLine();
        output.println(f);

        System.out.println(input.readLine());
        File f1 = new File("outputfile");
        FileOutputStream  fs=new FileOutputStream(f1);

        BufferedInputStream d=new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream outStream = new BufferedOutputStream(new             FileOutputStream(f1));
        byte buffer[] = new byte[1024];
        int read;
        while((read = d.read(buffer))!=-1)
        {
            outStream.write(buffer, 0, read);
            outStream.flush();
        }
        fs.close();
        System.out.println("File received");
        socket.close();
    }
}

