/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Control.CustomerListControl;
import DAO.myconnect;
import DAO.CustomerDAO;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import Model.Customer;
import Model.CustomerTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
//import java.awt.EventFilter;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author ADMIN
 */
public class CustomerList extends JFrame{

    private CustomerListControl cc = new CustomerListControl();
    private CustomerTableModel model;
    private List<Customer> list = cc.getAll();
    private JPanel header,main,footer;
    private JButton save,addBtn,remove,upt,filter,clear; 
    private JTable tbl;
    private JTextField cccd,name,phone,region,filterId,filterName,filterPhone,filterAddress;
    private JCheckBox gender;
    private int func = -1,row = -1,slPay = -1;
    private ImageIcon addIcon,rmIcon,uptIcon,saveIcon,filterIcon,clearIcon,clIcon;
    private JFrame parent;
    
    public CustomerList(JFrame parent) throws HeadlessException {
        super("Danh nục khách hàng");
        this.parent = parent;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(new Dimension(1000,500));
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
        uptIcon = new ImageIcon(getClass().getResource("/img/refresh.png"));
        saveIcon = new ImageIcon(getClass().getResource("/img/check.png"));
        filterIcon = new ImageIcon(getClass().getResource("/img/search.png"));
        clearIcon = new ImageIcon(getClass().getResource("/img/clear.png"));
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
        filter = new JButton(filterIcon);
        clear = new JButton(clearIcon);
        tbl = new JTable();
        tbl.setAutoCreateRowSorter(true);
        cccd = new JTextField(20);
        name = new JTextField(30);
        phone = new JTextField(20);
        region = new JTextField(35);
        gender = new JCheckBox("Male ");
        filterId = new JTextField(10);
        filterName = new JTextField(15);
        filterPhone = new JTextField(10);
        filterAddress = new JTextField(20);
    }
    
    public void initUI()
    {
        header.add(addBtn);
        header.add(upt);
        header.add(remove);
        header.add(save);
        header.setLayout(new BoxLayout(header,BoxLayout.X_AXIS));
        this.add(header,BorderLayout.NORTH);
        
        model = new CustomerTableModel(list);
        tbl.setModel(model);
        JScrollPane src = new JScrollPane(tbl);
        src.setBorder(new TitledBorder("Customer"));
        JPanel fl = new JPanel();
        fl.setBorder(new TitledBorder("Filter"));
        fl.add(new JLabel("Id: ")); fl.add(filterId);
        fl.add(new JLabel("Name: ")); fl.add(filterName);
        fl.add(new JLabel("Phone: ")); fl.add(filterPhone);
        fl.add(new JLabel("Address: ")); fl.add(filterAddress);
        fl.add(filter); fl.add(clear);
        main.add(fl,BorderLayout.NORTH);
        main.add(src,BorderLayout.CENTER);
        this.add(main,BorderLayout.CENTER);
        
        JPanel p1 = new JPanel(); JPanel p2 = new JPanel();
        p1.add(new JLabel("Id/PassPort: ")); p1.add(cccd);
        p1.add(new JLabel("Name: ")); p1.add(name);
        p1.add(gender);
        p2.add(new JLabel("Address: "));p2.add(region);
        p2.add(new JLabel("Phone: ")); p2.add(phone);
        footer.add(p1); footer.add(p2);
        footer.setBorder(new TitledBorder("Information"));
        footer.add(Box.createVerticalStrut(20));
        this.add(footer,BorderLayout.SOUTH);
        
    }
    
    public void loadData()
    {
        if(filterId.getText().isBlank() && filterName.getText().isBlank() && filterPhone.getText().isBlank() && filterAddress.getText().isBlank())
        {
            list = cc.getAll();
            model.loadData(list);
        }
    }
    
    public void clearText()
    {
        cccd.setText(""); 
        name.setText(""); 
        gender.setSelected(false); 
        phone.setText(""); 
        region.setText("");
    }
    
    public void action()
    {
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list = cc.filter(filterId.getText().trim(), filterName.getText().trim(), filterPhone.getText().trim(), filterAddress.getText().trim());
                model.loadData(list);
                row = -1;
                clearText();
            }
        });
        
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list = cc.getAll();
                model.loadData(list);
                row = -1;
                clearText();
                filterId.setText(""); filterName.setText(""); filterPhone.setText(""); filterAddress.setText("");
            }
        });
        
        tbl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting())
                {
                    row = tbl.getSelectedRow();
                    if(row != -1)
                    {
                        int modelRow = tbl.convertRowIndexToModel(row);
                        cccd.setText(tbl.getModel().getValueAt(modelRow, 0).toString());
                        name.setText(tbl.getModel().getValueAt(modelRow, 1).toString());
                        gender.setSelected(tbl.getModel().getValueAt(modelRow, 2).toString() == "Nam" ? true : false);
                        phone.setText(tbl.getModel().getValueAt(modelRow, 3).toString());
                        region.setText(tbl.getModel().getValueAt(modelRow, 4).toString());
                    }
                }
            }
        });
        
        tbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                if (e.getClickCount() == 2 && parent != null) {
                slPay = tbl.getSelectedRow();
                CustomerList.this.dispose();
                }
            }
        });
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(func == 0 && (JOptionPane.showConfirmDialog(rootPane, "Stop process", "Confirm", JOptionPane.YES_NO_OPTION) == 0) )
                {
                    save.setEnabled(false); remove.setEnabled(true); upt.setEnabled(true);
                    addBtn.setIcon(addIcon);
                    clearText();
                    func = -1;
                }
                else
                {
                    save.setEnabled(true); remove.setEnabled(false); upt.setEnabled(false);
                    addBtn.setIcon(clIcon); 
                    clearText();
                    func = 0;
                }
                
            }
        });
        
        upt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (row == -1)
                {
                    JOptionPane.showMessageDialog(rootPane, "Choose an customer first");
                    return;
                }
                int modelRow = tbl.convertRowIndexToModel(row);
                String oldID = tbl.getModel().getValueAt(modelRow, 0).toString();
                Customer cd = new Customer(cccd.getText().trim(),name.getText().trim(),gender.isSelected() ? "Nam" : "Nu",phone.getText().trim(),region.getText().trim());
                if (JOptionPane.showConfirmDialog(rootPane, "Update customer information", "Confirm", JOptionPane.YES_NO_OPTION) == 0 && cc.uptCus(cd, oldID) != 0)
                {
                    model.uptCus(cd, modelRow);
                    clearText();
                    JOptionPane.showMessageDialog(rootPane, "Update success");
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Update failed");
                    return;
                }
            }
        });
        
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (row == -1)
                {
                    JOptionPane.showMessageDialog(rootPane, "Choose an object first");
                    return;
                }
                int modelRow = tbl.convertRowIndexToModel(row);
                String ID = tbl.getModel().getValueAt(modelRow, 0).toString();
                if (JOptionPane.showConfirmDialog(rootPane, "Remove customer information", "Confirm", JOptionPane.YES_NO_OPTION) == 0 && cc.delCus(ID) != 0)
                {
                    model.delCus(modelRow);
                    clearText();
                    JOptionPane.showMessageDialog(rootPane, "Remove success");
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Remove failed");
                    return;
                }
            }
        });
        
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cccd.getText().isBlank() || name.getText().isBlank() || phone.getText().isBlank() || region.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(rootPane, "Information missing");
                    return;
                }
                if (cc.isNumeric(cccd.getText().trim()) == false || cc.isNumeric(phone.getText().trim()) == false)
                {
                    JOptionPane.showMessageDialog(rootPane, "Id and phone must be numeric");
                    return;
                }
                if (!cc.checkID(cccd.getText().trim()))
                {
                    JOptionPane.showMessageDialog(rootPane, "Id already exit");
                    return;
                }
                int check = JOptionPane.showConfirmDialog(rootPane, "Adding confirm","Confirm",JOptionPane.YES_NO_OPTION);
                if(check == 0)
                {
                    Customer a = new Customer(cccd.getText().trim(),name.getText().trim(),gender.isSelected() ? "Nam" : "Nu",phone.getText().trim(),region.getText().trim());
                    if( cc.insertCus(a) > 0)
                    {
                        model.addCustomer(a);
                        JOptionPane.showMessageDialog(rootPane, "Successfull");
                        addBtn.setIcon(addIcon); 
                        save.setEnabled(false); remove.setEnabled(true); upt.setEnabled(true);
                        clearText();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Failed");
                        return;
                    }
                }
                
            }
        });
    }
    
    
    public Customer getSlCus()
    {
        return cc.selectCus(model, slPay);
    }
    
    public int getSlPay()
    {
        return slPay;
    }
    
    public static void main(String[] args) {
        new CustomerList(null);
    }
    
    
    
}
