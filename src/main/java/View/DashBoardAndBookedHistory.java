/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author ADMIN
 */
import javax.swing.*;
import java.awt.*;

public class DashBoardAndBookedHistory extends JDialog {

    private JButton btnPrint;
    private JCheckBox chkDon,chkNhom;
    private JLabel lblNam ,lblThang ,lblNgay ,lblList;
    private JComboBox<String> cbxNam ,cbxThang ,cbxNgay ;
    private JTable table;
    private JScrollPane scrollPane;
    private JPanel topPanel;
    private GroupLayout layout;
    
    public DashBoardAndBookedHistory(Frame parent, boolean modal) {
        super(parent, "Lịch sử đặt và doanh thu", modal);
        setSize(750, 500);
        setLocationRelativeTo(parent);
        init();
        setUplayout();
    }
    
    private void init()
    {
        btnPrint = new JButton("In");
        chkDon = new JCheckBox("Đơn");
        chkNhom = new JCheckBox("Nhóm");

        lblNam = new JLabel("Năm:");
        lblThang = new JLabel("Tháng:");
        lblNgay = new JLabel("Ngày:");

        cbxNam = new JComboBox<>(new String[]{"All", "2025", "2024"});
        cbxThang = new JComboBox<>(new String[]{"All", "1", "2", "3"});
        cbxNgay = new JComboBox<>(new String[]{"All", "1", "2", "3"});

        lblList = new JLabel("Danh sách");
        lblList.setFont(new Font("Segoe UI", Font.BOLD, 13));

        table = new JTable(
                new Object[][]{
                        {"1", "101", "Tầng 1", "Đơn thường", "Sử dụng"},
                        {"2", "102", "Tầng 1", "Đơn VIP", "Trống"},
                        {"3", "201", "Tầng 2", "Đơn thường", "Trống"}
                },
                new String[]{"ID", "Phòng", "Khách hàng", "", "Trạng thái"}
        );
        scrollPane = new JScrollPane(table);

        // ===== Panel chứa thanh điều khiển =====
        topPanel = new JPanel();
        layout = new GroupLayout(topPanel);
    }
    
    private void setUplayout()
    {
        topPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Horizontal group
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(btnPrint, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                        .addGap(20)
                        .addComponent(chkDon)
                        .addComponent(chkNhom)
                        .addGap(400)
                        .addComponent(lblNam)
                        .addComponent(cbxNam, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblThang)
                        .addComponent(cbxThang, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNgay)
                        .addComponent(cbxNgay, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
        );

        // Vertical group — phải có TẤT CẢ các component xuất hiện ở trên
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(btnPrint)
                        .addComponent(chkDon)
                        .addComponent(chkNhom)
                        .addComponent(lblNam)
                        .addComponent(cbxNam)
                        .addComponent(lblThang)
                        .addComponent(cbxThang)
                        .addComponent(lblNgay)
                        .addComponent(cbxNgay)
        );

        // ===== Panel chứa bảng =====
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(lblList, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== Bố cục tổng =====
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    // ===== Chạy thử độc lập =====
    public static void main(String[] args) {
        HotelManagementSystem frame = new HotelManagementSystem();
        SwingUtilities.invokeLater(() -> {
            DashBoardAndBookedHistory dialog = new DashBoardAndBookedHistory(frame, true);
            dialog.setVisible(true);
        });
    }
}

