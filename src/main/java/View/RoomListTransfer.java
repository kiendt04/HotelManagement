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
import java.awt.*;
import java.text.DecimalFormat;

// Cửa sổ danh sách phòng chính
public class RoomListTransfer extends JFrame {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private RoomTransfer parentWindow; // Tham chiếu đến cửa sổ cha
    
    // Constructor mặc định (cho việc chạy độc lập)
    public RoomListTransfer() {
        this(null);
    }
    
    // Constructor nhận tham chiếu từ RoomTransferWindow
    public RoomListTransfer(RoomTransfer parent) {
        this.parentWindow = parent;
        initComponents();
        setupData();
    }
    
    private void initComponents() {
        setTitle("Danh sách phòng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton findButton = new JButton("Find");
        searchPanel.add(searchField);
        searchPanel.add(findButton);
        
        // Tạo bảng
        String[] columnNames = {"TÊN PHÒNG", "ĐƠN GIÁ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        roomTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        
        // Thêm listener cho double click
        roomTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = roomTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String roomName = (String) tableModel.getValueAt(selectedRow, 0);
                        String price = (String) tableModel.getValueAt(selectedRow, 1);
                        
                        // Nếu có cửa sổ cha, cập nhật phòng chuyển đến
                        if (parentWindow != null) {
                            //parentWindow.setDestinationRoom(roomName, price);
                            dispose(); // Đóng cửa sổ danh sách
                        } else {
                            // Nếu chạy độc lập, mở cửa sổ chuyển phòng mới
                            openTransferWindow(roomName, price);
                        }
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(clearButton);
        
        // Thêm các component vào panel chính
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event handlers
        clearButton.addActionListener(e -> {
            searchField.setText("");
            roomTable.clearSelection();
        });
        
        findButton.addActionListener(e -> {
            String searchText = searchField.getText().toLowerCase();
            if (!searchText.isEmpty()) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String roomName = ((String) tableModel.getValueAt(i, 0)).toLowerCase();
                    if (roomName.contains(searchText)) {
                        roomTable.setRowSelectionInterval(i, i);
                        roomTable.scrollRectToVisible(roomTable.getCellRect(i, 0, true));
                        break;
                    }
                }
            }
        });
    }
    
    private void setupData() {
        DecimalFormat df = new DecimalFormat("#,###");
        
        // Dữ liệu phòng giống như trong ảnh
        Object[][] roomData = {
            {"Phòng 101", "3500000"},
            {"Phòng 102", "3000000"},
            {"Phòng 103", "2500000"},
            {"Phòng 201", "2500000"},
            {"Phòng 301", "3000000"},
            {"Phòng 302", "2500000"},
            {"Phòng 401", "2500000"},
            {"Phòng 303", "2500000"},
            {"Phòng 203", "3500000"},
            {"Phòng 104", "3500000"},
            {"Phòng 402", "1500000"},
            {"Phòng 403", "1500000"},
            {"Phòng 404", "1500000"}
        };
        
        for (Object[] row : roomData) {
            long price = Long.parseLong((String) row[1]);
            tableModel.addRow(new Object[]{row[0], df.format(price)});
        }
    }
    
    private void openTransferWindow(String roomName, String price) {
        //new RoomTransfer(roomName, price).setVisible(true);
    }
}
