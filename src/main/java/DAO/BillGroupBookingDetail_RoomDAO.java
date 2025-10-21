/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import Model.*;
import Model.Bill;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ADMIN
 */
public class BillGroupBookingDetail_RoomDAO {
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public BillGroupBookingDetail_RoomDAO() {
    }
    
    public List<BillGroupBookingDetail_Room> getAll()
    {
        List<BillGroupBookingDetail_Room> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM billgroupbookingdetail_room");
            while (rs.next())
            {
                BillGroupBookingDetail_Room bd = new BillGroupBookingDetail_Room(rs.getInt("id"), rs.getString("room"), rs.getInt("time"),rs.getDouble("total"));
                list.add(bd);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public List<BillGroupBookingDetail_Room> getByID(int id)
    {
        List<BillGroupBookingDetail_Room> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM billgroupbookingdetail_room where id = " + id +"" );
            while (rs.next())
            {
                BillGroupBookingDetail_Room bd = new BillGroupBookingDetail_Room(rs.getInt("id"), rs.getString("room"), rs.getInt("time"),rs.getDouble("total"));
                list.add(bd);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertDetail(BillGroupBookingDetail_Room bd)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO billgroupbookingdetail_room(id,room,time,total) VALUES(?,?,?,?)");
            pt.setInt(1, bd.getId());
            pt.setString(2, bd.getRoom());
            pt.setInt(3, bd.getTime());
            pt.setDouble(4, bd.getTotal());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delDetail(int id, String room)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM billgroupbookingdetail_room where id = ? and room = ?");
            pt.setInt(1, id);
            pt.setString(2, room);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptTotal(BillGroupBookingDetail_Room bd)
    {
        int rs = 0;        
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE billgroupbookingdetail_room set time = ?, total = ? where id = ? and room = ?");
            pt.setInt(1, bd.getTime());
            pt.setDouble(2, bd.getTotal());
            pt.setInt(3, bd.getId());
            pt.setString(4, bd.getRoom());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delAll(int id)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM billgroupbookingdetail_room where id=?");
            pt.setInt(0, id);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public double getTotalRoomPrice_OneNight(int id)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(" SELECT sum(room.price_per_night) as tong FROM billgroupbooking_room br join room r on br.room = r.Number where id = " + id + "");
            if(rs.next())
            {
                return rs.getInt("tong");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
}
