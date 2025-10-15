/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;
import DAO.*;
import Model.*;
import View.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 *
 * @author ADMIN
 */
public class CustomerListControl {
    private CustomerDAO cc = new CustomerDAO();

    public CustomerListControl() {
    }
    
    public List<Customer> getAll()
    {
        return cc.getAll();
    }
    
    public List<Customer> filter(String id, String name,String phone, String adds)
    {
       return cc.filter(id, name, phone, adds);
    }
    
    public int uptCus(Customer cd,String id)
    {
        return cc.uptCus(cd, id);
    }
    
    public int delCus(String id)
    {
        return cc.delCus(id);
    }
    
    public boolean checkID(String id)
    {
        return cc.checkID(id);
    }
    
    public int insertCus(Customer c)
    {
        return cc.insertCus(c);
    }
    
    public boolean isNumeric(String x)
    {
        try {
            Long.parseLong(x);
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
    
    public Customer selectCus(CustomerTableModel model,int row)
    {
        if(row != -1)
        {
            String cccd = (model.getValueAt(row, 0).toString());
            String name = (model.getValueAt(row, 1).toString());
            String gender = (model.getValueAt(row, 2).toString() );
            String phone = (model.getValueAt(row, 3).toString());
            String region = (model.getValueAt(row, 4).toString());
            return new Customer(cccd, name, gender , name, region);
        }
        return null;
    }
    
    public Customer getById(String r)
    {
        return cc.getById(r);
    }
    
    public void dialogAction(JTextField id,JTextField name,JTextField phone,JTextField region,JCheckBox gender,JButton save,JButton canl,JDialog parent,String oldID)
    {
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(id.getText().isBlank() || name.getText().isBlank() || phone.getText().isBlank() || region.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(parent, "Thiếu thông tin!!!", "Lỗi", 0);
                    return;
                }
                if (isNumeric(id.getText().trim()) == false || isNumeric(phone.getText().trim()) == false)
                {
                    JOptionPane.showMessageDialog(parent, "Cccd và số diện thoại không đúng định dạng số", "Lỗi", 0);
                    return;
                }
                if (!cc.checkID(id.getText().trim()) && oldID.equals(""))
                {
                    JOptionPane.showMessageDialog(parent, "Người dùng có cccd này đã tồn tại", "Lỗi", 0);
                    return;
                }
                Customer actCus = new Customer(id.getText().trim(), name.getText().trim(), gender.getText().trim(), phone.getText().trim(), region.getText().trim());
                if(JOptionPane.showConfirmDialog(parent, "Lưu tác vụ", "Xác nhận", 0) == 0 && (oldID.equals("") ?  insertCus(actCus) : uptCus(actCus, oldID)) == 1 )
                {
                    JOptionPane.showMessageDialog(parent, "Thành công", "Thông báo", 1);
                    parent.dispose();
                    return;
                }
                if((oldID.equals("") ?  insertCus(actCus) : uptCus(actCus, oldID)) != 1)
                {
                    JOptionPane.showMessageDialog(parent, "Thất bại", "Lỗi", 0);
                    return;
                }
            }
        });
        canl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int res = JOptionPane.showConfirmDialog(parent,"Hủy tác vụ","Xác nhận", JOptionPane.YES_NO_OPTION);
               if(res == JOptionPane.YES_OPTION)
               {
                   parent.dispose();
               }
            }
        });
    }
    
    public void reload(CustomerTableModel model)
    {
        model.loadData(getAll());
    }
}
