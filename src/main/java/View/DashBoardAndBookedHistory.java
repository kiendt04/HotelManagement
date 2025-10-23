/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author ADMIN
 */
import Control.BillHistoryControl;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class DashBoardAndBookedHistory extends JDialog {

    private JButton btnPrint;
    private JCheckBox chkDon,chkNhom;
    private JLabel lblNam ,lblThang ,lblNgay ,lblList,lblTotal;
    private JComboBox<String> cbxNam ,cbxThang ,cbxNgay ;
    private JTable table;
    private DefaultTableModel billDetailModel;
    private JTextField txfTotal;
    private JScrollPane scrollPane;
    private JPanel topPanel,bottomPanel,centerPanel;
    private GroupLayout layout;
    private BillHistoryControl BHC;
    
    
    public DashBoardAndBookedHistory(Frame parent, boolean modal) {
        super(parent, "Lịch sử đặt và doanh thu", modal);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        setLocationRelativeTo(parent);
        init();
        setUplayout();
        BHC.loadData();
        componentLogic();
    }
    
    private void init()
    {
        btnPrint = new JButton("In");
        chkDon = new JCheckBox("Khách lẻ");
        chkNhom = new JCheckBox("Tour");

        lblNam = new JLabel("Năm:");
        lblThang = new JLabel("Tháng:");
        lblNgay = new JLabel("Ngày:");
        lblTotal = new JLabel("Tổng doanh thu");
        lblTotal.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTotal.setForeground(Color.RED);

        txfTotal = new JTextField();
        txfTotal.setPreferredSize(new Dimension(130, 20));
        txfTotal.setFont(new Font("Tahoma", Font.BOLD, 14));
        txfTotal.setForeground(Color.RED);
        txfTotal.setHorizontalAlignment(JTextField.CENTER);
        txfTotal.setBorder(BorderFactory.createLoweredBevelBorder());
        txfTotal.setText("00,000");
        txfTotal.setEditable(false);
        
        
        
        cbxNam = new JComboBox<>(new String[]{"All", "2025", "2024","2023"});
        cbxThang = new JComboBox<>(new String[]{"All", "1", "2", "3","4", "5", "6", "7","8", "9", "10", "11","12"});
        cbxNgay = new JComboBox<>(new String[]{"All", "1", "2", "3"});

        lblList = new JLabel("Danh sách");
        lblList.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        String[] billDetailColumn = {"ID", "Phòng", "Khách hàng", "Ngày nhận","Ngày trả","Tiền phòng","Tiền dịch vụ","Tổng","Phụ thu","Khuyến mãi","Đặt cọc","Đã thu", "Trạng thái"};
        billDetailModel = new DefaultTableModel(billDetailColumn, 0);
        table = new JTable(billDetailModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setMaxWidth(60);
        scrollPane = new JScrollPane(table);

        // ===== Panel chứa thanh điều khiển =====
        topPanel = new JPanel();
        layout = new GroupLayout(topPanel);
        BHC = new BillHistoryControl(btnPrint, chkDon, chkNhom, cbxNam, cbxThang, table, billDetailModel, txfTotal);
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
                        .addGap(100)
                        .addComponent(lblNam)
                        .addComponent(cbxNam, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblThang)
                        .addComponent(cbxThang, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
//                        .addComponent(lblNgay)
//                        .addComponent(cbxNgay, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
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
//                        .addComponent(lblNgay)
//                        .addComponent(cbxNgay)
        );

        // ===== Panel chứa bảng =====
        centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(lblList, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        //Panel bottom
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(220, 220, 220));
        bottomPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel vnd = new JLabel("VND");
        vnd.setFont(new Font("Tahoma", Font.BOLD, 14));
        vnd.setForeground(Color.RED);
        bottomPanel.add(lblTotal);
        bottomPanel.add(txfTotal);
        bottomPanel.add(vnd);
        bottomPanel.add(Box.createHorizontalStrut(150));
        
        // ===== Bố cục tổng =====
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel,BorderLayout.SOUTH);
    }
    
    private void componentLogic()
    {
        chkDon.addActionListener((e) -> {
            if(!chkDon.isSelected() && !chkNhom.isSelected())
            {
                chkDon.setSelected(true);
            }
            else
            {
                BHC.checkBoxAction();
            }
        });
        
        chkNhom.addActionListener((e) -> {
            if(!chkDon.isSelected() && !chkNhom.isSelected())
            {
                chkNhom.setSelected(true);
            }
            else
            {
                BHC.checkBoxAction();
            }
        });
        
        cbxNam.addActionListener((e) -> {
            BHC.checkBoxAction();
        });
        
        cbxThang.addActionListener((e) -> {
            BHC.checkBoxAction();
        });
        
        btnPrint.addActionListener((e) -> {
            BHC.ExportExcelFile();
        });
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

