/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teamviewer;

/**
 *
 * @author HaLeeM
 */

import java.io.*;
import java.awt.Robot;
import java.net.*;

public class TeamViewer {

    /**
     * @param args the command line arguments
     */
    static ObjectOutputStream output;
    static ObjectInputStream input;
    static ServerSocket server;
    static Socket socketForClient;
    static Robot robot;
    static boolean g = true;
    
    public static void main(String[] args) throws Exception {
        try{
                int PORT=6789;
                
                server =new ServerSocket(PORT);
                System.out.println("Server activated...");
                
                socketForClient= server.accept();
                output=new ObjectOutputStream(socketForClient.getOutputStream());
                System.out.println("Outputstream set up...");
                output.flush();
                input=new ObjectInputStream(socketForClient.getInputStream());
                System.out.println("InputStream set up....");
                
                robot= new Robot();
                
                ServerThread object=new ServerThread();
                object.startRunning();
                
        }catch(Exception e){
            System.err.println("Error : "+e);
        }
    }
    
}
