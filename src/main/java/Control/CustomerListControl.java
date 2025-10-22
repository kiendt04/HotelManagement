/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;
import DAO.*;
import Model.*;
import View.*;
import java.util.List;
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
    
    public boolean  checkID(String id)
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
}
