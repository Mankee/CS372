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
        Socket s=null;
        BufferedReader get=null;
        PrintWriter put=null;
        try
        {
            s=new Socket("127.0.0.1",8081);
            get=new BufferedReader(new InputStreamReader(s.getInputStream()));
            put=new PrintWriter(s.getOutputStream(),true);
        }
        catch(Exception e)
        {
            System.exit(0);
        }
        InputStreamReader get2=new InputStreamReader(s.getInputStream());
        String u,f;
        System.out.println("Enter the file name to transfer from server:");
        DataInputStream dis=new DataInputStream(System.in);
        f=dis.readLine();
        put.println(f);
        File f1=new File("c:\\output");
        FileOutputStream  fs=new FileOutputStream(f1);

        BufferedInputStream d=new BufferedInputStream(s.getInputStream());
        BufferedOutputStream outStream = new BufferedOutputStream(new             FileOutputStream(f1));
        byte buffer[] = new byte[1024];
        int read;
        while((read = d.read(buffer))!=-1)
        {
            outStream.write(buffer, 0, read);
            outStream.flush();
        }

        //while((u=get.readLine())!=null)
        // {
        //    byte jj[]=u.getBytes();
        //    fs.write(jj);
        //}
        fs.close();
        System.out.println("File received");
        s.close();
    }
}

