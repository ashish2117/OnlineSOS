/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import com.mysql.jdbc.Connection;
import java.net.Socket;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class AuthenticationManager {

    static Connection con;

    public static JSONObject addNewUser(JSONObject obj, Socket socket) {
        JSONObject result = null;
        try {
            result=new JSONObject();
            result.put("Type", "SIGN_UP_RESULT");
            con = ConnectionClass.createConnection();
            PreparedStatement statement = con.prepareStatement("insert into users values(?,?,?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1, obj.getString("UserName"));
            statement.setString(2, obj.getString("Name"));
            statement.setString(3, socket.getInetAddress().toString());
            statement.setInt(4, socket.getPort());
            statement.setBoolean(5, false);
            statement.setBoolean(6, false);
            statement.setInt(7, 0);
            statement.setInt(8, 0);
            statement.setInt(9, 0);
            statement.setDate(10, new Date(System.currentTimeMillis()));
            statement.setString(11, obj.getString("Email"));
            statement.setString(12, obj.getString("Password"));
            int rows = statement.executeUpdate();
            if (rows > 0) {
                result.put("Result", "Success");
            }
            else
            {
                result.put("Result", "Failed");
            }
            
        } catch (SQLException ex) {
            if(ex.getMessage().contains("Duplicate"))
            {
                try {
                    result.put("Result", "Username not available!");
                } catch (JSONException ex1) {
                    Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex1);
                }
                return result;
            }
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public static void makeUserOnline(JSONObject obj,Socket socket) {
        
        try {
            
            con=ConnectionClass.createConnection();
            PreparedStatement statement=con.prepareStatement("update users set IPAddress=?, PortNumber=?, isOnline=true where UserName=?");
            statement.setString(1, socket.getInetAddress().toString().substring(1));
            statement.setInt(2, socket.getPort());
            statement.setString(3, obj.getString("UserName"));
            int rows=statement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public static void makeUseroffline(String username) {
        try {
            System.out.println("Making user offline : "+username);
            con=ConnectionClass.createConnection();
            PreparedStatement statement=con.prepareStatement("update users set isOnline=false where UserName=?");
            statement.setString(1, username);
            int rows=statement.executeUpdate();
         } catch (SQLException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       if(AcceptNewUser.getOnlineUersSocket().containsKey(username))
                AcceptNewUser.getOnlineUersSocket().remove(username); 
    }

    static JSONObject loginUser(JSONObject obj, Socket socket) {
        JSONObject result=null;
        System.out.println("Recievd login");
        con=ConnectionClass.createConnection();
        try {
            result=new JSONObject();
            PreparedStatement statement=con.prepareStatement("Select Password from users where UserName=?");
            statement.setString(1, obj.getString("UserName"));
            System.out.println("Set String");
            ResultSet res=statement.executeQuery();
            System.out.println("Query Exec");
            result.put("Type", "LOGIN_RESULT");
            
            if(!res.next()){
                System.out.println("No username");
                result.put("Result","Username does not exist!");
            }
            else{
                System.out.println("Inside 1st else");
                if(res.getString(1).equals(obj.getString("Password"))){
                    System.out.println("Inside 1st else");
                    result.put("Result","Success");
                }
                else
                {
                   result.put("Result","Wrong Pasword!"); 
                   System.out.println("Inside 2md else");
                }
            }       
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    public static void setIsPlaying(String username,boolean isPlaying){
        System.out.println(username+" is now playing - "+isPlaying);
        con=ConnectionClass.createConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("Update users set isPlaying=? where UserName=?");
            preparedStatement.setBoolean(1, isPlaying);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    public static boolean getIsPlaying(String username){
        con=ConnectionClass.createConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement("Select isPlaying from users where UserName=?");
            preparedStatement.setString(1, username);
            ResultSet res=preparedStatement.executeQuery();
            if(res.next()){
                System.out.println(username+" is "+res.getBoolean(1));
                return res.getBoolean(1);
             }
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
