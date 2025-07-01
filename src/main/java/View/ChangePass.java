/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author ADMIN
 */
public class ChangePass extends JFrame{
    
    private JPasswordField oldpass,newpass,recheck;
    private JButton save,close,showOldPass,showNewPass;
    private int id;
    
    public ChangePass() throws HeadlessException {
    }
    
    public ChangePass(int id) throws HeadlessException {
        this.id = id;
        this.setSize(new Dimension(300,200));
        this.setLocationRelativeTo(null);
        initComp();
        initUI();
        this.setVisible(true);
    }
    
    private void initComp()
    {
        oldpass = new JPasswordField(20);
        newpass = new JPasswordField(20);
        recheck = new JPasswordField(20);
        save = new JButton("Luu");
        close = new JButton("Dong");
        showOldPass = new JButton("");
        showNewPass = new JButton("");
    }
    private void initUI()
    {
        JPanel main = new JPanel(), top = new JPanel(), bot = new JPanel(), act = new JPanel();
        top.add(new JLabel("Old pass: ")); top.add(oldpass);
        main.add(new JLabel("New pass: ")); main.add(newpass);
        bot.add(new JLabel("RePass:   ")); bot.add(recheck);
        act.add(save); act.add(close); 
        this.add(top); this.add(main); this.add(bot); this.add(act);
    }
    
    public static void main(String[] args) {
        new ChangePass(1);
    }
}
