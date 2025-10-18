package View;
import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

// Cửa sổ chuyển phòng
public class RoomTransfer extends JDialog {
    
    // Method main để chạy trực tiếp từ RoomTransferWindow
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new RoomTransfer("Phòng 107", "1,500,000").setVisible(true);
        });
    }
    private String currentRoom;
    private String currentPrice;
    private JComboBox<String> roomComboBox;
    
    public RoomTransfer(String roomName, String price) {
        this.currentRoom = roomName;
        this.currentPrice = price;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Chuyển phòng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tiêu đề
        JLabel titleLabel = new JLabel("Chọn phòng chuyển đến");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel thông tin phòng hiện tại
        JPanel currentRoomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        currentRoomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel currentLabel = new JLabel("Phòng hiện tại: ");
        JLabel roomPriceLabel = new JLabel(currentRoom + " - Đơn giá: " + currentPrice);
        roomPriceLabel.setForeground(Color.BLUE);
        
        currentRoomPanel.add(currentLabel);
        currentRoomPanel.add(roomPriceLabel);
        
        // Panel chọn phòng chuyển đến
        JPanel transferPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        transferPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel transferLabel = new JLabel("Phòng chuyển đến: ");
        
        // ComboBox danh sách phòng
        String[] rooms = {
            "Phòng 101 - 3,500,000",
            "Phòng 102 - 3,000,000", 
            "Phòng 103 - 2,500,000",
            "Phòng 201 - 2,500,000",
            "Phòng 301 - 3,000,000",
            "Phòng 302 - 2,500,000",
            "Phòng 401 - 2,500,000",
            "Phòng 303 - 2,500,000",
            "Phòng 203 - 3,500,000",
            "Phòng 104 - 3,500,000",
            "Phòng 402 - 1,500,000",
            "Phòng 403 - 1,500,000",
            "Phòng 404 - 1,500,000"
        };
        
        roomComboBox = new JComboBox<>(rooms);
        roomComboBox.setPreferredSize(new Dimension(200, 25));
        
        // Thêm listener để hiển thị RoomListWindow khi click vào ComboBox
        roomComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                // Mở RoomListWindow khi click vào ComboBox và truyền tham chiếu
                SwingUtilities.invokeLater(() -> {
                    new RoomListTransfer(RoomTransfer.this).setVisible(true);
                });
            }
            
            @Override
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
                // Không làm gì khi đóng popup
            }
            
            @Override
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
                // Không làm gì khi hủy popup
            }
        });
        
        transferPanel.add(transferLabel);
        transferPanel.add(roomComboBox);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton transferButton = new JButton("Chuyển phòng");
        transferButton.setIcon(createArrowIcon());
        
        buttonPanel.add(transferButton);
        
        // Thêm các component
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(currentRoomPanel);
        mainPanel.add(transferPanel);
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
        
        // Event handler
        transferButton.addActionListener(e -> {
            String selectedRoom = (String) roomComboBox.getSelectedItem();
            if (selectedRoom != null) {
                JOptionPane.showMessageDialog(this, 
                    "Chuyển từ " + currentRoom + " đến " + selectedRoom.split(" - ")[0],
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Đóng cửa sổ hiện tại và mở RoomListWindow
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new RoomListTransfer().setVisible(true);
                });
            }
        });
    }
    
    // Tạo icon mũi tên đơn giản
    private Icon createArrowIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 120, 0));
                
                // Vẽ mũi tên đơn giản
                int[] xPoints = {x + 2, x + 8, x + 8, x + 12, x + 8, x + 8, x + 2};
                int[] yPoints = {y + 6, y + 6, y + 3, y + 8, y + 13, y + 10, y + 10};
                g2.fillPolygon(xPoints, yPoints, 7);
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() { return 14; }
            
            @Override
            public int getIconHeight() { return 16;             }
        };
    }
    
    // Method để cập nhật phòng chuyển đến từ RoomListWindow
    public void setDestinationRoom(String roomName, String price) {
        // Cập nhật ComboBox với phòng được chọn
        String roomItem = roomName + " - " + price;
        
        // Tìm và chọn item trong ComboBox
        for (int i = 0; i < roomComboBox.getItemCount(); i++) {
            if (roomComboBox.getItemAt(i).startsWith(roomName)) {
                roomComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        // Hiển thị thông báo xác nhận
        JOptionPane.showMessageDialog(this, 
            "Đã chọn phòng chuyển đến: " + roomName,
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}