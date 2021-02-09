/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
class AcceptNewUser {
    
    static Map<String,Socket> onlineUsrersSocktes;
    
    public static void main(String args[]) {
        String str=JOptionPane.showInputDialog("Enter database name, username and password of MySQL seperated by spaces");
        try{ String strr[]=str.split(" ");
        ConnectionClass.databaseName=strr[0];
        ConnectionClass.dbUserName=strr[1];
        ConnectionClass.password=strr[2];
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Oops! Probably the information was wrong or it did not follow the format.");
            System.exit(0);
        }
        onlineUsrersSocktes=new HashMap<>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9876);
            while (true) {
                Socket newSocket = serverSocket.accept();
                new HandleUserThread(newSocket).start();
                System.out.println("One user Accepted");
            }
        } catch (Exception e) {

        }

    }
    public static Map getOnlineUersSocket(){
        return onlineUsrersSocktes;
    }
            
}
