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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import LMS.DBconnect;
import LMS.Members;

/**
 *
 * @author Basuru
 */

public class Members {
    public String fname;
    public String lname;
    public String address;
    public String email;
    public String phone;
    public int noOfItemsBo;
    public String type;
    
    Connection con = DBconnect.connect();
    PreparedStatement pst = null;
    public ResultSet rs;
    
    public Members()
    {
        //Empty Constructor for Deletion
    }
        //Parameterized Constructor for Insert, Edit functions
    public Members(String fname,String lname,String address,String email, String phone,String type,int noOfItemsBo)
    {
        this.fname=fname;
        this.lname=lname;
        this.address=address;
        this.email=email;
        this.phone=phone;
        this.type=type;
        this.noOfItemsBo=noOfItemsBo;
    }
    public void addMember()
    {  
        try
        {
        String no = "SELECT noOfAllowedBooks FROM Membertype WHERE type = '"+this.type+"'";
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(no);
            
           if(rs.next())
            {
                int noOfAllowedBooks = rs.getInt("noOfAllowedBooks");
                
                if(noOfAllowedBooks < noOfItemsBo )
                {
                JOptionPane.showMessageDialog(null, "Cannot Burrow Books Limit Exceeded Please Return borrowed Books");
                
                }
                else
                {
                    
                    try
                    {

                         String q  = "INSERT INTO Members(fname,lname,address,phone,email,type,NoOfItemsBorrowed) VALUES ('"+this.fname+"','"+this.lname+"','"+this.address+"','"+this.phone+"','"+this.email+"','"+this.type+"','"+this.noOfItemsBo+"')";

                         pst = con.prepareStatement(q);
                         pst.execute();

                         String sql = "SELECT libraryID,type FROM Members WHERE fname='"+this.fname+"'AND lname='"+this.lname+"'AND email='"+this.email+"'";
                         Statement st = con.createStatement();
                         ResultSet rs = st.executeQuery(sql);
                         String libID=null;
                         String stype=null;
                         if(rs.next())
                         {
                             libID = rs.getString("libraryID");
                             stype = rs.getString("type");
                         }

                        stype = stype.equals("Faculty")? "FAC" : stype.equals("Student")? "STU" : "STA" ;

                        JOptionPane.showMessageDialog(null,"New Member Added USER ID :"+libID+stype);
                        con.close();
                    }
                    
                     catch(Exception e)
                     { 
                         JOptionPane.showMessageDialog(null,"Not Added :"+e.getMessage());//Do nothing
                     }

                }
            }
        } 
            catch(Exception ees)
            {
                    //donoting
            }
      
  
        
    }
    public String editMember(String LibraryID)
    {
        
        try
        {
        String no = "SELECT noOfAllowedBooks FROM Membertype WHERE type = '"+this.type+"'";
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(no);
            
           if(rs.next())
            {
                int noOfAllowedBooks = rs.getInt("noOfAllowedBooks");
                
                if(noOfAllowedBooks < noOfItemsBo )
                {
                JOptionPane.showMessageDialog(null, "Cannot Burrow Books Limit Exceeded Please Return borrowed Books");
                }
                else
                {
                    
                    
                    try {
                        String sql = "UPDATE members SET fname='"+this.fname+"',lname='"+this.lname+"',address='"+this.address+"',phone='"+this.phone+"',email='"+this.email+"',type='"+this.type+"',NoOfItemsBorrowed='"+this.noOfItemsBo+"' WHERE libraryID = '"+ LibraryID +"'";
                        pst = con.prepareStatement(sql);
                        pst.execute();
         
            
                        } catch (SQLException ex) {
                            Logger.getLogger(Members.class.getName()).log(Level.SEVERE, null, ex);
                        }

                }
            }
        }catch(Exception esss)
        {
            //Do Nothing
        }
        return "Update Complete";
    }
    
    public String deleteMember(String userID)
    {
        try {
            String sql = "DELETE FROM Members WHERE libraryID='"+userID+"'";
            pst = con.prepareStatement(sql);
            pst.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(Members.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return "Member deleted with UserID : '"+userID+"'";
    }
//    public Members searchMember(String UserID)
//    { 
//        try {
//            String sql = "SELECT * FROM Members WHERE fname LIKE '%"+UserID+"%'";
//            pst = con.prepareStatement(sql);
//            this.rs = pst.executeQuery();
// 
//            
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(Members.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Members M2 = new Members();
//        
//        return M2;
//    }
    
}
