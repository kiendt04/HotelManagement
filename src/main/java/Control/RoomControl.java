/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import java.sql.Connection;
import java.util.ArrayList;
import Model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class RoomControl {
    
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();
    
    public RoomControl ()
    {
        
    }
    
    public List<Room> getAll()
    {
        List<Room> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM room");
            while(rs.next())
            {
                Room r = new Room(rs.getInt("Id"),rs.getString("Number"),rs.getInt("floor") ,rs.getInt("Type"),rs.getInt("Status"), rs.getString("Note"));
                list.add(r);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertRoom(Room r)
    {
        int rs=0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO room(Id,Number,floor,Type,Status,Note)  VALUES(,?,?,?,?,?)");
            pt.setString(1, r.getNum());
            pt.setInt(2, r.getFloor());
            pt.setInt(3, r.getType());
            pt.setInt(4, r.getStatus());
            pt.setString(5, r.getNote());
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
            PreparedStatement pt = conn.prepareStatement("DELETE FROM room where id = ?");
            pt.setInt(1, id);
            rs = pt.executeUpdate();
            
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptRoom(Room r)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE room set Number = ?, floor = ? ,Type = ? ,Status = ?, Note = ? WHERE id = ?");
            pt.setString(1, r.getNum());
            pt.setInt(2, r.getFloor());
            pt.setInt(3, r.getType());
            pt.setInt(4, r.getStatus());
            pt.setString(5, r.getNote());
            pt.setInt(6, r.getId());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public List<Room> getByFloor(int fl)
    {
        List<Room> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT  * FROM room where floor = "+ fl + "");
            while (rs.next())
            {
                Room r = new Room(rs.getInt("Id"),rs.getString("Number"),rs.getInt("floor"),rs.getInt("Type"),rs.getInt("Status"),rs.getString("Note"));
                list.add(r);
            }
        } catch (Exception e) {
        }
        return list;
    }
    
    
}
