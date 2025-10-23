/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Control.RoomManagementControl;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.*;
import java.util.List;

public class RoomManagement extends JFrame {
    
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JButton addButton, editButton, deleteButton;
    private ImageIcon addIcon, editIcon, delIcon, setIcon;
    private RoomManagementControl controller;
    private JComboBox<Floor> floorCbx;
    private JComboBox<Room_type> rtCbx;
    
    public RoomManagement() {
        controller = new RoomManagementControl();
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadTableData();
        this.setVisible(true);
    }
    
    private void initComponents() {
        setTitle("Danh mục Phòng");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
        // Icon
        addIcon = new ImageIcon(getClass().getResource("/img/add.png"));
        editIcon = new ImageIcon(getClass().getResource("/img/refresh.png"));
        delIcon = new ImageIcon(getClass().getResource("/img/trash.png"));
        setIcon = new ImageIcon(getClass().getResource("/img/settings.png"));
        
        // Tạo toolbar buttons với icons
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
        
        // Initialize ComboBoxes
        initializeComboBoxes();
        
        // Initialize table
        initializeTable();
        
        // Text field cho thông tin
        nameField = new JTextField();
        nameField.setEnabled(false);
    }
    
    private void initializeComboBoxes() {
        floorCbx = new JComboBox<>();
        rtCbx = new JComboBox<>();
        
        // Floor ComboBox
        List<Floor> floorList = controller.getAllFloors();
        DefaultComboBoxModel<Floor> floorModel = new DefaultComboBoxModel<>();
        floorModel.addElement(controller.createAllFloorsOption());
        for (Floor floor : floorList) {
            floorModel.addElement(floor);
        }
        floorCbx.setModel(floorModel);
        
        // Room Type ComboBox
        List<Room_type> roomTypeList = controller.getAllRoomTypes();
        DefaultComboBoxModel<Room_type> rtModel = new DefaultComboBoxModel<>();
        rtModel.addElement(controller.createAllRoomTypesOption());
        for (Room_type roomType : roomTypeList) {
            rtModel.addElement(roomType);
        }
        rtCbx.setModel(rtModel);
    }
    
    private void initializeTable() {
        String[] columnNames = controller.getTableColumnNames();
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.getTableHeader().setReorderingAllowed(false);
        
        // Thiết lập độ rộng cột
        roomTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        roomTable.getColumnModel().getColumn(0).setMaxWidth(30);
        roomTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        roomTable.setRowHeight(28);
        roomTable.setGridColor(new Color(230, 230, 230));
        roomTable.setSelectionBackground(new Color(173, 216, 230));
        roomTable.setSelectionForeground(Color.BLACK);
        roomTable.setBackground(Color.WHITE);
        roomTable.getTableHeader().setBackground(new Color(245, 245, 245));
        roomTable.getTableHeader().setForeground(Color.DARK_GRAY);
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
    
    private void loadTableData() {
        Floor selectedFloor = (Floor) floorCbx.getSelectedItem();
        Room_type selectedRoomType = (Room_type) rtCbx.getSelectedItem();
        
        if (selectedFloor != null && selectedRoomType != null) {
            refreshTableData(selectedFloor.getId(), selectedRoomType.getId());
        }
    }
    
    private void refreshTableData(int floorId, int roomTypeId) {
        tableModel.setRowCount(0);
        Object[][] data = controller.getFilteredRoomTableData(floorId, roomTypeId);
        
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbarPanel.setBackground(new Color(245, 247, 250)); // Màu nền sáng nhẹ
        toolbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        toolbarPanel.add(deleteButton);
        toolbarPanel.add(Box.createHorizontalStrut(100));
        toolbarPanel.add(new JLabel("Tầng: "));
        toolbarPanel.add(floorCbx);
        toolbarPanel.add(new JLabel("Loại: "));
        toolbarPanel.add(rtCbx);
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
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
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
        tablePanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(210, 210, 210)), 
        "Danh sách", 
        TitledBorder.LEFT, 
        TitledBorder.TOP,
        new Font("Segoe UI", Font.BOLD, 13)
        ));
        tablePanel.setBackground(Color.WHITE);
        infoPanel.setBackground(Color.WHITE);
        mainPanel.setBackground(new Color(250, 250, 250));
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.add(new JLabel("Tên:"));
        nameField.setPreferredSize(new Dimension(200, 25));
        fieldPanel.add(nameField);
        
        JLabel statusLabel = new JLabel("Disabled");
        statusLabel.setForeground(Color.GRAY);
        fieldPanel.add(statusLabel);
        
        infoPanel.add(fieldPanel, BorderLayout.NORTH);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Xử lý sự kiện chọn hàng trong bảng
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String roomName = (String) tableModel.getValueAt(selectedRow, 1);
                    nameField.setText(roomName);
                }
            }
        });
        
        // Filter event handlers
        floorCbx.addActionListener(e -> loadTableData());
        rtCbx.addActionListener(e -> loadTableData());
        
        // Button event handlers
        addButton.addActionListener(e -> addRoom());
        editButton.addActionListener(e -> editRoom());
        deleteButton.addActionListener(e -> deleteRoom());
    }
    
    private void addRoom() {
        List<Floor> floors = controller.getAllFloors();
        List<Room_type> roomTypes = controller.getAllRoomTypes();
        
        Room newRoom = new RoomDialog(this, floors, roomTypes, null).showDialog();
        if (newRoom != null) {
            int result = controller.insertRoom(newRoom);
            if (result != 0) {
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại");
            }
        }
    }
    
    private void editRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int roomId = (int) tableModel.getValueAt(selectedRow, 0);
            Room roomToEdit = controller.getRoomById(roomId);
            
            if (roomToEdit != null) {
                List<Floor> floors = controller.getAllFloors();
                List<Room_type> roomTypes = controller.getAllRoomTypes();
                
                Room editedRoom = new RoomDialog(this, floors, roomTypes, roomToEdit).showDialog();
                if (editedRoom != null) {
                    int result = controller.updateRoom(editedRoom);
                    if (result != 0) {
                        JOptionPane.showMessageDialog(this, "Sửa thành công");
                        loadTableData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Sửa thất bại");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int roomId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa phòng này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                int result = controller.deleteRoom(roomId);
                if (result != 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    loadTableData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    public class RoomDialog extends JDialog {
        private JTextField txtRoomNum;
        private JComboBox<Floor> cbxFloor;
        private JComboBox<Room_type> cbxType;
        private JComboBox<String> cbxStatus;
        private JTextArea txtNote;
        private Room originalRoom = null;
        private Room result = null;

        public RoomDialog(Frame parent, List<Floor> floorList, List<Room_type> typeList, Room roomToEdit) {
            super(parent, true);
            setTitle(roomToEdit == null ? "Thêm phòng" : "Cập nhật phòng");
            this.originalRoom = roomToEdit;

            initComponents(floorList, typeList);
            
            if (roomToEdit != null) {
                loadRoomData(roomToEdit);
            }
            
            pack();
            setLocationRelativeTo(parent);
        }

        private void initComponents(List<Floor> floors, List<Room_type> types) {
            setLayout(new BorderLayout(10, 10));

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            txtRoomNum = new JTextField(15);
            cbxFloor = new JComboBox<>(floors.toArray(new Floor[0]));
            cbxType = new JComboBox<>(types.toArray(new Room_type[0]));
            cbxStatus = new JComboBox<>(controller.getStatusOptions());
            txtNote = new JTextArea(3, 20);
            JScrollPane noteScrollPane = new JScrollPane(txtNote);

            formPanel.add(createRow("Số phòng:", txtRoomNum));
            formPanel.add(createRow("Tầng:", cbxFloor));
            formPanel.add(createRow("Loại phòng:", cbxType));
            formPanel.add(createRow("Trạng thái:", cbxStatus));
            formPanel.add(createRow("Ghi chú:", noteScrollPane));

            JButton btnSave = new JButton("Lưu");
            JButton btnCancel = new JButton("Hủy");

            btnSave.addActionListener(e -> onSave());
            btnCancel.addActionListener(e -> dispose());

            JPanel buttons = new JPanel();
            buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttons.add(btnSave);
            buttons.add(btnCancel);

            add(formPanel, BorderLayout.CENTER);
            add(buttons, BorderLayout.SOUTH);
        }

        private JPanel createRow(String labelText, Component field) {
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            JLabel label = new JLabel(labelText);
            label.setPreferredSize(new Dimension(100, 25));
            panel.add(label, BorderLayout.WEST);
            panel.add(field, BorderLayout.CENTER);
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            return panel;
        }

        private void loadRoomData(Room room) {
            txtRoomNum.setText(room.getNum());
            
            for (int i = 0; i < cbxFloor.getItemCount(); i++) {
                if (cbxFloor.getItemAt(i).getId() == room.getFloor()) {
                    cbxFloor.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < cbxType.getItemCount(); i++) {
                if (cbxType.getItemAt(i).getId() == room.getType()) {
                    cbxType.setSelectedIndex(i);
                    break;
                }
            }
            
            cbxStatus.setSelectedIndex(room.getStatus());
            txtNote.setText(room.getNote());
        }
        
        private void onSave() {
            String num = txtRoomNum.getText().trim();
            Floor selectedFloor = (Floor) cbxFloor.getSelectedItem();
            
            if (num.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Số phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (selectedFloor == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate room number format (only for new rooms)
            if (originalRoom == null && !controller.validateRoomNumber(num, selectedFloor.getId())) {
                JOptionPane.showMessageDialog(this, "Số phòng phải bắt đầu theo số tầng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if room number already exists (only for new rooms)
            if (originalRoom == null && controller.isRoomNumberExists(num)) {
                JOptionPane.showMessageDialog(this, "Số phòng đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int floor = selectedFloor.getId();
            int type = ((Room_type) cbxType.getSelectedItem()).getId();
            int status = cbxStatus.getSelectedItem().equals("Trống") ? 0 : (cbxStatus.getSelectedItem().equals("Sử dụng") ? 1 : -1);
            String note = txtNote.getText().trim();
            int id = originalRoom == null ? 0 : originalRoom.getId();
            Room_type choosen = (Room_type) cbxType.getSelectedItem();
            result = new Room(id, num, floor, type, status, note,choosen.getPrice_per_hour(),choosen.getPrice_per_night());
            dispose();
        }

        public Room showDialog() {
            setVisible(true);
            return result;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RoomManagement().setVisible(true));
    }
}