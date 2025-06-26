/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author ASUS
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class Payment extends JFrame {
    
    private JTextField customerNameField;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JTextField guestsField;
    private JComboBox<String> statusComboBox;
    private JTextField noteField;
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JLabel totalAmountLabel;
    private NumberFormat currencyFormat;
    
    public Payment() {
        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadSampleData();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Đặt phòng khách lẻ");
    }
    
    private void initializeComponents() {
        // Khởi tạo các text field
        customerNameField = new JTextField("Đặng Trần Anh");
        checkInField = new JTextField("01/09/2021");
        checkOutField = new JTextField("02/09/2021");
        guestsField = new JTextField("1");
        
        // Khởi tạo ComboBox cho trạng thái
        String[] statusOptions = {"Chưa hoàn tất", "Hoàn tất", "Chưa thanh toán"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setSelectedItem("Chưa hoàn tất");
        
        noteField = new JTextField("Khách lẻ");
        
        // Khởi tạo bảng
        String[] columnNames = {"PHÒNG", "TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Chỉ cho phép chỉnh sửa cột SL
            }
        };
        
        serviceTable = new JTable(tableModel);
        serviceTable.setRowHeight(25);
        serviceTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        serviceTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        serviceTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        serviceTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        serviceTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Styling cho header
        JTableHeader header = serviceTable.getTableHeader();
        header.setBackground(new Color(220, 220, 220));
        header.setFont(new Font("Arial", Font.BOLD, 12));
        
        totalAmountLabel = new JLabel("3,075,000 đồng");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountLabel.setForeground(Color.RED);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel trên cùng - tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  
        titlePanel.setBackground(new Color(240, 240, 240));
         titlePanel.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel titleLabel = new JLabel("📋 Đặt phòng khách lẻ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);
        
        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(new Color(240, 240, 240));
        
        JButton saveBtn = new JButton("💾 Lưu");
        JButton printBtn = new JButton("🖨️ In");
        JButton exitBtn = new JButton("❌ Thoát");
        
        toolbarPanel.add(saveBtn);
        toolbarPanel.add(printBtn);
        toolbarPanel.add(exitBtn);
        
        // Top panel container
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(titlePanel, BorderLayout.NORTH);
        topContainer.add(toolbarPanel, BorderLayout.SOUTH);
        
        // Panel thông tin khách hàng
        JPanel customerInfoPanel = createCustomerInfoPanel();
        
        // Panel danh sách sản phẩm
        JPanel servicePanel = createServicePanel();
        
        // Panel tổng tiền
        JPanel totalPanel = createTotalPanel();
        
        // Sắp xếp layout
        mainPanel.add(topContainer, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(customerInfoPanel, BorderLayout.NORTH);
        centerPanel.add(servicePanel, BorderLayout.CENTER);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createCustomerInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Cột 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Khách hàng:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(customerNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Ngày đặt:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(checkInField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Số người:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(guestsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(noteField, gbc);
        
        // Cột 2
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Ngày trả:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        panel.add(checkOutField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        panel.add(statusComboBox, gbc);
        
        // Cột 3 - Thông tin phòng
        JPanel roomInfoPanel = new JPanel();
        roomInfoPanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm - Dịch vụ"));
        roomInfoPanel.setLayout(new GridLayout(6, 2, 5, 2));
        
        String[] services = {"Coca Cola", "Nước suối", "Redbull", "Fanta", "Cam ép", "Trà Ô Long"};
        String[] prices = {"15000", "12000", "20000", "15000", "15000", "15000"};
        
        for (int i = 0; i < services.length; i++) {
            roomInfoPanel.add(new JLabel("• " + services[i]));
            roomInfoPanel.add(new JLabel(prices[i]));
        }
        
        gbc.gridx = 4; gbc.gridy = 0; gbc.gridheight = 4; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(roomInfoPanel, gbc);
        
        return panel;
    }
    
    private JPanel createServicePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách Sản phẩm - Dịch vụ"));
        
        // Header với thông tin phòng
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(255, 240, 240));
        JLabel roomLabel = new JLabel("Phòng 301 - Đơn giá: 3,000,000 VNĐ");
        roomLabel.setFont(new Font("Arial", Font.BOLD, 12));
        roomLabel.setForeground(Color.RED);
        headerPanel.add(roomLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(serviceTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTotalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("TỔNG THANH TOÁN"));
        
        JPanel totalInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel totalLabel = new JLabel("TỔNG TIỀN:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setForeground(Color.RED);
        
        totalInfoPanel.add(totalLabel);
        totalInfoPanel.add(Box.createHorizontalStrut(20));
        totalInfoPanel.add(totalAmountLabel);
        
        panel.add(totalInfoPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Thêm event handler cho việc thay đổi số lượng trong bảng
        serviceTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 2) { // Cột số lượng
                calculateTotal();
            }
        });
        
        // Thêm event handler cho ComboBox trạng thái
        statusComboBox.addActionListener(e -> {
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            System.out.println("Trạng thái đã chọn: " + selectedStatus);
            // Có thể thêm logic xử lý khác ở đây
        });
    }
    
    private void loadSampleData() {
        // Thêm dữ liệu mẫu vào bảng
        tableModel.addRow(new Object[]{"Phòng 301", "Redbull", 3, "20000", "60000"});
        tableModel.addRow(new Object[]{"Phòng 301", "Cam ép", 1, "15000", "15000"});
        
        // Thêm dòng trống
        for (int i = 0; i < 3; i++) {
            tableModel.addRow(new Object[]{"", "", "", "", ""});
        }
        
        calculateTotal();
    }
    
    private void calculateTotal() {
        long total = 3000000; // Giá phòng cơ bản
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object thanhTienObj = tableModel.getValueAt(i, 4);
            if (thanhTienObj != null && !thanhTienObj.toString().trim().isEmpty()) {
                try {
                    String thanhTienStr = thanhTienObj.toString().replaceAll("[^0-9]", "");
                    if (!thanhTienStr.isEmpty()) {
                        total += Long.parseLong(thanhTienStr);
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid numbers
                }
            }
        }
        
        totalAmountLabel.setText(currencyFormat.format(total) + " đồng");
    }
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            new Payment().setVisible(true);
        });
    }
}
