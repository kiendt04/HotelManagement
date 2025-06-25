///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package View;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.DefaultTableCellRenderer;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//
//public class RoomBooking extends JFrame {
//    
//    private JButton saveButton, printButton, settingsButton;
//    private JTextField customerField, noteField;
//    private JDateChooser checkInDate, checkOutDate;
//    private JSpinner guestSpinner;
//    private JComboBox<String> statusComboBox;
//    private JTable serviceTable, totalTable;
//    private DefaultTableModel serviceTableModel, totalTableModel;
//    private JLabel roomLabel, priceLabel, totalAmountLabel;
//    private NumberFormat currencyFormat;
//    
//    // Dữ liệu mẫu dịch vụ
//    private String[][] serviceData = {
//        {"Coca Cola", "15000"},
//        {"Nước suối", "5000"},
//        {"Redbull", "20000"},
//        {"Fanta", "15000"},
//        {"Cam ép", "15000"},
//        {"Trà Ô Long", "18000"}
//    };
//    
//    public RoomBooking() {
//        currencyFormat = NumberFormat.getNumberInstance(Locale.US);
//        initComponents();
//        setupLayout();
//        setupEventHandlers();
//        loadInitialData();
//    }
//    
//    private void initComponents() {
//        setTitle("Đặt phòng khách lẻ");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(800, 600);
//        setLocationRelativeTo(null);
//        
//        // Toolbar buttons
//        saveButton = new JButton();
//        saveButton.setIcon(createIcon(Color.GREEN, "💾"));
//        saveButton.setToolTipText("Lưu");
//        saveButton.setPreferredSize(new Dimension(30, 30));
//        
//        printButton = new JButton();
//        printButton.setIcon(createIcon(Color.BLUE, "🖨"));
//        printButton.setToolTipText("In");
//        printButton.setPreferredSize(new Dimension(30, 30));
//        
//        settingsButton = new JButton();
//        settingsButton.setIcon(createIcon(Color.GRAY, "⚙"));
//        settingsButton.setToolTipText("Thiết lập");
//        settingsButton.setPreferredSize(new Dimension(30, 30));
//        
//        // Form fields
//        customerField = new JTextField();
//        noteField = new JTextField();
//        checkInDate = new JDateChooser(new Date());
//        checkOutDate = new JDateChooser(new Date());
//        guestSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
//        statusComboBox = new JComboBox<>(new String[]{"Chưa hoàn tất", "Đã hoàn tất", "Đã hủy"});
//        
//        // Labels
//        roomLabel = new JLabel("Phòng 301");
//        roomLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        roomLabel.setForeground(Color.RED);
//        
//        priceLabel = new JLabel("Đơn giá: 3,000,000 VND");
//        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        priceLabel.setForeground(Color.RED);
//        
//        totalAmountLabel = new JLabel("3,075,000 đồng");
//        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        totalAmountLabel.setForeground(Color.RED);
//        
//        // Service table
//        String[] serviceColumns = {"TÊN SP - DV", "ĐƠN GIÁ"};
//        DefaultTableModel serviceModel = new DefaultTableModel(serviceColumns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        
//        // Add service data
//        for (String[] service : serviceData) {
//            serviceModel.addRow(service);
//        }
//        
//        JTable serviceListTable = new JTable(serviceModel);
//        serviceListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        serviceListTable.setRowHeight(20);
//        
//        // Service usage table
//        String[] usageColumns = {"PHÒNG", "TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
//        serviceTableModel = new DefaultTableModel(usageColumns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 2; // Only quantity editable
//            }
//        };
//        
//        serviceTable = new JTable(serviceTableModel);
//        serviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        serviceTable.setRowHeight(25);
//        
//        // Add sample data
//        serviceTableModel.addRow(new Object[]{"Phòng 301", "Redbull", "3", "20000", "60000"});
//        serviceTableModel.addRow(new Object[]{"Phòng 301", "Cam ép", "1", "15000", "15000"});
//        
//        // Total table
//        String[] totalColumns = {"", "", "", "", ""};
//        totalTableModel = new DefaultTableModel(totalColumns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        
//        totalTable = new JTable(totalTableModel);
//        totalTable.setRowHeight(25);
//        totalTable.setShowGrid(false);
//        totalTableModel.addRow(new Object[]{"", "", "4", "", "75,000"});
//        
//        // Setup table renderers
//        setupTableRenderers();
//    }
//    
//    private void setupTableRenderers() {
//        // Center renderer
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        
//        // Right renderer
//        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
//        
//        // Service table
//        serviceTable.getColumnModel().getColumn(0).setPreferredWidth(80);
//        serviceTable.getColumnModel().getColumn(1).setPreferredWidth(100);
//        serviceTable.getColumnModel().getColumn(2).setPreferredWidth(40);
//        serviceTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
//        serviceTable.getColumnModel().getColumn(3).setPreferredWidth(80);
//        serviceTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
//        serviceTable.getColumnModel().getColumn(4).setPreferredWidth(80);
//        serviceTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
//        
//        // Total table
//        totalTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
//        totalTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
//    }
//    
//    private void setupLayout() {
//        setLayout(new BorderLayout());
//        
//        // Toolbar
//        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        toolbarPanel.add(saveButton);
//        toolbarPanel.add(printButton);
//        toolbarPanel.add(settingsButton);
//        add(toolbarPanel, BorderLayout.NORTH);
//        
//        // Main panel
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        
//        // Left panel
//        JPanel leftPanel = new JPanel(new BorderLayout());
//        leftPanel.setPreferredSize(new Dimension(480, 0));
//        
//        // Customer info panel
//        JPanel customerPanel = new JPanel();
//        customerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
//        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
//        
//        // Room and price info
//        JPanel roomInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        roomInfoPanel.add(roomLabel);
//        roomInfoPanel.add(Box.createHorizontalStrut(20));
//        roomInfoPanel.add(priceLabel);
//        customerPanel.add(roomInfoPanel);
//        
//        // Form fields
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.anchor = GridBagConstraints.WEST;
//        
//        // Row 1
//        gbc.gridx = 0; gbc.gridy = 0;
//        formPanel.add(new JLabel("Khách hàng:"), gbc);
//        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
//        customerField.setPreferredSize(new Dimension(120, 25));
//        formPanel.add(customerField, gbc);
//        
//        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
//        formPanel.add(new JLabel("Đặng Tấn Anh"), gbc);
//        
//        gbc.gridx = 3;
//        formPanel.add(new JLabel("Ngày trả:"), gbc);
//        gbc.gridx = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
//        checkOutDate.setPreferredSize(new Dimension(100, 25));
//        formPanel.add(checkOutDate, gbc);
//        
//        // Row 2
//        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
//        formPanel.add(new JLabel("Ngày đặt:"), gbc);
//        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
//        checkInDate.setPreferredSize(new Dimension(120, 25));
//        formPanel.add(checkInDate, gbc);
//        
//        gbc.gridx = 3; gbc.fill = GridBagConstraints.NONE;
//        formPanel.add(new JLabel("Trạng thái:"), gbc);
//        gbc.gridx = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
//        statusComboBox.setPreferredSize(new Dimension(100, 25));
//        formPanel.add(statusComboBox, gbc);
//        
//        // Row 3
//        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
//        formPanel.add(new JLabel("Số người:"), gbc);
//        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
//        guestSpinner.setPreferredSize(new Dimension(120, 25));
//        formPanel.add(guestSpinner, gbc);
//        
//        // Row 4
//        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
//        formPanel.add(new JLabel("Ghi chú:"), gbc);
//        gbc.gridx = 1; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
//        noteField.setPreferredSize(new Dimension(300, 25));
//        formPanel.add(noteField, gbc);
//        
//        customerPanel.add(formPanel);
//        
//        // Service usage panel
//        JPanel serviceUsagePanel = new JPanel(new BorderLayout());
//        serviceUsagePanel.setBorder(BorderFactory.createTitledBorder("Danh sách Sản phẩm - Dịch vụ"));
//        
//        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);
//        serviceScrollPane.setPreferredSize(new Dimension(0, 120));
//        serviceUsagePanel.add(serviceScrollPane, BorderLayout.CENTER);
//        
//        JScrollPane totalScrollPane = new JScrollPane(totalTable);
//        totalScrollPane.setPreferredSize(new Dimension(0, 50));
//        serviceUsagePanel.add(totalScrollPane, BorderLayout.SOUTH);
//        
//        // Total panel
//        JPanel totalPanel = new JPanel(new BorderLayout());
//        totalPanel.setBorder(BorderFactory.createTitledBorder("TỔNG THÀNH TOÁN"));
//        totalPanel.setPreferredSize(new Dimension(0, 80));
//        
//        JPanel totalAmountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        totalAmountPanel.add(new JLabel("TỔNG TIỀN"));
//        totalAmountPanel.add(Box.createHorizontalStrut(20));
//        totalAmountPanel.add(totalAmountLabel);
//        totalPanel.add(totalAmountPanel, BorderLayout.CENTER);
//        
//        leftPanel.add(customerPanel, BorderLayout.NORTH);
//        leftPanel.add(serviceUsagePanel, BorderLayout.CENTER);
//        leftPanel.add(totalPanel, BorderLayout.SOUTH);
//        
//        // Right panel - Service list
//        JPanel rightPanel = new JPanel(new BorderLayout());
//        rightPanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm - Dịch vụ"));
//        
//        // Service list table
//        String[] serviceColumns = {"TÊN SP - DV", "ĐƠN GIÁ"};
//        DefaultTableModel serviceModel = new DefaultTableModel(serviceColumns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        
//        // Add service data
//        for (String[] service : serviceData) {
//            serviceModel.addRow(service);
//        }
//        
//        JTable serviceListTable = new JTable(serviceModel);
//        serviceListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        serviceListTable.setRowHeight(25);
//        
//        // Setup service list table
//        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
//        serviceListTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
//        
//        JScrollPane serviceListScrollPane = new JScrollPane(serviceListTable);
//        rightPanel.add(serviceListScrollPane, BorderLayout.CENTER);
//        
//        mainPanel.add(leftPanel, BorderLayout.CENTER);
//        mainPanel.add(rightPanel, BorderLayout.EAST);
//        
//        add(mainPanel, BorderLayout.CENTER);
//    }
//    
//    private void setupEventHandlers() {
//        saveButton.addActionListener(e -> saveBooking());
//        printButton.addActionListener(e -> printBooking());
//        settingsButton.addActionListener(e -> showSettings());
//    }
//    
//    private void loadInitialData() {
//        // Set initial dates
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        checkInDate.setDate(new Date());
//        checkOutDate.setDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // Tomorrow
//        
//        customerField.setText("Khách lẻ");
//        statusComboBox.setSelectedIndex(0);
//    }
//    
//    private Icon createIcon(Color color, String symbol) {
//        return new Icon() {
//            @Override
//            public void paintIcon(Component c, Graphics g, int x, int y) {
//                Graphics2D g2d = (Graphics2D) g.create();
//                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                
//                g2d.setColor(color);
//                g2d.fillRoundRect(x + 1, y + 1, 14, 14, 4, 4);
//                
//                g2d.setColor(Color.WHITE);
//                Font font = new Font("Arial", Font.BOLD, 8);
//                g2d.setFont(font);
//                FontMetrics fm = g2d.getFontMetrics();
//                int textX = x + (16 - fm.stringWidth(symbol)) / 2;
//                int textY = y + (16 - fm.getHeight()) / 2 + fm.getAscent();
//                g2d.drawString(symbol, textX, textY);
//                
//                g2d.dispose();
//            }
//            
//            @Override
//            public int getIconWidth() { return 16; }
//            
//            @Override
//            public int getIconHeight() { return 16; }
//        };
//    }
//    
//    private void saveBooking() {
//        JOptionPane.showMessageDialog(this, "Đã lưu thông tin đặt phòng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//    }
//    
//    private void printBooking() {
//        JOptionPane.showMessageDialog(this, "Chức năng in đang được phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//    }
//    
//    private void showSettings() {
//        JOptionPane.showMessageDialog(this, "Chức năng thiết lập đang được phát triển!", "Thiết lập", JOptionPane.INFORMATION_MESSAGE);
//    }
//    
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new RoomBooking().setVisible(true);
//        });
//    }
//}
