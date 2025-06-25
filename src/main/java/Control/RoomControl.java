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
    
    public List<room> getAll()
    {
        List<room> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM room");
            while(rs.next())
            {
                room r = new room(rs.getInt("Id"),rs.getString("Number"), rs.getInt("Type"),rs.getInt("Status"), rs.getString("Note"));
                list.add(r);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertRoom(room r)
    {
        int rs=0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO room(Id,Number,Type,Status,Note)  VALUES(,?,?,?,?)");
            pt.setString(1, r.getNum());
            pt.setInt(2, r.getType());
            pt.setInt(3, r.getStatus());
            pt.setString(4, r.getNote());
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
    
    public int uptRoom(room r)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE room set Number = ?,Type = ? ,Status = ?, Note = ? WHERE id = ?");
            pt.setString(1, r.getNum());
            pt.setInt(2, r.getType());
            pt.setInt(3, r.getStatus());
            pt.setString(4, r.getNote());
            pt.setInt(5, r.getId());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
}
