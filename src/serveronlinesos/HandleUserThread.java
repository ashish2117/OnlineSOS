/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.net.ConnectionResetException;

/**
 *
 * @author user
 */
public class HandleUserThread extends Thread {

    MessageSender messageSender;
    Socket socket;
    BufferedReader reader;
    JSONArray users;
    String username;

    public HandleUserThread(Socket socket) throws IOException {
        super();
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        messageSender = new MessageSender(socket);
        username = null;
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {

                String message = reader.readLine();
                
                JSONObject obj = new JSONObject(message);
                String type = obj.getString("Type");

                if (type.equals("GET_OFFLINE_USERS")) {

                    String key = obj.getString("Key");
                    JSONArray uers = GetUsers.getOfflineUsers(key);
                    messageSender.sendMessage(uers);

                } else if (type.equals("GET_ONLINE_USERS")) {

                    String key = obj.getString("Key");
                    JSONArray users = GetUsers.getOnlineUsers(key);
                    messageSender.sendMessage(users);

                } else if (type.equals("GET_ALL_USERS")) {

                    String key = obj.getString("Key");
                    JSONArray uers = GetUsers.getAllUsers(key);
                    messageSender.sendMessage(uers);

                } else if (type.equals("SIGN_UP_REQUEST")) {
                    JSONObject result = AuthenticationManager.addNewUser(obj, socket);
                    username = obj.getString("UserName");
                    messageSender.sendMessage(result);

                } else if (type.equals("LOGIN_REQUEST")) {
                    System.out.println("Recievd login");
                    username = obj.getString("UserName");
                    JSONObject result = AuthenticationManager.loginUser(obj, socket);
                    messageSender.sendMessage(result);

                } else if (type.equals("BACK_ONLINE")) {
                    AuthenticationManager.makeUserOnline(obj, socket);
                    username = obj.getString("UserName");
                    if (AcceptNewUser.getOnlineUersSocket().containsKey(username)) {
                        AcceptNewUser.getOnlineUersSocket().remove(username);
                    }
                    AcceptNewUser.getOnlineUersSocket().put(username, socket);
                    System.out.println("Online Sockets "+ AcceptNewUser.onlineUsrersSocktes.toString());
                    
                } else if (type.equals("INVITATION")) {
                    System.out.println("new invitation");
                    JSONObject myMessage = new JSONObject();
                    myMessage.put("Type", "INVITATION_RESPONSE");
                    Socket friendSocket = (Socket) AcceptNewUser.getOnlineUersSocket().get(obj.get("UserName"));
                    if(AuthenticationManager.getIsPlaying(obj.getString("UserName"))){
                        myMessage.put("Response", "User is now playing with someone else");
                        messageSender.sendMessage(myMessage);
                    }
                    if (friendSocket == null) {
                        System.out.println("User Unavailable");
                        myMessage.put("Response", "User Went Offline");
                        messageSender.sendMessage(myMessage);
                    } else {
                        System.out.println("new invitation for socket"+friendSocket.getPort());
                        MessageSender sender = new MessageSender(friendSocket);
                        JSONObject frndMessage = new JSONObject();
                        frndMessage.put("Type", "INVITATION");
                        frndMessage.put("UserName",username);
                        frndMessage.put("NumberOfGames",obj.get("NumberOfGames"));
                        frndMessage.put("HighestScore",obj.get("HighestScore"));
                        System.out.println(frndMessage.toString());
                        sender.sendMessage(frndMessage);
                        myMessage.put("Response", "Invitation Sent");
                        messageSender.sendMessage(myMessage);
                    }

                }else if(type.equals("START_GAME")){
                    JSONObject myMessage=new JSONObject();
                    myMessage.put("Type","START_GAME_RESPONSE");
                    Socket friendSocket = (Socket) AcceptNewUser.getOnlineUersSocket().get(obj.get("UserName"));
                    if(AuthenticationManager.getIsPlaying(obj.getString("UserName"))==true){
                        myMessage.put("Response", "User is now playing with someone else");
                        System.out.println("True executed");
                        messageSender.sendMessage(myMessage);
                    }
                    else if (friendSocket == null) {
                        myMessage.put("Response", "User Went Offline");
                        messageSender.sendMessage(myMessage);
                    }else{
                        MessageSender sender = new MessageSender(friendSocket);
                        JSONObject frndMessage = new JSONObject();
                        frndMessage.put("Type", "INVITATION_ACCEPTED");
                        frndMessage.put("UserName",username);
                        frndMessage.put("Turn",username);
                        frndMessage.put("Response","START_PLAYING");
                        System.out.println(frndMessage.toString());
                        myMessage.put("UserName", obj.getString("UserName"));
                        myMessage.put("Turn",username);
                        myMessage.put("Response","START_PLAYING");
                        messageSender.sendMessage(myMessage);
                        sender.sendMessage(frndMessage);
                        new GameThread(username, obj.getString("UserName")).start();
                        return;
                    }
                    
                }else if(type.endsWith("GAME_STARTED")){
                    return;
                }
            }
        } catch (ConnectionResetException ex) {
            System.out.println("One user left,ConnectionReset" + username);
            AuthenticationManager.makeUseroffline(username);
            AuthenticationManager.setIsPlaying(username, false);
            return;
        } catch (NullPointerException ex) {
            System.out.println("One user left,Null Pointer" + username);
            AuthenticationManager.makeUseroffline(username);
            AuthenticationManager.setIsPlaying(username, false);
            return;
        } catch (IOException ex) {
            //user lef
            System.out.println("One user left,IOException");
            AuthenticationManager.makeUseroffline(username);
            AuthenticationManager.setIsPlaying(username, false);
            return;
        } catch (JSONException ex) {
            Logger.getLogger(HandleUserThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
