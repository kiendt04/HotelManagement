/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.Room_typeDAO;
import DAO.RoomDAO;
import DAO.FloorDAO;
import Model.*;
import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.ui.tabbedui.RootPanel;

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
    
    public double getRoomTypePrice(int id,boolean days) {
        return roomTypeDAO.getPricePH(id,days);
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
    
    public void FormatAbility(Font header,Font hotelName,Font tableHeader, Font tableContent, Font footerlable,Font footerContent,Font headerExportTime)
    {
        // 2. Thiết lập Tên Font (Font Name)
        header.setFontName("Times New Roman"); 
        // 3. Thiết lập Cỡ Chữ (Font Size)
        header.setFontHeightInPoints((short) 16); // Ví dụ: cỡ 14
        // 4. Thiết lập In Đậm (Bold)
        header.setBold(true);
        // 5. Thiết lập Màu Chữ (Color)
        header.setColor(IndexedColors.BLACK.getIndex());
        
        hotelName.setFontName("Times New Roman"); 
        hotelName.setFontHeightInPoints((short) 11); 
        hotelName.setBold(true);
        hotelName.setColor(IndexedColors.BLACK.getIndex());
        
        headerExportTime.setFontName("Times New Roman"); 
        headerExportTime.setFontHeightInPoints((short) 11); 
        headerExportTime.setItalic(true);
        headerExportTime.setColor(IndexedColors.BLACK.getIndex());
        
        tableHeader.setFontName("Times New Roman"); 
        tableHeader.setFontHeightInPoints((short) 14); 
        tableHeader.setBold(true);
        tableHeader.setColor(IndexedColors.BLACK.getIndex());
        
        tableContent.setFontName("Times New Roman"); 
        tableContent.setFontHeightInPoints((short) 13); 
        tableContent.setBold(false);
        tableContent.setColor(IndexedColors.BLACK.getIndex());
        
        footerlable.setFontName("Times New Roman"); 
        footerlable.setFontHeightInPoints((short) 14); 
        footerlable.setBold(true);
        footerlable.setColor(IndexedColors.RED.getIndex());
        
        footerContent.setFontName("Times New Roman"); 
        footerContent.setFontHeightInPoints((short) 13); 
        footerContent.setBold(true);
        footerContent.setItalic(true);
        footerContent.setColor(IndexedColors.BLACK.getIndex());
    }
    
    public void ExportExcelFile(JTable table,Frame parent)
    {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DOANH THU");
        TableModel model = table.getModel();
        // 1. Tạo một đối tượng Font mới
        Font headerFont = workbook.createFont();
        Font hotelnameFont = workbook.createFont();
        Font headerExportTimeFont = workbook.createFont();
        Font tableHeaderFont = workbook.createFont();
        Font tableContentFont = workbook.createFont();
        Font footerLableFont = workbook.createFont();
        Font footerContentFont = workbook.createFont();
        FormatAbility(headerFont, hotelnameFont ,tableHeaderFont, tableContentFont, footerLableFont, footerContentFont,headerExportTimeFont);
        
        Color awtColor = new Color(61,203,181); // Light yellow RGB
        XSSFColor customColor = new XSSFColor(awtColor, null);
        
        try {
            Row headerRow = sheet.createRow(1);
            Cell header = headerRow.createCell(1);
            header.setCellValue("DANH SÁCH PHÒNG");
            sheet.addMergedRegion(new CellRangeAddress(1,1,1,5));
            CellStyle headerTitle = workbook.createCellStyle();
            headerTitle.setAlignment(HorizontalAlignment.CENTER);
            headerTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerTitle.setFont(headerFont);
            header.setCellStyle(headerTitle);
            
            Row header1Row = sheet.createRow(2);
            Cell hotelName = header1Row.createCell(1);
            sheet.addMergedRegion(new CellRangeAddress(2,2,1,5));
            hotelName.setCellValue("GREENHOTEL HADONG");
            CellStyle hotelname = workbook.createCellStyle();
            hotelname.setAlignment(HorizontalAlignment.CENTER);
            hotelname.setVerticalAlignment(VerticalAlignment.CENTER);
            hotelname.setFont(hotelnameFont);
            hotelName.setCellStyle(hotelname);
            
            Row header2Row = sheet.createRow(3);
            Cell exportDate = header2Row.createCell(2);
            sheet.addMergedRegion(new CellRangeAddress(3,3,2,5));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            exportDate.setCellValue("Ngày xuất: " + now.format(formatter));
            CellStyle exportdate = workbook.createCellStyle();
            exportdate.setAlignment(HorizontalAlignment.CENTER);
            exportdate.setVerticalAlignment(VerticalAlignment.CENTER);
            exportdate.setFont(headerExportTimeFont);
            exportDate.setCellStyle(exportdate);
            
            // Ghi tiêu đề (Header Row)
            Row tableRow = sheet.createRow(5);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = tableRow.createCell(i + 1);
                CellStyle style = workbook.createCellStyle();
                style.setFont(tableHeaderFont);
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setFillForegroundColor(customColor);
                cell.setCellStyle(style);
                cell.setCellValue(model.getColumnName(i));
            }

            // Ghi dữ liệu (Data Rows)
            for (int i = 0; i < model.getRowCount(); i++) 
            {
                Row dataRow = sheet.createRow(i + 6); // Bắt đầu từ hàng thứ 1 (sau tiêu đề)
                for (int j = 0; j < model.getColumnCount(); j++) 
                {
                    Cell cell = dataRow.createCell(j + 1);
                    CellStyle style = workbook.createCellStyle();
                    style.setFont(tableContentFont);
                    style.setAlignment(HorizontalAlignment.CENTER);
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBorderLeft(BorderStyle.THIN);
                    style.setBorderRight(BorderStyle.THIN);
                    style.setBorderTop(BorderStyle.THIN);
                    cell.setCellStyle(style);
                    Object value = model.getValueAt(i, j);

                    // Thiết lập giá trị dựa trên kiểu dữ liệu
                    if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Timestamp) {
                        // --- XỬ LÝ KIỂU TIMESTAMP ---
                        // Chuyển Timestamp sang java.util.Date để POI hiểu
                        Timestamp ts = (Timestamp) value;
                        cell.setCellValue(new java.util.Date(ts.getTime())); 
            
                        // Tùy chọn: Thiết lập CellStyle để định dạng ngày/giờ trong Excel
                        CellStyle dateStyle = workbook.createCellStyle();
                        CreationHelper createHelper = workbook.getCreationHelper();
            
                        // Ví dụ: Định dạng ngày giờ chuẩn
                        dateStyle.setDataFormat(
                            createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
                        dateStyle.setAlignment(HorizontalAlignment.CENTER);
                        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        dateStyle.setBorderBottom(BorderStyle.THIN);
                        dateStyle.setBorderLeft(BorderStyle.THIN);
                        dateStyle.setBorderRight(BorderStyle.THIN);
                        dateStyle.setBorderTop(BorderStyle.THIN);
                        dateStyle.setFont(tableContentFont);
                        cell.setCellStyle(dateStyle);
            
                    } else if (value instanceof Number) {
                        // --- XỬ LÝ KIỂU SỐ (Double, Integer, Long, v.v.) ---
                        cell.setCellValue(((Number) value).doubleValue());
            
                        // Tùy chọn: Thiết lập CellStyle cho tiền tệ/số thập phân
                        // Ví dụ: Định dạng 2 chữ số thập phân
                        // CellStyle numberStyle = workbook.createCellStyle();
                        // numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
                        // cell.setCellStyle(numberStyle);

                        } else {
                    // --- XỬ LÝ KIỂU CHUỖI (String) ---
                    cell.setCellValue(value.toString());
                    }
                }
            }
            
            
            sheet.addMergedRegion(new CellRangeAddress(model.getRowCount() + 6,model.getRowCount()+ 6 , 10, 13));
            
            // Tự động điều chỉnh độ rộng cột (tùy chọn)
            for(int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
            }
            
            String path = "D:/DH/Nam 2024-2025/JavaNC/HotelManagement/src/main/resources/FileList/";
            File file = new File(path + "Danh_Sach_Phong_" + exportDate() +".xlsx");
            
            // Ghi Workbook ra file
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        
            JOptionPane.showMessageDialog(parent, "Xuất file thành công!!!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    public String exportDate()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy_HH mm ss");
        return now.format(formatter);
    }
    }

