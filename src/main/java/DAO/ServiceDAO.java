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
public class ServiceDAO {
    
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public ServiceDAO() {
    }
    
    public List<Service> getAll()
    {
        List<Service> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM service");
            while(rs.next())
            {
                Service r = new Service(rs.getInt("id"), rs.getString("name"),rs.getDouble("price"));
                list.add(r);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertService(Service r)
    {
        int rs=0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO service(name,price) VALUES(?,?)");
            pt.setString(1, r.getName());
            pt.setDouble(2, r.getPrice());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delService(int id)
    {
        int rs =0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM service where id = ?");
            pt.setInt(1, id);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptService(Service r)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE service set name = ?,price = ?  WHERE id = ?");
            pt.setString(1, r.getName());
            pt.setDouble(2, r.getPrice());
            pt.setInt(3, r.getId());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    
}
