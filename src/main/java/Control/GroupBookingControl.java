/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;
import DAO.*;
import Model.*;
import com.toedter.calendar.JDateChooser;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import org.jfree.ui.tabbedui.RootPanel;

/**
 *
 * @author ADMIN
 */
public class GroupBookingControl {
    private BillDAO bill = new BillDAO();
    private BillDetailDAO billdetail = new BillDetailDAO();
    private CustomerDAO cus = new CustomerDAO();
    private FloorDAO floor = new FloorDAO();
    private RoomDAO room = new RoomDAO();
    private Room_typeDAO room_type = new Room_typeDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();
    public GroupBookingControl() {
    }
    
    public List<Room> getRoomavailable(Timestamp in, Timestamp out)
    {
        return room.getRoomAvailable(in, out);
    }
    
    public List<String> getRoomPrice()
    {
        List<String> list = new ArrayList<>(); list.add("0");
        List<Room_type> type_info = room_type.getAll();
        for(Room_type t : type_info)
        {
            list.add(t.getPrice_per_night() + "");
        }
        System.out.println(list);
        return list;
    }
    
    public void initDate(JDateChooser in, JDateChooser out)
    {
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        in.setDate(cal.getTime());
        in.getJCalendar().setMinSelectableDate(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 8);
        out.getJCalendar().setMinSelectableDate(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 14);
        out.setDate(cal.getTime());
        out.getJCalendar().setMaxSelectableDate(cal.getTime());
    }
    
}
