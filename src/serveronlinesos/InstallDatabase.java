/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class InstallDatabase {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String databaseName;
        String dbUserName;
        String password;
        String str = JOptionPane.showInputDialog("Enter database name, username and password of MySQL seperated by spaces");
        try {
            String strr[] = str.split(" ");
            databaseName = strr[0];
            dbUserName = strr[1];
            password = strr[2];
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName, dbUserName, password);
            Statement stmt=con.createStatement();
            stmt.execute("Create table users"
                    + "("
                    + "UserName varchar(20) primary key,"
                    + "Name varchar(30) NOT NULL,"
                    + "IPAddress varchar(20) NOT NULL,"
                    + "PortNumber int(5) NOT NULL,"
                    + "isPlaying boolean NOT NULL,"
                    + "isOnline boolean NOT NULL,"
                    + "HighestScore int(2) NOT NULL,"
                    + "TotalScore int(5) NOT NULL,"
                    + "NumberOfGames int(5) NOT NULL,"
                    + "DOJ date NOT NULL,"
                    + "Email varchar(50) NOT NULL,"
                    + "Password varchar(20) NOT NULL"
                    + ")");
         } catch(ClassNotFoundException ex){
            JOptionPane.showMessageDialog(null, "MySQL Driver might be missing.");
            System.exit(0);
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "SQL Error.");
            System.exit(0);
        }
            catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Oops! Probably the information was wrong or it did not follow the format.");
            System.exit(0);
        }
        
    }

}
