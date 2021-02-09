/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class GetUsers {

    static Connection con;

    public static JSONArray getAllUsers(String key) {
        JSONArray users = new JSONArray();
        JSONObject obj;
        try {
            users = new JSONArray();
            con = ConnectionClass.createConnection();
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("Select UserName, Name , isPlaying, isOnline, HighestScore,NumberOfGames from users where UserName like '" + key + "%'");
            while (res.next()) {

                obj = new JSONObject();
                obj.put("UserName", res.getString(1));
                obj.put("Name", res.getString(2));
                obj.put("IsPlaying", res.getBoolean(3));
                obj.put("IsOnline", res.getBoolean(4));
                obj.put("HighestScore", res.getInt(5));
                obj.put("NumberOfGames", res.getString(6));
                users.put(obj);

            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException ex) {
            Logger.getLogger(GetUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;

    }

    public static JSONArray getOnlineUsers(String key) {

        JSONArray users = new JSONArray();
        JSONObject obj = null;
        try {
            con = ConnectionClass.createConnection();
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("Select UserName, Name , isPlaying, HighestScore,isOnline,NumberOfGames "
                    + "from users where isOnline is true and UserName like '" + key + "%'");

            while (res.next()) {

                obj = new JSONObject();
                obj.put("UserName", res.getString(1));
                System.out.println("got username" + res.getString(1));
                obj.put("Name", res.getString(2));
                obj.put("IsPlaying", res.getBoolean(3));
                obj.put("HighestScore", res.getInt(4));
                obj.put("IsOnline", res.getBoolean(5));
                obj.put("NumberOfGames", res.getString(6));
                users.put(obj);

            }
            con.close();
        } catch (SQLException e) {

        } catch (JSONException ex) {
            Logger.getLogger(GetUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;
    }

    public static JSONArray getOfflineUsers(String key) {
        JSONArray users = new JSONArray();
        JSONObject obj = null;
        try {
            con = ConnectionClass.createConnection();
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("Select UserName, Name , isPlaying, HighestScore, isOnline,NumberOfGames "
                    + "from users where isOnline is false and UserName like '" + key + "%'");
            while (res.next()) {

                obj = new JSONObject();
                obj.put("UserName", res.getString(1));
                obj.put("Name", res.getString(2));
                obj.put("IsPlaying", res.getBoolean(3));
                obj.put("HighestScore", res.getInt(4));
                obj.put("IsOnline", res.getBoolean(5));
                obj.put("NumberOfGames", res.getString(6));
                users.put(obj);

            }
            con.close();
        } catch (SQLException e) {

        } catch (JSONException ex) {
            Logger.getLogger(GetUsers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;
    }

    public static void main(String[] args) {
        JSONArray users = getOnlineUsers("");
        System.out.print(users.toString());
    }
}
