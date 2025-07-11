/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import View.*;
import DAO.*;
import Model.*;
import com.mysql.cj.conf.PropertyKey;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.awt.Desktop;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import java.text.SimpleDateFormat;

/**
 *
 * @author ADMIN
 */
public class PaymentControl {

    public PaymentControl() {
    }
    
    public void openCusList(Payment pay , DefaultComboBoxModel<Customer> model)
    {
        SwingUtilities.invokeLater(() -> {
            CustomerList cus =  new CustomerList(pay);
            cus.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    reloadCBXModel(model, cus.getSlCus());
                }
            });
            cus.setVisible(true);
            
        });
    }
    
    public void reloadCBXModel(DefaultComboBoxModel<Customer> model,Customer selected)
    {
        CustomerDAO csd = new CustomerDAO();
        List<Customer> list = csd.getAll();
        model.removeAllElements();
        for (Customer i : list)
        {
            model.addElement(i);
        }
        if(selected != null){
        model.setSelectedItem(selected);
        }
    }
    
    public String formatPrice(double p)
    {
        DecimalFormat df = new DecimalFormat("00,000");
        return df.format(p);
    }
    
    public void setTblData(DefaultTableModel model,List<BillDetail> list)
    {
        List<Service> svl = new ArrayList<>();
        svl = new ServiceDAO().getAll();
        for (int i=0;i<list.size();i++)
        {
            String svName = "";
            int j=0;
            while(list.get(i).getService() != svl.get(j).getId())
            {
                j++;
            }
            svName = svl.get(j).getName();
            model.addRow(new Object[]{svName,list.get(i).getQuant(),formatPrice(list.get(i).getTotal()/list.get(i).getQuant()),formatPrice(list.get(i).getTotal())});
        }
        model.fireTableDataChanged();
    }
    
    public int uptRoom(Room r)
    {
        return new RoomDAO().uptRoom(r);
    }
    
    public Bill getRoomBill(String r, int st)
    {
        return new BillDAO().getRoomBill(r, st);
    }
    
    public int insertBill(Bill b)
    {
        return new BillDAO().insertBill(b);
    }
    
    public int uptBill(Bill b)
    {
        return new BillDAO().uptBill(b);
    }
    
    public int insertDetail(BillDetail bd)
    {
        return new BillDetailDAO().insertDetail(bd);
    }
    
    public int uptBD(BillDetail bd)
    {
        return new BillDetailDAO().uptBD(bd);
    }
    
    public void printed(List<Object> dataSRC, Map<String, Object> mainParam) {
    try {
        // Tạo datasource cho bảng
        JRDataSource tableDS = new JRBeanCollectionDataSource(dataSRC);

        // Gắn vào parameter tên đúng với dataset expression trong report
        mainParam.put("TableDataSource", tableDS);

        // Compile report
        JasperReport report = JasperCompileManager.compileReport("src/main/resources/report/PhieuDatPhong.jrxml");

        // Fill report với JREmptyDataSource (không dùng JDBC)
        JasperPrint print = JasperFillManager.fillReport(report, mainParam, new JREmptyDataSource());

        // Lưu thành PDF
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String outputPath = "src/main/resources/report/PhieuDatPhong_" + timestamp + ".pdf";
        JasperExportManager.exportReportToPdfFile(print, outputPath);

        System.out.println("In thành công: " + outputPath);
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("In thất bại: " + e.getMessage());
    }
}
    
    public List<Object> setDataPrint(List<BillDetail> bdls, Room r,Bill b) {
    List<Object> list = new ArrayList<>();

    // Lấy giá phòng và định dạng
    double roomPrice = new RoomDAO().getPrice(r.getType());
    String formattedRoomPrice = formatPrice(roomPrice);

    // Dòng đầu tiên: thông tin phòng
    Map<String, Object> roomRow = new HashMap<>();
    roomRow.put("STT", 1);
    roomRow.put("noidung", "Room " + r.getNum());
    roomRow.put("soluong", 1);
    roomRow.put("dongia", formattedRoomPrice);
    roomRow.put("thanhtien", formattedRoomPrice);
    list.add(roomRow);

    // Dịch vụ
    int i = 2;
    for (BillDetail bd : bdls) {
        String serviceName = new ServiceDAO().getName(bd.getService());
        double servicePrice = new ServiceDAO().getPrice(bd.getService());
        double total = servicePrice * bd.getQuant();

        Map<String, Object> serviceRow = new HashMap<>();
        serviceRow.put("STT", i++);
        serviceRow.put("noidung", serviceName);
        serviceRow.put("soluong", bd.getQuant());
        serviceRow.put("dongia", formatPrice(servicePrice));
        serviceRow.put("thanhtien", formatPrice(total));
        list.add(serviceRow);
    }
    Map<String,Object> sub =  new HashMap<>();
    sub.put("total", formatPrice(b.getTotal()));
    //list.add(sub);
    return list;
}
    
             
    
    public Map<String, Object> setupParameter(Bill b, String note) {
    Map<String, Object> data = new HashMap<>();
    Customer cs = new CustomerDAO().getById(b.getUser());

    data.put("id_bill", b.getId());
    data.put("cusname", cs.getName());
    data.put("check_in", b.getCheck_in());
    data.put("check_out", b.getCheck_out());
    data.put("phone", cs.getSdt());
    data.put("note", note);
    //data.put("total", formatPrice(b.getTotal())); // Gắn luôn tổng tiền
    data.put("createDate", new Date());
    data.put("summary", formatPrice(b.getTotal()));
    return data;
}
    
    public List<BillDetail> getBDList(int id){
        return new BillDetailDAO().getByBill(id);
    }
    
    public Bill getBill(int id){
        return new BillDAO().getBill(id);
    }
}
