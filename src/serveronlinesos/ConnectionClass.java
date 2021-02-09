/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.json.JSONArray;

/**
 *
 * @author user
 */
public class ConnectionClass {

    static Connection con;
    static String databaseName;
    static String dbUserName;
    static String password;
    public static Connection createConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName, dbUserName, password);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL Driver might be missing.");
            System.exit(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error!");
            System.exit(0);
        }
        return con;
    }
    
    

}
