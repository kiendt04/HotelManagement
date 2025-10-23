/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.util.ArrayList;
import Model.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class RoomDAO {
    
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();
    
    public RoomDAO ()
    {
        
    }
    
    public List<Room> getAll()
    {
        List<Room> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT room.Id, Number, floor, room.Type, Status, Note, room_type.price_per_hour, room_type.price_per_night "
                                         + "FROM room join room_type on room.Type = room_type.id");
            while(rs.next())
            {
                Room r = new Room(rs.getInt("Id"),rs.getString("Number"),rs.getInt("floor") ,rs.getInt("Type"),rs.getInt("Status"), rs.getString("Note"),rs.getDouble("price_per_hour"),rs.getDouble("price_per_night"));
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
            PreparedStatement pt = conn.prepareStatement("INSERT INTO room(Number,floor,Type,Status,Note)  VALUES(?,?,?,?,?)");
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
            PreparedStatement pt = conn.prepareStatement("DELETE FROM room where Id = ?");
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
            PreparedStatement pt = conn.prepareStatement("UPDATE room set Number = ?, floor = ? ,Type = ? ,Status = ?, Note = ? WHERE Id = ?");
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
            ResultSet rs = st.executeQuery("SELECT room.Id, Number, floor, room.Type, Status, Note, room_type.price_per_hour, room_type.price_per_night "
                                         + "FROM room join room_type on room.Type = room_type.id where floor = " + fl + "");
            while (rs.next())
            {
                Room r = new Room(rs.getInt("Id"),rs.getString("Number"),rs.getInt("floor") ,rs.getInt("Type"),rs.getInt("Status"), rs.getString("Note"),rs.getDouble("price_per_hour"),rs.getDouble("price_per_night"));
                list.add(r);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }
    
    public Room getbyID(int id)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT room.Id, Number, floor, room.Type, Status, Note, room_type.price_per_hour, room_type.price_per_night"
                                         + " FROM room join room_type on room.Type = room_type.id where room.Id = "+id+"");
            while(rs.next())
            {
                return new Room(rs.getInt("Id"),rs.getString("Number"),rs.getInt("floor") ,rs.getInt("Type"),rs.getInt("Status"), rs.getString("Note"),rs.getDouble("price_per_hour"),rs.getDouble("price_per_night"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    public Room getByNum(String num) {
    try {
        PreparedStatement pt = conn.prepareStatement(
            "SELECT room.Id, Number, floor, room.Type, Status, Note, " +
            "       room_type.price_per_hour, room_type.price_per_night " +
            "FROM room " +
            "JOIN room_type ON room.Type = room_type.id " +
            "WHERE room.Number = ? " +
            "LIMIT 1"
        );
        pt.setString(1, num);
        ResultSet rs = pt.executeQuery();
        if (rs.next()) {
            return new Room(
                rs.getInt("Id"),
                rs.getString("Number"),
                rs.getInt("floor"),
                rs.getInt("Type"),
                rs.getInt("Status"),
                rs.getString("Note"),
                rs.getDouble("price_per_hour"),
                rs.getDouble("price_per_night")
            );
        }
        rs.close();
        pt.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    
    public int count()
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT count(*) as count FROM room");
            while(rs.next())
            {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return 0;
    }
    
    public int count_filter(int floor,int type)
    {
        int count = 0;
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM room WHERE 1=1");

    if (floor != 0) {
        sql.append(" AND floor = ?");
    }

    if (type != 0) {
        sql.append(" AND type = ?");
    }

    try {
        PreparedStatement pt = conn.prepareStatement(sql.toString());

        int paramIndex = 1;
        if (floor != 0) {
            pt.setInt(paramIndex++, floor);
        }

        if (type != 0) {
            pt.setInt(paramIndex++, type);
        }

        ResultSet rs = pt.executeQuery();
        if (rs.next()) {
            count = rs.getInt(1);
        }

        rs.close();
        pt.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return count;
    }
    
    
    public List<Room> filter(int floor, int type)
    {
         List<Room> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT room.Id, Number, floor, room.Type, Status, Note, room_type.price_per_hour, room_type.price_per_night"
                                        + " FROM room join room_type on room.Type = room_type.id WHERE 1=1");

    if (floor != 0) {
        sql.append(" AND floor = ?");
    }

    if (type != 0) {
        sql.append(" AND type = ?");
    }

    try {
        PreparedStatement pt = conn.prepareStatement(sql.toString());

        int paramIndex = 1;
        if (floor != 0) {
            pt.setInt(paramIndex++, floor);
        }

        if (type != 0) {
            pt.setInt(paramIndex++, type);
        }

        ResultSet rs = pt.executeQuery();
        while (rs.next()) {
            Room r = new Room(rs.getInt("Id"),rs.getString("Number"),rs.getInt("floor") ,rs.getInt("Type"),rs.getInt("Status"), rs.getString("Note"),rs.getDouble("price_per_hour"),rs.getDouble("price_per_night"));
            list.add(r);
        }

        rs.close();
        pt.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
    }
    
    public boolean checkNum(String num)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM room where Number = '" + num +"'");
            while (rs.next())
            {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return true;
    }
    
    public boolean checkPreserver(Date d,String r)
    {
        try {
            PreparedStatement pt = conn.prepareStatement("SELECT * FROM room join bill on room.Number = bill.room where check_in = ? and room = ? and bill.status = -1");
            pt.setDate(1, d);
            pt.setString(2, r);
            ResultSet rs = pt.executeQuery();
            while (rs.next())
            {
                return true;
                    }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
    
    public double getPrice(int type)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT price FROM room_type where id = " + type + "");
            while(rs.next())
            {
                return rs.getDouble("price");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return 0;
    }
    
    public List<Room> getRoomAvailable( Timestamp in, Timestamp out)
    {
        System.err.println(in + " - " + out);
        List<Room> roomavailable = new ArrayList<>();
        ResultSet rs; 
        try {
            PreparedStatement pt = conn.prepareStatement("SELECT room.Id, Number, floor, room.Type, Status, Note, room_type.price_per_hour, room_type.price_per_night "
                                                       + "FROM room join room_type on room.Type = room_type.id "
                                                       + "where room.Number not in ( "
                                                       + "SELECT room from bill where (check_out >= ? or check_in <= ?) and status not in (0,-2)) "
                                                       + "AND room.Number not in ( "
                                                       + "SELECT room FROM billgroupbookingdetail_room where id in ( "
                                                       + "SELECT id FROM billgroupbooking where (check_out >= ? or check_in <= ? ) and status not in (-2,0,2)))");
            pt.setTimestamp(1, in);
            pt.setTimestamp(2, out);
            pt.setTimestamp(3, in);
            pt.setTimestamp(4, out);
            rs = pt.executeQuery();
            while (rs.next())
            {
                Room r = new Room(rs.getInt("Id"),rs.getString("Number"),rs.getInt("floor") ,rs.getInt("Type"),rs.getInt("Status"), rs.getString("Note"),rs.getDouble("price_per_hour"),rs.getDouble("price_per_night"));
                roomavailable.add(r);
            }
            pt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomavailable;
    }
}
