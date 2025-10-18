/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.Room_typeDAO;
import DAO.RoomDAO;
import DAO.FloorDAO;
import Model.*;
import java.util.List;
import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class RoomManagementControl {
    
    private FloorDAO floorDAO;
    private Room_typeDAO roomTypeDAO;
    private RoomDAO roomDAO;
    
    public RoomManagementControl() {
        this.floorDAO = new FloorDAO();
        this.roomTypeDAO = new Room_typeDAO();
        this.roomDAO = new RoomDAO();
    }
    
    // Floor operations
    public List<Floor> getAllFloors() {
        return floorDAO.getAll();
    }
    
    public int insertFloor(Floor floor) {
        return floorDAO.insertFloor(floor);
    }
    
    public int updateFloor(Floor floor) {
        return floorDAO.uptFloor(floor);
    }
    
    public int deleteFloor(int id) {
        return floorDAO.delFloor(id);
    }
    
    public int countFloors() {
        return floorDAO.countFloor();
    }
    
    public int createNewFloorId() {
        return floorDAO.creatNewID();
    }
    
    // Room Type operations
    public List<Room_type> getAllRoomTypes() {
        return roomTypeDAO.getAll();
    }
    
    public int insertRoomType(Room_type roomType) {
        return roomTypeDAO.insertRoom_type(roomType);
    }
    
    public int updateRoomType(Room_type roomType) {
        return roomTypeDAO.uptRoom(roomType);
    }
    
    public int deleteRoomType(int id) {
        return roomTypeDAO.delRoom(id);
    }
    
    public int getRoomTypeSize() {
        return roomTypeDAO.size();
    }
    
    public int createNewRoomTypeId() {
        return roomTypeDAO.createNewId();
    }
    
    public double getRoomTypePrice(int id) {
        return roomTypeDAO.getPricePH(id);
    }
    
    // Room operations
    public List<Room> getAllRooms() {
        return roomDAO.getAll();
    }
    
    public int insertRoom(Room room) {
        return roomDAO.insertRoom(room);
    }
    
    public int updateRoom(Room room) {
        return roomDAO.uptRoom(room);
    }
    
    public int deleteRoom(int id) {
        return roomDAO.delRoom(id);
    }
    
    public Room getRoomById(int id) {
        return roomDAO.getbyID(id);
    }
    
    public List<Room> getRoomsByFloor(int floor) {
        return roomDAO.getByFloor(floor);
    }
    
    public int countRooms() {
        return roomDAO.count();
    }
    
    public int countFilteredRooms(int floor, int type) {
        return roomDAO.count_filter(floor, type);
    }
    
    public List<Room> filterRooms(int floor, int type) {
        return roomDAO.filter(floor, type);
    }
    
    public boolean checkRoomNumber(String num) {
        return roomDAO.checkNum(num);
    }
    
    public boolean checkPreserver(Date date, String room) {
        return roomDAO.checkPreserver(date, room);
    }
    
    // Business logic methods
    public String getRoomTypeDisplayName(int typeId) {
        switch (typeId) {
            case 1:
                return "Đơn thường";
            case 2:
                return "Đơn VIP";
            case 3:
                return "Đôi thường";
            case 4:
                return "Đôi VIP";
            case 5:
                return "Vvip";
            default:
                return "Không xác định";
        }
    }
    
    public String getRoomStatusDisplayName(int status) {
        switch (status) {
            case 0:
                return "Trống";
            case 1:
                return "Sử dụng";
            case -1:
                return "Đặt trước";
            default:
                return "";
        }
    }
    
    public String getFloorDisplayName(int floorId) {
        return "Tầng " + floorId;
    }
    
    public boolean validateRoomNumber(String num, int floorId) {
        if (num == null || num.trim().isEmpty()) {
            return false;
        }
        
        try {
            int roomNum = Integer.parseInt(num);
            return roomNum / 100 == floorId;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean isRoomNumberExists(String num) {
        return !checkRoomNumber(num);
    }
    
    public Object[][] getRoomTableData() {
        List<Room> roomList = getAllRooms();
        Object[][] data = new Object[roomList.size()][5];
        
        for (int i = 0; i < roomList.size(); i++) {
            Room room = roomList.get(i);
            data[i][0] = room.getId();
            data[i][1] = room.getNum();
            data[i][2] = getFloorDisplayName(room.getFloor());
            data[i][3] = getRoomTypeDisplayName(room.getType());
            data[i][4] = getRoomStatusDisplayName(room.getStatus());
        }
        
        return data;
    }
    
    public Object[][] getFilteredRoomTableData(int floor, int type) {
        List<Room> roomList = filterRooms(floor, type);
        Object[][] data = new Object[roomList.size()][5];
        
        for (int i = 0; i < roomList.size(); i++) {
            Room room = roomList.get(i);
            data[i][0] = room.getId();
            data[i][1] = room.getNum();
            data[i][2] = getFloorDisplayName(room.getFloor());
            data[i][3] = getRoomTypeDisplayName(room.getType());
            data[i][4] = getRoomStatusDisplayName(room.getStatus());
        }
        
        return data;
    }
    
    public Floor createAllFloorsOption() {
        return new Floor(0, "All");
    }
    
    public Room_type createAllRoomTypesOption() {
        return new Room_type(0, "All", 0, 0,0);
    }
    
    public String[] getStatusOptions() {
        return new String[]{"Trống", "Sử dụng", "Đặt trước"};
    }
    
    public String[] getTableColumnNames() {
        return new String[]{"STT", "Phòng", "Tầng", "Loại", "Trạng thái"};
    }
}