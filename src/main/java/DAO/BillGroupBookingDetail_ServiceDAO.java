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
public class BillGroupBookingDetail_ServiceDAO {
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public BillGroupBookingDetail_ServiceDAO() {
    }
    
    public List<BillGroupBookingDetail_Service> getAll()
    {
        List<BillGroupBookingDetail_Service> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM billgroupbookingdetail");
            while (rs.next())
            {
                BillGroupBookingDetail_Service bd = new BillGroupBookingDetail_Service(rs.getInt("id"), rs.getString("room"), rs.getInt("service"), rs.getInt("quantity"),rs.getDouble("total")
                );
                list.add(bd);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public List<BillGroupBookingDetail_Service> getById(int id)
    {
        List<BillGroupBookingDetail_Service> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM billgroupbookingdetail id = " + id + "");
            while (rs.next())
            {
                BillGroupBookingDetail_Service bd = new BillGroupBookingDetail_Service(rs.getInt("id"), rs.getString("room"), rs.getInt("service"), rs.getInt("quantity"),rs.getDouble("total")
                );
                list.add(bd);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertDetail(BillGroupBookingDetail_Service bd)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO billgroupbookingdetail(id,room,service,quantity,total) VALUES(?,?,?,?,?)");
            pt.setInt(1, bd.getId());
            pt.setString(2, bd.getRoom());
            pt.setInt(3, bd.getSer());
            pt.setInt(4, bd.getQuantity());
            pt.setDouble(5, bd.getTotal());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delDetail(int id,int service, String room)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM billgroupbookingdetail where id = ? and service = ? and room = ?");
            pt.setInt(1, id);
            pt.setInt(2, service);
            pt.setString(3, room);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptBD(BillGroupBookingDetail_Service bd)
    {
        int rs = 0;
        if(bd.getQuantity()<= 0)
        {
            delDetail(bd.getId(), bd.getSer(),bd.getRoom());
            return 1;
        }
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE bill_detail set quantity = ?, total = ? where id_bill = ? and service = ? and room = ?");
            pt.setInt(1, bd.getQuantity());
            pt.setDouble(2, bd.getTotal());
            pt.setInt(3, bd.getId());
            pt.setInt(4, bd.getSer());
            pt.setString(5, bd.getRoom());
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
            PreparedStatement pt = conn.prepareStatement("DELETE FROM billgroupbookingdetail where id=?");
            pt.setInt(0, id);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
}
