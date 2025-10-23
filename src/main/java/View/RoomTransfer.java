package View;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import Control.*;
import Model.*;

public class RoomTransfer extends JDialog {

    private JCheckBox chkIssue, chkUpgrade;
    private JTable tblRooms;
    private DefaultTableModel tableModel;
    private JButton btnCancel, btnConfirm;
    private RoomTransferControl control = new RoomTransferControl();
    private Room OldRoom;
    private int id;
    private boolean isTour;

    public RoomTransfer(Frame parent,int id,Room oldRoom,boolean tour) {
        super(parent, "Chuyển phòng", true);
        this.OldRoom =oldRoom;
        this.id = id;
        this.isTour = tour;
        initComponents();
    }

    private void initComponents() {
        // ===== Cấu hình dialog =====
        setSize(550, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(245, 247, 250));

        // ===== Panel trên: 2 checkbox =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBackground(new Color(245, 247, 250));
        topPanel.setBorder(new EmptyBorder(10, 15, 5, 15));

        chkIssue = new JCheckBox("Sự cố");
        chkUpgrade = new JCheckBox("Nâng hạng phòng");

        Font checkFont = new Font("Segoe UI", Font.PLAIN, 13);
        chkIssue.setFont(checkFont);
        chkUpgrade.setFont(checkFont);

        chkIssue.setBackground(new Color(245, 247, 250));
        chkUpgrade.setBackground(new Color(245, 247, 250));

        topPanel.add(chkIssue);
        topPanel.add(chkUpgrade);
        add(topPanel, BorderLayout.NORTH);

        // ===== Bảng trung tâm =====
        String[] columnNames = {"ID","Tên phòng", "Giá / giờ", "Giá / đêm"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblRooms = new JTable(tableModel);
        tblRooms.setRowHeight(28);
        tblRooms.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRooms.setGridColor(new Color(230, 230, 230));
        
        control.loadRoomAvailable(tableModel, id,isTour);
        
        JTableHeaderStyle(tblRooms);

        JScrollPane scrollPane = new JScrollPane(tblRooms);
        scrollPane.setBorder(new CompoundBorder(
                new EmptyBorder(0, 15, 0, 15),
                new LineBorder(new Color(220, 220, 220), 1, true)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // ===== Panel dưới: 2 nút =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(245, 247, 250));

        btnConfirm = createStyledButton("Xác nhận", new Color(0x4CAF50));
        btnCancel = createStyledButton("Hủy", new Color(0xF44336));

        bottomPanel.add(btnConfirm);
        bottomPanel.add(btnCancel);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Sự kiện =====
        btnCancel.addActionListener(e -> dispose());
        btnConfirm.addActionListener(e -> handleConfirm());
    }

    private void JTableHeaderStyle(JTable table) {
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(60, 90, 150));
        table.getTableHeader().setForeground(Color.GRAY);
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 20, 8, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hiệu ứng hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void handleConfirm() {
        int row = tblRooms.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String room = tableModel.getValueAt(row, 0).toString();
        boolean issue = chkIssue.isSelected();
        boolean upgrade = chkUpgrade.isSelected();

        String msg = "Chuyển sang phòng: " + room;
        if (issue) msg += "\nLý do: Sự cố";
        if (upgrade) msg += "\nLý do: Nâng hạng phòng";

        JOptionPane.showMessageDialog(this, msg, "Xác nhận", JOptionPane.INFORMATION_MESSAGE);
    }
}