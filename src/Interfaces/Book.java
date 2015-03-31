/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import LMS.DBconnect;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Namila Radith
 */

//book class start
  public class Book {
    String title;
    String description;
    String authorFirstName;
    String authorSecondName;
    int qty;
    int itemNo;
    
    public Book(){
           Connection con = null;
           PreparedStatement pst = null;
           ResultSet rs = null;
           con = DBconnect.connect();
       
    }
    
    public void search(String keyWord){
    
        
    } 
    
}
//book class end

//textbook class start
  class TextBook extends Book{
    String section;
    String semester;
    String year;
    String category;
}
//textbook class end

//published class start
 class PublishedBooks extends Book{
    String ISBN;
    String yearPublished;
    String publisher;
    String category;
    String edition;
    
}
//published class end

//copyOfTextBooks class start
class copyOfTextBooks extends TextBook{
  
   //ADD TEXT BOOK METHOD
   public void add_Book(String title,String description,String section,String category,String semester, 
           String year, String authorFirstName, String authorSecondName,int qty){
       
      
           this.title = title;
           this.description = description;
           this.authorFirstName= authorFirstName;
           this.authorSecondName = authorSecondName;
           this.qty = qty;
           this.section = section;
           this.semester = semester;
           this.year = year;
           this.category = category;
           //DB
           Connection con = null;
           PreparedStatement pst = null;
           ResultSet rs = null;
           con = DBconnect.connect();
           
           //CALLING SQL PROCEDURE 
           String sql  = "CALL insertTextBooks ('"+this.title +"','"+this.description +"','"+this.section +"','"+this.category +"',"
                   + "'"+this.semester +"','"+this.year +"','"+this.authorFirstName +"','"+this.authorSecondName +"',"+qty +")";
           
           
            try {
                pst = con.prepareStatement(sql);
                pst.execute();
            } catch (SQLException ex) {
                Logger.getLogger(copyOfTextBooks.class.getName()).log(Level.SEVERE, null, ex);
            }
       
   } 
   
   //SET TEXT BOOK DATA METHOD 
    public void setData(String itemNo){
        
            Connection con = null;
            PreparedStatement pst = null;
            ResultSet rsPb = null;
            
            int itmNO = Integer.parseInt(itemNo);
            //CALLING SQL PROCEDURE 
            String sql = "CALL getTexBookData ('"+itmNO+"')"; 
            
            con = DBconnect.connect();
        try {
            pst = con.prepareStatement(sql);
            rsPb=pst.executeQuery();
            while(rsPb.next()){
                this.itemNo = rsPb.getInt("itemNO");
                this.section = rsPb.getString("section");
                this.title = rsPb.getString("title");
                this.description = rsPb.getString("description");
                this.semester = rsPb.getString("semester");
                this.category = rsPb.getString("category");
                this.year = rsPb.getString("year");
                this.authorFirstName = rsPb.getString("fname");
                this.authorSecondName = rsPb.getString("lname");
                this.qty = rsPb.getInt("qty");
               
              
                              
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(copyOfPublishedBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
          
          
         
          
    }
   
   
   
   //UPDATE TEXT BOOK METHOD 
   public void update_book(String title,String description,String section,String category,String semester, 
           String year, String authorFirstName, String authorSecondName,int qty){
       
           this.title = title;
           this.description = description;
           this.authorFirstName= authorFirstName;
           this.authorSecondName = authorSecondName;
           this.qty = qty;
           this.section = section;
           this.semester = semester;
           this.year = year;
           this.category = category;
           
           Connection con = null;
           PreparedStatement pst = null;
           ResultSet rs = null;
           con = DBconnect.connect();
           //CALLING SQL PROCEDURE 
           String sql  = "CALL updateTextBooks ('"+this.itemNo+"','"+this.title +"','"+this.description +"','"+this.section +"','"+this.category +"',"
                   + "'"+this.semester +"','"+this.year +"','"+this.authorFirstName +"','"+this.authorSecondName +"',"+qty +")";
           
           
            try {
                pst = con.prepareStatement(sql);
                pst.execute();
            } catch (SQLException ex) {
                Logger.getLogger(copyOfTextBooks.class.getName()).log(Level.SEVERE, null, ex);
            }
       
       
   } 
   
   public void remove_Book(){
       
   } 
    public void delete_book(){
         
            Connection con = null;
            PreparedStatement pst = null;
            
            
            int itmNO = this.itemNo;
            //CALLING SQL PROCEDURE
            String sql = "CALL deletePublishedBook ('"+itmNO+"')";
            
            con = DBconnect.connect();
        try {
            pst = con.prepareStatement(sql);
            pst.execute();
              
                              
            
            
        } catch (SQLException ex) {
            Logger.getLogger(copyOfPublishedBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
          
     
     }
}
//copyOfTextBooks class end

/*--------------------------------------------------------------------------------------------*/
//copyOfPubishedBooks class start
class copyOfPublishedBooks extends PublishedBooks{
    //ADD PUBLISHED BOOK DATA METHOD 
    public void add_Book(String ISBN,String title,String description,String edition,
          String category, String year, String authorFirstName,String authorSecondName,String publisher, int qty){
          this.ISBN = ISBN;
          this.title = title;
          this.description = description;
          this.edition = edition;
          this.category = category;
          this.yearPublished = year;
          this.authorFirstName = authorFirstName;
          this.authorSecondName = authorSecondName;
          this.publisher = publisher;
          this.qty = qty;
          
           Connection con = null;
           PreparedStatement pst = null;
           ResultSet rs = null;
           con = DBconnect.connect();
           //CALLING SQL PROCEDURE
           String sql  = "CALL insertPublisedBooks('"+this.ISBN +"','"+this.title +"','"+this.description +"','"+this.edition +"',"
                   + "'"+this.category +"','"+this.yearPublished +"','"+this.authorFirstName +"','"+this.authorSecondName +"','"+this.publisher+"',"+qty +")";
           
           
            try {
                pst = con.prepareStatement(sql);
                pst.execute();
            } catch (SQLException ex) {
                Logger.getLogger(copyOfTextBooks.class.getName()).log(Level.SEVERE, null, ex);
            }
                  
        
    }
    
    //SET PUBLISHED BOOK DATA METHOD 
    public void setData(String itemNo){
        
            Connection con = null;
            PreparedStatement pst = null;
            ResultSet rsPb = null;
            
            int itmNO = Integer.parseInt(itemNo);
            //CALLING SQL PROCEDURE
            String sql = "CALL getPublishedBookData ('"+itmNO+"')";
            
            con = DBconnect.connect();
        try {
            pst = con.prepareStatement(sql);
            rsPb=pst.executeQuery();
            while(rsPb.next()){
                this.itemNo = rsPb.getInt("itemNO");
                this.ISBN = rsPb.getString("ISBN");
                this.title = rsPb.getString("title");
                this.description = rsPb.getString("description");
                this.edition = rsPb.getString("edition");
                this.category = rsPb.getString("category");
                this.yearPublished = rsPb.getString("yearPublished");
                this.authorFirstName = rsPb.getString("fname");
                this.authorSecondName = rsPb.getString("lname");
                this.publisher = rsPb.getString("publisher");
                this.qty = rsPb.getInt("qty");
              ;
                              
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(copyOfPublishedBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
          
          
         
          
    }
    
    //UPDATE PUBLISHED BOOK METHOD 
    public void update_book(String ISBN,String title,String description,String edition,
          String category, String year, String authorFirstName,String authorSecondName,String publisher, int qty){
        
        
          this.ISBN = ISBN;
          this.title = title;
          this.description = description;
          this.edition = edition;
          this.category = category;
          this.yearPublished = year;
          this.authorFirstName = authorFirstName;
          this.authorSecondName = authorSecondName;
          this.publisher = publisher;
          this.qty = qty;
          
           Connection con = null;
           PreparedStatement pst = null;
           ResultSet rs = null;
           con = DBconnect.connect();
           //CALLING SQL PROCEDURE
           String sql  = "CALL updatePublisedBooks('"+this.itemNo +"','"+this.ISBN +"','"+this.title +"','"+this.description +"','"+this.edition +"',"
                   + "'"+this.category +"','"+this.yearPublished +"','"+this.authorFirstName +"','"+this.authorSecondName +"','"+this.publisher+"',"+qty +")";
           
           
            try {
                pst = con.prepareStatement(sql);
                pst.execute();
            } catch (SQLException ex) {
                Logger.getLogger(copyOfTextBooks.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
            
     public void delete_book(){
         
            Connection con = null;
            PreparedStatement pst = null;
           
            
            int itmNO =  this.itemNo;
            //CALLING SQL PROCEDURE
            String sql = "CALL deletePublishedBook ('"+itmNO+"')";
            
            con = DBconnect.connect();
        try {
            pst = con.prepareStatement(sql);
            pst.execute();
              
                              
            
            
        } catch (SQLException ex) {
            Logger.getLogger(copyOfPublishedBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
          
     
     }
}

//copyOfPubishedBooks class end