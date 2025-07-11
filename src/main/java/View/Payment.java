/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author ASUS
 */

import Control.CustomerListControl;
import Control.PaymentControl;
import DAO.Room_typeDAO;
import DAO.RoomDAO;
import DAO.BillDAO;
import DAO.ServiceDAO;
import DAO.CustomerDAO;
import DAO.BillDetailDAO;
import com.toedter.calendar.JDateChooser;
import Model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

public class Payment extends JFrame {

    private JComboBox<Customer> customerCBX;
    private DefaultComboBoxModel<Customer> customerModel;

    private JDateChooser checkInField;
    private JDateChooser checkOutField;
    private JTextField totalService;
    private JTextField totalRoom;
    private JComboBox<String> statusComboBox;
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JLabel totalAmountLabel;
    private NumberFormat currencyFormat;
    private JButton saveBtn, printBtn, exitBtn;
    private int idRoom,idBill = 0;
    private boolean isClick = false;
    private Room slRoom;
    private PaymentControl pc = new PaymentControl();
    private CustomerListControl clc = new CustomerListControl();

    public Payment(int id) {
        this.idRoom = id;
        this.slRoom = new RoomDAO().getbyID(id);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Đặt phòng");
        if((slRoom.getStatus() == 1 || slRoom.getStatus() == -1) && pc.getRoomBill(slRoom.getNum(),slRoom.getStatus()) != null)
        {
            billData(pc.getRoomBill(slRoom.getNum(),slRoom.getStatus()));
        }
    }

    private void initializeComponents() {
        // Khởi tạo các text field
        customerModel = new DefaultComboBoxModel<>();
        customerCBX = new JComboBox<>(customerModel);
        customerCBX.setEditable(true);

        // Load dữ liệu từ CustomerDAO
        List<Customer> customers = clc.getAll();
        for (Customer c : customers) {
            customerModel.addElement(c);
        }

        checkOutField = new JDateChooser();
        checkInField = new JDateChooser();

        checkInField.setDateFormatString("dd/MM/yyyy");
        checkOutField.setDateFormatString("dd/MM/yyyy");
        totalRoom = new JTextField("0");
        // Khởi tạo ComboBox cho trạng thái
        totalService = new JTextField("0");
        String[] statusOptions = { "Đang dùng","Hoàn tất","Đã đặt"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setSelectedItem("Chưa hoàn tất");

        // Khởi tạo bảng
        String[] columnNames = {"TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Chỉ cho phép chỉnh sửa cột SL
            }
        };

        serviceTable = new JTable(tableModel);
        serviceTable.setRowHeight(25);
        serviceTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        serviceTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        serviceTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        serviceTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Styling cho header
        JTableHeader header = serviceTable.getTableHeader();
        header.setBackground(new Color(220, 220, 220));
        header.setFont(new Font("Arial", Font.BOLD, 12));

        totalAmountLabel = new JLabel();
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountLabel.setForeground(Color.RED);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(new Color(240, 240, 240));

        saveBtn = new JButton("💾 Lưu");
        printBtn = new JButton("🖨️ In");
        exitBtn = new JButton("❌ Thoát");

        toolbarPanel.add(saveBtn);
        toolbarPanel.add(printBtn);
        toolbarPanel.add(exitBtn);

        // Top panel container
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(toolbarPanel, BorderLayout.SOUTH);

        // Panel thông tin khách hàng
        JPanel customerInfoPanel = createCustomerInfoPanel();

        // Panel danh sách sản phẩm
        JPanel servicePanel = createServicePanel();

        // Sắp xếp layout
        mainPanel.add(topContainer, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(customerInfoPanel, BorderLayout.NORTH);
        centerPanel.add(servicePanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void billData(Bill b)
    {
        idBill = b.getId();
        Customer csInfo = clc.getById(b.getUser());
        customerCBX.setSelectedItem(csInfo);
        checkInField.setDate(b.getCheck_in());
        checkOutField.setDate(b.getCheck_out());
        totalService.setText(pc.formatPrice(b.getTotal_service()));
        totalRoom.setText(pc.formatPrice(b.getTotal_time()));
        totalAmountLabel.setText(pc.formatPrice(b.getTotal()));
        pc.setTblData(tableModel, new BillDetailDAO().getByBill(b.getId()));
        statusComboBox.setSelectedItem(b.getStatus() == 0 ? "Hoàn tất" : (b.getStatus() == 1) ? "Đang dùng" : "Đã đặt");
    }
    
    
    
   private JPanel createCustomerInfoPanel() {
    // Panel chứa toàn bộ (trái + phải)
    JPanel mainPanel = new JPanel(new BorderLayout());

    // ==================== PANEL TRÁI ====================
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new GridBagLayout());
    leftPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;

    // Row 0 - Khách hàng
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
    leftPanel.add(new JLabel("Khách hàng:"), gbc);

    JPanel customerInputPanel = new JPanel(new BorderLayout());
    customerInputPanel.add(customerCBX, BorderLayout.CENTER);
    JButton btnOpenCustomer = new JButton("...");
    btnOpenCustomer.setPreferredSize(new Dimension(40, 25));
    btnOpenCustomer.addActionListener(e -> {
        pc.openCusList(Payment.this,customerModel);
    });
    customerInputPanel.add(btnOpenCustomer, BorderLayout.EAST);

    gbc.gridx = 1; gbc.gridwidth = 2;
    leftPanel.add(customerInputPanel, gbc);

    // Row 0 - Trạng thái
    gbc.gridx = 3; gbc.gridwidth = 1;
    leftPanel.add(new JLabel("Trạng thái:"), gbc);

    gbc.gridx = 4;
    leftPanel.add(statusComboBox, gbc);

    // Row 1 - Ngày đặt
    gbc.gridx = 0; gbc.gridy = 1;
    leftPanel.add(new JLabel("Ngày đặt:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2;
    leftPanel.add(checkInField, gbc);

    // Row 1 - Tiền dịch vụ
    gbc.gridx = 3; gbc.gridwidth = 1;
    leftPanel.add(new JLabel("Tiền dịch vụ:"), gbc);
    gbc.gridx = 4;
    leftPanel.add(totalService, gbc);

    // Row 2 - Ngày trả
    gbc.gridx = 0; gbc.gridy = 2;
    leftPanel.add(new JLabel("Ngày trả:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2;
    leftPanel.add(checkOutField, gbc);

    // Row 2 - Tiền phòng
    gbc.gridx = 3; gbc.gridwidth = 1;
    leftPanel.add(new JLabel("Tiền phòng:"), gbc);
    gbc.gridx = 4;
    leftPanel.add(totalRoom, gbc);

    // Row 3 - Tổng thanh toán
    gbc.gridx = 0; gbc.gridy = 3;
    leftPanel.add(new JLabel("Tổng thanh toán:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2;
    leftPanel.add(totalAmountLabel, gbc);

    // ==================== PANEL PHẢI ====================
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm - Dịch vụ"));
    rightPanel.setPreferredSize(new Dimension(250, 200)); // đảm bảo không co

    DefaultListModel<Service> model = new DefaultListModel<>();
    for (Service s : new ServiceDAO().getAll()) {
        model.addElement(s);
        
    }

    JList<Service> serviceList = new JList<>(model);
    serviceList.setFont(new Font("Arial", Font.PLAIN, 14));
    serviceList.setFixedCellHeight(26);

    JScrollPane scrollPane = new JScrollPane(serviceList);
    rightPanel.add(scrollPane, BorderLayout.CENTER);

    // Mouse click để thêm DV
    serviceList.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            Service selected = serviceList.getSelectedValue();
            if (selected == null) return;

            String name = selected.getName();
            double price = selected.getPrice();

            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (name.equalsIgnoreCase(tableModel.getValueAt(i, 0).toString())) {
                    int sl = Integer.parseInt(tableModel.getValueAt(i, 1).toString()) + 1;
                    tableModel.setValueAt(sl, i, 1);
                    tableModel.setValueAt(pc.formatPrice(sl * price), i, 3);
                    found = true;
                    break;
                }
            }
            if (!found) {
                tableModel.addRow(new Object[]{name, 1, pc.formatPrice(price), pc.formatPrice(price)});
            }

            calculateTotal();
        }
    });

    // ==================== GỘP 2 PANEL LẠI ====================
    mainPanel.add(leftPanel, BorderLayout.CENTER);
    mainPanel.add(rightPanel, BorderLayout.EAST);

    return mainPanel;
}

    private JPanel createServicePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách Sản phẩm - Dịch vụ"));

        // Header với thông tin phòng
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(255, 240, 240));
        String infor =  "Phòng " + slRoom.getNum();
        JLabel roomLabel = new JLabel(infor);
        roomLabel.setFont(new Font("Arial", Font.BOLD, 12));
        roomLabel.setForeground(Color.RED);
        headerPanel.add(roomLabel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(serviceTable), BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        // Thêm event handler cho việc thay đổi số lượng trong bảng
        serviceTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 2) { // Cột số lượng
                calculateTotal();
            }
        });
        serviceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Nếu click 2 lần
                if (e.getClickCount() == 1) {
                    int row = serviceTable.getSelectedRow();
                    if (row != -1) {
                        try {
                            int quantity = Integer.parseInt(tableModel.getValueAt(row, 1).toString());
                            double unitPrice = Double.parseDouble(tableModel.getValueAt(row, 2).toString().replace(",", ""));

                            if (quantity > 1) {
                                quantity--;
                                tableModel.setValueAt(quantity, row, 1);
                                tableModel.setValueAt(pc.formatPrice(quantity * unitPrice), row, 3);
                            } else {
                                tableModel.removeRow(row);
                            }

                            calculateTotal();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // Thêm event handler cho ComboBox trạng thái
        statusComboBox.addActionListener(e -> {
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            // Có thể thêm logic xử lý khác ở đây
        });
        checkInField.addPropertyChangeListener("date", e -> {
    if (e.getOldValue() != e.getNewValue()) {
        validateAndCalculateDays();

        Object newValue = e.getNewValue();
        if (newValue instanceof java.util.Date) {
            java.util.Date selectedDate = (java.util.Date) newValue;

            Calendar selected = Calendar.getInstance();
            selected.setTime(selectedDate);

            Calendar today = Calendar.getInstance();
            // So sánh ngày, không tính giờ
            if (selected.get(Calendar.YEAR) > today.get(Calendar.YEAR) ||
                (selected.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                 selected.get(Calendar.DAY_OF_YEAR) > today.get(Calendar.DAY_OF_YEAR))) {
                statusComboBox.setSelectedItem("Đã đặt");
                statusComboBox.setEnabled(false);
            } else {
                statusComboBox.setSelectedItem("Đang dùng");
                statusComboBox.setEnabled(true);
            }
        } else {
            System.err.println("Giá trị không phải kiểu java.util.Date: " + newValue);
        }
    }
});

        checkOutField.addPropertyChangeListener("date", e -> {
            if (e.getOldValue() != e.getNewValue()) {
                System.out.println("Check-out date changed: " + e.getNewValue());
                validateAndCalculateDays();
            }
        });
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer cs = (Customer) customerCBX.getSelectedItem();
                if(checkInField.getDate() == null || checkOutField == null)
                {
                    JOptionPane.showMessageDialog(rootPane, "Chọn ngày đặt phòng !!!!");
                    return;
                }
                java.sql.Date dt_in = new java.sql.Date(checkInField.getDate().getTime());
                java.sql.Date dt_out = new java.sql.Date(checkOutField.getDate().getTime());
                double totalroom = Double.parseDouble(totalRoom.getText().replaceAll("[^0-9]", ""));
                int totalservice = Integer.parseInt(totalService.getText().replaceAll("[^0-9]", ""));
                double total = totalroom + totalservice;
                int stats = statusComboBox.getSelectedItem().equals("Hoàn tất") ? 0 : (statusComboBox.getSelectedItem().equals("Đang dùng") ? 1 : -1);
                Bill b = new Bill(idBill, slRoom.getNum(), cs.getId(), dt_in, dt_out, totalroom, totalservice, total, stats);
                if(JOptionPane.showConfirmDialog(rootPane, "Luu hoa don", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0 && (idBill == 0 ? pc.insertBill(b) : pc.uptBill(b)) != 0)
                {
                    slRoom.setStatus(stats);
                    int bdID = pc.getRoomBill(slRoom.getNum(),slRoom.getStatus()).getId();
                    List<BillDetail> lst = new ArrayList<>();
                    List<Service> svl = new ServiceDAO().getAll();
                    for (int i = 0 ;i<serviceTable.getModel().getRowCount();i++)
                    {
                        int j = 0;
                        while(!serviceTable.getModel().getValueAt(i, 0).equals(svl.get(j).getName()))
                        {
                            j++;
                        }
                        int srId = svl.get(j).getId();
                        int quan = Integer.parseInt(serviceTable.getModel().getValueAt(i, 1).toString().trim());
                        int ttl = Integer.parseInt(serviceTable.getModel().getValueAt(i, 3).toString().replace(",", "").trim());
                        lst.add(new BillDetail(bdID,srId,quan,ttl));
                    }
                    for (int i=0;i<lst.size();i++)
                    {
                        int rs = idBill == 0 ? pc.insertDetail(lst.get(i)) : pc.uptBD(lst.get(i));
                    }
                    JOptionPane.showMessageDialog(rootPane, "Lưu thành công");
                    if(stats == 0)
                    {
                        saveBtn.setEnabled(false);
                    }
                    if(stats == -1)
                    {
                        slRoom.setStatus(0);
                    }
                    else
                    {
                        slRoom.setStatus(stats);
                    }
                    int r = pc.uptRoom(slRoom);
//                    dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Lỗi! Lưu thất bại");
                }
            }
        });
        printBtn.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Object> datasrc = pc.setDataPrint(pc.getBDList(idBill),slRoom,pc.getBill(idBill));
                Map<String ,Object> mainparam = pc.setupParameter(pc.getBill(idBill), "");
                pc.printed(datasrc, mainparam);
            }
        });
        
        exitBtn.addActionListener(e -> {dispose();});
    }
    
    private void validateAndCalculateDays() {
        java.util.Date checkIn = checkInField.getDate();
        java.util.Date checkOut = checkOutField.getDate();

        if (checkIn == null || checkOut == null) {
            return;
        }
        
        // Kiểm tra check-in phải từ hôm nay trở đi
        // Ngày được chọn từ giao diện (JDateChooser)
        java.util.Date checkInUtil = checkInField.getDate();

        // Lấy ngày hôm nay
        java.util.Date todayUtil = new java.util.Date();

        // Chuyển cả hai về java.sql.Date (loại bỏ giờ/phút/giây)
        java.sql.Date checkInD = new java.sql.Date(checkInUtil.getTime());
       
        LocalDate yesterday = LocalDate.now();
        java.sql.Date today = java.sql.Date.valueOf(yesterday);

        // So sánh
        if (checkInD.before(today)) {
            JOptionPane.showMessageDialog(this, "Ngày đặt phòng phải từ hôm nay trở đi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        long diffMillis = checkOut.getTime() - checkIn.getTime();
        if (diffMillis <= 0) {
            JOptionPane.showMessageDialog(this, "Ngày trả phải sau ngày đặt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long days = diffMillis / (1000 * 60 * 60 * 24);
        if (days == 0) {
            days = 1;
        }
        Room_typeDAO rt = new Room_typeDAO();
        
        double roomRate = rt.getPrice(slRoom.getType());
        double totalRoomPrice = roomRate * days;

        totalRoom.setText(NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(totalRoomPrice));
        calculateTotal();
    }

    private void calculateTotal() {
        long tongDichVu = 0;

        // Tính tổng tiền dịch vụ từ bảng
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object thanhTienObj = tableModel.getValueAt(i, 3);
            if (thanhTienObj != null && !thanhTienObj.toString().trim().isEmpty()) {
                try {
                    String thanhTienStr = thanhTienObj.toString().replaceAll(",", "");
                    if (!thanhTienStr.isEmpty()) {
                        tongDichVu += Long.parseLong(thanhTienStr);
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua lỗi
                }
            }
        }

        // Cập nhật totalService theo định dạng tiền tệ
        totalService.setText(currencyFormat.format(tongDichVu));

        // Parse lại giá trị từ totalRoom đang hiển thị có thể đã được format
        long tienPhong = 0;
        try {
            String rawText = totalRoom.getText().replaceAll("\\.", "").replaceAll("[^0-9]", "");
            if (!rawText.isEmpty()) {
                tienPhong = Long.parseLong(rawText);
            }
        } catch (NumberFormatException e) {
            tienPhong = 0;
        }

        // Format lại totalRoom luôn
        totalRoom.setText(currencyFormat.format(tienPhong));

        // Tính và hiển thị tổng thanh toán
        long tongThanhToan = tienPhong + tongDichVu;
        totalAmountLabel.setText(currencyFormat.format(tongThanhToan) + " đồng");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Payment(1).setVisible(true);
        });
    }
}
