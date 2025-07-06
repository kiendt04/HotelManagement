/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Control.*;
import Model.*;
import java.util.List;

public class RoomManagement extends JFrame {
    
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JButton addButton, editButton, deleteButton;
    private ImageIcon addIcon,editIcon,delIcon,setIcon;
    private FloorControl flc = new FloorControl();
    private Room_typeControl rtc = new Room_typeControl();
    private RoomControl rc = new RoomControl();
    private JComboBox<Floor> floorCbx;
    private JComboBox<Room_type> rtCbx;
    
    public RoomManagement() {
        initComponents();
        setupLayout();
        setupEventHandlers();
        this.setVisible(true);
    }
    
    private void initComponents() {
        setTitle("Danh mục Phòng");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        //Icon
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
        
        floorCbx = new JComboBox<>();
        rtCbx = new JComboBox<>();
        
        // Tạo bảng dữ liệu
        List<Floor> flList = flc.getAll();
        DefaultComboBoxModel<Floor> floorModel = new DefaultComboBoxModel<>();
        floorModel.addElement(new Floor(0,"All"));
        for (int i=0;i<flList.size();i++)
        {
            floorModel.addElement(flList.get(i));
        }
        floorCbx.setModel(floorModel);
        
        List<Room_type> rtList = rtc.getAll();
        DefaultComboBoxModel<Room_type> rtModel = new DefaultComboBoxModel<>();
        rtModel.addElement(new Room_type(0,"All",0,0));
        for(int i=0;i<rtList.size();i++)
        {
            rtModel.addElement(rtList.get(i));
        }
        rtCbx.setModel(rtModel);
        
        int countRoom = rc.count();
        String[] columnNames = {"STT", "Phòng","Tầng","Loại","Trạng thái"};
        List<Room> roomList = rc.getAll();
        Object[][] data = new Object[countRoom][5];
        for (int i=0;i<roomList.size();i++)
        {
            String type = "";
            String floor = "Tầng " + roomList.get(i).getFloor();
            switch (roomList.get(i).getType()) {
                case 1:
                    type = "Đơn thường";
                    break;
                case 2:
                    type = "Đôi thường";
                    break;
                case 3:
                    type = "Đơn VIP";
                    break;
                case 4:
                    type = "Đôi VIP";
                    break;
                case 5:
                    type = "Vvip";
                    break;
                
            }
            data[i] = new Object[]{roomList.get(i).getId(),roomList.get(i).getNum(),floor,type,roomList.get(i).getStatus() == 0 ? "Trống": (roomList.get(i).getStatus() == 1 ? "Sử dụng" : "Dọn dẹp")};
        }
        
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };
       
        
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.getTableHeader().setReorderingAllowed(false);
        
        // Thiết lập độ rộng cột
        roomTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        roomTable.getColumnModel().getColumn(0).setMaxWidth(30);
        roomTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        
        // Text field cho thông tin
        nameField = new JTextField();
        nameField.setEnabled(false); // Disabled như trong hình
    }
    
    private void setDatatbl(DefaultTableModel model,int floor,int type)
    {
        model.setRowCount(0);
        int countRoom = rc.count_filter(floor,type);
        String[] columnNames = {"STT", "Phòng","Tầng","Loại","Trạng thái"};
        List<Room> roomList = rc.filter(floor, type);
        Object[][] data = new Object[countRoom][5];
        for (int i=0;i<roomList.size();i++)
        {
            String type_s = "";
            String floor_s = "Tầng " + roomList.get(i).getFloor();
            switch (roomList.get(i).getType()) {
                case 1:
                    type_s = "Đơn thường";
                    break;
                case 3:
                    type_s = "Đôi thường";
                    break;
                case 2:
                    type_s = "Đơn VIP";
                    break;
                case 4:
                    type_s = "Đôi VIP";
                    break;
                case 5:
                    type_s = "Vvip";
                    break;
                
            }
            model.addRow(new Object[]{roomList.get(i).getId(),roomList.get(i).getNum(),floor_s,type_s,roomList.get(i).getStatus() == 0 ? "Trống": (roomList.get(i).getStatus() == 1 ? "Sử dụng" : "Dọn dẹp")});
        }
        
        
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        toolbarPanel.add(deleteButton);
        toolbarPanel.add(Box.createHorizontalStrut(100));
        toolbarPanel.add(new JLabel("Tầng: ")); toolbarPanel.add(floorCbx);
        toolbarPanel.add(new JLabel("Loại: ")); toolbarPanel.add(rtCbx);
        add(toolbarPanel, BorderLayout.NORTH);
        
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel filter = new JPanel();
        
        
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
        
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.add(new JLabel("Tên:"));
        nameField.setPreferredSize(new Dimension(200, 25));
        fieldPanel.add(nameField);
        
        JLabel statusLabel = new JLabel("Disabled");
        statusLabel.setForeground(Color.GRAY);
        fieldPanel.add(statusLabel);
        
        infoPanel.add(fieldPanel, BorderLayout.NORTH);
        
        //mainPanel.add(filter, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Xử lý sự kiện chọn hàng trong bảng
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String floorName = (String) tableModel.getValueAt(selectedRow, 1);
                    nameField.setText(floorName);
                }
            }
        });
        
        floorCbx.addActionListener(e -> 
        {
            Floor flr = (Floor) floorCbx.getModel().getSelectedItem();
            Room_type rt = (Room_type) rtCbx.getModel().getSelectedItem();
            setDatatbl(tableModel, flr.getId(), rt.getId());
        });
        
        rtCbx.addActionListener(e -> 
        {
            Floor flr = (Floor) floorCbx.getModel().getSelectedItem();
            Room_type rt = (Room_type) rtCbx.getModel().getSelectedItem();
            setDatatbl(tableModel, flr.getId(), rt.getId());
        });
        
        // Xử lý sự kiện các nút
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoom();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRoom();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoom();
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
    
    private void addRoom() {
        Room addRoom = new RoomDialog(this, flc.getAll(), rtc.getAll(), null).showDialog();
        if(rc.insertRoom(addRoom) != 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Thêm thành công");
            Floor flr = (Floor) floorCbx.getModel().getSelectedItem();
            Room_type rt = (Room_type) rtCbx.getModel().getSelectedItem();
            setDatatbl(tableModel, flr.getId(), rt.getId());
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "Thêm thất bại");
        }
    }
    
    private void editRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int idRoom = (int) tableModel.getValueAt(selectedRow, 0);
            
            Room editroom = new RoomDialog(this, flc.getAll(), rtc.getAll(), rc.getbyID(idRoom)).showDialog();
            if(rc.uptRoom(editroom) != 0)
            {
                JOptionPane.showMessageDialog(rootPane, "Sửa thành công");
                Floor flr = (Floor) floorCbx.getModel().getSelectedItem();
                Room_type rt = (Room_type) rtCbx.getModel().getSelectedItem();
                setDatatbl(tableModel, flr.getId(), rt.getId());
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "Sửa thất bại");
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa phòng này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && rc.delRoom(id) != 0) {
                tableModel.removeRow(selectedRow);
                Floor flr = (Floor) floorCbx.getModel().getSelectedItem();
                Room_type rt = (Room_type) rtCbx.getModel().getSelectedItem();
                setDatatbl(tableModel, flr.getId(), rt.getId());
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
    private Room input = null;

    private Room result = null;

    public RoomDialog(Frame parent, List<Floor> floorList, List<Room_type> typeList, Room roomToEdit) {
        super(parent, true);
        setTitle(roomToEdit == null ? "Thêm phòng" : "Cập nhật phòng");

        initComponents(floorList, typeList);
        
        if (roomToEdit != null) {
            input = roomToEdit;
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
        cbxStatus = new JComboBox<>(new String[]{"Trống", "Đã thuê", "Bảo trì"});
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
    
    private boolean checkNum(String num,int fl)
    {
        if(Integer.parseInt(num) / 100 != fl) return false;
        return true;
    }
    
    private void onSave() {
        String num = txtRoomNum.getText().trim();
        Floor fl = (Floor) cbxFloor.getModel().getSelectedItem();
        if (num.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!checkNum(num,fl.getId()) && input==null)
        {
            JOptionPane.showMessageDialog(this, "Số phòng phải bắt đầu theo số tầng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!rc.checkNum(num) && input == null)
        {
            JOptionPane.showMessageDialog(this, "Số phòng đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int floor = ((Floor) cbxFloor.getSelectedItem()).getId();
        int type = ((Room_type) cbxType.getSelectedItem()).getId();
        int status = cbxStatus.getSelectedIndex();
        String note = txtNote.getText().trim();
        int id = input == null ? 0 : input.getId();
        result = new Room(id, num, floor, type, status, note);
        dispose();
    }

    public Room showDialog() {
        setVisible(true);
        return result;
    }
}


    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RoomManagement().setVisible(true);
            }
        });
    }
}

