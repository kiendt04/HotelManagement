/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author ASUS
 */
import Model.Room;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import Control.GroupBookingControl;
import com.toedter.calendar.JDateChooser;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGroupBooking extends JDialog {
    private JTree roomTree;
    private JTable roomListTable, serviceTable;
    private DefaultTableModel roomTableModel, serviceTableModel;
    private JComboBox<String> customerCombo;
    private JTextField dateFromField, dateToField, guestCountField, noteField;
    private JTextField totalAmountField;
    private JLabel totalLabel;
    private GroupBookingControl control = new GroupBookingControl();
    private Timestamp time_in, time_out;
    private JDateChooser check_in,check_out;
    
    public AddGroupBooking(Frame parent) {
        super(parent, "Quản lý đặt phòng theo đoàn", true);
        initializeComponents();
        setupLayout();
        setupTables();
        loadSampleData();
        setVisible(true);
    }
    public AddGroupBooking() {
        setTitle("Quản lý đặt phòng theo đoàn");
        initializeComponents();
        setupLayout();
        setupTables();
        loadSampleData();
        setVisible(true);
    }
    
    private void initializeComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        
        // Initialize components
        customerCombo = new JComboBox<>(new String[]{"Nguyễn Minh Triết", "Lê Thị Thắm", "Hoàng Anh Tuấn"});
        dateFromField = new JTextField();
        dateToField = new JTextField();
        guestCountField = new JTextField();
        noteField = new JTextField();
        totalAmountField = new JTextField();
        totalAmountField.setEditable(false);
        totalLabel = new JLabel("TỔNG TIỀN");
        check_in = new JDateChooser();
        check_in.setDateFormatString("dd/MM/yyyy HH:mm:ss");
        check_out = new JDateChooser();
        check_out.setDateFormatString("dd/MM/yyyy HH:MM:ss");
        control.initDate(check_in,check_out);
        time_in = new Timestamp(check_in.getDate().getTime());
        time_out = new Timestamp(check_out.getDate().getTime());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create toolbar
        JPanel toolbar = createToolbar();
        add(toolbar, BorderLayout.NORTH);
        
        // Create main split pane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(350);
        
        // Left panel - Room tree
        JPanel leftPanel = createLeftPanel();
        mainSplitPane.setLeftComponent(leftPanel);
        
        // Right panel - Room booking details
        JPanel rightPanel = createRightPanel();
        mainSplitPane.setRightComponent(rightPanel);
        
        add(mainSplitPane, BorderLayout.CENTER);
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(new Color(240, 240, 240));
        toolbar.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JButton saveBtn = createToolbarButton("Lưu", "💾");
        JButton helpBtn = createToolbarButton("Bộ quả", "❓");
        
        toolbar.add(saveBtn);
        toolbar.add(helpBtn);
        
        // Add menu items
        JLabel menuLabel1 = new JLabel("Danh sách");
        JLabel menuLabel2 = new JLabel("Chi tiết");
        menuLabel1.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuLabel2.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuLabel1.setOpaque(true);
        menuLabel1.setBackground(Color.WHITE);
        
        toolbar.add(Box.createHorizontalStrut(20));
//        toolbar.add(menuLabel1);
//        toolbar.add(menuLabel2);
        
        return toolbar;
    }
    
    private JButton createToolbarButton(String tooltip, String icon) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(32, 32));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        return button;
    }
    
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Danh sách phòng trống"));
        leftPanel.setBackground(new Color(230, 230, 250));
        List<Room> roomAvailable = control.getRoomavailable(time_in, time_out);
        List<String> roomPrice = control.getRoomPrice();
        // Create room tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("TẦNG");
        
        Map<Integer,List<Room>> roombyFloor = new HashMap<>();
        for (Room r : roomAvailable) {
        int floor = r.getFloor(); // giả sử Room có getFloor()
    
        // Nếu tầng chưa có list, tạo mới
        roombyFloor.computeIfAbsent(floor, k -> new ArrayList<>()).add(r);
        }
        for (Map.Entry<Integer, List<Room>> entry : roombyFloor.entrySet()) {
        int floor = entry.getKey();
        List<Room> rooms = entry.getValue();

        String floorLabel = "TẦNG: Tầng " + floor + " (" + rooms.size() + " phòng trống)";
        DefaultMutableTreeNode floorNode = new DefaultMutableTreeNode(floorLabel);

        for (Room r : rooms) {
            // Node phòng: hiển thị tên + giá (hoặc tùy chỉnh)
            String roomLabel = "Phòng " + r.getNum() +  roomPrice.get(r.getType());
            DefaultMutableTreeNode roomNode = new DefaultMutableTreeNode(roomLabel);
            roomNode.setUserObject(r); // gắn Room thật vào node
            floorNode.add(roomNode);
        }

        root.add(floorNode);
    }
        roomTree = new JTree(new DefaultTreeModel(root));       
        roomTree.setRootVisible(false);
        roomTree.setShowsRootHandles(true);
        
        // Expand all nodes
        for (int i = 0; i < roomTree.getRowCount(); i++) {
            roomTree.expandRow(i);
        }
        
        JScrollPane treeScrollPane = new JScrollPane(roomTree);
        treeScrollPane.setPreferredSize(new Dimension(330, 500));
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        
        return leftPanel;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Top section - Customer info
        JPanel customerPanel = createCustomerInfoPanel();
        rightPanel.add(customerPanel, BorderLayout.NORTH);
        
        // Middle section - Tables
        JPanel tablesPanel = createTablesPanel();
        rightPanel.add(tablesPanel, BorderLayout.CENTER);
        
        // Bottom section - Total
        JPanel totalPanel = createTotalPanel();
        rightPanel.add(totalPanel, BorderLayout.SOUTH);
        
        return rightPanel;
    }
    
    private JPanel createCustomerInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 255));
        
        // Customer row
        JPanel customerRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerRow.setOpaque(false);
        customerRow.add(new JLabel("Khách hàng"));
        customerCombo.setPreferredSize(new Dimension(200, 25));
        customerRow.add(customerCombo);
        customerRow.add(new JButton("🔍"));
        
        // Date and guest info row
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateRow.setOpaque(false);
        dateRow.add(new JLabel("Ngày đặt"));
        dateFromField.setPreferredSize(new Dimension(80, 25));
        //dateRow.add(dateFromField);
        dateRow.add(check_in);
        
        dateRow.add(Box.createHorizontalStrut(10));
        dateRow.add(new JLabel("Ngày trả"));
        dateToField.setPreferredSize(new Dimension(80, 25));
        //dateRow.add(dateToField);
        dateRow.add(check_out);
        
        // Guest count and note row
        JPanel guestRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        guestRow.setOpaque(false);
//        guestRow.add(new JLabel("Số người"));
//        guestCountField.setPreferredSize(new Dimension(50, 25));
//        guestRow.add(guestCountField);
        
//        guestRow.add(Box.createHorizontalStrut(10));
//        guestRow.add(new JLabel("Trạng thái"));
//        
        
        guestRow.add(Box.createHorizontalStrut(10));
        guestRow.add(new JLabel("Ghi chú"));
        noteField.setPreferredSize(new Dimension(120, 25));
        guestRow.add(noteField);
        
        panel.add(customerRow);
        panel.add(dateRow);
        panel.add(guestRow);
        
        return panel;
    }
    
    private JPanel createTablesPanel() {
        JPanel tablesPanel = new JPanel(new BorderLayout());
        
        // Split pane for two tables
        JSplitPane tablesSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        tablesSplitPane.setDividerLocation(200);
        
        // Top table - Room list
        JPanel roomListPanel = new JPanel(new BorderLayout());
        roomListPanel.setBorder(BorderFactory.createTitledBorder("Danh sách phòng đặt"));
        
        String[] roomColumns = {"TÊN PHÒNG", "ĐƠN GIÁ"};
        roomTableModel = new DefaultTableModel(roomColumns, 0);
        roomListTable = new JTable(roomTableModel);
        roomListTable.setRowHeight(20);
        roomListTable.setFont(new Font("Tahoma", Font.PLAIN, 11));
        
        JScrollPane roomScrollPane = new JScrollPane(roomListTable);
        roomScrollPane.setPreferredSize(new Dimension(400, 150));
        roomListPanel.add(roomScrollPane, BorderLayout.CENTER);
        
        // Service table on right
        JPanel servicePanel = new JPanel(new BorderLayout());
        servicePanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm - Dịch vụ"));
        servicePanel.setBackground(new Color(255, 250, 240));
        
        String[] serviceColumns = {"TÊN SP - DV", "ĐƠN GIÁ"};
        serviceTableModel = new DefaultTableModel(serviceColumns, 0);
        serviceTable = new JTable(serviceTableModel);
        serviceTable.setRowHeight(20);
        serviceTable.setFont(new Font("Tahoma", Font.PLAIN, 11));
        serviceTable.setBackground(new Color(255, 250, 240));
        
        // Add sample service data
        serviceTableModel.addRow(new Object[]{"Coca Cola", "15000"});
        serviceTableModel.addRow(new Object[]{"Nước suối", "12000"});
        serviceTableModel.addRow(new Object[]{"RedBull", "20000"});
        serviceTableModel.addRow(new Object[]{"Fanta", "15000"});
        serviceTableModel.addRow(new Object[]{"Cam ép", "15000"});
        serviceTableModel.addRow(new Object[]{"Trà Ô Long", "15000"});
        
        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);
        serviceScrollPane.setPreferredSize(new Dimension(200, 150));
        servicePanel.add(serviceScrollPane, BorderLayout.CENTER);
        
        // Create horizontal split for room list and services
        JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        horizontalSplit.setLeftComponent(roomListPanel);
        horizontalSplit.setRightComponent(servicePanel);
        horizontalSplit.setDividerLocation(500);
        
        tablesSplitPane.setTopComponent(horizontalSplit);
        
        // Bottom table - Service details
        JPanel serviceDetailPanel = new JPanel(new BorderLayout());
        serviceDetailPanel.setBorder(BorderFactory.createTitledBorder("Danh sách Sản phẩm - Dịch vụ"));
        
        String[] serviceDetailColumns = {"PHÒNG", "TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        DefaultTableModel serviceDetailModel = new DefaultTableModel(serviceDetailColumns, 0);
        JTable serviceDetailTable = new JTable(serviceDetailModel);
        serviceDetailTable.setRowHeight(20);
        serviceDetailTable.setFont(new Font("Tahoma", Font.PLAIN, 11));
        
        JScrollPane serviceDetailScrollPane = new JScrollPane(serviceDetailTable);
        serviceDetailScrollPane.setPreferredSize(new Dimension(700, 150));
        serviceDetailPanel.add(serviceDetailScrollPane, BorderLayout.CENTER);
        
        tablesSplitPane.setBottomComponent(serviceDetailPanel);
        
        tablesPanel.add(tablesSplitPane, BorderLayout.CENTER);
        
        return tablesPanel;
    }
    
    private JPanel createTotalPanel() {
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.setBackground(new Color(220, 220, 220));
        totalPanel.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel totalTitleLabel = new JLabel("TỔNG THANH TOÁN");
        totalTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        totalTitleLabel.setForeground(Color.RED);
        
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        totalLabel.setForeground(Color.RED);
        
        totalAmountField.setPreferredSize(new Dimension(100, 30));
        totalAmountField.setFont(new Font("Tahoma", Font.BOLD, 14));
        totalAmountField.setForeground(Color.RED);
        totalAmountField.setHorizontalAlignment(JTextField.CENTER);
        totalAmountField.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JLabel dongLabel = new JLabel("đồng");
        dongLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        dongLabel.setForeground(Color.RED);
        
        totalPanel.add(totalTitleLabel);
        totalPanel.add(Box.createHorizontalStrut(50));
        totalPanel.add(totalLabel);
        totalPanel.add(totalAmountField);
        totalPanel.add(dongLabel);
        
        return totalPanel;
    }
    
    private void setupTables() {
        // Setup table headers
        JTableHeader roomHeader = roomListTable.getTableHeader();
        roomHeader.setBackground(new Color(240, 240, 240));
        roomHeader.setFont(new Font("Tahoma", Font.BOLD, 11));
        
        JTableHeader serviceHeader = serviceTable.getTableHeader();
        serviceHeader.setBackground(new Color(240, 240, 240));
        serviceHeader.setFont(new Font("Tahoma", Font.BOLD, 11));
    }
    
    private void loadSampleData() {
        // No initial room bookings
        totalAmountField.setText("0");
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new AddGroupBooking();
        });
    }
}
