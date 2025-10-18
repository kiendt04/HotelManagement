/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.ServiceDAO;
import Model.Service;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author ADMIN
 */
public class ServiceControl {
    
    private ServiceDAO serviceDAO;
    
    public ServiceControl() {
        this.serviceDAO = new ServiceDAO();
    }
    
    public List<Service> getAllServices() {
        return serviceDAO.getAll();
    }
    
    public void action(JButton save,JButton canl,JTextField id, JTextField name,JTextField price, JTextField quant, JTextField donvi, JDialog parent)
    {
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!validateServiceData(name.getText().trim(), price.getText().trim(), quant.getText().trim(), donvi.getText().trim()))
                {
                    JOptionPane.showMessageDialog(parent, getValidationErrorMessage(name.getText().trim(), price.getText().trim(), quant.getText().trim(), donvi.getText().trim()));
                    return;
                }
                int res = JOptionPane.showConfirmDialog(parent,"lưu tác vụ","Xác nhận", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.NO_OPTION)
                {
                   parent.dispose();
                }
                else
                {
                    try {
                        Service ser = createService(name.getText().trim(), price.getText().trim(), quant.getText().trim(), donvi.getText().trim());
                        ser.setId( id.getText().isBlank() ? 0 : Integer.parseInt(id.getText().trim()));
                        if(ser.getId() == 0 ? addService(ser) : updateService(ser))
                        {
                            JOptionPane.showMessageDialog(parent, "Thành công");
                            parent.dispose();
                        }
                        else JOptionPane.showMessageDialog(parent, "Thất bại!!!"); 
                    } catch (Exception l) {
                        JOptionPane.showMessageDialog(parent, l.getMessage());
                    }
                }
            }
        
        });
        canl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int res = JOptionPane.showConfirmDialog(parent,"Hủy tác vụ","Xác nhận", JOptionPane.YES_NO_OPTION);
               if(res == JOptionPane.YES_OPTION)
               {
                   parent.dispose();
               }
            }
        });
    }
    
    public boolean addService(Service service) {
        if (service == null) {
            return false;
        }
        
        if (service.getName() == null || service.getName().trim().isEmpty() || service.getUnit().trim().isEmpty()) {
            return false;
        }
        
        if (service.getPrice() < 0 || service.getQuant() < 0) {
            return false;
        }
        
        return serviceDAO.insertService(service) > 0;
    }
    
    public boolean updateService(Service service) {
        if (service == null) {
            return false;
        }
        
        // Kiểm tra dữ liệu đầu vào
        if (service.getName() == null || service.getName().trim().isEmpty() || service.getUnit().trim().isEmpty()) {
            return false;
        }
        
        if (service.getPrice() < 0 || service.getQuant() < 0) {
            return false;
        }
        
        return serviceDAO.uptService(service) > 0;
    }
    
    public boolean deleteService(int serviceId) {
        if (serviceId <= 0) {
            return false;
        }
        
        return serviceDAO.delService(serviceId) > 0;
    }
    
    public Service createService(String name, String priceStr,String quant, String donvi) {
        try {
            if (name == null || name.trim().isEmpty() || donvi.trim().isEmpty()) {
                return null;
            }
            
            double price = Double.parseDouble(priceStr.trim());
            if (price < 0) {
                return null;
            }
            
            return new Service(0, name.trim(), price,Integer.parseInt(quant),"");
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public boolean validateServiceData (String name, String priceStr,String quant, String donvi) {
        if (name == null || name.trim().isEmpty() || donvi.trim().isEmpty()) {
            return false;
        }
        
        try {
            double price = Double.parseDouble(priceStr.trim());
            int quan = Integer.parseInt(quant);
            return price >= 0 && quan >=0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public String getValidationErrorMessage(String name, String priceStr, String quant, String donvi) {
        if (name == null || name.trim().isEmpty() || donvi.trim().isEmpty()) {
            return "Tên dịch vụ không được để trống";
        }
        
        try {
            double price = Double.parseDouble(priceStr.trim());
            int qun = Integer.parseInt(quant);
            if (price < 0) {
                return "Giá dịch vụ không hợp lệ";
            }
            if (qun < 0) {
                return "Số lượng dịch vụ không hợp lệ";
            }
        } catch (NumberFormatException e) {
            return "Giá dịch vụ không hợp lệ";
        }
        
        return null;
    }
    
    
}