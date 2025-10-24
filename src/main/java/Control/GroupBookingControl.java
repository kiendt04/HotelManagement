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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import jdk.jshell.execution.JdiDefaultExecutionControl;

/**
 *
 * @author ADMIN
 */
public class GroupBookingControl {
    private BillDAO billDAO = new BillDAO();
    private BillDetailDAO billdetailDAO = new BillDetailDAO();
    private CustomerDAO cusDAO = new CustomerDAO();
    private FloorDAO floorDAO = new FloorDAO();
    private RoomDAO roomDAO = new RoomDAO();
    private Room_typeDAO room_type = new Room_typeDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();
    private BillGroupBookingDAO billgroupDAO = new BillGroupBookingDAO();
    private BillGroupBookingDetail_RoomDAO roomGroupDetail = new BillGroupBookingDetail_RoomDAO();
    private BillGroupBookingDetail_ServiceDAO serviceGroupDetail = new BillGroupBookingDetail_ServiceDAO();
    private JButton save,canl,finish;
    private int indexServ, indexRoom = -1,time = 1,LoadTimes = -1;
    private boolean isSavedBill = false,upt = false;
    private JDialog parent;
    private JTable selectedRoom,Services,ServicesDetails;
    private Timestamp in,out;
    private DefaultTreeModel modelTree;
    
    public GroupBookingControl(JDialog Parent) {
        this.parent = parent;
    }
    
    public List<Room> getRoomavailable(Timestamp in, Timestamp out)
    {
        return roomDAO.getRoomAvailable(in, out);
    }
    
    public void getTreeModel(DefaultTreeModel model,JTree roomTree)
    {
        this.modelTree = model;
        roomTree.getModel().addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) { for(int i = 0 ;i<roomTree.getRowCount();i++){roomTree.expandRow(i);} }

            @Override
            public void treeNodesInserted(TreeModelEvent e) { for(int i = 0 ;i<roomTree.getRowCount();i++){roomTree.expandRow(i);} }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) { for(int i = 0 ;i<roomTree.getRowCount();i++){roomTree.expandRow(i);} }

            @Override
            public void treeStructureChanged(TreeModelEvent e) { for(int i = 0 ;i<roomTree.getRowCount();i++){roomTree.expandRow(i);} } 
        });
    }
    
    public List<String> getRoomPrice()
    {
        List<String> list = new ArrayList<>(); list.add("0");
        List<Room_type> type_info = room_type.getAll();
        for(Room_type t : type_info)
        {
            list.add(t.getPrice_per_night() + "");
        }
        return list;
    }
    
    public void reloadRoomModelByTime()
    {
        try {
            DefaultTableModel roomModel = (DefaultTableModel) selectedRoom.getModel();
            for (int i = 0 ;i<roomModel.getRowCount();i++)
            {
                double newTotal =  praseFromString(roomModel.getValueAt(i, 2).toString()) * time;
                roomModel.setValueAt(formatDouble(newTotal), i, 3);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void loadExitBill(int id_bill, DefaultTableModel roomModel, DefaultTableModel service_detailModel, JTextField deposit, JDateChooser time_in,JDateChooser time_out,ComboBoxModel<Customer> cbx )
    {
        this.isSavedBill = true;
        List<Room> roomLst = roomDAO.getAll();
        List<Service> serLst = serviceDAO.getAll();
        BillGroupBooking bgb = billgroupDAO.getBillById(id_bill);
        List<BillGroupBookingDetail_Room> bookedRoom = roomGroupDetail.getByID(id_bill);
        List<BillGroupBookingDetail_Service> serviceDetail = serviceGroupDetail.getById(id_bill);
        deposit.setText(formatDouble(bgb.getDeposit()));
        cbx.setSelectedItem(cusDAO.getById(bgb.getCus()));
        
        for (int i=0;i<bookedRoom.size();i++)
        {
            String Number = bookedRoom.get(i).getRoom();
            Room roomID = roomLst.stream().filter(r-> r.getNum().equals(Number)).findFirst().orElse(null);
            roomModel.addRow(new Object[]{roomID.getId(),roomID.getNum(),formatDouble(roomID.getPpn()),formatDouble(bookedRoom.get(i).getTotal())});
        }
        roomModel.fireTableDataChanged();
        
        for(int i=0;i<serviceDetail.size();i++)
        {
            String Number = serviceDetail.get(i).getRoom();
            Room roomID = roomLst.stream().filter(r-> r.getNum().equals(Number)).findFirst().orElse(null);
            int id = serviceDetail.get(i).getSer();
            Service serviceID = serLst.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
            service_detailModel.addRow(new Object[]{roomID.getId(),Number,serviceID.getId(),serviceID.getName(),serviceDetail.get(i).getQuantity(),formatDouble(serviceID.getPrice()),formatDouble(serviceDetail.get(i).getTotal())});
        }
        service_detailModel.fireTableDataChanged();        
//        time_in.setDate(bgb.getIn());
//        time_out.setDate(bgb.getOut());
        System.out.println(time_in.getDate() + " - " + time_out.getDate());
        this.upt = true;
    }
    
    public void initDate(JDateChooser in, JDateChooser out,int id)
    {
        in.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(out.getDate() != null)
                {
                    GroupBookingControl.this.in = new Timestamp(in.getDate().getTime());
                    GroupBookingControl.this.out = new Timestamp(out.getDate().getTime());
                    time = (int) ChronoUnit.DAYS.between(in.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),out.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    reloadRoomModelByTime();
                    if(LoadTimes >= 0 ) {
                        System.err.println("reLoadModel");
                        modelTree.reload();
                        
                    } else {
                        LoadTimes += 1;
                    }
                }
            }
        });
        
        out.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(in.getDate() != null)
                {
                    GroupBookingControl.this.in = new Timestamp(in.getDate().getTime());
                    GroupBookingControl.this.out = new Timestamp(out.getDate().getTime());
                    time = (int) ChronoUnit.DAYS.between(in.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),out.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    reloadRoomModelByTime();
                    if(LoadTimes >= 0) {
                        modelTree.reload();
                    } else {
                        LoadTimes += 1;
                    }
                }
            }
        });
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        if(id == -1)
        {
            in.setDate(cal.getTime());
        }
        else
        {
            BillGroupBooking bgb = billgroupDAO.getBillById(id);
            in.setDate(bgb.getIn());
        }
        in.getJCalendar().setMinSelectableDate(cal.getTime());  
        cal.add(Calendar.DAY_OF_MONTH, 1);
        //cal.add(in.getDate().getDay(), 1);
        out.getJCalendar().setMinSelectableDate(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 6);
        //cal.add(in.getDate().getDay(), 6);
        if(id == -1)
        {
            out.setDate(cal.getTime());
        }
        else
        {
            BillGroupBooking bgb = billgroupDAO.getBillById(id);
            out.setDate(bgb.getOut());
        }
        out.getJCalendar().setMaxSelectableDate(cal.getTime());
        in.getJCalendar().setMaxSelectableDate(cal.getTime());
    }
    
    public void setEnableBtn(JButton finish,JButton cancel,JButton save,JDateChooser time_in,int id)
    {
        this.save = save; this.finish = finish; this.canl = cancel;
        Date now = new Date();
        BillGroupBooking bgb =  billgroupDAO.getBillById(id);
        if(bgb.getStatus() == 0 || bgb.getStatus() == -2 || bgb.getStatus() == 2)
        {
            finish.setEnabled(false);
            cancel.setEnabled(false);
            save.setEnabled(false);
        }
        else
        {
        if(time_in.getDate().after(now))
        {
            cancel.setEnabled(true);
            finish.setEnabled(false);
        }
        else
        {
            cancel.setEnabled(false);
            finish.setEnabled(true);
        }
        }
    }
    
    public void fillCustomerCbx(DefaultComboBoxModel<Customer> model, JButton btn)
    {
         List<Customer> listCus = cusDAO.getAll();
         model.addAll(listCus);
         
         btn.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 CustomerList cusFrame = new CustomerList(null,parent);
                 cusFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    model.removeAllElements();
                    model.addAll(cusDAO.getAll());
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
        this.Services = tbl;
    }
    
    public void fillRoomtbl(JTable tbl, DefaultTableModel model)
    {
        TableColumn idCol = tbl.getColumnModel().getColumn(0);
        idCol.setMaxWidth(0);
        idCol.setMinWidth(0);
        idCol.setPreferredWidth(0);
        List<Room> ListRoom = roomDAO.getAll();
        this.selectedRoom = tbl;
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
        this.ServicesDetails = tbl;
    }
    
    public void roomSelectAction(JTree roomTree,JTable roomTbl,JTextField totalRoom,DefaultTableModel model)
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
                            model.addRow(new Object[]{selectedRoom.getId(),selectedRoom.getNum(),formatDouble(selectedRoom.getPpn()),formatDouble(selectedRoom.getPpn() * time)}); 
                        }
                    } catch (Exception l) {
                        System.err.println(l);
                    }
                }
            }
        });
        
        roomTbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3)
                {
                    JMenuItem delSer = new JMenuItem("Xóa");
                    JPopupMenu menu = new JPopupMenu();
                    delSer.addActionListener((l) -> {
                       if(JOptionPane.showConfirmDialog(parent, "Xóa các phòng và dịch vụ đi kèm đã chọn ?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0)
                       {
                           int[] selectedrows = roomTbl.getSelectedRows();
                           int[] idRoomSelected = new int[selectedrows.length];
                           for (int i=0;i<selectedrows.length;i++)
                           {
                               idRoomSelected[i] = (int) model.getValueAt(selectedrows[i], 0);
                           }
                           delTableItems(selectedrows, roomTbl);
                           DefaultTableModel srdModel = (DefaultTableModel) ServicesDetails.getModel();
                           for (int i = srdModel.getRowCount() - 1; i>=0; i--)
                           {
                               int id = (int) srdModel.getValueAt(i, 0);
                               if(Arrays.stream(idRoomSelected).anyMatch(x -> x == id))
                               {
                                   srdModel.removeRow(i);
                               }
                           }                           
                       }
                    });
                    menu.add(delSer);
                    menu.show(roomTbl, e.getX(), e.getY());
                }
            }
        });
        
        roomTbl.getModel().addTableModelListener((e) -> {
            totalRoom.setText(formatDouble(caculateTotalRoom(roomTbl,time))); 
        });
    }
    
    public void chooseServicesAction(JTable serviceDetail,JTable service, JTable room, DefaultTableModel model,JTextField totalserText)
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
        
        serviceDetail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int point = serviceDetail.getSelectedRow();
                if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
                {
                    String title = "Phòng " + model.getValueAt(point, 1) + " - " + model.getValueAt(point, 3);
                    int sl = Integer.parseInt(JOptionPane.showInputDialog(parent,"Số lượng: ",title,JOptionPane.QUESTION_MESSAGE,null,null,model.getValueAt(point, 4)).toString());    
                    double totalSer = praseFromString(service.getValueAt(indexServ, 2).toString()) * sl;
                    model.setValueAt(sl, point, 4);
                    model.setValueAt(formatDouble(totalSer), point, 6);
                }
                else if(e.getButton() == MouseEvent.BUTTON3)
                {
                    JMenuItem delSer = new JMenuItem("Xóa");
                    JPopupMenu menu = new JPopupMenu();
                    delSer.addActionListener((l) -> {
                       if(JOptionPane.showConfirmDialog(parent, "Xóa các dịch vụ đã chọn ?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0)
                       {
                           int[] selectedrows = serviceDetail.getSelectedRows();
                           delTableItems(selectedrows, serviceDetail);
                       }
                    });
                    menu.add(delSer);
                    menu.show(serviceDetail, e.getX(), e.getY());
                }
            }
        });
        serviceDetail.getModel().addTableModelListener((e) -> { totalserText.setText(formatDouble(caculateTotalser(serviceDetail))); });    
    }
        
    
    public void btnSaveAction(int id_bill, Customer cus , Timestamp in,Timestamp out, JTextField total_r, JTextField total_sr, JLabel dis ,JTextField dps, JTextField act,JDialog parent)
    {
        if(checkVailidateInfor(act, cus))
            {
                int id = id_bill ;
                List<BillGroupBookingDetail_Room> listRoom = new ArrayList<>();
                List<BillGroupBookingDetail_Service> listservices = new ArrayList<>();
                DefaultTableModel roomModel = (DefaultTableModel) selectedRoom.getModel();
                DefaultTableModel serviceDetailModel = (DefaultTableModel) ServicesDetails.getModel();
                double total_room = praseFromString(total_r.getText().trim());
                double total_ser = praseFromString(total_sr.getText().trim());
                double total = total_room + total_ser;
                double deposit = praseFromString(dps.getText().trim());
                double discount = praseFromString(dis.getText().split("VND")[0].trim());
                double actual_pay = praseFromString(act.getText().trim());
                if(JOptionPane.showConfirmDialog(parent, id == -1 ? " Tạo hóa đơn " : "Cập nhật hóa đơn", "Xác nhận", 0) == JOptionPane.YES_OPTION)
                    {
                        if(id == -1)
                        {
                            BillGroupBooking bgb = new BillGroupBooking(id, cus.getId(), in, out, total_room, total_ser, total, 0, discount, deposit, actual_pay, -1);
                            if(billgroupDAO.insertBill(bgb) == 1)
                            {
                                id = billgroupDAO.getNewestBill();
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(parent, "Thất bại","Lỗi", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        else
                        {
                            BillGroupBooking bgb = new BillGroupBooking(id, cus.getId(), in, out, total_room, total_ser, total, 0, discount, deposit, actual_pay, -1);
                            if (billgroupDAO.uptBill(bgb) !=  0)
                            {
                               roomGroupDetail.delAll(id);
                               serviceGroupDetail.delAll(id);
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(parent, "lưu hóa đơn Thất bại","Lỗi", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        
                        for(int i = 0;i<roomModel.getRowCount();i++)
                        {
                            String num = roomModel.getValueAt(i, 1).toString();
                            int time = (int) ChronoUnit.DAYS.between(in.toLocalDateTime().toLocalDate(), out.toLocalDateTime().toLocalDate());
                            double total_eeach_room = praseFromString(roomModel.getValueAt(i, 2).toString()) * time;
                            BillGroupBookingDetail_Room bookedRoom = new BillGroupBookingDetail_Room(id, num, time, total_eeach_room);
                            if(roomGroupDetail.insertDetail(bookedRoom) == 0)
                            {
                                JOptionPane.showMessageDialog(parent, "room Thất bại","Lỗi", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
            
                        for(int i = 0;i<serviceDetailModel.getRowCount();i++)
                        {
                            String num = serviceDetailModel.getValueAt(i, 1).toString();
                            int serviceID = (int) serviceDetailModel.getValueAt(i, 2);
                            int quantitySer = (int) serviceDetailModel.getValueAt(i, 4);
                            double total_each_service = praseFromString(serviceDetailModel.getValueAt(i, 6).toString());
                            BillGroupBookingDetail_Service Roomservice = new BillGroupBookingDetail_Service(id, num, serviceID, quantitySer,total_each_service);
                            if(serviceGroupDetail.insertDetail(Roomservice) == 0)
                            {
                                JOptionPane.showMessageDialog(parent, " service Thất bại","Lỗi", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        JOptionPane.showMessageDialog(parent, "Thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        canl.setEnabled(true);
                        upt = true;
                        parent.dispose();
                    }
            }
                else 
                {
                    JOptionPane.showMessageDialog(parent, cus == null ? "Thiếu thông tin khách hàng!!!" : "Không thể lưu hóa đơn trống" , "Thông báo", JOptionPane.ERROR_MESSAGE);
                    return;
                } 
    }
    
    public void btnFinishAction(JDateChooser time_out, int id)
    {
        int status = -1;
        double extra_charge = roomGroupDetail.getTotalRoomPrice_OneNight(id);
        if(!upt)
        {
            JOptionPane.showMessageDialog(parent, "Lưu các thay đổi trước","Cảnh báo",JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            Date now = new Date();
            if(time_out.getDate().after(now))
            {
                if(JOptionPane.showConfirmDialog(parent, "Chưa đến hạn trả phòng!!\nXác nhận thanh toán?", "Thông báo", JOptionPane.QUESTION_MESSAGE) ==  0)
                {
                    status = -2;
                }
            }
            else
            {
                status = 0;
            }
            if(billgroupDAO.finishBill(id, status,status == -2 ? extra_charge : 0) != 0 )
            {
                JOptionPane.showMessageDialog(parent, "Thành công");
                this.save.setEnabled(false);
                this.finish.setEnabled(false);
                this.canl.setEnabled(false);
            }
            else
            {
                JOptionPane.showMessageDialog(parent, "Thất bại");
            }
        }
    }
    
    public void btnCancelAction(int id)
    {
        if(upt)
        {
            if(JOptionPane.showConfirmDialog(parent, "Hủy đặt phòng?","Thông báo",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION)
            {
                if(billgroupDAO.cancelBill(id) != 0)
                {
                    JOptionPane.showMessageDialog(parent, "Thành công","Thông báo",JOptionPane.INFORMATION_MESSAGE);
                    this.save.setEnabled(false);
                    this.finish.setEnabled(false);
                    this.canl.setEnabled(false);
                }
                else
                {
                    JOptionPane.showMessageDialog(parent, "Thất bại","Thông báo",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        else
        {
            if(JOptionPane.showConfirmDialog(parent, "Dữ liệu mới chưa được cập nhật!\nVẫn hủy đặt phòng?","Thông báo",JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION)
            {
                if(billgroupDAO.cancelBill(id) != 0)
                {
                    JOptionPane.showMessageDialog(parent, "Thành công","Thông báo",JOptionPane.INFORMATION_MESSAGE);
                    this.save.setEnabled(false);
                    this.finish.setEnabled(false);
                    this.canl.setEnabled(false);
                }
                else
                {
                    JOptionPane.showMessageDialog(parent, "Thất bại","Thông báo",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    
    public boolean checkVailidateInfor(JTextField act,Customer cus)
    {
        if(cus != null && praseFromString(act.getText().toString().trim()) > 0)
        {
            return true;
        }
        return false;
    }
    
    public void delTableItems(int[] row,JTable tbl)
    {
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        for (int i = row.length - 1 ; i >= 0; i--) {
            model.removeRow(row[i]);
        }
    }
    
    public void setTextTotal(JTextField room,JTextField ser,JTextField total,JTextField deposit,JLabel dis)
    {
        room.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                total.setText(formatDouble(caculateTotal(praseFromString(room.getText()), praseFromString(ser.getText()), dis)));
                upt = false;
                if(!isSavedBill)
                {
                    deposit.setText(formatDouble(caculateTotal(praseFromString(room.getText()), praseFromString(ser.getText()), dis)/2));
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {}
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
        ser.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                total.setText(formatDouble(caculateTotal(praseFromString(room.getText()), praseFromString(ser.getText()), dis)));
                upt = false;
                if(!isSavedBill)
                {
                    deposit.setText(formatDouble(caculateTotal(praseFromString(room.getText()), praseFromString(ser.getText()), dis)/2));
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {}
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }
    
    
    public double caculateTotalRoom(JTable room,int time)
    {
        double total = 0;
        DefaultTableModel model = (DefaultTableModel) room.getModel();
        for (int i=0;i<model.getRowCount();i++)
        {
            total += praseFromString(model.getValueAt(i, 2).toString()) * time;
        }
        return total;
    }
    
    public double caculateTotalser(JTable service)
    {
        double total = 0;
        DefaultTableModel model = (DefaultTableModel) service.getModel();
        for (int i=0;i<model.getRowCount();i++)
        {
            total += praseFromString(model.getValueAt(i, 6).toString());
        }
        return total;
    }
    
    public double caculateTotal(double room,double service,JLabel dis)
    {
        double total = room + service;
        double discount = 0;
        if (total < 10000000) discount = 0;
        else if(total >= 10000000 && total < 49999999) discount = 0.02;
        else if(total >=50000000 && total < 99999999) discount = 0.05;
        else if(total >=100000000 && total < 199999999) discount = 0.1;
        else discount = 0.2;
        dis.setText( formatDouble(total * discount) + " VND" + " ("+ discount * 100 + "%)");
        return total - (total*discount);
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
