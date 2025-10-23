/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.*;
import Model.*;
import View.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import java.awt.Color; // Rất tiện lợi!

/**
 *
 * @author ADMIN
 */
public class BillHistoryControl {
    private BillDAO billDAO = new BillDAO();
    private BillDetailDAO billdetailDAO = new BillDetailDAO();
    private BillGroupBookingDAO billgroupDAO = new BillGroupBookingDAO();
    private BillGroupBookingDetail_RoomDAO billgroupRoomDAO = new BillGroupBookingDetail_RoomDAO();
    private BillGroupBookingDetail_ServiceDAO billgroupServiceDAO = new BillGroupBookingDetail_ServiceDAO();
    private JButton btnPrint;
    private JCheckBox chkDon,chkNhom;
    private JComboBox<String> cbxNam ,cbxThang ;
    private JTable table;
    private DefaultTableModel billDetailModel;
    private JTextField txfTotal;
    private List<Bill> billLst;
    private List<BillDetail> billdetailLst;
    private List<BillGroupBooking> billgroupLst;
    private List<BillGroupBookingDetail_Room> billgrouproomLst;
    private List<BillGroupBookingDetail_Service> billgroupserviceLst;
    private List<Customer> customerLst ;
    private TableColumn hiddenColumn; // Biến instance của Class
    private List<Bill> billFilterLst;
    private List<BillGroupBooking> billgroupFilterLst;
    
    public BillHistoryControl(JButton btnPrint, JCheckBox chkDon, JCheckBox chkNhom, JComboBox<String> cbxNam, JComboBox<String> cbxThang, JTable table, DefaultTableModel billDetailModel, JTextField txfTotal) {
        this.btnPrint = btnPrint;
        this.chkDon = chkDon;
        this.chkNhom = chkNhom;
        this.cbxNam = cbxNam;
        this.cbxThang = cbxThang;
        this.table = table;
        this.billDetailModel = billDetailModel;
        this.txfTotal = txfTotal;
    }
    
    public void loadData()
    {
        billLst = billDAO.getAll();
        billdetailLst = billdetailDAO.getAll();
        billgroupLst = billgroupDAO.getAll();
        billgrouproomLst = billgroupRoomDAO.getAll();
        billgroupserviceLst = billgroupServiceDAO.getAll();
        customerLst = new CustomerDAO().getAll();
        TableColumnModel ClnModel = table.getColumnModel();
        ClnModel.getColumn(0).setPreferredWidth(30);
        ClnModel.getColumn(0).setMaxWidth(30);
        ClnModel.getColumn(1).setPreferredWidth(60);
        ClnModel.getColumn(1).setMaxWidth(60);
        setColumnAlignment(table, 12, SwingConstants.CENTER);
        for(int i=0;i<5;i++)
        {
            setColumnAlignment(table, i, SwingConstants.CENTER);
        }
        for (int i = 5;i<12;i++)
        {
            setColumnAlignment(table, i, SwingConstants.RIGHT);
        }
        chkDon.setSelected(true);
        chkNhom.setSelected(true);
        checkBoxAction();
    }
    
    public void checkBoxAction()
    {
        cbxAction();
        double sum = 0;
        if(chkDon.isSelected() && !chkNhom.isSelected())
        {
            billDetailModel.setRowCount(0);
            showColumn(table, 1);
            for (int i=0;i<billFilterLst.size();i++)
            {
                Bill b = billFilterLst.get(i);
                sum += b.getActual_pay();
                Customer cus = customerLst.stream().filter(c -> c.getId().equals(b.getUser())).findFirst().orElse(null);
                String Stats = b.getStatus() == -2 ? "Đặt trước/Đã hủy" : b.getStatus() == -1 ? "Đặt trước/Chưa nhận phòng" : b.getStatus() == 0 ? "Đã thanh toán" : "Chưa thanh toán" ;
                billDetailModel.addRow(new Object[]{b.getId(),b.getRoom(),cus.getName(),b.getCheck_in(),b.getCheck_out(),formatDouble(b.getTotal_time()),formatDouble((double) b.getTotal_service()),formatDouble(b.getTotal()),formatDouble(b.getExchange()),formatDouble(b.getDisount()),formatDouble(b.getDeposit()),formatDouble(b.getActual_pay()),Stats});
            }
            billDetailModel.fireTableDataChanged();
        }
        else if (chkNhom.isSelected() && !chkDon.isSelected())
        {
            billDetailModel.setRowCount(0);
            hideColumn(table, 1);
            for (int i=0;i<billgroupFilterLst.size();i++)
            {
                BillGroupBooking bgb = billgroupFilterLst.get(i);
                sum+= bgb.getActual_pay();
                Customer cus = customerLst.stream().filter(c -> c.getId().equals(bgb.getCus())).findFirst().orElse(null);
                String Stats = bgb.getStatus() == -2 ? "Đặt trước/Đã hủy" : bgb.getStatus() == -1 ? "Đặt trước/Chưa nhận phòng" : bgb.getStatus() == 0 ? "Đã trả" : bgb.getStatus() == 1 ? "Chưa trả" : "Trả phòng sớm";
                billDetailModel.addRow(new Object[]{bgb.getId(),cus.getName(),bgb.getIn(),bgb.getOut(),formatDouble(bgb.getTotal_room()),formatDouble(bgb.getTotal_ser()),formatDouble(bgb.getTotal()),formatDouble(bgb.getEx_charge()),formatDouble(bgb.getDiscount()),formatDouble(bgb.getDeposit()),formatDouble(bgb.getActual_pay()),Stats});
            }
            billDetailModel.fireTableDataChanged();
        }
        else if(chkNhom.isSelected() && chkDon.isSelected())
        {
            billDetailModel.setRowCount(0);
            showColumn(table, 1);
            for (int i=0;i<billFilterLst.size();i++)
            {
                Bill b = billFilterLst.get(i);
                sum += b.getActual_pay();
                Customer cus = customerLst.stream().filter(c -> c.getId().equals(b.getUser())).findFirst().orElse(null);
                String Stats = b.getStatus() == -2 ? "Đặt trước/Đã hủy" : b.getStatus() == -1 ? "Đặt trước/Chưa nhận phòng" : b.getStatus() == 0 ? "Đã thanh toán" : "Chưa thanh toán" ;
                billDetailModel.addRow(new Object[]{b.getId(),b.getRoom(),cus.getName(),b.getCheck_in(),b.getCheck_out(),formatDouble(b.getTotal_time()),formatDouble((double) b.getTotal_service()),formatDouble(b.getTotal()),formatDouble(b.getExchange()),formatDouble(b.getDisount()),formatDouble(b.getDeposit()),formatDouble(b.getActual_pay()),Stats});
            }
            
            for (int i=0;i<billgroupFilterLst.size();i++)
            {   
                BillGroupBooking bgb = billgroupFilterLst.get(i);
                sum+= bgb.getActual_pay();
                Customer cus = customerLst.stream().filter(c -> c.getId().equals(bgb.getCus())).findFirst().orElse(null);
                String Stats = bgb.getStatus() == -2 ? "Đặt trước/Đã hủy" : bgb.getStatus() == -1 ? "Đặt trước/Chưa nhận phòng" : bgb.getStatus() == 0 ? "Đã trả" : bgb.getStatus() == 1 ? "Chưa trả" : "Trả phòng sớm";
                billDetailModel.addRow(new Object[]{bgb.getId(),"",cus.getName(),bgb.getIn(),bgb.getOut(),formatDouble(bgb.getTotal_room()),formatDouble(bgb.getTotal_ser()),formatDouble(bgb.getTotal()),formatDouble(bgb.getEx_charge()),formatDouble(bgb.getDiscount()),formatDouble(bgb.getDeposit()),formatDouble(bgb.getActual_pay()),Stats});
            }

            billDetailModel.fireTableDataChanged();
        }
        else
        {
            table.removeAll();
            billDetailModel.setRowCount(0);
        }
        txfTotal.setText(formatDouble(sum));
    }
    
    public void cbxAction()
    {
        String nam = cbxNam.getSelectedItem().toString();
        String thang = cbxThang.getSelectedItem().toString();
        billFilterLst = billLst;
        billgroupFilterLst = billgroupLst;
        if(!nam.equals("All"))
        {
            int year = Integer.parseInt(nam);
            billFilterLst = billFilterLst.stream().filter(r -> r.getCheck_in().toLocalDateTime().getYear() ==  year).collect(Collectors.toList());
            billgroupFilterLst = billgroupFilterLst.stream().filter(r -> r.getIn().toLocalDateTime().getYear() == year).collect(Collectors.toList());
        }
        if(!thang.equals("All"))
        {
            int month = Integer.parseInt(thang);
            billFilterLst =  billFilterLst.stream().filter(r -> r.getCheck_in().toLocalDateTime().getMonthValue() ==  month).collect(Collectors.toList());
            billgroupFilterLst = billgroupFilterLst.stream().filter(r -> r.getIn().toLocalDateTime().getMonthValue()== month).collect(Collectors.toList());
        }
        
    }
    
    public void FormatAbility(Font header,Font tableHeader, Font tableContent, Font footerlable,Font footerContent,Font headerExportTime)
    {
        // 2. Thiết lập Tên Font (Font Name)
        header.setFontName("Times New Roman"); 
        // 3. Thiết lập Cỡ Chữ (Font Size)
        header.setFontHeightInPoints((short) 16); // Ví dụ: cỡ 14
        // 4. Thiết lập In Đậm (Bold)
        header.setBold(true);
        // 5. Thiết lập Màu Chữ (Color)
        header.setColor(IndexedColors.BLACK.getIndex());
        
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
    
    public void ExportExcelFile()
    {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DOANH THU");
        TableModel model = table.getModel();
        // 1. Tạo một đối tượng Font mới
        Font headerFont = workbook.createFont();
        Font headerExportTimeFont = workbook.createFont();
        Font tableHeaderFont = workbook.createFont();
        Font tableContentFont = workbook.createFont();
        Font footerLableFont = workbook.createFont();
        Font footerContentFont = workbook.createFont();
        FormatAbility(headerFont, tableHeaderFont, tableContentFont, footerLableFont, footerContentFont,headerExportTimeFont);
        
        Color awtColor = new Color(255, 255, 153); // Light yellow RGB
        // 2. Create the XSSFColor wrapper object
        // The second argument (null) is for compatibility with the old HSSF palette, which is usually null for XSSF
        XSSFColor customColor = new XSSFColor(awtColor, null);
        
        try {
            Row headerRow = sheet.createRow(1);
            Cell header = headerRow.createCell(1);
            header.setCellValue("DOANH THU " + getTittel());
            sheet.addMergedRegion(new CellRangeAddress(1,1,1,13));
            CellStyle headerTitle = workbook.createCellStyle();
            headerTitle.setAlignment(HorizontalAlignment.CENTER);
            headerTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerTitle.setFont(headerFont);
            header.setCellStyle(headerTitle);
            
            Row header1Row = sheet.createRow(2);
            Cell hotelName = header1Row.createCell(5);
            sheet.addMergedRegion(new CellRangeAddress(2,2,5,9));
            hotelName.setCellValue("GREENHOTEL HADONG");
            CellStyle hotelname = workbook.createCellStyle();
            hotelname.setAlignment(HorizontalAlignment.CENTER);
            hotelname.setVerticalAlignment(VerticalAlignment.CENTER);
            headerFont.setFontHeightInPoints((short) 11);
            hotelname.setFont(headerFont);
            hotelName.setCellStyle(hotelname);
            
            Row header2Row = sheet.createRow(3);
            Cell exportDate = header2Row.createCell(10);
            sheet.addMergedRegion(new CellRangeAddress(3,3,10,13));
            exportDate.setCellValue("Ngày xuất: " + LocalDate.now() + " " + LocalDateTime.now());
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
                        cell.setCellValue(new Date(ts.getTime())); 
            
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
            
            Row footerTitle = sheet.createRow(model.getRowCount()+ 6);
            Cell tittle = footerTitle.createCell(1);
            tittle.setCellValue("TỔNG (VND)");
            CellStyle tittleStyle = workbook.createCellStyle();
            tittleStyle.setAlignment(HorizontalAlignment.RIGHT);
            tittleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            tittleStyle.setBorderBottom(BorderStyle.THIN);
            tittleStyle.setBorderLeft(BorderStyle.THIN);
            tittleStyle.setBorderRight(BorderStyle.THIN);
            tittleStyle.setBorderTop(BorderStyle.THIN);
            tittleStyle.setFont(footerLableFont);
            tittle.setCellStyle(tittleStyle);
            
            for (int j = 1; j <= 9; j++) {
                // Lấy Cell hoặc tạo Cell mới nếu nó chưa tồn tại
                Cell cellToStyle = footerTitle.getCell(j);
                if (cellToStyle == null) {
                    cellToStyle = footerTitle.createCell(j);
                }
                // Áp dụng style cho tất cả các ô
                cellToStyle.setCellStyle(tittleStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(model.getRowCount() + 6,model.getRowCount()+ 6 , 1, 9));
            
            Cell content = footerTitle.createCell(10);
            content.setCellValue(txfTotal.getText().trim());
            CellStyle contentStyle = workbook.createCellStyle();
            contentStyle.setAlignment(HorizontalAlignment.RIGHT);
            contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentStyle.setBorderBottom(BorderStyle.THIN);
            contentStyle.setBorderLeft(BorderStyle.THIN);
            contentStyle.setBorderRight(BorderStyle.THIN);
            contentStyle.setBorderTop(BorderStyle.THIN);
            contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            contentStyle.setFont(footerContentFont);
            content.setCellStyle(contentStyle);
            
            for (int j = 10; j <= 13; j++) {
                Cell cellToStyle = footerTitle.getCell(j);
                if (cellToStyle == null) {
                    cellToStyle = footerTitle.createCell(j);
                }
                cellToStyle.setCellStyle(contentStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(model.getRowCount() + 6,model.getRowCount()+ 6 , 10, 13));
            
            // Tự động điều chỉnh độ rộng cột (tùy chọn)
            for(int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
            }
            
            File file = new File("D:/DH/Nam 2024-2025/JavaNC/HotelManagement/src/main/resources/revenue/Baocao_Doanhthu_" + getFileName() + "_" + exportDate() +".xlsx");
            
            // Ghi Workbook ra file
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        
            System.out.println("Xuất file Excel thành công!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getTittel()
    {
        String nam = cbxNam.getSelectedItem().toString();
        String thang = cbxThang.getSelectedItem().toString();
        if(nam.equals("All") )
        {
            if(thang.equals("All"))
            {
                return "TỔNG CÁC NĂM";
            }
            else
            {
                return "THÁNG" + thang + "/" + nam;
            }
        }
        else
        {
            if(thang.equals("All"))
            {
                return  "NĂM" + nam;
            }
            else
            {
                return "THÁNG " + thang + " QUA CÁC NĂM";
            }
        }
    }
    
    private String getFileName()
    {
        String nam = cbxNam.getSelectedItem().toString();
        String thang = cbxThang.getSelectedItem().toString();
        if(nam.equals("All") )
        {
            if(thang.equals("All"))
            {
                return "quacacnam";
            }
            else
            {
                return "Thang" + thang + "/" + nam;
            }
        }
        else
        {
            if(thang.equals("All"))
            {
                return  "Nam" + nam;
            }
            else
            {
                return "Thang" + thang + "_quacacnam";
            }
        }
    }
    
    public String exportDate()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        return now.format(formatter);
    }
    
    public void hideColumn(JTable table, int columnIndex) {
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(columnIndex);
    
        // Lưu cột vào biến instance và loại bỏ nó khỏi model
        hiddenColumn = column; 
        columnModel.removeColumn(column);
    }
    
    public void showColumn(JTable table, int columnIndex) {
        if (hiddenColumn != null) {
            // Thêm lại cột vào vị trí ban đầu
            hiddenColumn.setMaxWidth(60);
            hiddenColumn.setPreferredWidth(60);
            table.getColumnModel().addColumn(hiddenColumn);
            // Di chuyển cột đến vị trí mong muốn (nếu cần)
            table.getColumnModel().moveColumn(table.getColumnModel().getColumnCount() - 1, columnIndex);
            hiddenColumn = null; // Xóa tham chiếu
        }
    }
    
    public void setColumnAlignment(JTable table, int columnIndex, int alignment) {
    
    // 1. Tạo Renderer
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    
    // 2. Thiết lập kiểu căn chỉnh
    // alignment có thể là: SwingConstants.LEFT, SwingConstants.CENTER, hoặc SwingConstants.RIGHT
    renderer.setHorizontalAlignment(alignment);
    
    // 3. Lấy đối tượng cột
    TableColumn column = table.getColumnModel().getColumn(columnIndex);
    
    // 4. Áp dụng Renderer cho cột
    column.setCellRenderer(renderer);
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



