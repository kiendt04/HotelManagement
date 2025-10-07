package View;

/**
 *
 * @author ASUS
 */
import Control.RoomManagementControl;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
import Model.*;
import java.text.DecimalFormat;

public class RoomTypeManagement extends JFrame {
    
    private JTable roomTypeTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, pricePH,pricePN, guestCountField, bedCountField;
    private JButton addButton, editButton, deleteButton, settingsButton;
    private NumberFormat currencyFormat;
    private ImageIcon addIcon, editIcon, delIcon, setIcon;
    private RoomManagementControl controller;
    
    public RoomTypeManagement() {
        currencyFormat = NumberFormat.getNumberInstance(Locale.US);
        controller = new RoomManagementControl();
        initComponents();
        setupLayout();
        setupEventHandlers();
        this.setVisible(true);
    }
    
    private void initComponents() {
        setTitle("Danh mục Loại phòng");
        setSize(550, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Initialize icons
        addIcon = new ImageIcon(getClass().getResource("/img/add.png"));
        editIcon = new ImageIcon(getClass().getResource("/img/refresh.png"));
        delIcon = new ImageIcon(getClass().getResource("/img/trash.png"));
        setIcon = new ImageIcon(getClass().getResource("/img/settings.png"));
        
        // Create toolbar buttons with icons
        addButton = new JButton();
        addButton.setIcon(addIcon);
        addButton.setToolTipText("Thêm");
        addButton.setPreferredSize(new Dimension(30, 30));
        
        editButton = new JButton();
        editButton.setIcon(editIcon);
        editButton.setToolTipText("Sửa");
        editButton.setPreferredSize(new Dimension(30, 30));
        
        deleteButton = new JButton();
        deleteButton.setIcon(delIcon);
        deleteButton.setToolTipText("Xóa");
        deleteButton.setPreferredSize(new Dimension(30, 30));
        
        settingsButton = new JButton();
        settingsButton.setIcon(setIcon);
        settingsButton.setToolTipText("Thiết lập");
        settingsButton.setPreferredSize(new Dimension(30, 30));
        
        // Create data table
        initializeTable();
        
        // Text fields for information
        nameField = new JTextField();
        nameField.setEnabled(false);
        
        pricePH = new JTextField();
        pricePH.setEnabled(false);
        
        pricePN = new JTextField();
        pricePN.setEnabled(false);
        
        bedCountField = new JTextField();
        bedCountField.setEnabled(false);
    }
    
    private void initializeTable() {
        String[] columnNames = {"STT", "TÊN LOẠI PHÒNG", "SỐ GIƯỜNG", "ĐƠN GIÁ/GIỜ","ĐƠN GIÁ/ĐÊM"};
        
        // Get data from controller
        List<Room_type> roomTypes = controller.getAllRoomTypes();
        Object[][] data = new Object[roomTypes.size()][5];
        
        for (int i = 0; i < roomTypes.size(); i++) {
            Room_type roomType = roomTypes.get(i);
            data[i] = new Object[]{
                roomType.getId(),
                roomType.getName(),
                roomType.getBed(),
                formatDouble(roomType.getPrice_per_hour()),
                formatDouble(roomType.getPrice_per_night())
            };
        }
        
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
        
        // Set column widths
        roomTypeTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        roomTypeTable.getColumnModel().getColumn(0).setMaxWidth(40);
        roomTypeTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        roomTypeTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        roomTypeTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        // Set cell renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        roomTypeTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        roomTypeTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        roomTypeTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Toolbar panel
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        toolbarPanel.add(deleteButton);
        toolbarPanel.add(settingsButton);
        
        add(toolbarPanel, BorderLayout.NORTH);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table panel
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
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Thông tin", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));
        
        // Row 1: Room type name and price
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row1.add(new JLabel("Tên loại phòng:"));
        nameField.setPreferredSize(new Dimension(120, 25));
        row1.add(nameField);
        
        row1.add(Box.createHorizontalStrut(20));
        row1.add(new JLabel("Đơn giá:"));
        pricePH.setPreferredSize(new Dimension(100, 25));
        row1.add(pricePH);
        
        JLabel disabledLabel1 = new JLabel("Disabled");
        disabledLabel1.setForeground(Color.GRAY);
        row1.add(disabledLabel1);
        
        // Row 2: Number of beds
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));        
        row2.add(Box.createHorizontalStrut(35));
        row2.add(new JLabel("Số giường:"));
        bedCountField.setPreferredSize(new Dimension(100, 25));
        row2.add(bedCountField);
        
        infoPanel.add(row1);
        infoPanel.add(row2);
        infoPanel.add(Box.createVerticalStrut(10));
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        //mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Handle table row selection
        roomTypeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = roomTypeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    pricePH.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    bedCountField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                }
            }
        });
        
        // Handle button events
        addButton.addActionListener(e -> addRoomType());
        editButton.addActionListener(e -> editRoomType());
        deleteButton.addActionListener(e -> deleteRoomType());
        settingsButton.addActionListener(e -> showSettings());
    }
    
    private void addRoomType() {
        RoomTypeDialog dialog = new RoomTypeDialog(this, "Thêm loại phòng", true);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Room_type data = dialog.getRoomTypeData();
            int result = controller.insertRoomType(data);
            
            if (result > 0) {
                // Add to table
                tableModel.addRow(new Object[]{
                    data.getId(),
                    data.getName(),
                    data.getBed(),
                    formatDouble(data.getPrice_per_hour()),
                    formatDouble(data.getPrice_per_night())
                });
                
                JOptionPane.showMessageDialog(this, "Thêm loại phòng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm loại phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editRoomType() {
        int selectedRow = roomTypeTable.getSelectedRow();
        if (selectedRow >= 0) {
            Room_type currentData = new Room_type(
                Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()),
                tableModel.getValueAt(selectedRow, 1).toString(),
                Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString()),
                Double.parseDouble(tableModel.getValueAt(selectedRow, 3).toString().replace(",", "")),
                Double.parseDouble(tableModel.getValueAt(selectedRow, 4).toString().replace(",", ""))
            );
            
            RoomTypeDialog dialog = new RoomTypeDialog(this, "Sửa loại phòng", true);
            dialog.setRoomTypeData(currentData);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Room_type data = dialog.getUpdatedData(currentData.getId());
                int result = controller.updateRoomType(data);
                
                if (result > 0) {
                    // Update table
                    tableModel.setValueAt(data.getName(), selectedRow, 1);
                    tableModel.setValueAt(formatDouble(data.getPrice_per_hour()), selectedRow, 3);
                    tableModel.setValueAt(formatDouble(data.getPrice_per_night()), selectedRow, 4);
                    tableModel.setValueAt(data.getBed(), selectedRow, 2);
                    
                    // Update form
                    nameField.setText(data.getName());
                    pricePH.setText(formatDouble(data.getPrice_per_hour()));
                    pricePN.setText(formatDouble(data.getPrice_per_night()));
                    bedCountField.setText(data.getBed() + "");
                    
                    JOptionPane.showMessageDialog(this, "Cập nhật loại phòng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật loại phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
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
                int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                int result = controller.deleteRoomType(id);
                
                if (result > 0) {
                    tableModel.removeRow(selectedRow);
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Xóa loại phòng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa loại phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        pricePH.setText("");
        bedCountField.setText("");
    }
    
    private void showSettings() {
        JOptionPane.showMessageDialog(this, "Chức năng thiết lập đang được phát triển!", "Thiết lập", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Room_type> roomTypes = controller.getAllRoomTypes();
        
        for (Room_type roomType : roomTypes) {
            tableModel.addRow(new Object[]{
                roomType.getId(),
                roomType.getName(),
                roomType.getBed(),
                formatDouble(roomType.getPrice_per_hour()),
                formatDouble(roomType.getPrice_per_night())
            });
        }
    }
    
    // Inner class for add/edit dialog
    private class RoomTypeDialog extends JDialog {
        private JTextField dialogNameField, dialogPricePH, dialogPricePN, dialogBedField;
        private boolean confirmed = false;
        
        public RoomTypeDialog(Frame parent, String title, boolean modal) {
            super(parent, title, modal);
            initDialog();
        }
        
        private void initDialog() {
            setSize(350, 250);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout());
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Room type name
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(new JLabel("Tên loại phòng:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            dialogNameField = new JTextField(15);
            formPanel.add(dialogNameField, gbc);
            
            // Price
            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Đơn giá/giờ:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            dialogPricePH = new JTextField(15);
            formPanel.add(dialogPricePH, gbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
            formPanel.add(new JLabel("Đơn giá/đêm:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            dialogPricePN = new JTextField(15);
            formPanel.add(dialogPricePN, gbc);
            
            // Number of beds
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
                Double.parseDouble(dialogPricePH.getText().trim().replace(",", ""));
                Integer.parseInt(dialogBedField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Đơn giá và số giường phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
        
        public void setRoomTypeData(Room_type data) {
            dialogNameField.setText(data.getName());
            dialogPricePH.setText(formatDouble(data.getPrice_per_hour()));
            dialogPricePN.setText(formatDouble(data.getPrice_per_night()));
            dialogBedField.setText(data.getBed() + "");
        }
        
        public Room_type getUpdatedData(int id) {
            return new Room_type(
                id,
                dialogNameField.getText().trim(),
                Integer.parseInt(dialogBedField.getText().trim()),
                Double.parseDouble(dialogPricePH.getText().trim().replace(",", "")),
                Double.parseDouble(dialogPricePN.getText().trim().replace(",", ""))
            );
        }
        
        public Room_type getRoomTypeData() {
            return new Room_type(
                controller.createNewRoomTypeId(),
                dialogNameField.getText().trim(),
                Integer.parseInt(dialogBedField.getText().trim()),
                Double.parseDouble(dialogPricePH.getText().trim().replace(",", "")),
                Double.parseDouble(dialogPricePN.getText().trim().replace(",", ""))
            );
        }
        
        public boolean isConfirmed() {
            return confirmed;
        }
    }
    
    public static String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("00,000");
        return df.format(value);
    }
    
    public static String formatInt(int value) {
        DecimalFormat df = new DecimalFormat("00,000");
        return df.format(value);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RoomTypeManagement().setVisible(true);
        });
    }
}