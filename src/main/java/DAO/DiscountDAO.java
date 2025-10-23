/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Discount;
import Model.Room_type;
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
public class DiscountDAO {
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public DiscountDAO() {
    }
        
    public List<Discount> getAll()
    {
        List<Discount> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM discount");
            while(rs.next())
            {
                Discount r = new Discount(rs.getInt("percent"), rs.getString("code"));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertDiscount(Discount r)
    {
        int rs=0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO discoun(code,percent) VALUES(?,?)");
            pt.setString(1, r.getCode());
            pt.setInt(2, r.getValue());
            
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delDiscount(String code)
    {
        int rs =0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM discount where code = ?");
            pt.setString(1, code);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptDiscount(Discount r)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE discount set value = ?  WHERE code = ?");
            pt.setInt(1, r.getValue());
            pt.setString(2,r.getCode());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
}
