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
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import Control.*;
import Model.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HotelManagementSystem extends JFrame {
    
    private FloorControl flc = new FloorControl();
    private RoomControl rc = new RoomControl();
    
    public HotelManagementSystem() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("PHẦN MỀM QUẢN LÝ KHÁCH SẠN - Administrator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        // Tạo Menu Bar
        createMenuBar();
        
        // Layout chính
        setLayout(new BorderLayout());
        
        // Panel trái - Menu
        JPanel leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);
        
        // Panel phải - Hiển thị phòng
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Hệ thống
        JMenu heThongMenu = new JMenu("Hệ thống");
        heThongMenu.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                JOptionPane.showMessageDialog(HotelManagementSystem.this, "Chức năng Hệ thống");
            }
            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {}
            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });
        
        // Menu Báo cáo
        JMenu baoCaoMenu = new JMenu("Báo cáo");
        baoCaoMenu.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                JOptionPane.showMessageDialog(HotelManagementSystem.this, "Chức năng Báo cáo");
            }
            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {}
            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });
        
        // Menu Thoát
        JMenu thoatMenu = new JMenu("Thoát");
        thoatMenu.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                int result = JOptionPane.showConfirmDialog(HotelManagementSystem.this, 
                    "Bạn có chắc chắn muốn thoát?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {}
            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });
        
        menuBar.add(heThongMenu);
        menuBar.add(baoCaoMenu);
        menuBar.add(thoatMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createLeftPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setPreferredSize(new Dimension(200, 0));
    panel.setBackground(new Color(240, 240, 240));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel lblDanhMuc = new JLabel("Danh mục");
    lblDanhMuc.setFont(new Font("Arial", Font.BOLD, 14));
    panel.add(lblDanhMuc);
    panel.add(Box.createVerticalStrut(10));

    String[] menuItems = {
        "Khách hàng",
        "Quản lý tầng",
        "Loại phòng",
        "Quản lý phòng",
        "Sản phẩm - Dịch vụ",
        "Thiết bị",
        "Phòng - Thiết bị",
        "Đặt phòng theo đoàn"
    };

    JList<String> menuList = new JList<>(menuItems);
    menuList.setFont(new Font("Arial", Font.PLAIN, 13));
    menuList.setBackground(Color.WHITE);
    menuList.setSelectionBackground(new Color(200, 230, 255));
    menuList.setFixedCellHeight(30);
    menuList.setVisibleRowCount(menuItems.length);
    menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    menuList.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Xử lý sự kiện khi chọn mục trong danh sách
    menuList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            String selected = menuList.getSelectedValue();
            switch (selected) {
                case "Khách hàng":
                    new CustomerList();
                    break;
                case "Quản lý tầng":
                    new FloorManagement();
                    break;
                case "Loại phòng":
                    new RoomTypeManagement();
                    break;
                case "Quản lý phòng":
                    // TODO: xử lý quản lý phòng
                    break;
                case "Sản phẩm - Dịch vụ":
                    // TODO: xử lý sản phẩm dịch vụ
                    break;
                case "Thiết bị":
                    // TODO: xử lý thiết bị
                    break;
                case "Phòng - Thiết bị":
                    // TODO: xử lý phòng - thiết bị
                    break;
                case "Đặt phòng theo đoàn":
                    // TODO: xử lý đặt phòng theo đoàn
                    break;
            }
        }
    });

    JScrollPane scrollPane = new JScrollPane(menuList);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    panel.add(scrollPane);
    panel.add(Box.createVerticalStrut(20));

    // Hệ thống
    JLabel lblHeThong = new JLabel("Hệ thống");
    lblHeThong.setFont(new Font("Arial", Font.BOLD, 14));
    panel.add(lblHeThong);
    panel.add(Box.createVerticalStrut(10));

    JButton btnDoiMK = new JButton("Đổi mật khẩu");
    btnDoiMK.setAlignmentX(Component.LEFT_ALIGNMENT);
    btnDoiMK.setBorderPainted(false);
    btnDoiMK.setContentAreaFilled(false);
    btnDoiMK.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(btnDoiMK);

    JButton btnQuanTri = new JButton("Quản trị người dùng");
    btnQuanTri.setAlignmentX(Component.LEFT_ALIGNMENT);
    btnQuanTri.setBorderPainted(false);
    btnQuanTri.setContentAreaFilled(false);
    btnQuanTri.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(btnQuanTri);

    return panel;
}

    
    private JPanel createRightPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("All groups"));
        
        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));
        roomPanel.setBackground(Color.WHITE);
        
        int floorCount = flc.countFloor();
        
        // Tạo 4 tầng
        for (int i = 0; i < floorCount; i++) {
            JPanel floorPanel = createFloorPanel(i+1);
            roomPanel.add(floorPanel);
            roomPanel.add(Box.createVerticalStrut(20));
        }
        
        JScrollPane scrollPane = new JScrollPane(roomPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createFloorPanel(int floor) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Tầng " + floor));
        panel.setBackground(Color.WHITE);
        
        List<Room> roomLst = rc.getByFloor(floor);
        
        int roomCount = roomLst.size();
        panel.setLayout(new GridLayout(1, roomCount, 10, 10));
        
        for (int i = 0; i < roomCount; i++) {
            JPanel roomPanel = createSingleRoomPanel(roomLst.get(i));
            panel.add(roomPanel);
        }
        
        return panel;
    }
    
    private JPanel createSingleRoomPanel(Room r) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(80, 100));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Icon
        JLabel iconLabel = new JLabel("🏠");
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        
        // Màu theo trạng thái (mặc định xanh = trống)
        if (r.getStatus() == 1) {
            iconLabel.setBackground(Color.PINK); // Đỏ = đã thuê
        } else {
            iconLabel.setBackground(Color.CYAN); // Xanh = trống
        }
        
        // Tên phòng
        JLabel nameLabel = new JLabel("Phòng " + r.getNum());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        
        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(nameLabel, BorderLayout.SOUTH);
        
        // Sự kiện click
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                new RoomBooking();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });
        
        return panel;
    }
    
    public static void main(String[] args) {
        new HotelManagementSystem();
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new HotelManagementSystem();
//            }
//        });
    }
}