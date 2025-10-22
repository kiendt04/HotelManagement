/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.util.List;
import Model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author ADMIN
 */
public class Room_typeDAO {
    
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public Room_typeDAO() {
    }
    
    public List<Room_type> getAll()
    {
        List<Room_type> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM room_type");
            while(rs.next())
            {
                Room_type r = new Room_type(rs.getInt("id"), rs.getString("name"),rs.getInt("bed"),rs.getDouble("price_per_hour"), rs.getDouble("price_per_night"));
                list.add(r);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertRoom_type(Room_type r)
    {
        int rs=0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO room_type(id,name,bed,price_per_hour,price_per_night) VALUES(?,?,?,?,?)");
            pt.setInt(1, r.getId());
            pt.setString(2, r.getName());
            pt.setInt(3,r.getBed());
            pt.setDouble(4, r.getPrice_per_hour());
            pt.setDouble(5, r.getPrice_per_night());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delRoom(int id)
    {
        int rs =0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM room_type where id = ?");
            pt.setInt(1, id);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptRoom(Room_type r)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE room_type set name = ?,bed = ?,price_per_hour = ?, price_per_night = ?  WHERE id = ?");
            pt.setString(1, r.getName());
            pt.setInt(2, r.getBed());
            pt.setDouble(3, r.getPrice_per_hour());
            pt.setDouble(4, r.getPrice_per_night());
            pt.setInt(5, r.getId());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int size()
    {
        int size = 0;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT count(*) as count FROM room_type");
            while(rs.next())
            {
                size = rs.getInt("count");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return size;
    }
    
    public int createNewId()
    {
        int id = 0;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id from room_type ORDER BY id DESC LIMIT 1");
            while(rs.next())
            {
                id = rs.getInt("id") + 1;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return id;
    }
    
    public double getPricePH(int id, boolean days)
    {
        try {
            Statement st = conn.createStatement();
            String sql1 = "SELECT price_per_hour FROM room_type where id = " + id  +"";
            String sql2 = "SELECT price_per_night FROM room_type where id = " + id  +"";
            ResultSet rs = st.executeQuery(days ? sql2 : sql1);
            while (rs.next())
            {
                return rs.getDouble( days ?"price_per_night" : "price_per_hour");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return 0;
    }
    
    public double getPricePN(int id)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT price_per_night FROM room_type where id = " + id  +"");
            while (rs.next())
            {
                return rs.getDouble("price_per_night");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return 0;
    }
    
    
}
