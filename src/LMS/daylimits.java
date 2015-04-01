/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LMS;

import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Chamara
 */
public class daylimits {
    
    public int staffDays1;
    public int studentDays1;
    public int staffLimit1;
    public int facultyLimit1;
    public double fine1;
    
        Connection con = DBconnect.connect();
    PreparedStatement pst = null;
    public ResultSet rst;
    
    
    
    public daylimits(int stday, int studay, int stlimit,int fuclimit, double fine)
    {
    
    this.staffDays1 = stday;
    this.studentDays1 = studay;
    this.staffLimit1 = stlimit;
    this.facultyLimit1 = fuclimit;
    this.fine1 = fine;
    
    
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void defultdaylimit() //throws SQLException
    {
    try{
    String sql11 = "Update daylimits SET staffDays ='"+this.staffDays1+"' , studentDays = '"+this.studentDays1+"', staffLimit ='"+this.staffLimit1+"' , facultyLimit = '"+this.facultyLimit1+"', fine= '"+this.fine1+"' Where ID='"+1+"' ";
    pst = con.prepareStatement(sql11);
    pst.execute();
    
    }
    
    catch(Exception e)
    {
    
    System.out.println(e);
    
    }
    
    
    
    }
    
    
    
    
    
    
    
}






