/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.ServiceDAO;
import Model.Service;
import java.util.List;

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
    
    public boolean addService(Service service) {
        if (service == null) {
            return false;
        }
        
        if (service.getName() == null || service.getName().trim().isEmpty()) {
            return false;
        }
        
        if (service.getPrice() < 0) {
            return false;
        }
        
        return serviceDAO.insertService(service) > 0;
    }
    
    public boolean updateService(Service service) {
        if (service == null) {
            return false;
        }
        
        // Kiểm tra dữ liệu đầu vào
        if (service.getName() == null || service.getName().trim().isEmpty()) {
            return false;
        }
        
        if (service.getPrice() < 0) {
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
    
    public Service createService(int id, String name, String priceStr) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            
            double price = Double.parseDouble(priceStr.trim());
            if (price < 0) {
                return null;
            }
            
            return new Service(id, name.trim(), price);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public boolean validateServiceData(String name, String priceStr) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        try {
            double price = Double.parseDouble(priceStr.trim());
            return price >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public String getValidationErrorMessage(String name, String priceStr) {
        if (name == null || name.trim().isEmpty()) {
            return "Tên dịch vụ không được để trống";
        }
        
        try {
            double price = Double.parseDouble(priceStr.trim());
            if (price < 0) {
                return "Giá dịch vụ không được âm";
            }
        } catch (NumberFormatException e) {
            return "Giá dịch vụ không hợp lệ";
        }
        
        return null;
    }
}