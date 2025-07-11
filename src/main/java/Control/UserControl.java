/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.UserDAO;
import Model.User;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class UserControl {
    
    private UserDAO userDAO;
    
    public UserControl() {
        this.userDAO = new UserDAO();
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }
    
    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return false;
        }
        
        if (user.getPass() == null || user.getPass().trim().isEmpty()) {
            return false;
        }
        
        if (user.getRole() < 0 || user.getRole() > 1) {
            return false;
        }
        
        return userDAO.insertUser(user) > 0;
    }
    
    public boolean updateUser(User user) {
        if (user == null) {
            return false;
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return false;
        }
        
        if (user.getRole() < 0 || user.getRole() > 1) {
            return false;
        }
        
        return userDAO.uptUser(user) > 0;
    }
    
    public boolean updateUserWithoutPassword(User user) {
        if (user == null) {
            return false;
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return false;
        }
        
        if (user.getRole() < 0 || user.getRole() > 1) {
            return false;
        }
        
        return userDAO.uptWithoutPass(user) > 0;
    }
    
    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            return false;
        }
        
        return userDAO.delUser(userId) > 0;
    }
    
    public User createUser(int id, String name, String password, int role) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        
        if (role < 0 || role > 1) {
            return null;
        }
        
        return new User(id, name.trim(), password.trim(), role);
    }
    
    public boolean validateUserData(String name, String password, boolean isUpdate) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        // Đối với update, password có thể để trống (không thay đổi password)
        if (!isUpdate && (password == null || password.trim().isEmpty())) {
            return false;
        }
        
        return true;
    }
    
    public String getValidationErrorMessage(String name, String password, boolean isUpdate) {
        if (name == null || name.trim().isEmpty()) {
            return "Tên đăng nhập không được để trống";
        }
        
        if (!isUpdate && (password == null || password.trim().isEmpty())) {
            return "Mật khẩu không được để trống";
        }
        
        return null;
    }
    
    public int checkLogin(String name, String password) {
        if (name == null || name.trim().isEmpty()) {
            return -1;
        }
        
        if (password == null || password.trim().isEmpty()) {
            return -1;
        }
        
        return userDAO.checkLogin(name.trim(), password.trim());
    }
    
    public int getUserId(String name) {
        if (name == null || name.trim().isEmpty()) {
            return -1;
        }
        
        return userDAO.getId(name.trim());
    }
    
    public boolean checkChangePassword(int userId, String currentPassword) {
        if (userId <= 0) {
            return false;
        }
        
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return false;
        }
        
        return userDAO.checkChangePass(userId, currentPassword.trim());
    }
    
    public boolean changePassword(int userId, String newPassword) {
        if (userId <= 0) {
            return false;
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        
        return userDAO.changePass(userId, newPassword.trim()) > 0;
    }
    
    public String getRoleDisplayName(int role) {
        return role == 1 ? "Admin" : "Staff";
    }
    
    public boolean isValidRole(int role) {
        return role == 0 || role == 1;
    }
}