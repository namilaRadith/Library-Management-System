/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LMS; // add your project here

import Interfaces.Admin;
import Interfaces.Admin;
import LMS.DBconnect;
import com.mysql.jdbc.Connection;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Basuru
 */
public class ReportGenerator {
    Connection con = DBconnect.connect();
    
    public void generateReport()
    {
        FileInputStream fis = null;
        try {
            
            
            fis = new FileInputStream("src\\Reports\\MemberList.jrxml"); // source file
            BufferedInputStream bInputStream = new BufferedInputStream(fis);
            //String Loc = "D:\\ST2 project 2015\\Basuru\\Library Management System\\src\\Reports\\MemberList.jrxml";
            JasperReport jasperreport = (JasperReport) JasperCompileManager.compileReport(bInputStream);
            
            //JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, null,con);
             JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, null, con);
             
             JasperViewer.viewReport(jasperprint);
             JasperExportManager.exportReportToPdfFile(jasperprint, "src//Reports//Report.pdf"); // Location to output the pdf file
             //JasperViewer.viewReport(jasperprint);
             
             JOptionPane.showMessageDialog(null, "Done");
             
            
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void generateParameterizedReport(HashMap type)
    {
        FileInputStream fis = null;
        try {
            
            
            fis = new FileInputStream("src\\Reports\\MemberType.jrxml"); // source file
            BufferedInputStream bInputStream = new BufferedInputStream(fis);
            //String Loc = "D:\\ST2 project 2015\\Basuru\\Library Management System\\src\\Reports\\MemberList.jrxml";
            JasperReport jasperreport = (JasperReport) JasperCompileManager.compileReport(bInputStream);
            
            //JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, null,con);
             JasperPrint jasperprint = JasperFillManager.fillReport(jasperreport, type, con);
             
             //JasperViewer.viewReport(jasperprint);
             JasperExportManager.exportReportToPdfFile(jasperprint, "src//Reports//ParameterizedReport.pdf"); // Location to output the pdf file
             //JasperViewer.viewReport(jasperprint);
             
             JOptionPane.showMessageDialog(null, "Done");
             
            
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
