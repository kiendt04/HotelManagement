/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class CustomerDAO {
    
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public CustomerDAO() {
    }
    
    public List<Customer> getAll()
    {
        List<Customer> list = new ArrayList<>();
        try {
            Statement st =conn.createStatement();
            ResultSet rs = st.executeQuery( "SELECT * FROM customer");
            while (rs.next())
            {
                String gender = rs.getInt("gt") == 1 ? "Nam" : "Nữ";
                Customer cs = new Customer(rs.getString("cccd"), rs.getString("name"),gender, rs.getString("sdt"), rs.getString("region"));
                list.add(cs);
            }
        } catch (Exception e) { 
            System.err.println(e);
        }
        return list;
    }
    
    public int insertCus(Customer cs)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO customer(cccd,name,gt,sdt,region) VALUES(?,?,?,?,?)");
            pt.setString(1, cs.getId());
            pt.setString(2, cs.getName());
            pt.setInt(3, cs.getGender() == "Nam" ? 1 : 0);
            pt.setString(4, cs.getSdt());
            pt.setString(5, cs.getRegion());
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptCus(Customer cs ,String oldID)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE customer set name = ?, sdt = ? ,gt = ?, region = ?, cccd = ? WHERE cccd = ?");
            pt.setString(1, cs.getName());
            pt.setString(2, cs.getSdt());
            pt.setInt(3, cs.getGender() == "Nam" ? 1 : 0);
            pt.setString(4, cs.getRegion());
            pt.setString(5, cs.getId());
            pt.setString(6, oldID);
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delCus(String cccd)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM customer where cccd = ?");
            pt.setString(1, cccd);
            rs = pt.executeUpdate();
            
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public boolean checkID(String id)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs =  st.executeQuery("SELECT * FROM customer WHERE cccd = '" + id + "'");
            if(rs.next())
            {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return true;
    }
    
    public List<Customer> searchByName(String keyword) {
    List<Customer> list = new ArrayList<>();
    try {
        String sql = "SELECT * FROM customer WHERE name LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Customer c = new Customer();
            c.setId(rs.getString("cccd"));
            c.setName(rs.getString("name"));
            list.add(c);
        }
        rs.close(); ps.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


    
    public List<Customer> filter(String id,String name,String phone,String adds)
    {
        List<Customer> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM customer WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (id != null && !id.trim().isEmpty()) {
            sql.append(" AND cccd = ?");
            params.add(id);
        }
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%"); 
        }
        if (phone != null && !phone.trim().isEmpty()) {
            sql.append(" AND sdt = ?");
            params.add(phone);
        }
        if (adds != null && !adds.trim().isEmpty()) {
            sql.append(" AND region LIKE ?");
            params.add("%" + adds + "%");
        }
        try {
            PreparedStatement pt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
            pt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pt.executeQuery();
            while (rs.next())
            {
                String gender = rs.getInt("gt") == 1 ? "Nam" : "Nu";
                Customer cs = new Customer(rs.getString("cccd"), rs.getString("name"),gender, rs.getString("sdt"), rs.getString("region"));
                list.add(cs);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    
    public Customer getById(String id)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM customer where cccd = '" + id +"'");
            while (rs.next())
            {
                String gender = rs.getInt("gt") == 1 ? "Nam" : "Nu";
                return new Customer(rs.getString("cccd"), rs.getString("name"),gender, rs.getString("sdt"), rs.getString("region"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
}
