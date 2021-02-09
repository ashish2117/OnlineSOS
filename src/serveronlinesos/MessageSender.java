/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class MessageSender {

    private Socket socket;
    private DataOutputStream outStream;

    public MessageSender(Socket socket) throws UnknownHostException, IOException {
        this.socket = socket;
        outStream = new DataOutputStream(socket.getOutputStream());
        
    }

    public void sendMessage(JSONArray obj) throws IOException {

        outStream.writeBytes(obj.toString() + "\n");

    }

    public void sendMessage(JSONObject obj) throws IOException {
        outStream.writeBytes(obj.toString() + "\n");
    }
}
