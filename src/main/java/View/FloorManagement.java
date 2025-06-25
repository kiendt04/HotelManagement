package View;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FloorManagement extends JFrame {
    
    private JTable floorTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JButton addButton, editButton, deleteButton, settingsButton;
    
    public FloorManagement() {
        initComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initComponents() {
        setTitle("Danh mục Tầng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Tạo toolbar buttons với icons
        addButton = new JButton();
        addButton.setIcon(createColoredIcon(Color.GREEN, 16, 16));
        addButton.setToolTipText("Thêm");
        addButton.setPreferredSize(new Dimension(30, 30));
        
        editButton = new JButton();
        editButton.setIcon(createColoredIcon(Color.BLUE, 16, 16));
        editButton.setToolTipText("Sửa");
        editButton.setPreferredSize(new Dimension(30, 30));
        
        deleteButton = new JButton();
        deleteButton.setIcon(createColoredIcon(Color.RED, 16, 16));
        deleteButton.setToolTipText("Xóa");
        deleteButton.setPreferredSize(new Dimension(30, 30));
        
        settingsButton = new JButton();
        settingsButton.setIcon(createColoredIcon(Color.GRAY, 16, 16));
        settingsButton.setToolTipText("Thiết lập");
        settingsButton.setPreferredSize(new Dimension(30, 30));
        
        // Tạo bảng dữ liệu
        String[] columnNames = {"STT", "TÊN TẦNG"};
        Object[][] data = {
            {"1", "Tầng 1"},
            {"2", "Tầng 2"},
            {"3", "Tầng 3"},
            {"4", "Tầng 4"},
            {"5", "Tầng 5"}
        };
        
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };
        
        floorTable = new JTable(tableModel);
        floorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        floorTable.getTableHeader().setReorderingAllowed(false);
        
        // Thiết lập độ rộng cột
        floorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        floorTable.getColumnModel().getColumn(0).setMaxWidth(50);
        floorTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        
        // Text field cho thông tin
        nameField = new JTextField();
        nameField.setEnabled(false); // Disabled như trong hình
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        toolbarPanel.add(deleteButton);
        toolbarPanel.add(settingsButton);
        
        add(toolbarPanel, BorderLayout.NORTH);
        
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel bảng dữ liệu
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Danh sách", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));
        
        JScrollPane scrollPane = new JScrollPane(floorTable);
        scrollPane.setPreferredSize(new Dimension(350, 180));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel thông tin
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Thông tin", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));
        
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.add(new JLabel("Tên:"));
        nameField.setPreferredSize(new Dimension(200, 25));
        fieldPanel.add(nameField);
        
        JLabel statusLabel = new JLabel("Disabled");
        statusLabel.setForeground(Color.GRAY);
        fieldPanel.add(statusLabel);
        
        infoPanel.add(fieldPanel, BorderLayout.NORTH);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Xử lý sự kiện chọn hàng trong bảng
        floorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = floorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String floorName = (String) tableModel.getValueAt(selectedRow, 1);
                    nameField.setText(floorName);
                }
            }
        });
        
        // Xử lý sự kiện các nút
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFloor();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFloor();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFloor();
            }
        });
        
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSettings();
            }
        });
    }
    
    private Icon createColoredIcon(Color color, int width, int height) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillOval(x + 2, y + 2, width - 4, height - 4);
                g.setColor(Color.WHITE);
                g.drawString("+", x + width/2 - 3, y + height/2 + 3);
            }
            
            @Override
            public int getIconWidth() { return width; }
            
            @Override
            public int getIconHeight() { return height; }
        };
    }
    
    private void addFloor() {
        String floorName = JOptionPane.showInputDialog(this, "Nhập tên tầng:", "Thêm tầng", JOptionPane.PLAIN_MESSAGE);
        if (floorName != null && !floorName.trim().isEmpty()) {
            int rowCount = tableModel.getRowCount();
            tableModel.addRow(new Object[]{String.valueOf(rowCount + 1), floorName.trim()});
        }
    }
    
    private void editFloor() {
        int selectedRow = floorTable.getSelectedRow();
        if (selectedRow >= 0) {
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);
            String newName = JOptionPane.showInputDialog(this, "Sửa tên tầng:", currentName);
            if (newName != null && !newName.trim().isEmpty()) {
                tableModel.setValueAt(newName.trim(), selectedRow, 1);
                nameField.setText(newName.trim());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteFloor() {
        int selectedRow = floorTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa tầng này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                nameField.setText("");
                // Cập nhật lại STT
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt(String.valueOf(i + 1), i, 0);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showSettings() {
        JOptionPane.showMessageDialog(this, "Chức năng thiết lập đang được phát triển!", "Thiết lập", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FloorManagement().setVisible(true);
            }
        });
    }
}
