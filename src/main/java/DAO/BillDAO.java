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
public class BillDAO {
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public BillDAO() {
    }
    
    public List<Bill> getAll()
    {
        List<Bill> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM bill");
            while(rs.next())
            {
                Bill b = new Bill(rs.getInt("id"),rs.getString("room"), rs.getString("user"), rs.getTimestamp("check_in"), rs.getTimestamp("check_out"),rs.getDouble("total_time"),rs.getInt("total_service"), rs.getDouble("total"),rs.getDouble("exchange"),rs.getDouble("discount"),rs.getDouble("deposit"),rs.getDouble("actual_pay"), rs.getInt("status"));
                list.add(b);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertBill(Bill b)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO bill(room,user,check_in,check_out,total_time,total_service,total,exchange,discount,deposit,actual_pay,status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            pt.setString(1, b.getRoom());
            pt.setString(2, b.getUser());
            pt.setTimestamp(3, b.getCheck_in());
            pt.setTimestamp(4, b.getCheck_out());
            pt.setDouble(5, b.getTotal_time());
            pt.setInt(6, b.getTotal_service());
            pt.setDouble(7, b.getTotal());
            pt.setDouble(8, b.getExchange());
            pt.setDouble(9, b.getDisount());
            pt.setDouble(10, b.getDeposit());
            pt.setDouble(11, b.getActual_pay());
            pt.setInt(12, b.getStatus());
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptBill(Bill b)
    {
        int rs =0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE bill SET room = ?,user = ?,check_in = ?,check_out= ?,total_time = ?, total_service = ? ,total= ?,exchange =? , discount = ?, deposit = ? ,actual_pay = ?,status = ?  WHERE id = ? ");
            pt.setString(1, b.getRoom());
            pt.setString(2, b.getUser());
            pt.setTimestamp(3, b.getCheck_in());
            pt.setTimestamp(4, b.getCheck_out());
            pt.setDouble(5, b.getTotal_time());
            pt.setInt(6, b.getTotal_service());
            pt.setDouble(7, b.getTotal());
            pt.setDouble(8, b.getExchange());
            pt.setDouble(9, b.getDisount());
            pt.setDouble(10, b.getDeposit());
            pt.setDouble(11, b.getActual_pay());
            pt.setInt(12, b.getStatus());
            pt.setInt(13, b.getId());
            rs = pt.executeUpdate();
            pt.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public List<Bill> search(String x,Date in,Date out)
    {
        List<Bill> list = new ArrayList<>();
        ResultSet rs ;
        try {
            if (x.isBlank())
            {
                PreparedStatement pt = conn.prepareStatement("SELECT * FROM bill where check_in = ? or check_out = ?");
                pt.setDate(1, in);
                pt.setDate(2, out);
                rs = pt.executeQuery();
            }
            else if (!x.isBlank() && in != null && out != null)
            {
                PreparedStatement pt = conn.prepareStatement("SELECT * FROM bill where user = ? or check_in = ? or check_out = ?");
                pt.setString(1, x);
                pt.setDate(2, in);
                pt.setDate(3, out);
                rs = pt.executeQuery();
            }
            else
            {
               PreparedStatement pt = conn.prepareStatement("SELECT * FROM bill where user = ?");
               pt.setString(1, x);
               rs = pt.executeQuery();
                
            }
            if(rs == null)
            {
                return list;
            }
            while(rs.next())
                {
                    Bill b = new Bill(rs.getInt("id"),rs.getString("room"), rs.getString("user"), rs.getTimestamp("check_in"), rs.getTimestamp("check_out"),rs.getDouble("total_time"),rs.getInt("total_service"), rs.getDouble("total"),rs.getDouble("exchange"),rs.getDouble("discount"),rs.getDouble("deposit"),rs.getDouble("actual_pay") ,rs.getInt("status"));
                    list.add(b);
                } 
        } catch (Exception e) {
            System.err.println(e);
        }
        
        return list;
    }
    
    public int finishBill(int id,int status)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE bill set status = ? where id = ?");
            pt.setInt(1, id);
            pt.setInt(2, status);
            rs = pt.executeUpdate();
            
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    
    public Bill getRoomBill(String r, int st)
    {
        try {
            PreparedStatement pt = conn.prepareStatement("SELECT * FROM bill where room = ? and status = ? ORDER BY id DESC LIMIT 1");
            pt.setString(1, r);
            pt.setInt(2, st);
            ResultSet rs = pt.executeQuery();
            while(rs.next())
            {
                return new Bill(rs.getInt("id"),rs.getString("room"), rs.getString("user"), rs.getTimestamp("check_in"), rs.getTimestamp("check_out"),rs.getDouble("total_time"),rs.getInt("total_service"), rs.getDouble("total"),rs.getDouble("exchange"),rs.getDouble("discount"),rs.getDouble("deposit"),rs.getDouble("actual_pay") ,rs.getInt("status"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    public Bill getBill(int id)
    {
        try {
            PreparedStatement pt = conn.prepareStatement("SELECT * FROM bill  where id = ?");
            pt.setInt(1, id);
             ResultSet rs = pt.executeQuery();
            if(rs.next())
            {
                return new Bill(rs.getInt("id"),rs.getString("room"), rs.getString("user"), rs.getTimestamp("check_in"), rs.getTimestamp("check_out"),rs.getDouble("total_time"),rs.getInt("total_service"), rs.getDouble("total"),rs.getDouble("exchange"),rs.getDouble("discount"),rs.getDouble("deposit"),rs.getDouble("actual_pay") ,rs.getInt("status"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    public int getIDforchange (String room)
    {
        try {
            PreparedStatement pt = conn.prepareStatement("SELECT id FROM bill where room = ? and status = 1 ORDER BY check_in DESC LIMIT 1 ");
            pt.setString(1, room);
            ResultSet rs = pt.executeQuery();
            if(rs.next())
            {
                return rs.getInt("id");
            }
        } catch (Exception e) {
        }
        return -1;
    }
    
    public double getEtra_chagre(int id)
    {
        try {
            PreparedStatement pt = conn.prepareStatement("SELECT exchange FROM bill where id = ? ");
            pt.setInt(1, id);
            ResultSet rs = pt.executeQuery();
            if(rs.next())
            {
                return rs.getInt("exchange");
            }
        } catch (Exception e) {
        }
        return 0;
    }
}
