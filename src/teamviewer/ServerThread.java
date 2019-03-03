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
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level; 
import java.util.logging.Logger;
import javax.swing.ImageIcon;
public class ServerThread extends TeamViewer{
    
    public ServerThread(){
        
    }
    
    public static void sendImage(){
        Rectangle rect=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage image=robot.createScreenCapture(rect);
        ImageIcon imageIcon=new ImageIcon(image);
        try{
            System.out.println("before sending image...");
            output.writeObject(imageIcon);
            output.reset();
            System.out.println("New screenshots sent"); 
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    Thread imageThread =new Thread(new Runnable(){
        @Override
        public void run() {
            while(true){
                sendImage();
            }
        } 
    });
    
    public static String getMouseCoordinateActionKeyboard() throws IOException{
        String coordinateAction =null;
        try{
            coordinateAction=(String) input.readObject();
        }catch(ClassNotFoundException ex){
            Logger.getLogger(TeamViewer.class.getName()).log(Level.SEVERE,null,ex);
        }
        return coordinateAction;
    }
    
    public static void mouseMoveAction() throws AWTException,IOException{
        String mouseString=getMouseCoordinateActionKeyboard();
        String[] parts=mouseString.split(" ");
        int mouseX=Integer.parseInt(parts[0]);
        int mouseY=Integer.parseInt(parts[1]);
        robot.mouseMove(mouseX,mouseY-25);
        
        int mouseAction=Integer.parseInt(parts[2]);
        int button=Integer.parseInt(parts[3]);
        int click=Integer.parseInt(parts[4]);
        
        switch(button){
            case 1:
                button=InputEvent.BUTTON1_DOWN_MASK;
                break;
            case 2:
                button=InputEvent.BUTTON2_DOWN_MASK;
                break;
            case 3:
                button=InputEvent.BUTTON3_DOWN_MASK;
                break;
            default:System.out.println("Nothing to do..");
                break;
        }
        switch(mouseAction){
            
            case 1:{
                robot.mouseRelease(button);
                break;
            }
            case 2:{
                robot.mousePress(button);
                break;
            }
            case 3:{
                robot.mouseRelease(button);
                break;
            }
            default: System.out.println("Nothin to do..."); break;
            
        }
        System.out.println("x : "+mouseX+" y : "+mouseY+" Action : "+mouseAction+" Button : "+button+" click :"+click);
    }
    
    Thread mouseKeyboardThread= new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                    try{
                        mouseMoveAction();
                            try{
                                Thread.sleep(100);
                            }catch(InterruptedException ex){
                                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE,null,ex);
                            }
                    }                        
                    catch(AWTException |IOException e){
                        Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE,null,e);
                    }
            }
        }
    });
    
    public void startRunning(){
            imageThread.start();
            mouseKeyboardThread.start();
    }
}
