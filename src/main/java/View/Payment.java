/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author ASUS
 */
import Control.ServiceControl;
import Control.myconnect;
import Model.Service;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Payment extends JFrame {
    private JTextField customerNameField;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JTextField totalService;
    private JTextField totalRoom;
    private JComboBox<String> statusComboBox;
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JLabel totalAmountLabel;
    private NumberFormat currencyFormat;
    
    public Payment() {
        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Đặt phòng");
    }
    
    private void initializeComponents() {
        // Khởi tạo các text field
        customerNameField = new JTextField("Đặng Trần Anh");
        checkInField = new JTextField("01/09/2021");
        checkOutField = new JTextField("02/09/2021");
        totalRoom = new JTextField("6000000");
        totalService = new JTextField("100000");
        // Khởi tạo ComboBox cho trạng thái
        String[] statusOptions = {"Chưa hoàn tất", "Hoàn tất", "Chưa thanh toán"};
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
        
        totalAmountLabel = new JLabel("3,075,000 đồng");
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
        
        JButton saveBtn = new JButton("💾 Lưu");
        JButton printBtn = new JButton("🖨️ In");
        JButton exitBtn = new JButton("❌ Thoát");
        
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
    
    private JPanel createCustomerInfoPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
    panel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    // Hàng 0
    gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
    panel.add(new JLabel("Khách hàng:"), gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
    panel.add(customerNameField, gbc);

    gbc.gridx = 2; gbc.weightx = 0;
    panel.add(new JLabel("Trạng thái:"), gbc);
    gbc.gridx = 3; gbc.weightx = 1.0;
    panel.add(statusComboBox, gbc);

   
    gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
    panel.add(new JLabel("Ngày đặt:"), gbc);
    gbc.gridx = 1; gbc.weightx = 1.0;
    panel.add(checkInField, gbc);

    
    gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
    panel.add(new JLabel("Ngày trả:"), gbc);
    gbc.gridx = 1; gbc.weightx = 1.0;
    panel.add(checkOutField, gbc);


    gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
    panel.add(new JLabel("Tiền dịch vụ:"), gbc);
    gbc.gridx = 3; gbc.weightx = 1.0;
    panel.add(totalService, gbc);


    gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
    panel.add(new JLabel("Tiền Phòng:"), gbc);
    gbc.gridx = 3; gbc.weightx = 1.0;
    panel.add(totalRoom, gbc);
    
    gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1;
    panel.add(new JLabel("Tổng thanh toán:"), gbc);
    gbc.gridx = 1; gbc.weightx = 1.0;
    panel.add(totalAmountLabel, gbc);

    
    
        
    // Dịch vụ (JList) bên phải
    JPanel roomInfoPanel = new JPanel(new BorderLayout());
    roomInfoPanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm - Dịch vụ"));

    ServiceControl serviceControl = new ServiceControl();
    ArrayList<Service> serviceList = (ArrayList<Service>) serviceControl.getAll();  

    DefaultListModel<String> listModel = new DefaultListModel<>();
    for (Service s : serviceList) {
        listModel.addElement(s.toString());
    }

    JList<String> serviceJList = new JList<>(listModel);
    serviceJList.setFont(new Font("Arial", Font.PLAIN, 14));
    serviceJList.setFixedCellHeight(26);
    serviceJList.addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        String selected = serviceJList.getSelectedValue();
        if (selected == null || selected.trim().isEmpty()) return;

        String[] parts = selected.split(" - ");
        if (parts.length < 2) return;

        String tenDV = parts[0].trim();
        long donGia = Long.parseLong(parts[1].replaceAll("[^0-9]", "")); 

        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String tenTable = (String) tableModel.getValueAt(i, 0);
            if (tenDV.equalsIgnoreCase(tenTable)) {
                int currentSL = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                currentSL++;
                tableModel.setValueAt(currentSL, i, 1);
                tableModel.setValueAt(donGia * currentSL, i, 3); 
                found = true;
                break;
            }
        }

        if (!found) {
            // Thêm mới dịch vụ vào bảng
            tableModel.addRow(new Object[]{tenDV, 1, donGia, donGia});
        }

        calculateTotal();
    }
});


    JScrollPane scrollPane = new JScrollPane(serviceJList);
    roomInfoPanel.add(scrollPane, BorderLayout.CENTER);

    gbc.gridx = 4;
    gbc.gridy = 0;
    gbc.gridheight = 5;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.5;
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
        tableModel.addRow(new Object[]{"Redbull", 3, "20000", "60000"});
        tableModel.addRow(new Object[]{ "Cam ép", 1, "15000", "15000"});
        
        // Thêm dòng trống
        for (int i = 0; i < 3; i++) {
            tableModel.addRow(new Object[]{ "", "", "", ""});
        }
        
        calculateTotal();
    }
    
    private void calculateTotal() {
        long total = 3000000; // Giá phòng cơ bản
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object thanhTienObj = tableModel.getValueAt(i, 3);
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
