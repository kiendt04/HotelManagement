/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import java.sql.Connection;
import Model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class billDetailControl {
    
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public billDetailControl() {
    }
    
    public List<BillDetail> getAll()
    {
        List<BillDetail> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM bill_detail");
            while (rs.next())
            {
                BillDetail bd = new BillDetail(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                list.add(bd);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertDetail(BillDetail bd)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO bill_detail(id_bill,service,quantity,total_service) VALUES(?,?,?,?)");
            pt.setInt(1, bd.getId_bill());
            pt.setInt(2, bd.getService());
            pt.setInt(3, bd.getQuant());
            pt.setInt(4, bd.getTotal());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delDetail(int id_bill,int service)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM bill_detail where id_bill = ? and service = ?");
            pt.setInt(1, id_bill);
            pt.setInt(2, service);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptQuant(BillDetail bd)
    {
        int rs = 0;
        if(bd.getQuant() <= 0)
        {
            delDetail(bd.getId_bill(), bd.getService());
            return 1;
        }
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE bill_detail set quantity = ? where id_bill = ? and service = ?");
            pt.setInt(1, bd.getQuant());
            pt.setInt(2, bd.getId_bill());
            pt.setInt(3, bd.getService());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptBD(BillDetail bd)
    {
        int rs = 0;
        if(bd.getQuant() <= 0)
        {
            delDetail(bd.getId_bill(), bd.getService());
            return 1;
        }
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE bill_detail set quantity = ?, total_service = ? where id_bill = ? and service = ?");
            pt.setInt(1, bd.getQuant());
            pt.setInt(2, bd.getTotal());
            pt.setInt(3, bd.getId_bill());
            pt.setInt(4, bd.getService());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
   public List<BillDetail> getByBill(int id)
    {
        List<BillDetail> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM bill_detail where id_bill = "+id+"");
            while (rs.next())
            {
                BillDetail bd = new BillDetail(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                list.add(bd);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    } 
}
