/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;
import DAO.*;
import Model.*;
import View.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;


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
    private int indexServ, indexRoom = -1;
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
        cal.add(Calendar.DAY_OF_MONTH, 1);
        //cal.add(in.getDate().getDay(), 1);
        out.getJCalendar().setMinSelectableDate(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 6);
        //cal.add(in.getDate().getDay(), 6);
        out.setDate(cal.getTime());
        out.getJCalendar().setMaxSelectableDate(cal.getTime());
    }
    
    public void fillCustomerCbx(DefaultComboBoxModel<Customer> model, JButton btn,JDialog parent)
    {
         List<Customer> listCus = cus.getAll();
         model.addAll(listCus);
         
         btn.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 CustomerList cusFrame = new CustomerList(null,parent);
                 cusFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    model.removeAllElements();
                    model.addAll(cus.getAll());
                    model.setSelectedItem(cusFrame.getSlCus());
                }
            });
                 cusFrame.setVisible(true);
             }
         });
    }
    
    public void fillServicetbl(JTable tbl, DefaultTableModel model)
    {
        List<Service> ListSer = serviceDAO.getAll();
        TableColumn idCol = tbl.getColumnModel().getColumn(0);
        idCol.setMaxWidth(0);
        idCol.setMinWidth(0);
        idCol.setPreferredWidth(0);
        for(Service sr : ListSer)
        {
            model.addRow(new Object[]{sr.getId(),sr.getName(),formatDouble(sr.getPrice())});
        }
    }
    
    public void fillRoomtbl(JTable tbl, DefaultTableModel model)
    {
        TableColumn idCol = tbl.getColumnModel().getColumn(0);
        idCol.setMaxWidth(0);
        idCol.setMinWidth(0);
        idCol.setPreferredWidth(0);
        List<Room> ListRoom = room.getAll();
    }
    
    public void fillServiceDetailtbl(JTable tbl, DefaultTableModel model)
    {
        TableColumn idser = tbl.getColumnModel().getColumn(2);
        TableColumn idroom = tbl.getColumnModel().getColumn(0);
        idser.setMaxWidth(0);
        idser.setMinWidth(0);
        idser.setPreferredWidth(0);
        idroom.setMaxWidth(0);
        idroom.setMinWidth(0);
        idroom.setPreferredWidth(0);
    }
    
    public void roomSelectAction(JTree roomTree,JTable roomTbl,DefaultTableModel model)
    {
        roomTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectednode = (DefaultMutableTreeNode)roomTree.getLastSelectedPathComponent();
                if(selectednode != null)
                {
                    try {
                        Room selectedRoom = (Room) selectednode.getUserObject();
                        if (!checkObjectAvailabled(model, selectedRoom.getId(),0))
                        {
                            model.addRow(new Object[]{selectedRoom.getId(),selectedRoom.getNum(),formatDouble(selectedRoom.getPpn())}); 
                        }
                    } catch (Exception l) {
                        System.err.println(l);
                    }
                }
            }
        });
    }
    
    public void chooseServicesAction(JTable serviceDetail,JTable service,JTable room, DefaultTableModel model, JDialog parent)
    {
        room.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                indexRoom = room.getSelectedRow();
            }
        });
        //Su kien chon service
        service.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                indexServ = service.getSelectedRow();
                if (indexRoom == -1)
                {
                    JOptionPane.showMessageDialog(parent, "Hãy chọn 1 phòng để thao tác!!", "Thông báo", 2);
                    return;
                }
                int roomId = (int) room.getValueAt(indexRoom, 0);
                int servId = (int) service.getValueAt(indexServ, 0);
                if(checkRoomServiceAvailable(model, servId, roomId) != -1)
                {
                    int point = checkRoomServiceAvailable(model, servId, roomId);
                    String title = "Phòng " + model.getValueAt(point, 1) + " - " + model.getValueAt(point, 3);
                    int sl = Integer.parseInt(JOptionPane.showInputDialog(parent,"Số lượng: ",title,JOptionPane.QUESTION_MESSAGE,null,null,model.getValueAt(point, 4)).toString());    
                    double totalSer = praseFromString(service.getValueAt(indexServ, 2).toString()) * sl;
                    model.setValueAt(sl, point, 4);
                    model.setValueAt(formatDouble(totalSer), point, 6);
                }
                else
                {
                    String title = "Phòng " + room.getValueAt(indexRoom, 1) + " - " + service.getValueAt(indexServ, 1);
                    int sl = Integer.parseInt(JOptionPane.showInputDialog(parent,"Số lượng: ",title,JOptionPane.QUESTION_MESSAGE,null,null,null).toString());
                    double totalSer = praseFromString(service.getValueAt(indexServ, 2).toString()) * sl;
                    model.addRow(new Object[]{roomId,room.getValueAt(indexRoom, 1),servId,service.getValueAt(indexServ, 1),sl,service.getValueAt(indexServ, 2),formatDouble(totalSer)});
                }
            }
            
        });
    }
    
    public void caculateTotal(JTextField totalRoom, JTextField totalService, JTextField total)
    {
        
    }
    public int checkRoomServiceAvailable(DefaultTableModel model, int ser,int room)
    {
        for (int i = 0;i<model.getRowCount();i++)
        {
            if (model.getValueAt(i, 0).equals(room) && model.getValueAt(i, 2).equals(ser))
                return i;
        }
        return -1;
    }
    
    public boolean checkObjectAvailabled(DefaultTableModel model, Object key,int col)
    {
        for (int i = 0;i<model.getRowCount();i++)
        {
            if(model.getValueAt(i, col).equals(key))
            {
                return true;
            }
        }
        return false;
    }
    
    public String formatDouble(Double d)
    {
        DecimalFormat df = new DecimalFormat("00,000");
        return df.format(d);
    }
    
    public Double praseFromString(String s)
    {
        return Double.parseDouble(s.replace(",", ""));
    }
}
