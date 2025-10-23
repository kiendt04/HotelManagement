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
    private BillGroupBookingDAO billgroupDAO = new BillGroupBookingDAO();
    private BillDAO billDAO = new BillDAO();

    public RoomTransferControl() {
    }
    
    public void loadRoomAvailable(DefaultTableModel model,int id,boolean isTour)
    {
        Bill b = billDAO.getBill(id);
        BillGroupBooking bgb = billgroupDAO.getBillById(id);
        Timestamp in = isTour ? bgb.getIn() : b.getCheck_in();
        Timestamp out = isTour ? bgb.getOut() : b.getCheck_out();
        List<Room> available = roomDao.getRoomAvailable(in, out);
        for (int i=0;i<available.size();i++)
        {
            Room r = available.get(i);
            model.addRow(new Object[]{r.getId(),r.getNum(),r.getPph(),r.getPpn()});
        }
    }
    
    
}
