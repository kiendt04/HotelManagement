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
        return checkID(id);
    }
    
    public int insertCus(Customer c)
    {
        return cc.insertCus(c);
    }
}
