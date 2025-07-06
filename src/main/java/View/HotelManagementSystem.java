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
    private int role,id;
    
    public HotelManagementSystem() {
        initComponents();
    }
    
    public HotelManagementSystem(int role,int id)
    {
        this.id = id;
        this.role = role;
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
        
        JMenuItem qlyUser = new JMenuItem("Qly USer");
        JMenuItem doimk = new JMenuItem("Đổi mật khẩu");
        JMenuItem thoat = new JMenuItem("Thoat");
        JMenuItem doanhthu = new JMenuItem("Doanh thu");
        // Menu Hệ thống
        qlyUser.addActionListener(e -> {
            if(role == 1)
            {
                new UserList();
            }
            else 
            {
                JOptionPane.showMessageDialog(rootPane, "khong co quyen truy cap");
            }
        });
        doimk.addActionListener(e -> {new ChangePass(id);});
        thoat.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception a) {
                a.printStackTrace();
            }
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
            this.dispose();
        });
        JMenu heThongMenu = new JMenu("Hệ thống");
        if(role == 1)
        {
            heThongMenu.add(qlyUser);
        }
            heThongMenu.add(doimk); heThongMenu.addSeparator(); heThongMenu.add(thoat);
        
        // Menu Báo cáo
        JMenu baoCaoMenu = new JMenu("Báo cáo");
        baoCaoMenu.add(doanhthu);
        
        menuBar.add(heThongMenu);
        if(role == 1)
        {menuBar.add(baoCaoMenu);}
            
        
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
        "Đặt phòng theo đoàn",
        "Lịch sử đặt"
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
            int selected = menuList.getSelectedIndex();
            switch (selected) {
                case 0:
                    if (role == 1)
                    {
                        new CustomerList();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Quyền truy cập bị giới hạn");
                    }
                    break;
                case 1:
                    if (role == 1)
                    {
                        new FloorManagement();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Quyền truy cập bị giới hạn");
                    }
                    break;
                    // TODO: xử lý quản lý phòng
                case 2:
                    if (role == 1)
                    {
                        new RoomTypeManagement();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Quyền truy cập bị giới hạn");
                    }
                    // TODO: xử lý sản phẩm dịch vụ
                    break;
                 case 3:
                    if (role == 1)
                    {
                        new RoomManagement();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Quyền truy cập bị giới hạn");
                    }
                case 5:
                    
                    break;
                    // TODO: xử lý đặt phòng theo đoàn
            }
        }
    });

    JScrollPane scrollPane = new JScrollPane(menuList);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    panel.add(scrollPane);
    panel.add(Box.createVerticalStrut(20));

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
                SwingUtilities.invokeLater(() -> {
            new Payment(r.getId()).setVisible(true);
        });
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