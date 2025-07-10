/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import Model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ADMIN
 */
public class FloorDAO {
    
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();
    
    public List<Floor> getAll()
    {
        List<Floor> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM floor");
            while(rs.next())
            {
                Floor fl = new Floor(rs.getInt("id"),rs.getString("name"));
                list.add(fl);
            }
            st.close();
        } catch (Exception e) {
        }
        return list;
    }
    
    public int insertFloor(Floor fl)
    {
        int rs=0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO floor(id,name) VALUES(?,?)");
            pt.setInt(1, fl.getId());
            pt.setString(2, fl.getName());
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delFloor(int id)
    {
        int rs =0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM floor where id = ?");
            pt.setInt(1, id);
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptFloor(Floor fl)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE floor set name = ?  WHERE id = ?");
            pt.setString(1, fl.getName());
            pt.setInt(2, fl.getId());
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int countFloor()
    {
        int n = 0;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT count(id) as count FROM floor");
            while(rs.next())
            {
                n = rs.getInt("count");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return n;
    }
    
    public int creatNewID()
    {
        int id = 0;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id FROM floor ORDER BY id DESC LIMIT 1");
            while(rs.next())
            {
                id = rs.getInt("id") + 1;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return id;
    }
}
