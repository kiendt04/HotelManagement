/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Control.UserControl;
import DAO.myconnect;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import Model.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class UserList extends JFrame{

    private myconnect mc = new myconnect();
    private UserControl uc = new UserControl();
    private DefaultTableModel model;
    private List<User> list = uc.getAllUsers();
    private JPanel header,main,footer;
    private JButton save,addBtn,remove,upt; 
    private JTable tbl;
    private JTextField username;
    private JPasswordField password;
    private JCheckBox role;
    private int func = -1,row = -1;
    private ImageIcon addIcon,rmIcon,uptIcon,saveIcon,clIcon;
    
    
    public UserList() throws HeadlessException {
        super("Danh mục người dùng");
        //this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(new Dimension(500,400));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10,0));
        initComp();
        initUI();
        action();
        loadData();
        this.setVisible(true);
    }
    
    public void initComp()
    {
        addIcon = new ImageIcon(getClass().getResource("/img/add.png"));
        rmIcon = new ImageIcon(getClass().getResource("/img/trash.png"));
        uptIcon = new ImageIcon(getClass().getResource("/img/save.png"));
        saveIcon = new ImageIcon(getClass().getResource("/img/check.png"));
        clIcon = new ImageIcon(getClass().getResource("/img/cross.png"));
        
        header = new JPanel();
        header.setPreferredSize(new Dimension(0,40));
        main = new JPanel();
        main.setLayout(new BorderLayout());
        footer = new JPanel();
        footer.setLayout(new BoxLayout(footer,BoxLayout.Y_AXIS));
        footer.setPreferredSize(new Dimension(0, 100));
        
        save = new JButton(saveIcon);
        save.setEnabled(false);
        addBtn = new JButton(addIcon);
        remove = new JButton(rmIcon);
        upt = new JButton(uptIcon);
        tbl = new JTable();
        tbl.setAutoCreateRowSorter(true);
        
        username = new JTextField(10);
        password = new JPasswordField(10);
        role = new JCheckBox("Admin");
    }
    
    public void initUI()
    {
        // Header với các nút chức năng
        header.add(addBtn);
        header.add(remove);
        header.add(save);
        header.setLayout(new BoxLayout(header,BoxLayout.X_AXIS));
        this.add(header,BorderLayout.NORTH);
        
        // Main panel chứa bảng User
        model = new DefaultTableModel();
        model.addColumn("Số thứ tự");
        model.addColumn("Tên đăng nhập");
        model.addColumn("Phân quyền");
        tbl.setModel(model);
        JScrollPane src = new JScrollPane(tbl);
        src.setBorder(new TitledBorder("User"));
        main.add(src,BorderLayout.CENTER);
        this.add(main,BorderLayout.CENTER);
        
        // Footer chứa thông tin chi tiết
        JPanel p1 = new JPanel(); 
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(0,50));
        p1.add(new JLabel("Username: ")); 
        p1.add(username);
        p1.add(new JLabel("Password: ")); 
        p1.add(password);
        p1.add(role);
        p2.add(upt);
        footer.add(p1); 
        footer.add(p2);
        footer.setBorder(new TitledBorder("Information"));
        footer.add(Box.createVerticalStrut(20));
        this.add(footer,BorderLayout.SOUTH);
    }
    
    public void loadData()
    {
        list = uc.getAllUsers();
        model.setRowCount(0); // Xóa dữ liệu cũ
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            String roleDisplay = uc.getRoleDisplayName(user.getRole());
            model.addRow(new Object[]{i + 1, user.getName(), roleDisplay});
        }
    }
    
    public void clearText()
    {
        username.setText(""); 
        password.setText(""); 
        role.setSelected(false);
    }
    
    public void action()
    {
        // Xử lý khi chọn dòng trong bảng
        tbl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting())
                {
                    row = tbl.getSelectedRow();
                    if(row != -1)
                    {
                        User user = list.get(row);
                        username.setText(user.getName());
                        password.setText(""); // Không hiển thị password vì lý do bảo mật
                        role.setSelected(user.getRole() == 1);
                    }
                }
            }
        });
        
        // Nút Add
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(func == 0 && (JOptionPane.showConfirmDialog(rootPane, "Dừng quá trình", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) )
                {
                    save.setEnabled(false); 
                    remove.setEnabled(true); 
                    upt.setEnabled(true);
                    addBtn.setIcon(addIcon);
                    clearText();
                    func = -1;
                }
                else
                {
                    save.setEnabled(true); 
                    remove.setEnabled(false); 
                    upt.setEnabled(false);
                    addBtn.setIcon(clIcon); 
                    clearText();
                    func = 0;
                }
            }
        });
        
        // Nút Update
        upt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (row == -1)
                {
                    JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn một người dùng trước");
                    return;
                }
                
                String errorMsg = uc.getValidationErrorMessage(username.getText(), String.valueOf(password.getPassword()), true);
                if (errorMsg != null) {
                    JOptionPane.showMessageDialog(rootPane, errorMsg);
                    return;
                }
                
                User selectedUser = list.get(row);
                int roleValue = role.isSelected() ? 1 : 0;
                User user = uc.createUser(selectedUser.getId(), username.getText(), String.valueOf(password.getPassword()), roleValue);
                
                if (user == null) {
                    JOptionPane.showMessageDialog(rootPane, "Dữ liệu không hợp lệ");
                    return;
                }
                
                if (JOptionPane.showConfirmDialog(rootPane, "Cập nhật thông tin người dùng", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                    boolean result = String.valueOf(password.getPassword()).isBlank() ? 
                                   uc.updateUserWithoutPassword(user) : uc.updateUser(user);
                    
                    if (result) {
                        loadData(); // Reload dữ liệu
                        clearText();
                        JOptionPane.showMessageDialog(rootPane, "Cập nhật thành công");
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Cập nhật thất bại");
                    }
                }
            }
        });
        
        // Nút Remove
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (row == -1)
                {
                    JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn một đối tượng trước");
                    return;
                }
                
                User selectedUser = list.get(row);
                
                if (JOptionPane.showConfirmDialog(rootPane, "Xóa thông tin người dùng", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                    if (uc.deleteUser(selectedUser.getId())) {
                        loadData(); // Reload dữ liệu
                        clearText();
                        JOptionPane.showMessageDialog(rootPane, "Xóa thành công");
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Xóa thất bại");
                    }
                }
            }
        });
        
        // Nút Save
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String errorMsg = uc.getValidationErrorMessage(username.getText(), String.valueOf(password.getPassword()), false);
                if (errorMsg != null) {
                    JOptionPane.showMessageDialog(rootPane, errorMsg);
                    return;
                }
                
                int check = JOptionPane.showConfirmDialog(rootPane, "Xác nhận thêm mới","Xác nhận",JOptionPane.YES_NO_OPTION);
                if(check == 0)
                {
                    int roleValue = role.isSelected() ? 1 : 0;
                    User user = uc.createUser(0, username.getText(), String.valueOf(password.getPassword()), roleValue);
                    
                    if (user == null) {
                        JOptionPane.showMessageDialog(rootPane, "Dữ liệu không hợp lệ");
                        return;
                    }
                    
                    if(uc.addUser(user))
                    {
                        loadData(); // Reload dữ liệu
                        JOptionPane.showMessageDialog(rootPane, "Thành công");
                        addBtn.setIcon(addIcon); 
                        save.setEnabled(false); 
                        remove.setEnabled(true); 
                        upt.setEnabled(true);
                        func = -1;
                        clearText();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Thất bại");
                        return;
                    }
                }
            }
        });
    }
    
    public static void main(String[] args) {
        new UserList();
    }
}