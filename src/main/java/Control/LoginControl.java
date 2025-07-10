/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.UserDAO;
import View.HotelManagementSystem;
import View.Login;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author ADMIN
 */
public class LoginControl {

    public LoginControl() {
    }
    
    public void handleLogin(JTextField txtUsername,JPasswordField txtPassword,Login frame) {
        String username = txtUsername.getText();
        String password = String.valueOf(txtPassword.getPassword());
        UserDAO uc = new UserDAO();
        int role = uc.checkLogin(username,password);
        if (role != -1) {
            // Chuyển sang frame chính
            new HotelManagementSystem(role,uc.getId(username));
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
