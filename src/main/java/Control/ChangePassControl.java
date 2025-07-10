/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.UserDAO;
import View.ChangePass;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author ADMIN
 */
public class ChangePassControl {

    public ChangePassControl() {
    }
    
    public boolean isValidPassword(String password) {
        String allowedRegex = "^[a-zA-Z0-9@#$%!_]+$";
        return password.matches(allowedRegex);
    }
    
    public void showNP(JPasswordField newpass,JPasswordField recheck,JButton showNewPass,ImageIcon show,ImageIcon hide)
    {
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
    
    public void showOP(JPasswordField oldpass,JButton showOldPass,ImageIcon show,ImageIcon hide)
    {
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
    
    public void saveNP(ChangePass frame,int id,JPasswordField oldpass,JPasswordField newpass,JPasswordField recheck)
    {
        UserDAO uc = new UserDAO();
        if(!uc.checkChangePass(id, String.valueOf(oldpass.getPassword())))
        {
            JOptionPane.showMessageDialog(frame, "Mat khau khong dung");
            return;
        }
        else if(!String.valueOf(newpass.getPassword()).equals(String.valueOf(recheck.getPassword())))
        {
            JOptionPane.showMessageDialog(frame, "Mat khau nhap lai khong trung khop");
            return;
        }
        else if(String.valueOf(newpass.getPassword()).length() > 10)
        {
            JOptionPane.showMessageDialog(frame, "Mat khau qua dai");
            return;
        }
        else if(!isValidPassword(String.valueOf(newpass.getPassword())))
        {
            JOptionPane.showMessageDialog(frame, "Mat khau chua ky tu khong hop le");
            return;
        }
        if(uc.changePass(id, String.valueOf(newpass.getPassword())) != -1)
        {
            JOptionPane.showMessageDialog(frame, "Doi mat khau thanh cong");
            frame.dispose();
        }
    }
}
