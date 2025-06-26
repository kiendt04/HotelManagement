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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class RoomTypeManagement extends JFrame {
    
    private JTable roomTypeTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, priceField, guestCountField, bedCountField;
    private JButton addButton, editButton, deleteButton, settingsButton;
    private NumberFormat currencyFormat;
    
    public RoomTypeManagement() {
        currencyFormat = NumberFormat.getNumberInstance(Locale.US);
        initComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initComponents() {
        setTitle("Danh mục Loại phòng");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Tạo toolbar buttons với icons
        addButton = new JButton();
        addButton.setIcon(createColoredIcon(Color.GREEN, 16, 16, "+"));
        addButton.setToolTipText("Thêm");
        addButton.setPreferredSize(new Dimension(30, 30));
        
        editButton = new JButton();
        editButton.setIcon(createColoredIcon(Color.BLUE, 16, 16, "✎"));
        editButton.setToolTipText("Sửa");
        editButton.setPreferredSize(new Dimension(30, 30));
        
        deleteButton = new JButton();
        deleteButton.setIcon(createColoredIcon(Color.RED, 16, 16, "✕"));
        deleteButton.setToolTipText("Xóa");
        deleteButton.setPreferredSize(new Dimension(30, 30));
        
        settingsButton = new JButton();
        settingsButton.setIcon(createColoredIcon(Color.GRAY, 16, 16, "⚙"));
        settingsButton.setToolTipText("Thiết lập");
        settingsButton.setPreferredSize(new Dimension(30, 30));
        
        // Tạo bảng dữ liệu
        String[] columnNames = {"STT", "TÊN LOẠI PHÒNG", "ĐƠN GIÁ", "SỐ NGƯỜI", "SỐ GIƯỜNG"};
        Object[][] data = {
            {"1", "VIP 1", "3500000", "2", "2"},
            {"2", "VIP 2", "3000000", "2", "2"},
            {"3", "Loại 1", "2500000", "2", "2"},
            {"4", "Loại 2", "2000000", "1", "1"},
            {"5", "Loại 2", "1500000", "4", "2"}
        };
        
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return String.class; // Đơn giá
                return String.class;
            }
        };
        
        roomTypeTable = new JTable(tableModel);
        roomTypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTypeTable.getTableHeader().setReorderingAllowed(false);
        roomTypeTable.setRowHeight(25);
        
        // Thiết lập độ rộng cột
        roomTypeTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        roomTypeTable.getColumnModel().getColumn(0).setMaxWidth(40);
        roomTypeTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        roomTypeTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        roomTypeTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        roomTypeTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        // Căn giữa cho các cột số
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        roomTypeTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        roomTypeTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        roomTypeTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        // Căn phải cho cột đơn giá
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        roomTypeTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        
        // Text fields cho thông tin
        nameField = new JTextField();
        nameField.setEnabled(false);
        
        priceField = new JTextField();
        priceField.setEnabled(false);
        
        guestCountField = new JTextField();
        guestCountField.setEnabled(false);
        
        bedCountField = new JTextField();
        bedCountField.setEnabled(false);
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
        
        JScrollPane scrollPane = new JScrollPane(roomTypeTable);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel thông tin
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Thông tin", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));
        
        // Hàng 1: Tên loại phòng và Đơn giá
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row1.add(new JLabel("Tên loại phòng:"));
        nameField.setPreferredSize(new Dimension(120, 25));
        row1.add(nameField);
        
        row1.add(Box.createHorizontalStrut(20));
        row1.add(new JLabel("Đơn giá:"));
        priceField.setPreferredSize(new Dimension(100, 25));
        row1.add(priceField);
        
        JLabel disabledLabel1 = new JLabel("Disabled");
        disabledLabel1.setForeground(Color.GRAY);
        row1.add(disabledLabel1);
        
        // Hàng 2: Số người và Số giường
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.add(new JLabel("Số người:"));
        guestCountField.setPreferredSize(new Dimension(120, 25));
        row2.add(guestCountField);
        
        row2.add(Box.createHorizontalStrut(35));
        row2.add(new JLabel("Số giường:"));
        bedCountField.setPreferredSize(new Dimension(100, 25));
        row2.add(bedCountField);
        
        infoPanel.add(row1);
        infoPanel.add(row2);
        infoPanel.add(Box.createVerticalStrut(10));
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Xử lý sự kiện chọn hàng trong bảng
        roomTypeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = roomTypeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    priceField.setText((String) tableModel.getValueAt(selectedRow, 2));
                    guestCountField.setText((String) tableModel.getValueAt(selectedRow, 3));
                    bedCountField.setText((String) tableModel.getValueAt(selectedRow, 4));
                }
            }
        });
        
        // Xử lý sự kiện các nút
        addButton.addActionListener(e -> addRoomType());
        editButton.addActionListener(e -> editRoomType());
        deleteButton.addActionListener(e -> deleteRoomType());
        settingsButton.addActionListener(e -> showSettings());
    }
    
    private Icon createColoredIcon(Color color, int width, int height, String symbol) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(color);
                g2d.fillRoundRect(x + 1, y + 1, width - 2, height - 2, 4, 4);
                
                g2d.setColor(Color.WHITE);
                Font font = new Font("Arial", Font.BOLD, 10);
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = x + (width - fm.stringWidth(symbol)) / 2;
                int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(symbol, textX, textY);
                
                g2d.dispose();
            }
            
            @Override
            public int getIconWidth() { return width; }
            
            @Override
            public int getIconHeight() { return height; }
        };
    }
    
    private void addRoomType() {
        RoomTypeDialog dialog = new RoomTypeDialog(this, "Thêm loại phòng", true);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            RoomTypeData data = dialog.getRoomTypeData();
            int rowCount = tableModel.getRowCount();
            tableModel.addRow(new Object[]{
                String.valueOf(rowCount + 1),
                data.name,
                data.price,
                data.guestCount,
                data.bedCount
            });
        }
    }
    
    private void editRoomType() {
        int selectedRow = roomTypeTable.getSelectedRow();
        if (selectedRow >= 0) {
            RoomTypeData currentData = new RoomTypeData(
                (String) tableModel.getValueAt(selectedRow, 1),
                (String) tableModel.getValueAt(selectedRow, 2),
                (String) tableModel.getValueAt(selectedRow, 3),
                (String) tableModel.getValueAt(selectedRow, 4)
            );
            
            RoomTypeDialog dialog = new RoomTypeDialog(this, "Sửa loại phòng", true);
            dialog.setRoomTypeData(currentData);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                RoomTypeData data = dialog.getRoomTypeData();
                tableModel.setValueAt(data.name, selectedRow, 1);
                tableModel.setValueAt(data.price, selectedRow, 2);
                tableModel.setValueAt(data.guestCount, selectedRow, 3);
                tableModel.setValueAt(data.bedCount, selectedRow, 4);
                
                // Cập nhật form
                nameField.setText(data.name);
                priceField.setText(data.price);
                guestCountField.setText(data.guestCount);
                bedCountField.setText(data.bedCount);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteRoomType() {
        int selectedRow = roomTypeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa loại phòng này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                clearFields();
                // Cập nhật lại STT
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt(String.valueOf(i + 1), i, 0);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        guestCountField.setText("");
        bedCountField.setText("");
    }
    
    private void showSettings() {
        JOptionPane.showMessageDialog(this, "Chức năng thiết lập đang được phát triển!", "Thiết lập", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Inner class cho dialog thêm/sửa
    private class RoomTypeDialog extends JDialog {
        private JTextField dialogNameField, dialogPriceField, dialogGuestField, dialogBedField;
        private boolean confirmed = false;
        
        public RoomTypeDialog(Frame parent, String title, boolean modal) {
            super(parent, title, modal);
            initDialog();
        }
        
        private void initDialog() {
            setSize(350, 200);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout());
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Tên loại phòng
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(new JLabel("Tên loại phòng:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            dialogNameField = new JTextField(15);
            formPanel.add(dialogNameField, gbc);
            
            // Đơn giá
            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Đơn giá:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            dialogPriceField = new JTextField(15);
            formPanel.add(dialogPriceField, gbc);
            
            // Số người
            gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Số người:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            dialogGuestField = new JTextField(15);
            formPanel.add(dialogGuestField, gbc);
            
            // Số giường
            gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Số giường:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            dialogBedField = new JTextField(15);
            formPanel.add(dialogBedField, gbc);
            
            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Hủy");
            
            okButton.addActionListener(e -> {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            });
            
            cancelButton.addActionListener(e -> dispose());
            
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            
            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        private boolean validateInput() {
            if (dialogNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                Integer.parseInt(dialogPriceField.getText().trim());
                Integer.parseInt(dialogGuestField.getText().trim());
                Integer.parseInt(dialogBedField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Đơn giá, số người và số giường phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
        
        public void setRoomTypeData(RoomTypeData data) {
            dialogNameField.setText(data.name);
            dialogPriceField.setText(data.price);
            dialogGuestField.setText(data.guestCount);
            dialogBedField.setText(data.bedCount);
        }
        
        public RoomTypeData getRoomTypeData() {
            return new RoomTypeData(
                dialogNameField.getText().trim(),
                dialogPriceField.getText().trim(),
                dialogGuestField.getText().trim(),
                dialogBedField.getText().trim()
            );
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
    }
    
    // Data class
    private class RoomTypeData {
        String name, price, guestCount, bedCount;
        
        public RoomTypeData(String name, String price, String guestCount, String bedCount) {
            this.name = name;
            this.price = price;
            this.guestCount = guestCount;
            this.bedCount = bedCount;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RoomTypeManagement().setVisible(true);
        });
    }
}
