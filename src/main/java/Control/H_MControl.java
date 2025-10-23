/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.BillDAO;
import DAO.FloorDAO;
import DAO.RoomDAO;
import Model.Room;
import View.HotelManagementSystem;
import View.Payment;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author ADMIN
 */
public class H_MControl {
    
    private FloorDAO flc = new FloorDAO();
    private RoomDAO rc = new RoomDAO();
    private BillDAO bc = new BillDAO();
    
    public H_MControl() {
    }
    
    public List<Room> getRoomData(int floor)
    {
        List<Room> roomLst = rc.getByFloor(floor);
        return roomLst;
    }
    
    public int countFloor()
    {
        return flc.countFloor();
    }
    
    public void uptRoom(Room r)
    {
        rc.uptRoom(r);
    }
    
    public void checkRoomStats(Room r, JLabel iconLabel,java.sql.Date currentDate)
    {
        if (r.getStatus() == 1) {
            iconLabel.setBackground(Color.PINK); // Đỏ = đã thuê
        }
        else if (new RoomDAO().checkPreserver(currentDate, r.getNum()))
        {
            r.setStatus(-1);
            rc.uptRoom(r);
            iconLabel.setBackground(Color.YELLOW);
        }
         else {
            iconLabel.setBackground(Color.CYAN); // Xanh = trống
        }
    }
    
    public void PayMentFunc(HotelManagementSystem frame,Room r,boolean booking)
    {
        SwingUtilities.invokeLater(() -> {
            Payment pay =  new Payment(r.getId(),booking);
            pay.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    frame.refreshRoomDisplay();
                }
            });
            pay.setVisible(true);
            
        });
    }
    
    public void getRoom_bill(String room)
    {
        int bill;
        
    }
}
