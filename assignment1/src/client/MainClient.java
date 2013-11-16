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
        Socket clientSocket = null;
        BufferedReader readFileData = null;
        PrintWriter writeFileData = null;
        try
        {
            clientSocket = new Socket("127.0.0.1",30021);
            readFileData = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writeFileData = new PrintWriter(clientSocket.getOutputStream(),true);
        }
        catch(IOException error)
        {
            System.out.println("Error intializing connection: " + error.getMessage());
            System.exit(1);
        }
        String filename;
        System.out.println("Enter the file name to transfer from server:");
        DataInputStream dis = new DataInputStream(System.in);
        filename = dis.readLine();
        writeFileData.println(filename);
        File f1 = new File("outputfile");
        FileOutputStream  fs=new FileOutputStream(f1);

        BufferedInputStream dataFromServer = new BufferedInputStream(clientSocket.getInputStream());
        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(f1));
        byte buffer[] = new byte[1024];
        int read;
        while((read = dataFromServer.read(buffer))!=-1)
        {
            outStream.write(buffer, 0, read);
            outStream.flush();
        }
        fs.close();
        System.out.println("File received");
        clientSocket.close();
    }
}

