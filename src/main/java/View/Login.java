/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Control.LoginControl;
import DAO.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*;

public class Login extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private LoginControl lc = new LoginControl();

    public Login() {
        setTitle("Đăng nhập hệ thống");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        // Panel chính với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Tiêu đề
        JLabel title = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 144, 255));
        mainPanel.add(title, BorderLayout.NORTH);

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setOpaque(false);

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        inputPanel.add(lblUsername);
        inputPanel.add(txtUsername);
        inputPanel.add(lblPassword);
        inputPanel.add(txtPassword);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        btnLogin = new JButton("Đăng nhập");
        btnExit = new JButton("Thoát");

        btnLogin.setFocusPainted(false);
        btnExit.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnLogin.setBackground(new Color(30, 144, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(120, 35));

        btnExit.setBackground(Color.LIGHT_GRAY);
        btnExit.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Sự kiện nút
        btnLogin.addActionListener(e -> lc.handleLogin(txtUsername,txtPassword,this));
        btnExit.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(btnLogin);
        
    }

    

    public static void main(String[] args) {
        // Giao diện phẳng (nếu muốn)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
