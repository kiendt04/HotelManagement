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
public class BillGroupBookingDAO {
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public BillGroupBookingDAO() {
    }
    
    public List<BillGroupBooking> getAll()
    {
        List<BillGroupBooking> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM billgroupbooking");
            while(rs.next())
            {
                BillGroupBooking b = new BillGroupBooking(rs.getInt("id"),rs.getString("customer"), rs.getTimestamp("check_in"), rs.getTimestamp("check_out"),rs.getDouble("total_room"),rs.getInt("total_service"), rs.getDouble("total"),rs.getDouble("extra_charge"),rs.getDouble("discount"),rs.getDouble("deposit"),rs.getDouble("actual_pay"), rs.getInt("status"));
                list.add(b);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertBill(BillGroupBooking b)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO billgroupbooking"
                                                       + "(customer,check_in,check_out,total_room,total_service,total,extra_charge,discount,deposit,actual_pay,status) "
                                                       + "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            pt.setString(1, b.getCus());
            pt.setTimestamp(2, b.getIn());
            pt.setTimestamp(3, b.getOut());
            pt.setDouble(4, b.getTotal_room());
            pt.setDouble(5, b.getTotal_ser());
            pt.setDouble(6, b.getTotal());
            pt.setDouble(7, b.getEx_charge());
            pt.setDouble(8, b.getDiscount());
            pt.setDouble(9, b.getDeposit());
            pt.setDouble(10, b.getActual_pay());
            pt.setInt(11, b.getStatus());
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptBill(BillGroupBooking b)
    {
        int rs =0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE billgroupbooking SET customer = ?,check_in = ?,check_out= ?,total_room = ?, total_service = ? ,total= ?,extra_charge =? , discount = ?,actual_pay = ?,status = ?  WHERE id = ? ");
            pt.setString(1, b.getCus());
            pt.setTimestamp(2, b.getIn());
            pt.setTimestamp(3, b.getOut());
            pt.setDouble(4, b.getTotal_room());
            pt.setDouble(5, b.getTotal_ser());
            pt.setDouble(6, b.getTotal());
            pt.setDouble(7, b.getEx_charge());
            pt.setDouble(8, b.getDiscount());
            pt.setDouble(9, b.getActual_pay());
            pt.setInt(10, b.getStatus());
            pt.setInt(11, b.getId());
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int finishBill(int id,int status)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE billgroupbooking set status = ? where id = ?");
            pt.setInt(1, id);
            pt.setInt(2, status);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int cancelBill(int id)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE billgroupbooking set status = -2, actual_pay = deposit where id = ?");
            pt.setInt(1, id);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    public BillGroupBooking getBillById(int id)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM billgroupbooking where id = " + id + "");
            while(rs.next())
            {
                BillGroupBooking b = new BillGroupBooking(rs.getInt("id"),rs.getString("customer"), rs.getTimestamp("check_in"), rs.getTimestamp("check_out"),rs.getDouble("total_room"),rs.getInt("total_service"), rs.getDouble("total"),rs.getDouble("extra_charge"),rs.getDouble("discount"),rs.getDouble("deposit"),rs.getDouble("actual_pay"), rs.getInt("status"));
                return b;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    public int getNewestBill()
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(" SELECT id FROM billgroupbooking order by id DESC LIMIT 1 ");
            if(rs.next())
            {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    public int getIdByRoom(String room)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE billgroupbooking set status = -2, actual_pay = deposit where id = ?");
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
}
