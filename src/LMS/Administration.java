/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LMS;

import LMS.DBconnect;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Chamara
 */
public class Administration {
    
    public String fname;
    public String lname;
    public String Uname;
    public String password;
    public String address;
    public String email;
    public String phone;
    public String type;
    
    
    
    Connection con = DBconnect.connect();
    PreparedStatement pst = null;
    public ResultSet rst;
    
    
    public Administration(String fname, String lname,String Uname, String address, String email, String phone, String type)
     {

         this.fname = fname;
         this.lname = lname;
         this.Uname = Uname;
         this.address = address;
         this.email = email;
         this.phone = phone;
         this.type = type;


}

    public Administration() {
        
    }
    
    
    public void addnewmember()
    {
    try{
        
        String dat = this.Uname+"@#1";
        
        
        
        String sql = "INSERT INTO administration( fname, lname, UserName, password, address, email, phone, type) VALUES ('"+this.fname+"','"+this.lname+"','"+this.Uname+"','"+dat+"','"+this.address+"','"+this.email+"','"+this.phone+"','"+this.type+"') ";

        pst = con.prepareStatement(sql);
        pst.execute();
        JOptionPane.showMessageDialog(null,"User added \n"+" Password is: " + dat);
        
        
        
        String sql6 = "Select id From administration Where UserName = '"+this.Uname+"' and password = '"+dat+"'";
      
        Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql6);
            
            String ID1=null;
        
            rs.first();
            ID1 = rs.getString("id");
            
            String sql7 = "Insert into login (id, userName, password, type) Values ('"+ID1+"', '"+this.Uname+"','"+dat+"','"+this.type+"' )";
             pst = con.prepareStatement(sql7);
            pst.execute();
            
            
        
        
        
        
        
        
        
        
                }
    
    catch (Exception e){
    
    System.out.println(e);
    
    }
    
    
    }
    
    
    
    public void deleteUser(String id) 
    {
        
        try{
        
        String sql2 = "Delete From administration Where id = '"+id+"'";
        pst = con.prepareStatement(sql2);
        pst.execute();
        String sql8 = "Delete From login Where id = '"+id+"'";
        pst = con.prepareStatement(sql8);
        pst.execute();
    
        }
        
        catch (Exception e)
        {
        
        }
        
    }
    
    public String updateUser(String id) 
    {
    try{
        String sql3 = "Update administration Set fname='"+this.fname+"', lname = '"+this.lname+"',UserName = '"+this.Uname+"' ,address = '"+this.address+"', email = '"+this.email+"', phone = '"+this.phone+"', type = '"+this.type+"' WHERE id = '"+id+"' ";
        pst = con.prepareStatement(sql3);
        pst.execute();
        
        String sql9 = "Update login Set userName = '"+this.Uname+"', type = '"+this.type+"' Where id = '"+id+"'";
         pst = con.prepareStatement(sql9);
        pst.execute();
    }
    catch(Exception e){
    
     JOptionPane.showMessageDialog(null,"Update Failed!" );
    
    
    }
        
    return "Updated";
    
    
    }
    
    public String restpassword(String id) 
    {
        try{
        
String dat = "abcd1234";
      
String sql4 = "Update administration Set password = '"+dat+"' Where id = '"+id+"' ";
pst = con.prepareStatement(sql4);
pst.execute();

String sql10 = "Update login Set password = '"+dat+"' Where id = '"+id+"' ";
pst = con.prepareStatement(sql10);
pst.execute();



JOptionPane.showMessageDialog(null,"Password Reset! \n New Password is: "+dat );
        
        
        }
        
        
        catch(Exception e){
        
        JOptionPane.showMessageDialog(null,e );
        }
        
    return "reset";
    
    
    
    
    
    }

    
    public String EditProfile(int id)
    {
    
     try{
        String sq25 = "Update administration Set fname='"+this.fname+"', lname = '"+this.lname+"',UserName = '"+this.Uname+"' ,address = '"+this.address+"', email = '"+this.email+"', phone = '"+this.phone+"' WHERE id = '"+id+"' ";
        pst = con.prepareStatement(sq25);
        pst.execute();
        
        String sql9 = "Update login Set userName = '"+this.Uname+"' Where id = '"+id+"'";
         pst = con.prepareStatement(sql9);
        pst.execute();
    }
    catch(Exception e){
    
     JOptionPane.showMessageDialog(null,"Update Failed!" );
    
    
    }
        
    return "profile";
    }
    
    
    
   
    
    
    
    
}
