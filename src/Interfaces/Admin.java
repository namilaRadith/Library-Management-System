/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import LMS.ReportGenerator;
import LMS.Members;
import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import LMS.DBconnect;
import static LMS.InterfaceMethods.hideGroup;
import java.awt.HeadlessException;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Namila Radith
 */
public class Admin extends javax.swing.JFrame {

    /**
     * Creates new form Admin
     */
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int rowCount; // Row Selection variable in Edit Method
    public String LibraryID; // Delete member GLOBAL ID
    
    
    String title, authorFirstName, authorSecondName, publisher, description, ISBN, edition, noOfCopies, catogory, section, semester, year;
    copyOfTextBooks ct = new copyOfTextBooks(); //copy of textbooks object
    copyOfPublishedBooks cp = new copyOfPublishedBooks(); //copy of publishedbooks object
   

    

    
    private Object model;
    private Object rsc2;

    public Admin() {
        //basuru 
        initComponents();
        setExtendedState(NORMAL);

        //connect to DB
        con = DBconnect.connect();
        //this hide the renew group when page load 
        hideGroup(jPanel9, jPanel10);

        tableload();
        hideButtons();

        

    }
    /*------ namila ---------*/
    //function control the enability of below radio buttons
    public void BookTypeRadioButtonEnable(boolean b) {
        publishedBookRadio.setEnabled(b);
        textBookRadio.setEnabled(b);
    }

    public void textBookSelected() {
        publisherIn.setEnabled(false);
        isbnIn.setEnabled(false);

        publishedBookYearIn.setVisible(false);
        yearLable.setVisible(false);
        sectionIn.setEnabled(true);
        semesterIn.setEnabled(true);
        yearIn.setEnabled(true);
    }

    public void publishedBookSelected() {
        publisherIn.setEnabled(true);
        isbnIn.setEnabled(true);
        categoryIn.setEnabled(true);
        publishedBookYearIn.setVisible(true);
        yearLable.setVisible(true);
        sectionIn.setEnabled(false);
        semesterIn.setEnabled(false);
        yearIn.setEnabled(false);
        publishedBookRadio.setSelected(rootPaneCheckingEnabled);
    }

    public void resetResorcesFileds() {
        titleIn.setText("");
        aFirstNameIn.setText("");
        aSecondNameIn.setText("");
        publisherIn.setText("");
        bookDescriptionIn.setText("");
        isbnIn.setText("");
        editionIn.setText("");
        noOfCopiesIn.setText("");
        categoryIn.setSelectedItem("Select One");
        publishedBookYearIn.setText("Select One");
        sectionIn.setSelectedItem("Select One");
        yearIn.setSelectedItem("Select One");
        semesterIn.setSelectedItem("Select One");
        ItemNoLable.setText("");
        publishedBookYearIn.setText("");

        BookTypeRadioButtonEnable(true);
    }

    public void ResorcesFiledsEnable(boolean b) {
        titleIn.setEnabled(b);
        aFirstNameIn.setEnabled(b);
        aSecondNameIn.setEnabled(b);
        publisherIn.setEnabled(b);
        bookDescriptionIn.setEnabled(b);
        isbnIn.setEnabled(b);
        editionIn.setEnabled(b);
        noOfCopiesIn.setEnabled(b);
        categoryIn.setEnabled(b);
        publishedBookYearIn.setEnabled(b);
        sectionIn.setEnabled(b);
        yearIn.setEnabled(b);
        semesterIn.setEnabled(b);
        ItemNoLable.setEnabled(b);
        publishedBookYearIn.setEnabled(b);

       
    }
    
    public void hideButtons() {
      
        sectionIn.setEnabled(false);
        semesterIn.setEnabled(false);
        yearIn.setEnabled(false);
        editBookButton.setEnabled(false);
        saveBookButton.setEnabled(false);
        removeBookButton.setEnabled(false);
        updateMember.setEnabled(false);
        deleteMember.setEnabled(false);

    }
     
    public void reset() {
        resetResorcesFileds();
        ResorcesFiledsEnable(true);
        publishedBookSelected();
        addBookButton.setEnabled(true);
        editBookButton.setEnabled(false);
        saveBookButton.setEnabled(false);
        removeBookButton.setEnabled(false);
    }
     
    public boolean validatePublishedBooks() throws HeadlessException {
        boolean success3 = false;
        
        String alphaNumeric = "[A-Za-z0-9]+";
        String regex="^[a-zA-Z ]+$";
        String numeric = "[-+]?\\d*\\.?\\d+";
        
        
        
        if(edition.matches(alphaNumeric)){
            if(publisher.matches(regex)){
                if(ISBN.matches(numeric)){
                    if(year.matches(numeric) && year.length() <5){
                        success3 = true;
                    }else{
                        JOptionPane.showMessageDialog(null, " Year should be Numeric and less than 5 digits ", "Warning", WIDTH);
                        success3 = false;
                    }
                }else{
                    JOptionPane.showMessageDialog(null, " ISBN  should be Numeric ", "Warning", WIDTH);
                    success3 = false;
                }
            }else{
                JOptionPane.showMessageDialog(null, "Publisher should be letters ", "Warning", WIDTH);
                success3 = false;
            }
        }else{
            JOptionPane.showMessageDialog(null, "Edition Should be Alpha Numeric", "Warning", WIDTH);
            success3 = false;
        }
        return  success3;
    }

    public boolean validateResourseInputs() throws HeadlessException {
        String regex="^[a-zA-Z ]+$";
        String numeric = "[-+]?\\d*\\.?\\d+";
        boolean success = false;
        if(title.matches(regex)){
            if(authorFirstName.matches(regex)){
                if(authorSecondName.matches(regex)){
                    
                    if(!description.isEmpty()){
                        if(noOfCopies.matches(numeric)){
                            if(categoryIn.getSelectedIndex() != 0){
                                success = true;
                            }else{
                                JOptionPane.showMessageDialog(null, "Please select a category", "Warning", WIDTH);
                                success = false;
                                
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "No of Copies should be Numeric ", "Warning", WIDTH);
                            success = false;
                        }
                        
                    }else{
                        JOptionPane.showMessageDialog(null, "Description can't be empty ", "Warning", WIDTH);
                        success = false;
                    }
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Author second name should be letters ", "Warning", WIDTH);
                    success = false;
                }
            }else{
                JOptionPane.showMessageDialog(null, "Author first name should be letters ", "Warning", WIDTH);
                success = false;
            }
        }else{
            
            JOptionPane.showMessageDialog(null, "Title should be letters ", "Warning", WIDTH);
            success = false;
        }
        return success;
    }

    public boolean validateTextBooks() throws HeadlessException {
        boolean success2 = false;
        
        //validation
        if(sectionIn.getSelectedIndex() != 0 ){
            if(semesterIn.getSelectedIndex() != 0 ){
                if(yearIn.getSelectedIndex() != 0 ){
                    success2 = true;
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Please select a Year ", "Warning", WIDTH);
                    success2 = false;
                }
            }else{
                JOptionPane.showMessageDialog(null, "Please select a Semester ", "Warning", WIDTH);
                success2 = false;
                
            }
        }else{
            JOptionPane.showMessageDialog(null, "Please select a Section ", "Warning", WIDTH);
            success2 = false;
            
        }
        return  success2;
    }  
    
    public void edit() {
        /*THIS ACTION ENABLE EDITING */
        
        //buttons and fileds enability
        ResorcesFiledsEnable(true);
        addBookButton.setEnabled(false);
        editBookButton.setEnabled(false);
        saveBookButton.setEnabled(true);
        removeBookButton.setEnabled(true);
        
        if(publishedBookRadio.isSelected()){
            //button enability
            publishedBookSelected();
            
        }
        if(textBookRadio.isSelected()){
            //button enability
            textBookSelected();
            
        }
    }
     
    /*------ namila ---------*/  
    
    //basuru
    public void clearFields()
    {
        fnameIn.setText("");
        lnameIn.setText("");
        emailIn.setText("");
        phoneIn.setText("");
        memberComboIn.getSelectedItem().toString();
        MemAddressIn.setText("");
        NOIBIN.setText("");
       
        searchByUserId.setText("");
        searchByNameIn.setText("");
        
        memberComboIn.setSelectedIndex(0);
    }
    //basuru
    public void tableload()
    {
        try {
            String sql = "SELECT * FROM Members"; 
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            
            jTable2.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (SQLException ex) {
            //Do nothing
        }
    
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        checkInOrRenew = new javax.swing.ButtonGroup();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        bookType = new javax.swing.ButtonGroup();
        searchBook = new javax.swing.ButtonGroup();
        SearchByGroup = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton16 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jButton7 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jButton9 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea7 = new javax.swing.JTextArea();
        jButton13 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea8 = new javax.swing.JTextArea();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        checkIn = new javax.swing.JRadioButton();
        renew = new javax.swing.JRadioButton();
        jPanel22 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        searchByUserId = new javax.swing.JTextField();
        searchByNameIn = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        searchMember = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        addMember = new javax.swing.JButton();
        updateMember = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        fnameIn = new javax.swing.JTextField();
        lnameIn = new javax.swing.JTextField();
        emailIn = new javax.swing.JTextField();
        phoneIn = new javax.swing.JTextField();
        NOIBIN = new javax.swing.JTextField();
        memberComboIn = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        MemAddressIn = new javax.swing.JTextArea();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        deleteMember = new javax.swing.JButton();
        editMember = new javax.swing.JButton();
        generateReport = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        searchByNameRadio = new javax.swing.JRadioButton();
        searchByIdRadio = new javax.swing.JRadioButton();
        jButton25 = new javax.swing.JButton();
        searchByIdBox = new javax.swing.JTextField();
        searchByNameBox = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        titleIn = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        aFirstNameIn = new javax.swing.JTextField();
        aSecondNameIn = new javax.swing.JTextField();
        isbnIn = new javax.swing.JTextField();
        publisherIn = new javax.swing.JTextField();
        editionIn = new javax.swing.JTextField();
        noOfCopiesIn = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        bookDescriptionIn = new javax.swing.JTextArea();
        jLabel49 = new javax.swing.JLabel();
        categoryIn = new javax.swing.JComboBox();
        addBookButton = new javax.swing.JButton();
        editBookButton = new javax.swing.JButton();
        saveBookButton = new javax.swing.JButton();
        removeBookButton = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        sectionIn = new javax.swing.JComboBox();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        semesterIn = new javax.swing.JComboBox();
        yearIn = new javax.swing.JComboBox();
        textBookRadio = new javax.swing.JRadioButton();
        publishedBookRadio = new javax.swing.JRadioButton();
        yearLable = new javax.swing.JLabel();
        publishedBookYearIn = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        ItemNoLable = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();
        jTextField37 = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jTextField33 = new javax.swing.JTextField();
        jTextField34 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTextArea10 = new javax.swing.JTextArea();
        jLabel56 = new javax.swing.JLabel();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jTextField32 = new javax.swing.JTextField();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jButton36 = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jMenuBar4 = new javax.swing.JMenuBar();
        jMenu8 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("jMenu5");

        jMenu6.setText("File");
        jMenuBar3.add(jMenu6);

        jMenu7.setText("Edit");
        jMenuBar3.add(jMenu7);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Library Management System");

        jTabbedPane1.setBackground(new java.awt.Color(153, 0, 204));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setFont(jTabbedPane1.getFont().deriveFont(jTabbedPane1.getFont().getStyle() | java.awt.Font.BOLD, jTabbedPane1.getFont().getSize()+1));
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jLabel4.setText("Search");

        jTextField1.setText("jTextField1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Search Options");

        jButton16.setText("IReport");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton16)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(45, 45, 45)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator1)))
                .addContainerGap(671, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16)
                .addContainerGap(445, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Search  ", new javax.swing.ImageIcon("C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library-Management-System\\icons\\Search-icon.png"), jPanel1); // NOI18N

        jLabel1.setText("First Name : ");

        jLabel2.setText("Last Name ");

        jLabel3.setText("Address 1");

        jLabel6.setText("Phone ");

        jLabel7.setText("Email");

        jLabel8.setText("Password");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addContainerGap(1186, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addContainerGap(364, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Profile", jPanel2);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Select Member"));
        jPanel5.setToolTipText("Group");

        jLabel11.setText("User ID :");

        jRadioButton1.setText("Search By Name ");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Search By User ID ");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setText("Select  Member");

        jLabel12.setText("Name :");

        jButton1.setText("Search Member");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton2)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel11)
                                .addComponent(jRadioButton1)
                                .addComponent(jLabel12)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2)
                            .addComponent(jTextField3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 32, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(16, 16, 16))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Check Out Details"));

        jLabel13.setText("Enter Copy ID to View Details");

        jLabel14.setText("Copy ID :");

        jTextField4.setText("jTextField4");

        jButton3.setText("Get Details");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jButton4.setText("Add Book");

        jLabel15.setText("Copy Details");

        jLabel16.setText("Check Out Details");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        jButton5.setText("Proceed Check Out");

        jButton6.setText("Remove Selected");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGap(63, 63, 63)
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton3))
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGap(32, 32, 32))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addGap(9, 9, 9)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton5))
                .addGap(56, 56, 56))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Trasaction Details"));

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane4.setViewportView(jTextArea4);

        jButton7.setText("Generate Recipt");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton7)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(120, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Check Out  ", new javax.swing.ImageIcon("C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library-Management-System\\icons\\out.png"), jPanel3); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Check In Details"));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setText("Enter Copy ID to View Details");
        jPanel9.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel18.setText("Copy ID :");
        jPanel9.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));

        jTextField5.setText("jTextField4");
        jPanel9.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 335, -1));

        jButton8.setText("Get Details");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, -1, -1));

        jTextArea5.setColumns(20);
        jTextArea5.setRows(5);
        jScrollPane5.setViewportView(jTextArea5);

        jPanel9.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 444, 70));

        jButton9.setText("Add Book");
        jPanel9.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 220, -1, -1));

        jLabel19.setText("Copy Details");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel20.setText("Check In Details");
        jPanel9.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        jTextArea6.setColumns(20);
        jTextArea6.setRows(5);
        jScrollPane6.setViewportView(jTextArea6);

        jPanel9.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 444, 81));

        jButton10.setText("Proceed Check In");
        jPanel9.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 360, -1, -1));

        jButton11.setText("Remove Selected");
        jPanel9.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Renew Details"));
        jPanel10.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jPanel10ComponentAdded(evt);
            }
        });
        jPanel10.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jPanel10ComponentHidden(evt);
            }
        });

        jLabel21.setText("Enter Copy ID to View Details");

        jLabel22.setText("Copy ID :");

        jTextField6.setText("jTextField4");

        jButton12.setText("Get Details");

        jTextArea7.setColumns(20);
        jTextArea7.setRows(5);
        jScrollPane7.setViewportView(jTextArea7);

        jButton13.setText("Add Book");

        jLabel23.setText("Copy Details");

        jLabel24.setText("Renew Details");

        jTextArea8.setColumns(20);
        jTextArea8.setRows(5);
        jScrollPane8.setViewportView(jTextArea8);

        jButton14.setText("Proceed Renew");

        jButton15.setText("Remove Selected");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel21))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel22)
                        .addGap(10, 10, 10)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(376, 376, 376)
                        .addComponent(jButton12))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel23))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(387, 387, 387)
                        .addComponent(jButton13))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel24))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton15)
                        .addGap(232, 232, 232)
                        .addComponent(jButton14)))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel21)
                .addGap(6, 6, 6)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel22))
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jButton12)
                .addGap(17, 17, 17)
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jButton13)
                .addGap(6, 6, 6)
                .addComponent(jLabel24)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton15)
                    .addComponent(jButton14))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        checkInOrRenew.add(checkIn);
        checkIn.setSelected(true);
        checkIn.setText("Check In");
        checkIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkInActionPerformed(evt);
            }
        });

        checkInOrRenew.add(renew);
        renew.setText("Renew");
        renew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(checkIn)
                        .addGap(10, 10, 10)
                        .addComponent(renew)))
                .addContainerGap(339, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkIn)
                    .addComponent(renew))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(131, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Check In  ", new javax.swing.ImageIcon("C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library-Management-System\\icons\\IN.png"), jPanel6); // NOI18N

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1348, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 574, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Overdues  ", new javax.swing.ImageIcon("C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library-Management-System\\icons\\overdue.png"), jPanel22); // NOI18N

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Transction Details"));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 86, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1336, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Select Member"));
        jPanel17.setToolTipText("Group");

        jLabel46.setText("User ID :");

        SearchByGroup.add(jRadioButton11);
        jRadioButton11.setText("Search By Name ");
        jRadioButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton11ActionPerformed(evt);
            }
        });

        SearchByGroup.add(jRadioButton12);
        jRadioButton12.setSelected(true);
        jRadioButton12.setText("Search By User ID ");
        jRadioButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton12ActionPerformed(evt);
            }
        });

        searchByUserId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchByUserIdActionPerformed(evt);
            }
        });
        searchByUserId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchByUserIdFocusGained(evt);
            }
        });

        searchByNameIn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchByNameInKeyPressed(evt);
            }
        });

        jLabel47.setText("Name :");

        searchMember.setText("Search Member");
        searchMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchMemberActionPerformed(evt);
            }
        });

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Member Data"));

        addMember.setText("Add New Member");
        addMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMemberActionPerformed(evt);
            }
        });

        updateMember.setText("Update Member");
        updateMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateMemberActionPerformed(evt);
            }
        });

        jLabel29.setText("First Name :");

        jLabel30.setText("Last Name :");

        jLabel31.setText("Email Address :");

        jLabel32.setText("Phone Number :");

        jLabel35.setText("Member Type :");

        jLabel37.setText("Number of Items Borrowed :");

        fnameIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fnameInActionPerformed(evt);
            }
        });

        NOIBIN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NOIBINActionPerformed(evt);
            }
        });

        memberComboIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Please Select", "Faculty", "Student", "Staff" }));
        memberComboIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberComboInActionPerformed(evt);
            }
        });

        jLabel36.setText("Address :");

        MemAddressIn.setColumns(20);
        MemAddressIn.setRows(5);
        jScrollPane16.setViewportView(MemAddressIn);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(addMember)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(updateMember))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lnameIn, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fnameIn, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(emailIn, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(phoneIn, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addComponent(jLabel36)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addGap(81, 81, 81)
                                        .addComponent(jLabel35)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(memberComboIn, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel37)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(NOIBIN, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addMember)
                    .addComponent(updateMember))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel35)
                    .addComponent(fnameIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(memberComboIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(lnameIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(emailIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(NOIBIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(phoneIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addGap(0, 84, Short.MAX_VALUE))
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "LibraryID", "First Name", "Last Name", "Address", "Phone", "Email", "Type", "No of Items Borrowd"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.setSelectionBackground(new java.awt.Color(255, 102, 102));
        jTable2.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(jTable2);

        deleteMember.setText("Delete Member");
        deleteMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMemberActionPerformed(evt);
            }
        });

        editMember.setText("Edit Member");
        editMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMemberActionPerformed(evt);
            }
        });

        generateReport.setText("Genarate Report");
        generateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(searchMember))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton12)
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel46)
                                        .addComponent(jRadioButton11)
                                        .addComponent(jLabel47)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(searchByUserId, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                                    .addComponent(searchByNameIn))))
                        .addGap(56, 56, 56))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(generateReport, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(editMember, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteMember))
                            .addComponent(jScrollPane14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jRadioButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel46))
                    .addComponent(searchByUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jRadioButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(searchByNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchMember)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteMember)
                    .addComponent(editMember)
                    .addComponent(generateReport))
                .addGap(9, 9, 9))
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Members  ", new javax.swing.ImageIcon("C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library-Management-System\\icons\\members.png"), jPanel7); // NOI18N

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 102)), "Select Resource", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(0, 51, 255))); // NOI18N
        jPanel16.setToolTipText("Group");

        jLabel44.setText("Book ID :");

        searchBook.add(searchByNameRadio);
        searchByNameRadio.setText("Search By Name ");
        searchByNameRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchByNameRadioActionPerformed(evt);
            }
        });

        searchBook.add(searchByIdRadio);
        searchByIdRadio.setSelected(true);
        searchByIdRadio.setText("Search By Book ID ");
        searchByIdRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchByIdRadioActionPerformed(evt);
            }
        });

        jButton25.setText("Select  Resource");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jLabel45.setText("Name :");

        jButton26.setIcon(new javax.swing.ImageIcon("C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library-Management-System\\icons\\Search-iconsmall.png")); // NOI18N
        jButton26.setText("Search Book");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton30.setText("Remove Selected");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTable1MouseExited(evt);
            }
        });
        jScrollPane13.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchByIdRadio)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jLabel44)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchByIdBox, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jButton30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton25))
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(searchByNameRadio)
                                    .addComponent(jLabel45))
                                .addGap(18, 18, 18)
                                .addComponent(searchByNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton26, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(searchByIdRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(searchByIdBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(searchByNameRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(searchByNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jButton26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton30)
                    .addComponent(jButton25))
                .addGap(16, 16, 16))
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resource Details ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(0, 51, 204))); // NOI18N
        jPanel18.setForeground(new java.awt.Color(0, 51, 204));
        jPanel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel9.setText("Title :");

        jLabel10.setText("Author First  Name :");

        jLabel25.setText("Author Second  Name  :");

        jLabel26.setText("Publisher :");

        jLabel28.setText("Description :");

        jLabel40.setText("Barcode :");

        jLabel41.setText("ISBN # :");

        jLabel42.setText("Edition :");

        jLabel43.setText("# Of Copies :");

        jLabel48.setText("Category :");

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        bookDescriptionIn.setColumns(20);
        bookDescriptionIn.setRows(5);
        jScrollPane9.setViewportView(bookDescriptionIn);

        jLabel49.setText("Book Type :");

        categoryIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select One ", "IT", "Maths " }));

        addBookButton.setText("Add Book");
        addBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBookButtonActionPerformed(evt);
            }
        });

        editBookButton.setText("Edit Book");
        editBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBookButtonActionPerformed(evt);
            }
        });

        saveBookButton.setText("Save ");
        saveBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBookButtonActionPerformed(evt);
            }
        });

        removeBookButton.setText("Remove Book");
        removeBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBookButtonActionPerformed(evt);
            }
        });

        jLabel54.setText("Section :");

        sectionIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select One", "Text Books ", "Published Books " }));
        sectionIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sectionInActionPerformed(evt);
            }
        });

        jLabel57.setText("Semster :");

        jLabel58.setText("Year :");

        semesterIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select One", "1st Semester", "2nd Semester" }));
        semesterIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                semesterInActionPerformed(evt);
            }
        });

        yearIn.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select One", "Year 1", "Year 2", "Year 3", "Year 4" }));
        yearIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearInActionPerformed(evt);
            }
        });

        bookType.add(textBookRadio);
        textBookRadio.setText("Text Book");
        textBookRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textBookRadioActionPerformed(evt);
            }
        });

        bookType.add(publishedBookRadio);
        publishedBookRadio.setSelected(true);
        publishedBookRadio.setText("Published Book");
        publishedBookRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publishedBookRadioActionPerformed(evt);
            }
        });

        yearLable.setText("Year :");

        jLabel27.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel27.setText("ITEM NO :");

        ItemNoLable.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel28)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(yearLable)
                                    .addComponent(jLabel27))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(publishedBookYearIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ItemNoLable, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(jLabel48))
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(jLabel49))
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addComponent(jLabel54))
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(jLabel58)))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noOfCopiesIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(categoryIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(publishedBookRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(textBookRadio))
                                    .addComponent(sectionIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(yearIn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(51, 51, 51)
                                        .addComponent(jLabel57)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(semesterIn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel9)
                        .addGap(10, 10, 10)
                        .addComponent(titleIn, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(addBookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel10)
                        .addGap(10, 10, 10)
                        .addComponent(aFirstNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel40)
                        .addGap(10, 10, 10)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(editBookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel25)
                        .addGap(10, 10, 10)
                        .addComponent(aSecondNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel41)
                        .addGap(10, 10, 10)
                        .addComponent(isbnIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(saveBookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jLabel26)
                        .addGap(10, 10, 10)
                        .addComponent(publisherIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel42)
                        .addGap(10, 10, 10)
                        .addComponent(editionIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(removeBookButton)))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel9))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(titleIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addBookButton))
                .addGap(11, 11, 11)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel10))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(aFirstNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel40))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(editBookButton))
                .addGap(11, 11, 11)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel25))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(aSecondNameIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel41))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(isbnIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(saveBookButton))
                .addGap(11, 11, 11)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel26))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(publisherIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel42))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(editionIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(removeBookButton))
                .addGap(10, 10, 10)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addComponent(jLabel43)
                                    .addGap(17, 17, 17)
                                    .addComponent(jLabel48)
                                    .addGap(14, 14, 14)
                                    .addComponent(jLabel49)
                                    .addGap(15, 15, 15)
                                    .addComponent(jLabel54)
                                    .addGap(17, 17, 17)
                                    .addComponent(jLabel58))
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(noOfCopiesIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(resetButton))
                                    .addGap(9, 9, 9)
                                    .addComponent(categoryIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(7, 7, 7)
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(publishedBookRadio)
                                        .addComponent(textBookRadio))
                                    .addGap(7, 7, 7)
                                    .addComponent(sectionIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(11, 11, 11)
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(yearIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addGap(3, 3, 3)
                                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel57)
                                                .addComponent(semesterIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(yearLable)
                                .addComponent(publishedBookYearIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel27)
                                .addComponent(ItemNoLable)))
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Resources  ", new javax.swing.ImageIcon("C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library-Management-System\\icons\\resourses.png"), jPanel14); // NOI18N

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Check Out Peroid "));

        jLabel50.setText("Staff Days :");

        jLabel51.setText("Faculty Days :");

        jLabel52.setText("Student Days :");

        jTextField35.setText("jTextField31");

        jTextField36.setText("jTextField31");

        jTextField37.setText("jTextField31");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel50))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel52))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel51)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField36, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(jTextField37, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField35, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(37, 37, 37))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(173, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52)))
                    .addComponent(jLabel50))
                .addGap(33, 33, 33))
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Fine"));

        jLabel55.setText("Fine Charge :");

        jTextField38.setText("jTextField31");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField38)
                .addGap(38, 38, 38))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Resource Limitation"));

        jLabel59.setText("Staff Limit :");

        jLabel60.setText("Faculty Limit :");

        jLabel61.setText("Student Limit :");

        jTextField31.setText("jTextField31");

        jTextField33.setText("jTextField31");

        jTextField34.setText("jTextField31");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel59, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel60, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel61, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField31, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jTextField33, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField34, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60)
                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Category Managment"));

        jTextArea10.setColumns(20);
        jTextArea10.setRows(5);
        jScrollPane10.setViewportView(jTextArea10);

        jLabel56.setText("Category :");

        jButton31.setText("Remove Category");

        jButton32.setText("Add new Category");

        jTextField32.setText("jTextField31");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel56)
                            .addComponent(jButton31))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                    .addContainerGap(184, Short.MAX_VALUE)
                    .addComponent(jButton32)
                    .addGap(20, 20, 20)))
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addGap(62, 62, 62)
                    .addComponent(jTextField32)
                    .addGap(20, 20, 20)))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel56)
                .addGap(62, 62, 62)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton31)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addGap(63, 63, 63)
                    .addComponent(jButton32)
                    .addContainerGap(362, Short.MAX_VALUE)))
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(393, Short.MAX_VALUE)))
        );

        jButton33.setText("Edit ");

        jButton34.setText("Save");

        jButton35.setText("Default");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(232, 232, 232))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton33)
                    .addComponent(jButton34)
                    .addComponent(jButton35))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Administration  ", new javax.swing.ImageIcon("C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library-Management-System\\icons\\admin.png"), jPanel11); // NOI18N

        jButton36.setText("Log Out");

        jLabel53.setText("Logged in As : ");

        jMenu8.setText("File");
        jMenu8.setActionCommand("File New");
        jMenuBar4.add(jMenu8);

        jMenu9.setText("Edit");
        jMenuBar4.add(jMenu9);

        jMenu11.setText("Help");
        jMenuBar4.add(jMenu11);

        setJMenuBar(jMenuBar4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel53)
                        .addGap(219, 219, 219)
                        .addComponent(jButton36)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator3)
                            .addComponent(jTabbedPane1))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton36)
                    .addComponent(jLabel53))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked

    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        int status =  JOptionPane.showConfirmDialog(null,"This will Reset the fileds ? ", "Reset", WIDTH, WIDTH,null);
        if(status == 0){
            reset();
        }
    }//GEN-LAST:event_resetButtonActionPerformed

   

    private void publishedBookRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishedBookRadioActionPerformed
        if (publishedBookRadio.isSelected()) {
            publishedBookSelected();
        }
    }//GEN-LAST:event_publishedBookRadioActionPerformed

    private void textBookRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textBookRadioActionPerformed
        if (textBookRadio.isSelected()) {
            textBookSelected();
        }
    }//GEN-LAST:event_textBookRadioActionPerformed

    private void yearInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_yearInActionPerformed

    private void semesterInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_semesterInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_semesterInActionPerformed

    private void sectionInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sectionInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sectionInActionPerformed

    private void removeBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBookButtonActionPerformed
        int status =  JOptionPane.showConfirmDialog(null,"This will Delete the recorde Permently ? ", "DELETE", WIDTH, WIDTH,null);
        
        if(status == 0){
        
        if(publishedBookRadio.isSelected()){
            cp.delete_book();

        }

        if(textBookRadio.isSelected()){

            ct.delete_book();

        }
            reset();
        }
    }//GEN-LAST:event_removeBookButtonActionPerformed

    private void saveBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBookButtonActionPerformed

        /* THIS ACTION SAVE EDITED  DATA*/

        //buttons
        //pop a confiram box and get the user choice
        int status =  JOptionPane.showConfirmDialog(null,"Do you need to save updated content ? ", "Save", WIDTH, WIDTH,null);
        //if user choose yes perform the action
        if( status == 0){
            editBookButton.setEnabled(true);
            saveBookButton.setEnabled(false);
            removeBookButton.setEnabled(false);
            ResorcesFiledsEnable(false);

            title = titleIn.getText();
            authorFirstName = aFirstNameIn.getText();
            authorSecondName = aSecondNameIn.getText();            
            description = bookDescriptionIn.getText();            
            noOfCopies = noOfCopiesIn.getText();
            catogory = categoryIn.getSelectedItem().toString();
            
            if(validateResourseInputs()){
            
            

                //publlished book selected then below action
                if(publishedBookRadio.isSelected()){

                    edition = editionIn.getText();
                    ISBN = isbnIn.getText();
                    publisher = publisherIn.getText();
                    year = publishedBookYearIn.getText();

                    if(validatePublishedBooks()){
                        int noOfcp = Integer.parseInt(noOfCopies);
                        JOptionPane.showMessageDialog(null, "In published books.!!", "in IF", JOptionPane.PLAIN_MESSAGE);
                        cp.update_book(ISBN, title, description, edition, catogory, year, authorFirstName, authorSecondName, publisher, noOfcp);
                        JOptionPane.showMessageDialog(null, "Sussess Fully Updated .!!", "Error", JOptionPane.PLAIN_MESSAGE);
                    }else{
                        edit();
                    }
                
                }    
                
                //text book selected then below action
                if(textBookRadio.isSelected()){               


                    if(validateTextBooks()){
                        section = sectionIn.getSelectedItem().toString();
                        semester = semesterIn.getSelectedItem().toString();
                        year = yearIn.getSelectedItem().toString();

                        int noOfcp = Integer.parseInt(noOfCopies);
                        JOptionPane.showMessageDialog(null, "In published books.!!", "in IF", JOptionPane.PLAIN_MESSAGE);
                        ct.update_book(title,description,section,catogory,semester,year,authorFirstName,authorSecondName,noOfcp);
                        JOptionPane.showMessageDialog(null, "Sussess Fully Updated .!!", "Error", JOptionPane.PLAIN_MESSAGE);
                    }else {
                        edit();
                    }
                }    
                

            }else {
                edit();
            }
        }     
    }//GEN-LAST:event_saveBookButtonActionPerformed

    private void editBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBookButtonActionPerformed

        edit();

    }//GEN-LAST:event_editBookButtonActionPerformed

   

    private void addBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBookButtonActionPerformed
        /*THIS ACTION ADD NEW BOOK*/

        title = titleIn.getText();
        authorFirstName = aFirstNameIn.getText();
        authorSecondName = aSecondNameIn.getText();
        
        description = bookDescriptionIn.getText();
        
        noOfCopies = noOfCopiesIn.getText();
        catogory = categoryIn.getSelectedItem().toString();
        
      
       
        
     
        if (validateResourseInputs()) {
            //JOptionPane.showMessageDialog(null,"addded.!!","in IF" ,JOptionPane.PLAIN_MESSAGE);
            //for textBook
            if (textBookRadio.isSelected()) {
                
                if(validateTextBooks()){
                
                        section = sectionIn.getSelectedItem().toString();
                        semester = semesterIn.getSelectedItem().toString();
                        year = yearIn.getSelectedItem().toString();
                            
                        int noOfcp = Integer.parseInt(noOfCopies);
                        //call add textbook
                        ct.add_Book(title,description,section,catogory,semester,year,authorFirstName,authorSecondName,noOfcp);

                        JOptionPane.showMessageDialog(null, "Record Added Sussesfully", "Message", JOptionPane.PLAIN_MESSAGE);
                }
                            
            }
            //for publishedBook
            if (publishedBookRadio.isSelected()) {
                
                edition = editionIn.getText();
                publisher = publisherIn.getText();
                ISBN = isbnIn.getText();
                year = publishedBookYearIn.getText();
               
                if(validatePublishedBooks()){
                
                    int noOfcp = Integer.parseInt(noOfCopies);

                    //JOptionPane.showMessageDialog(null, "In published books.!!", "in IF", JOptionPane.PLAIN_MESSAGE);
                    cp.add_Book(ISBN, title, description, edition, catogory, year, authorFirstName, authorSecondName, publisher, noOfcp);
                    JOptionPane.showMessageDialog(null, "Record Added Sussesfully", "Message", JOptionPane.PLAIN_MESSAGE);    
                } 
                //bookType = "Published Book";
            }

            //String sql  = "INSERT INTO Members(fname) VALUES ('"+jTextField9.getText()+"')";
        } else {
            JOptionPane.showMessageDialog(null, "fileds are empty.!!", "Error", JOptionPane.PLAIN_MESSAGE);
        }

    }//GEN-LAST:event_addBookButtonActionPerformed

 

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTable1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseExited

    }//GEN-LAST:event_jTable1MouseExited

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        jButton25.setEnabled(true);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
        if (jTable1.getSelectedRow() != -1) {
            model1.removeRow(jTable1.getSelectedRow());
        }

    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        if (searchByIdRadio.isSelected()) {
            String getSearchId = searchByIdBox.getText();
            int id = Integer.parseInt(getSearchId);
            String sql = "SELECT B.itemNO,B.title,CT.qty FROM Books B,textbooks T,c_textbook CT WHERE B.itemNO =T.itemNo AND T.itemNo = CT.itemNo AND B.itemNO = '" + id + "' UNION SELECT B.itemNO,B.title,CP.qty FROM Books B,publishedbooks P,c_publishedbooks CP WHERE B.itemNO =P.itemNo AND P.itemNo = CP.itemNo AND B.itemNO = '" + id + "' ";

            try {
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                jTable1.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (SQLException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (searchByNameRadio.isSelected()) {

            String getSearchName = searchByNameBox.getText();

            String sql = "SELECT B.itemNO,B.title,CT.qty FROM Books B,textbooks T,c_textbook CT WHERE B.itemNO =T.itemNo  AND T.itemNo = CT.itemNo AND B.title LIKE '%" + getSearchName + "%' UNION SELECT B.itemNO,B.title,CP.qty FROM Books B,publishedbooks P,c_publishedbooks CP WHERE B.itemNO =P.itemNo AND P.itemNo = CP.itemNo AND B.title LIKE '%" + getSearchName + "%' ";

            try {
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                jTable1.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (SQLException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed

        /*---THIS ACTION FORWAD SELECTED DATA TO EDIT PANEL USING ITEM NO ---*/

        //buttons
        ResorcesFiledsEnable(false);
        editBookButton.setEnabled(true);
        //get row from the Jtable
        int r = jTable1.getSelectedRow();

        String itemno = jTable1.getValueAt(r, 0).toString();

        JOptionPane.showMessageDialog(null, itemno);

        int ID = Integer.parseInt(itemno);
        int Status = 0;

        try {
            //call the getStatus SQL function to determine the table
            String sql = " SELECT getStatus ('" + ID + "') AS 'Status' ;";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Status = rs.getInt("Status");

            }
            //getStatus function returns 1 then perform following action  (pub;ished books)
            if (Status == 1) {
                publishedBookRadio.setSelected(rootPaneCheckingEnabled);
                //publishedBookSelected();
                BookTypeRadioButtonEnable(false);
                JOptionPane.showMessageDialog(null, "yeah a Published Book");
                cp.setData(itemno);
                //JOptionPane.showMessageDialog(null, cp.title);

                titleIn.setText(cp.title);
                aFirstNameIn.setText(cp.authorFirstName);
                aSecondNameIn.setText(cp.authorSecondName);
                publisherIn.setText(cp.publisher);
                bookDescriptionIn.append(cp.description);
                isbnIn.setText(cp.ISBN);
                editionIn.setText(cp.edition);
                Integer nocop;
                nocop = new Integer(cp.qty);
                noOfCopiesIn.setText(nocop.toString());
                categoryIn.setSelectedItem(cp.category);
                publishedBookYearIn.setText(cp.yearPublished);
                Integer itn;
                itn = new Integer(cp.itemNo);
                ItemNoLable.setText(itn.toString());

            }
            //getStatus function returns 0 then perform following action (textbooks)
            if (Status == 0) {
                textBookRadio.setSelected(rootPaneCheckingEnabled);
                BookTypeRadioButtonEnable(false);
                //textBookSelected();
                JOptionPane.showMessageDialog(null, "yeah a Text Book");

                ct.setData(itemno);

                titleIn.setText(ct.title);
                aFirstNameIn.setText(ct.authorFirstName);
                aSecondNameIn.setText(ct.authorSecondName);

                bookDescriptionIn.append(ct.description);

                Integer nocop;
                nocop = new Integer(ct.qty);
                noOfCopiesIn.setText(nocop.toString());
                categoryIn.setSelectedItem(ct.category);
                sectionIn.setSelectedItem(ct.section);
                yearIn.setSelectedItem(ct.year);
                semesterIn.setSelectedItem(ct.semester);
                Integer itn;
                itn = new Integer(ct.itemNo);
                ItemNoLable.setText(itn.toString());

            }
            //titleIn.setText(title);
            //isbnIn.setText(ISBN);

        } catch (SQLException ex) {
            Logger.getLogger(copyOfPublishedBooks.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton25ActionPerformed

    private void searchByIdRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchByIdRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchByIdRadioActionPerformed

    private void searchByNameRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchByNameRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchByNameRadioActionPerformed

    private void renewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewActionPerformed

        //if renew selectd then below method hide the check-in group
        if (renew.isSelected()) {
            hideGroup(jPanel10, jPanel9);
        }
    }//GEN-LAST:event_renewActionPerformed

    private void checkInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkInActionPerformed
        //if check-in selectd then below method hide the renew group
        if (checkIn.isSelected()) {
            hideGroup(jPanel9, jPanel10);
        }
    }//GEN-LAST:event_checkInActionPerformed

    private void jPanel10ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel10ComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel10ComponentHidden

    private void jPanel10ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jPanel10ComponentAdded

    }//GEN-LAST:event_jPanel10ComponentAdded

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        try {
            String report = "C:\\Users\\Namila Radith\\Documents\\NetBeansProjects\\Library Management System\\report1.jrxml";
            JasperReport jr = JasperCompileManager.compileReport(report);
            JasperPrint jp = JasperFillManager.fillReport(jr, null,con);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton16ActionPerformed

    private void jRadioButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton11ActionPerformed
        searchByUserId.setEditable(false);
        searchByNameIn.setEditable(true);
        searchMember.setEnabled(true);
        clearFields();
    }//GEN-LAST:event_jRadioButton11ActionPerformed

    private void jRadioButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton12ActionPerformed
        searchByNameIn.setEditable(false);
        searchByUserId.setEditable(true);
        searchMember.setEnabled(true);
        clearFields();
    }//GEN-LAST:event_jRadioButton12ActionPerformed

    private void searchByUserIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchByUserIdActionPerformed

    }//GEN-LAST:event_searchByUserIdActionPerformed

    private void searchByUserIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchByUserIdFocusGained
        searchMember.setEnabled(true);
    }//GEN-LAST:event_searchByUserIdFocusGained

    private void searchByNameInKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchByNameInKeyPressed
        //        Members M2 = new Members();
        //        M2.searchMember(searchByNameIn.getText());
        //          try {
            //            String sql = "SELECT * FROM Members WHERE fname LIKE '%"+searchByNameIn.getText()+"%'";
            //            pst = con.prepareStatement(sql);
            //            rs = pst.executeQuery();
            //
            //        } catch (SQLException ex) {
            //            Logger.getLogger(Members.class.getName()).log(Level.SEVERE, null, ex);
            //        }
        //        jTable2.setModel(DbUtils.resultSetToTableModel(rs));

    }//GEN-LAST:event_searchByNameInKeyPressed

    private void searchMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchMemberActionPerformed
        if(jRadioButton11.isSelected())
        {
            try {
                String sql = "SELECT * FROM Members WHERE fname LIKE '%"+searchByNameIn.getText()+"%'";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(Members.class.getName()).log(Level.SEVERE, null, ex);
            }

            jTable2.setModel(DbUtils.resultSetToTableModel(rs));
        }

        if(jRadioButton12.isSelected())
        {
            String userId = searchByUserId.getText();
            String newuserID = "";
            int length = userId.length();
            for(int i = 0;i<length;i++)
            {
                if(Character.isDigit(userId.charAt(i)))
                {
                    newuserID+=userId.charAt(i);
                }
            }
            try {
                String sql = "SELECT * FROM Members WHERE libraryID='"+newuserID+"'";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(Members.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTable2.setModel(DbUtils.resultSetToTableModel(rs));
        }
    }//GEN-LAST:event_searchMemberActionPerformed

    private void addMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMemberActionPerformed
        /**
        * @author Basuru
        *
        */
        //            jButton18.setEnabled(false);
        //            jButton20.setEnabled(true);

        String firstName = fnameIn.getText();
        String lastName = lnameIn.getText();
        String email = emailIn.getText();
        String phone = phoneIn.getText();
        String MemType = memberComboIn.getSelectedItem().toString();
        String Address = MemAddressIn.getText();
        String noOfItemsBo = NOIBIN.getText();

        String regex="^[a-zA-Z ]+$";
        String regexemail="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String regexphone="^[0-9]+$";
        String regexuser="^[0-9a-zA-Z]+$";
        String regexadd="^[0-9a-zA-Z ,]+$";

        if(!firstName.matches(regex))
        {
            JOptionPane.showMessageDialog(null,"The Name should be Letters" );

        }else if(!lastName.matches(regex))
        {
            JOptionPane.showMessageDialog(null,"The Last Name should be Letters" );

        }else if(!email.matches(regexemail))
        {
            JOptionPane.showMessageDialog(null,"The Email is not valid" );

        }else if(!phone.matches(regexphone))
        {
            JOptionPane.showMessageDialog(null,"Phone number should only be numbers" );

        }else if(!MemType.matches(regex)||MemType.equals("Please Select"))
        {
            JOptionPane.showMessageDialog(null,"The Member Type should be filled" );

        }else if(!Address.matches(regexadd))
        {
            JOptionPane.showMessageDialog(null,"The Address should contain letters and numbers" );

        }else if(!noOfItemsBo.matches(regexphone))
        {
            JOptionPane.showMessageDialog(null,"No of borrowed items should only contain numbers numbers" );
        }
        else
        {
            int noOfItems = Integer.parseInt(noOfItemsBo);

            Members M = new Members(firstName,lastName,Address,email,phone,MemType,noOfItems);

            M.addMember();

            //update new values for the table
            tableload();

            // clears the text fields
            clearFields();
        }

    }//GEN-LAST:event_addMemberActionPerformed

    private void updateMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateMemberActionPerformed
        addMember.setEnabled(true);

        String firstName = fnameIn.getText();
        String lastName = lnameIn.getText();
        String email = emailIn.getText();
        String phone = phoneIn.getText();
        String MemType = memberComboIn.getSelectedItem().toString();
        String Address = MemAddressIn.getText();
        String noOfItemsBo = NOIBIN.getText();

        String regex="^[a-zA-Z ]+$";
        String regexemail="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String regexphone="^[0-9]+$";
        String regexuser="^[0-9a-zA-Z]+$";
        String regexadd="^[0-9a-zA-Z ,]+$";

        if(!firstName.matches(regex))
        {
            JOptionPane.showMessageDialog(null,"The Name should be Letters" );

        }else if(!lastName.matches(regex))
        {
            JOptionPane.showMessageDialog(null,"The Last Name should be Letters" );

        }else if(!email.matches(regexemail))
        {
            JOptionPane.showMessageDialog(null,"The Email is not valid" );

        }else if(!phone.matches(regexphone))
        {
            JOptionPane.showMessageDialog(null,"Phone number should only be numbers" );

        }else if(!MemType.matches(regex)||MemType.equals("Please Select"))
        {
            JOptionPane.showMessageDialog(null,"The Member Type should be filled" );

        }else if(!Address.matches(regexadd))
        {
            JOptionPane.showMessageDialog(null,"The Address should contain letters and numbers" );

        }else if(!noOfItemsBo.matches(regexphone))
        {
            JOptionPane.showMessageDialog(null,"No of borrowed items should only contain numbers numbers" );
        }
        else
        {
            int x = JOptionPane.showConfirmDialog(null, "Are you Sure you want to Update the currunt Member ?", "Update Member", WIDTH);
            if(x==0)
            {
                int noOfItems = Integer.parseInt(noOfItemsBo);
                Members M4 = new Members(firstName, lastName, Address, email, phone, MemType, noOfItems);
                updateMember.setEnabled(false);
                clearFields();
                JOptionPane.showMessageDialog(null, M4.editMember(LibraryID),"Update Completed",WIDTH);
                tableload();
                jTable2.setRowSelectionInterval(rowCount,rowCount);
            }

        }

    }//GEN-LAST:event_updateMemberActionPerformed

    private void fnameInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fnameInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fnameInActionPerformed

    private void NOIBINActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NOIBINActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NOIBINActionPerformed

    private void memberComboInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberComboInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_memberComboInActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        deleteMember.setEnabled(true);
        editMember.setEnabled(true);

    }//GEN-LAST:event_jTable2MouseClicked

    private void deleteMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMemberActionPerformed
        Members M1 = new Members();
        rowCount = jTable2.getSelectedRow();

        LibraryID = jTable2.getValueAt(rowCount, 0).toString();
        if(LibraryID==""|| LibraryID==null)
        {
            JOptionPane.showMessageDialog(null, "You must Select a Member to Delete");
        }
        else
        {
            int selection = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Member ? ","Delete Member",1);
            if (selection==0)
            {
                JOptionPane.showMessageDialog(null, M1.deleteMember(LibraryID));
                clearFields();
                tableload();
            }

        }

    }//GEN-LAST:event_deleteMemberActionPerformed

    private void editMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMemberActionPerformed
        updateMember.setEnabled(true);
        addMember.setEnabled(false);

        rowCount = jTable2.getSelectedRow();

        LibraryID = jTable2.getValueAt(rowCount, 0).toString();
        fnameIn.setText(jTable2.getValueAt(rowCount, 1).toString());
        lnameIn.setText(jTable2.getValueAt(rowCount,2).toString());
        MemAddressIn.setText(jTable2.getValueAt(rowCount,3).toString());
        phoneIn.setText(jTable2.getValueAt(rowCount,4).toString());
        emailIn.setText(jTable2.getValueAt(rowCount,5).toString());
        memberComboIn.setSelectedItem(jTable2.getValueAt(rowCount,6).toString());
        NOIBIN.setText(jTable2.getValueAt(rowCount,7).toString());
    }//GEN-LAST:event_editMemberActionPerformed

    private void generateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportActionPerformed
        /**
        *
        * @reportGenerator Calls the Genarate report function
        */
        HashMap parameter = new HashMap();
        parameter.put("MemberType", "Admin");

        ReportGenerator RP = new ReportGenerator();
        RP.generateReport();
        RP.generateParameterizedReport(parameter);

    }//GEN-LAST:event_generateReportActionPerformed



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ItemNoLable;
    private javax.swing.JTextArea MemAddressIn;
    private javax.swing.JTextField NOIBIN;
    private javax.swing.ButtonGroup SearchByGroup;
    private javax.swing.JTextField aFirstNameIn;
    private javax.swing.JTextField aSecondNameIn;
    private javax.swing.JButton addBookButton;
    private javax.swing.JButton addMember;
    private javax.swing.JTextArea bookDescriptionIn;
    private javax.swing.ButtonGroup bookType;
    private javax.swing.JComboBox categoryIn;
    private javax.swing.JRadioButton checkIn;
    private javax.swing.ButtonGroup checkInOrRenew;
    private javax.swing.JButton deleteMember;
    private javax.swing.JButton editBookButton;
    private javax.swing.JButton editMember;
    private javax.swing.JTextField editionIn;
    private javax.swing.JTextField emailIn;
    private javax.swing.JTextField fnameIn;
    private javax.swing.JButton generateReport;
    private javax.swing.JTextField isbnIn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuBar jMenuBar4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea10;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextArea jTextArea7;
    private javax.swing.JTextArea jTextArea8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField lnameIn;
    private javax.swing.JComboBox memberComboIn;
    private javax.swing.JTextField noOfCopiesIn;
    private javax.swing.JTextField phoneIn;
    private javax.swing.JRadioButton publishedBookRadio;
    private javax.swing.JTextField publishedBookYearIn;
    private javax.swing.JTextField publisherIn;
    private javax.swing.JButton removeBookButton;
    private javax.swing.JRadioButton renew;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveBookButton;
    private javax.swing.ButtonGroup searchBook;
    private javax.swing.JTextField searchByIdBox;
    private javax.swing.JRadioButton searchByIdRadio;
    private javax.swing.JTextField searchByNameBox;
    private javax.swing.JTextField searchByNameIn;
    private javax.swing.JRadioButton searchByNameRadio;
    private javax.swing.JTextField searchByUserId;
    private javax.swing.JButton searchMember;
    private javax.swing.JComboBox sectionIn;
    private javax.swing.JComboBox semesterIn;
    private javax.swing.JRadioButton textBookRadio;
    private javax.swing.JTextField titleIn;
    private javax.swing.JButton updateMember;
    private javax.swing.JComboBox yearIn;
    private javax.swing.JLabel yearLable;
    // End of variables declaration//GEN-END:variables

}
