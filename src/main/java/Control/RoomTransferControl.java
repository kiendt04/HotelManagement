/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.*;
import Model.*;
import View.*;
import java.sql.Timestamp;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class RoomTransferControl {
    private RoomDAO roomDao = new RoomDAO();
    private BillGroupBookingDetail_RoomDAO bookingRoomDAO = new BillGroupBookingDetail_RoomDAO();
    private BillDAO billDAO = new BillDAO();

    public RoomTransferControl() {
    }
    
    public void loadRoomAvailable(DefaultTableModel model,int id)
    {
        Bill b = billDAO.getBill(id);
        Timestamp in = b.getCheck_in();
        Timestamp out = b.getCheck_out();
        List<Room> available = roomDao.getRoomAvailable(in, out);
        for (int i=0;i<available.size();i++)
        {
            model.addRow(new Object[]{});
        }
    }
}
