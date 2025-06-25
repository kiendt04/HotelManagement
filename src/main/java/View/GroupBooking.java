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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GroupBooking extends JFrame {
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JTextField fromDateField, toDateField;
    private JButton searchButton;
    
    public GroupBooking() {
        initializeComponents();
        setupLayout();
        setupTable();
        loadSampleData();
        setVisible(true);
    }
    
    private void initializeComponents() {
        setTitle("Quản lý đặt phòng theo đoàn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 650);
        setLocationRelativeTo(null);
        
        fromDateField = new JTextField("01/09/2021", 10);
        toDateField = new JTextField("01/09/2021", 10);
        searchButton = new JButton("Tìm");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create top panel with blue gradient background
        JPanel topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(135, 175, 215), 0, getHeight(), new Color(175, 200, 225)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1300, 100));
        
        // Create toolbar
        JPanel toolbar = createToolbar();
        topPanel.add(toolbar, BorderLayout.NORTH);
        
        // Create date filter panel
        JPanel datePanel = createDatePanel();
        topPanel.add(datePanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Create main panel for table
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        toolbar.setOpaque(false);
        
        // Create toolbar buttons with icons (simplified)
        JButton newBtn = createToolbarButton("Sửa");
        JButton openBtn = createToolbarButton("Xóa");  
        JButton saveBtn = createToolbarButton("In");
        JButton printBtn = createToolbarButton("Thoát");
        
        toolbar.add(newBtn);
        toolbar.add(openBtn);
        toolbar.add(saveBtn);
        toolbar.add(printBtn);
        
        return toolbar;
    }
    
    private JButton createToolbarButton(String text) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(32, 32));
        button.setToolTipText(text);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setBackground(new Color(220, 220, 220));
        button.setFocusPainted(false);
        
        // Add simple icon representation
        button.setText(text.substring(0, 1));
        button.setFont(new Font("Arial", Font.BOLD, 10));
        
        return button;
    }
    
    private JPanel createDatePanel() {
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        datePanel.setOpaque(false);
        
        // Style labels
        JLabel fromLabel = new JLabel("Từ ngày");
        JLabel toLabel = new JLabel("Đến ngày");
        fromLabel.setForeground(Color.BLACK);
        toLabel.setForeground(Color.BLACK);
        
        // Style date fields
        fromDateField.setPreferredSize(new Dimension(80, 22));
        toDateField.setPreferredSize(new Dimension(80, 22));
        fromDateField.setBorder(BorderFactory.createLoweredBevelBorder());
        toDateField.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Add calendar icons (simplified as buttons)
        JButton fromCalBtn = new JButton("📅");
        JButton toCalBtn = new JButton("📅");
        fromCalBtn.setPreferredSize(new Dimension(25, 22));
        toCalBtn.setPreferredSize(new Dimension(25, 22));
        fromCalBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        toCalBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        
        datePanel.add(fromLabel);
        datePanel.add(fromDateField);
        datePanel.add(fromCalBtn);
        datePanel.add(Box.createHorizontalStrut(20));
        datePanel.add(toLabel);
        datePanel.add(toDateField);
        datePanel.add(toCalBtn);
        
        return datePanel;
    }
    
    private void setupTable() {
        // Create table model with exact column names from image
        String[] columnNames = {
            "", "DEL", "SỐ HĐ", "NGÀY ĐẶT", "NGÀY TRẢ", "SỐ TIỀN", 
            "SỐ NGƯỜI", "TRẠNG THÁI", "ĐOÀN", "KHÁCH HÀNG", "GHI CHÚ"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 1) return Boolean.class;
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 1; // Only first two columns editable
            }
        };
        
        bookingTable = new JTable(tableModel);
        bookingTable.setRowHeight(20);
        bookingTable.setFont(new Font("Tahoma", Font.PLAIN, 11));
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.setGridColor(new Color(192, 192, 192));
        bookingTable.setShowGrid(true);
        
        // Set header style
        JTableHeader header = bookingTable.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(new Font("Tahoma", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 25));
        
        // Set column widths to match image
        TableColumnModel columnModel = bookingTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);   // Empty column
        columnModel.getColumn(1).setPreferredWidth(40);   // DEL
        columnModel.getColumn(2).setPreferredWidth(60);   // SỐ HĐ
        columnModel.getColumn(3).setPreferredWidth(80);   // NGÀY ĐẶT
        columnModel.getColumn(4).setPreferredWidth(80);   // NGÀY TRẢ
        columnModel.getColumn(5).setPreferredWidth(80);   // SỐ TIỀN
        columnModel.getColumn(6).setPreferredWidth(70);   // SỐ NGƯỜI
        columnModel.getColumn(7).setPreferredWidth(90);   // TRẠNG THÁI
        columnModel.getColumn(8).setPreferredWidth(80);   // ĐOÀN
        columnModel.getColumn(9).setPreferredWidth(130);  // KHÁCH HÀNG
        columnModel.getColumn(10).setPreferredWidth(100); // GHI CHÚ
        
        // Custom cell renderer for checkmarks
        DefaultTableCellRenderer checkRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if (column == 7 || column == 8) { // TRẠNG THÁI and ĐOÀN columns
                    if ("✓".equals(value)) {
                        setForeground(Color.GREEN);
                        setFont(new Font("Arial", Font.BOLD, 14));
                    } else {
                        setForeground(Color.BLACK);
                    }
                }
                return this;
            }
        };
        
        bookingTable.getColumnModel().getColumn(7).setCellRenderer(checkRenderer);
        bookingTable.getColumnModel().getColumn(8).setCellRenderer(checkRenderer);
        
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setPreferredSize(new Dimension(1250, 450));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Add table to main panel
        JPanel mainPanel = (JPanel) getContentPane().getComponent(1);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add instruction label at bottom
        JLabel instructionLabel = new JLabel("Drag a column header here to group by that column");
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setFont(new Font("Tahoma", Font.ITALIC, 10));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        mainPanel.add(instructionLabel, BorderLayout.SOUTH);
    }
    
    private void loadSampleData() {
        // Add exact data from the image
        Object[][] sampleData = {
            {false, false, "1014", "9/1/2021", "9/2/2021", "1527000", "1", "✓", "✓", "Nguyễn Minh Triết", ""},
            {false, false, "1015", "9/1/2021", "9/2/2021", "2527000", "1", "✓", "☐", "Lê Thị Thắm", "Khách lẻ"},
            {false, false, "1016", "9/1/2021", "9/2/2021", "2550000", "1", "✓", "✓", "Nguyễn Minh Triết", "Đoàn Travel"},
            {false, false, "1021", "9/1/2021", "9/3/2021", "14085000", "8", "☐", "✓", "Nguyễn Minh Triết", "Đoàn Travel"},
            {false, false, "1022", "9/1/2021", "9/2/2021", "1586000", "2", "✓", "☐", "Hoàng Anh Tuấn", "Khách lẻ"},
            {false, false, "1023", "9/1/2021", "9/2/2021", "2535000", "1", "☐", "☐", "Lê Thị Thắm", "Khách lẻ"}
        };
        
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GroupBooking();
        });
    }
}