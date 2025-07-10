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
    
    private List<PrintedData> setDataPrint(List<Service> svrLS,Customer cus,Bill b,Room r)
    {
        List<PrintedData> list = new ArrayList<>();
        
        return null;
    }
}
