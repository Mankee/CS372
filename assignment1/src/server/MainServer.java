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
        ServerSocket ss=null;
        try
        {
            ss=new ServerSocket(8081);
        }
        catch(IOException e)
        {
            System.out.println("couldn't listen");
            System.exit(0);
        }
        Socket cs=null;
        try
        {
            cs=ss.accept();
            System.out.println("Connection established"+cs);
        }
        catch(Exception e)
        {
            System.out.println("Accept failed");
            System.exit(1);
        }
        PrintWriter put=new PrintWriter(cs.getOutputStream(),true);
        BufferedReader st=new BufferedReader(new InputStreamReader(cs.getInputStream()));
        String s=st.readLine();
        System.out.println("The requested file is : "+s);
        File f=new File(s);
        if(f.exists())
        {
            BufferedInputStream d=new BufferedInputStream(new FileInputStream(s));
            BufferedOutputStream outStream = new BufferedOutputStream(cs.getOutputStream());
            byte buffer[] = new byte[1024];
            int read;
            while((read = d.read(buffer))!=-1)
            {
                outStream.write(buffer, 0, read);
                outStream.flush();
            }
            d.close();
            System.out.println("File transfered");
            cs.close();
            ss.close();
        }
    }
}
