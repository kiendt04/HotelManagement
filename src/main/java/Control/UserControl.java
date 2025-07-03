/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;
import Model.*;
import Model.Bill;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author ADMIN
 */
public class UserControl {
    private myconnect mc = new myconnect();
    private Connection conn = mc.getConnection();

    public UserControl() {
    }
    
    public List<User> getAll()
    {
        List<User> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user");
            while (rs.next())
            {
                User us =new User(rs.getInt("id"),rs.getString("name"),rs.getString("pass"),rs.getInt("role"));
                list.add(us);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return list;
    }
    
    public int insertUser(User us)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO user(id,name,pass,role) VALUES(,?,?,?)");
            pt.setString(1, us.getName());
            pt.setString(2, hashPass(us.getPass()));
            pt.setInt(3, us.getRole());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int uptUser(User us)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE user set name = ?, pass = ?, role = ? where id = ?");
            pt.setString(1,us.getName());
            pt.setString(2, hashPass(us.getPass()));
            pt.setInt(3, us.getId());
            pt.setInt(4, us.getId());
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    public int delUser(int id)
    {
        int rs = 0;
        try {
            PreparedStatement pt = conn.prepareStatement("DELETE FROM user where id = ?");
            pt.setInt(1,id);
            rs = pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return rs;
    }
    
    private String hashPass(String pass)
    {
        return BCrypt.hashpw(pass, BCrypt.gensalt());
    }
    
    public int checkLogin(String name,String pass)
    {
        try {
            PreparedStatement pt = conn.prepareStatement("SELECT * FROM user where name = ? ");
            pt.setString(1, name);
            ResultSet rs = pt.executeQuery();
            while (rs.next())
            {
                if(BCrypt.checkpw(pass, rs.getString("pass")))
                {
                   return  rs.getInt("role");
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return -1;
    }
    
    public int getId(String name)
    {
        try {
            Statement st = conn.createStatement();
            ResultSet rs  = st.executeQuery("SELECT id from user where name = '"+ name +"'");
            while(rs.next()){
                return rs.getInt("id");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return -1;
    }
    
    public boolean checkChangePass(int id,String pass)
    {
        try {
            PreparedStatement pt = conn.prepareStatement("SELECT pass FROM user where id = ?");
            pt.setInt(1, id);
            ResultSet rs = pt.executeQuery();
            while(rs.next())
            {
                if(BCrypt.checkpw(pass, rs.getString("pass")))
                {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }
        public boolean isValidPassword(String password) {
        String allowedRegex = "^[a-zA-Z0-9@#$%!_]+$";
        return password.matches(allowedRegex);
    }
    public int changePass(int id,String pass)
    {
        try {
            PreparedStatement pt = conn.prepareStatement("UPDATE user set pass = ? where id = ?");
            pt.setString(1, hashPass(pass));
            pt.setInt(2, id);
            return pt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        return -1;
    }
}
