/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LMS;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Namila Radith
 */
public class DBconnect {
    
    public static Connection connect()
    {
        Connection conn = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/library_m_system", "root", "1234");
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return conn;
    }

    public void Connection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
