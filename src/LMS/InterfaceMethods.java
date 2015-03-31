/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LMS;

import java.awt.Component;
import javax.swing.JPanel;

/**
 *
 * @author Namila Radith
 */
public class InterfaceMethods {
    
    //this method use to hide groups 
    public static void hideGroup(JPanel showThis, JPanel hideThis) {
        Component[] component9 = showThis.getComponents();
        Component[] component10 = hideThis.getComponents();
        showThis.setEnabled(true);
        hideThis.setEnabled(false);
        for (int i = 0; i < component9.length; i++) {
            component9[i].setEnabled(true);
            component10[i].setEnabled(false);
        }
    }
    
}
