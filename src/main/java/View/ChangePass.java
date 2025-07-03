/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

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
import Control.*;

/**
 *
 * @author ADMIN
 */
public class ChangePass extends JFrame{
    
    private JPasswordField oldpass,newpass,recheck;
    private JButton save,close,showOldPass,showNewPass;
    private ImageIcon show,hide;
    private int id;
    
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
    
    private void action()
    {
        showNewPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(newpass.getEchoChar() == '•')
                {
                    showNewPass.setIcon(hide);
                    newpass.setEchoChar((char)0);
                    recheck.setEchoChar((char)0);
                }
                else
                {
                    showNewPass.setIcon(show);
                    newpass.setEchoChar('•');
                    recheck.setEchoChar('•');
                }
            }
        });
        showOldPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(oldpass.getEchoChar() == '•')
                {
                    showOldPass.setIcon(hide);
                    oldpass.setEchoChar((char)0);
                }
                else
                {
                    showOldPass.setIcon(show);
                    oldpass.setEchoChar('•');
                }
            }
        });
        
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserControl uc = new UserControl();
                if(!uc.checkChangePass(id, String.valueOf(oldpass.getPassword())))
                {
                    JOptionPane.showMessageDialog(rootPane, "Mat khau khong dung");
                    return;
                }
                else if(!String.valueOf(newpass.getPassword()).equals(String.valueOf(recheck.getPassword())))
                {
                    JOptionPane.showMessageDialog(rootPane, "Mat khau nhap lai khong trung khop");
                    return;
                }
                else if(String.valueOf(newpass.getPassword()).length() > 10)
                {
                    JOptionPane.showMessageDialog(rootPane, "Mat khau qua dai");
                    return;
                }
                else if(!uc.isValidPassword(String.valueOf(newpass.getPassword())))
                {
                    JOptionPane.showMessageDialog(rootPane, "Mat khau chua ky tu khong hop le");
                    return;
                }
                if(uc.changePass(id, String.valueOf(newpass.getPassword())) != -1)
                {
                    JOptionPane.showMessageDialog(rootPane, "Doi mat khau thanh cong");
                    close();
                }
            }
        });
        
        close.addActionListener(e -> {this.dispose();});
    }
    
    private void close()
    {
        this.dispose();
    }
    
    public static void main(String[] args) {
        new ChangePass(1);
    }
}
