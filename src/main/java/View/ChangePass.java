/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Control.ChangePassControl;
import DAO.UserDAO;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
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
    private ImageIcon show,hide;
    private int id;
    private ChangePassControl cpc = new ChangePassControl();
    
    public ChangePass() throws HeadlessException {
    }
    
    public ChangePass(int id) throws HeadlessException {
        super("Đổi mật khẩu");
        this.id = id;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(350,200));
        pack();
        setResizable(false);
        this.setLocationRelativeTo(null);
        initComp();
        initUI();
        action();
        this.setVisible(true);
    }
    
    private void initComp()
    {
        oldpass = new JPasswordField(10);
        newpass = new JPasswordField(10);
        recheck = new JPasswordField(10);
        oldpass.setEchoChar('•');
        newpass.setEchoChar('•');
        recheck.setEchoChar('•');
        save = new JButton("Luu");
        close = new JButton("Dong");
        show = new ImageIcon(getClass().getResource("/img/eye.png"));
        hide = new ImageIcon(getClass().getResource("/img/hidden.png"));
        showOldPass = new JButton();
        showOldPass.setPreferredSize(new Dimension(18,18));
        showOldPass.setIcon(show);
        showNewPass = new JButton();
        showNewPass.setPreferredSize(new Dimension(18,18));
        showNewPass.setIcon(show);
        showOldPass.setContentAreaFilled(false); // tắt nền
        showOldPass.setBorderPainted(false);     // tắt viền
        showOldPass.setOpaque(false);            // đảm bảo nền trong suốt

        showNewPass.setContentAreaFilled(false);
        showNewPass.setBorderPainted(false);
        showNewPass.setOpaque(false);
    }
    private void initUI()
    {
        JPanel main = new JPanel(), top = new JPanel(), bot = new JPanel(), act = new JPanel();
        top.add(new JLabel("Old pass: ")); top.add(oldpass); top.add(showOldPass);
        main.add(new JLabel("New pass: ")); main.add(newpass); main.add(showNewPass);
        bot.add(new JLabel("RePass:  ")); bot.add(recheck); bot.add(Box.createHorizontalStrut(showOldPass.getPreferredSize().width));
        act.add(save); act.add(close); 
        this.add(top); this.add(main); this.add(bot); this.add(act);
    }
        
    public void action()
    {
        showNewPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cpc.showNP(newpass, recheck, showNewPass, show, hide);
            }
        });
        showOldPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cpc.showOP(oldpass, showOldPass, show, hide);
            }
        });
        
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cpc.saveNP(ChangePass.this, id, oldpass, newpass, recheck);
            }
        });
        
        close.addActionListener(e -> {ChangePass.this.dispose();});
    }
    
    public static void main(String[] args) {
        //new ChangePass(1);
    }
}
