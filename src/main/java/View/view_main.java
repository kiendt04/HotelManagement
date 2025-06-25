/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.HeadlessException;
import javax.swing.JFrame;
import Control.myconnect;
import java.awt.Dimension;
import java.sql.Connection;

/**
 *
 * @author ADMIN
 */
public class view_main extends JFrame{
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public view_main() throws HeadlessException {
        super("Qly Khách sạn");
        initUI();
    }
    
    private void initUI()
    {
        this.setSize(new Dimension(200,200));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public static void main(String[] args) {
        new view_main();
    }
}
