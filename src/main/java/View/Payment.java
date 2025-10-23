/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author ASUS
 */

import Control.CustomerListControl;
import Control.PaymentControl;
import DAO.Room_typeDAO;
import DAO.RoomDAO;
import DAO.BillDAO;
import DAO.ServiceDAO;
import DAO.CustomerDAO;
import DAO.BillDetailDAO;
import com.toedter.calendar.JDateChooser;
import Model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

public class Payment extends JFrame {

    private JComboBox<Customer> customerCBX;
    private DefaultComboBoxModel<Customer> customerModel;

    private JDateChooser checkInField;
    private JDateChooser checkOutField;
    private JTextField totalService,deposit,totalAmountField,discountCodeField,totalRoom,noteArea,acutal_pay;
    private JLabel discountPercentLable ;
    private JComboBox<String> statusComboBox;
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JLabel totalAmountLabel;
    private NumberFormat currencyFormat;
    private JButton saveBtn, printBtn, exitBtn,finishBtn,cancelBtn;
    private int idRoom,idBill = -1,days = 0,hour = 0;
    private boolean isClick = false,booked = false;
    private List<Discount> discountLst;
    private Room slRoom;
    private PaymentControl pc = new PaymentControl();
    private CustomerListControl clc = new CustomerListControl();
    private JPanel mainPanel;

    public Payment(int id,boolean booked) {
        this.idRoom = id;
        this.booked = booked;
        this.slRoom = new RoomDAO().getbyID(id);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setSize(950, 600);
        setLocationRelativeTo(null);
        setTitle("Đặt phòng");
        if((slRoom.getStatus() == 1 || slRoom.getStatus() == -1) && pc.getRoomBill(slRoom.getNum(),slRoom.getStatus()) != null)
        {
            billData(pc.getRoomBill(slRoom.getNum(),slRoom.getStatus()));
        }
        setUpBtn();
    }

    private void initializeComponents() {
        // Khởi tạo các text field
        customerModel = new DefaultComboBoxModel<>();
        customerCBX = new JComboBox<>(customerModel);
        customerCBX.setEditable(true);

        // Load dữ liệu từ CustomerDAO
        List<Customer> customers = clc.getAll();
        for (Customer c : customers) {
            customerModel.addElement(c);
        }

        checkOutField = new JDateChooser();
        checkInField = new JDateChooser();

        checkInField.setDateFormatString("dd/MM/yyyy HH:mm:ss");
        checkOutField.setDateFormatString("dd/MM/yyyy HH:mm:ss");
        totalRoom = new JTextField();
        // Khởi tạo ComboBox cho trạng thái
        totalService = new JTextField();
        String[] statusOptions = { "Đang dùng","Hoàn tất","Đặt trước"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setSelectedItem("Chưa hoàn tất");

        // Khởi tạo bảng
        String[] columnNames = {"TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        tableModel = new DefaultTableModel(columnNames, 0) ;

        serviceTable = new JTable(tableModel);
        serviceTable.setRowHeight(25);
        serviceTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        serviceTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        serviceTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        serviceTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Styling cho header
        JTableHeader header = serviceTable.getTableHeader();
        header.setBackground(new Color(220, 220, 220));
        header.setFont(new Font("Arial", Font.BOLD, 12));

        totalAmountLabel = new JLabel();
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountLabel.setForeground(Color.RED);
        
        totalAmountField = new JTextField();
        deposit = new JTextField();
        acutal_pay = new JTextField();
        
        discountLst = pc.getDiscountLst();
        discountPercentLable = new JLabel();
        
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel chính
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(new Color(240, 240, 240));

        saveBtn = new JButton("💾 Lưu"); //1
        printBtn = new JButton("🖨️ In");//4
        exitBtn = new JButton("➔ Thoát");//5
        finishBtn = new JButton("💰 Thanh toán");//3
        cancelBtn = new JButton("❌ Hủy đặt trước");//2
        toolbarPanel.add(saveBtn); 
        toolbarPanel.add(cancelBtn);
        toolbarPanel.add(finishBtn);
        toolbarPanel.add(printBtn);
        toolbarPanel.add(exitBtn);

        cancelBtn.setVisible(false);
        printBtn.setVisible(false);
        // Top panel container
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(toolbarPanel, BorderLayout.SOUTH);

        // Panel thông tin khách hàng
        JPanel customerInfoPanel = createCustomerInfoPanel();

        // Panel danh sách sản phẩm
        JPanel servicePanel = createServicePanel();

        // Sắp xếp layout
        mainPanel.add(topContainer, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(customerInfoPanel, BorderLayout.NORTH);
        centerPanel.add(servicePanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        
        createTotalPanel();
        add(createTotalPanel(),BorderLayout.SOUTH);
        initAndsetupDate();
    }
    
    private void billData(Bill b)
    {
        idBill = b.getId();
        Customer csInfo = clc.getById(b.getUser());
        customerCBX.setSelectedItem(csInfo);
        checkInField.setDate(b.getCheck_in());
        checkOutField.setDate(b.getCheck_out());
        totalService.setText(pc.formatPrice(b.getTotal_service()));
        totalRoom.setText(pc.formatPrice(b.getTotal_time()));
        totalAmountLabel.setText(pc.formatPrice(b.getTotal()));
        pc.setTblData(tableModel, new BillDetailDAO().getByBill(b.getId()));
    }
    
    
    
   private JPanel createCustomerInfoPanel() {
    // Panel chứa toàn bộ (trái + phải)
    JPanel mainPanel = new JPanel(new BorderLayout());

     // ==================== PANEL TRÁI ====================
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

    // ---- Hàng 1: Loại phòng + Khách hàng ----
    JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

    topRow.add(new JLabel("Khách hàng:"));
    customerCBX.setPreferredSize(new Dimension(150, 25));
    topRow.add(customerCBX);

    JButton btnOpenCustomer = new JButton("🔍");
    btnOpenCustomer.setPreferredSize(new Dimension(40, 25));
    btnOpenCustomer.addActionListener(e -> {
        pc.openCusList(Payment.this, customerModel);
    });
    topRow.add(btnOpenCustomer);
    
    // ---- Hàng 4: Mã giảm giá + % giảm ----
    topRow.add(new JLabel("Mã giảm giá:"));
    discountCodeField = new JTextField(10);
    JLabel discountPercentLabel = new JLabel("0%");
    discountPercentLabel.setPreferredSize(new Dimension(35, 25));
    discountPercentLabel.setHorizontalAlignment(SwingConstants.CENTER);
    topRow.add(discountCodeField);
    topRow.add(discountPercentLabel);
    leftPanel.add(topRow);

    // ---- Hàng 2: Ngày đặt + Ngày trả ----
    JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    dateRow.add(new JLabel("Ngày đặt:"));
    dateRow.add(checkInField);

    dateRow.add(new JLabel("Ngày trả:"));
    dateRow.add(checkOutField);
    leftPanel.add(dateRow);

    // ---- Hàng 3: Ghi chú ----
    JPanel noteRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    noteRow.add(new JLabel("Ghi chú:"));
    noteArea = new JTextField(40);
    noteRow.add(noteArea);
    leftPanel.add(noteRow);

    // ==================== PANEL PHẢI ====================
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm - Dịch vụ"));
    rightPanel.setPreferredSize(new Dimension(200, 200)); // đảm bảo không co

    DefaultListModel<Service> model = new DefaultListModel<>();
    for (Service s : new ServiceDAO().getAll()) {
        model.addElement(s);
    }

    JList<Service> serviceList = new JList<>(model);
    serviceList.setFont(new Font("Arial", Font.PLAIN, 14));
    serviceList.setFixedCellHeight(26);

    JScrollPane scrollPane = new JScrollPane(serviceList);
    rightPanel.add(scrollPane, BorderLayout.CENTER);

    // Mouse click để thêm DV
    serviceList.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            Service selected = serviceList.getSelectedValue();
            if (selected == null) return;

            String name = selected.getName();
            double price = selected.getPrice();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (name.equalsIgnoreCase(tableModel.getValueAt(i, 0).toString())) {
                    int sl = Integer.parseInt(JOptionPane.showInputDialog(rootPane, "Số lượng: ", Integer.parseInt(tableModel.getValueAt(i, 1).toString())));
                    tableModel.setValueAt(sl, i, 1);
                    tableModel.setValueAt(pc.formatPrice(sl * price), i, 3);
                    calculateTotal();
                    return;
                }
            }
            
            int sl = Integer.parseInt(JOptionPane.showInputDialog(rootPane, "Số lượng: ", null));
            tableModel.addRow(new Object[]{name, sl, pc.formatPrice(price), pc.formatPrice(price * sl)});
            calculateTotal();
        }
    });

    // ==================== GỘP 2 PANEL LẠI ====================
    mainPanel.add(leftPanel, BorderLayout.CENTER);
    mainPanel.add(rightPanel, BorderLayout.EAST);

    return mainPanel;
}

    private JPanel createServicePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách Sản phẩm - Dịch vụ"));

        // Header với thông tin phòng
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(255, 240, 240));
        String infor =  "Phòng " + slRoom.getNum();
        JLabel roomLabel = new JLabel(infor);
        roomLabel.setFont(new Font("Arial", Font.BOLD, 12));
        roomLabel.setForeground(Color.RED);
        headerPanel.add(roomLabel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(serviceTable), BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createTotalPanel() {
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.setBackground(new Color(220, 220, 220));
        totalPanel.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel totalLabel = new JLabel();
        totalLabel.setText("Tổng tiền");
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 10));
        totalLabel.setForeground(Color.RED);
        JLabel totalroom = new JLabel("Tổng tiền phòng");
        totalroom.setFont(new Font("Tahoma", Font.BOLD, 10));
        totalroom.setForeground(Color.RED);
        JLabel totalser = new JLabel("Tổng tiền dịch vụ");
        totalser.setFont(new Font("Tahoma", Font.BOLD, 10));
        totalser.setForeground(Color.RED);
        totalroom.setForeground(Color.RED);
        JLabel depo = new JLabel("Đặt cọc");
        depo.setFont(new Font("Tahoma", Font.BOLD, 10));
        depo.setForeground(Color.RED);
        JLabel actualpay = new JLabel("Đã thu");
        actualpay.setFont(new Font("Tahoma", Font.BOLD, 10));
        actualpay.setForeground(Color.RED);
        
        totalService.setPreferredSize(new Dimension(80, 16));
        totalService.setFont(new Font("Tahoma", Font.BOLD, 10));
        totalService.setForeground(Color.RED);
        totalService.setHorizontalAlignment(JTextField.CENTER);
        totalService.setBorder(BorderFactory.createLoweredBevelBorder());
        totalService.setText("00,000");
        
        totalRoom.setPreferredSize(new Dimension(80, 16));
        totalRoom.setFont(new Font("Tahoma", Font.BOLD, 10));
        totalRoom.setForeground(Color.RED);
        totalRoom.setHorizontalAlignment(JTextField.CENTER);
        totalRoom.setBorder(BorderFactory.createLoweredBevelBorder());
        totalRoom.setText("00,000");
        
        totalAmountField.setPreferredSize(new Dimension(80, 16));
        totalAmountField.setFont(new Font("Tahoma", Font.BOLD, 10));
        totalAmountField.setForeground(Color.RED);
        totalAmountField.setHorizontalAlignment(JTextField.CENTER);
        totalAmountField.setBorder(BorderFactory.createLoweredBevelBorder());
        totalAmountField.setText("00,000");
        
        deposit.setPreferredSize(new Dimension(80, 16));
        deposit.setFont(new Font("Tahoma", Font.BOLD, 10));
        deposit.setForeground(Color.RED);
        deposit.setHorizontalAlignment(JTextField.CENTER);
        deposit.setBorder(BorderFactory.createLoweredBevelBorder());
        deposit.setText("00,000");
        
        acutal_pay.setPreferredSize(new Dimension(80, 16));
        acutal_pay.setFont(new Font("Tahoma", Font.BOLD, 10));
        acutal_pay.setForeground(Color.RED);
        acutal_pay.setHorizontalAlignment(JTextField.CENTER);
        acutal_pay.setBorder(BorderFactory.createLoweredBevelBorder());
        acutal_pay.setText("00,000");
        
        JLabel dongLabel = new JLabel("VDN");
        dongLabel.setFont(new Font("Tahoma", Font.BOLD, 10));
        dongLabel.setForeground(Color.RED);
        JLabel dongLabel1 = new JLabel("VDN");
        dongLabel1.setFont(new Font("Tahoma", Font.BOLD, 10));
        dongLabel1.setForeground(Color.RED);
        JLabel dongLabel2 = new JLabel("VDN");
        dongLabel2.setFont(new Font("Tahoma", Font.BOLD, 10));
        dongLabel2.setForeground(Color.RED);
        JLabel dongLabel3 = new JLabel("VDN");
        dongLabel3.setFont(new Font("Tahoma", Font.BOLD, 10));
        dongLabel3.setForeground(Color.RED);
        
        totalPanel.add(totalroom);
        totalPanel.add(totalRoom);
        totalPanel.add(Box.createHorizontalStrut(20));
        totalPanel.add(totalser);
        totalPanel.add(totalService);
        totalPanel.add(Box.createHorizontalStrut(20));
        totalPanel.add(totalLabel);
        totalPanel.add(totalAmountField);
        totalPanel.add(Box.createHorizontalStrut(20));
        totalPanel.add(actualpay);
        totalPanel.add(acutal_pay);
        totalPanel.add(Box.createHorizontalStrut(20));
        totalPanel.add(depo);
        totalPanel.add(deposit);
        
        depo.setVisible(booked);
        deposit.setVisible(booked);
        return totalPanel;
    }

    private void initAndsetupDate()
    {
        if(booked)
        {
            Calendar cal= Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 7);
            checkInField.setDate(cal.getTime());
            checkInField.getJCalendar().setMinSelectableDate(cal.getTime());
            checkOutField.getJCalendar().setMinSelectableDate(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            //cal.add(in.getDate().getDay(), 1);
            checkOutField.getJCalendar().setMinSelectableDate(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 6);
            //cal.add(in.getDate().getDay(), 6);
            checkOutField.setDate(cal.getTime());
            checkOutField.getJCalendar().setMaxSelectableDate(cal.getTime());
            checkInField.getJCalendar().setMaxSelectableDate(cal.getTime());
        }
        else
        {
            Calendar cal= Calendar.getInstance();
            checkInField.getJCalendar().setMinSelectableDate(cal.getTime());
            checkOutField.getJCalendar().setMinSelectableDate(cal.getTime());
            checkInField.setDate(cal.getTime());
            checkInField.setEnabled(false);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            checkOutField.setMaxSelectableDate(cal.getTime());
        }
        
        checkInField.addPropertyChangeListener("date", e -> {
            String time = pc.getUsedTime(checkInField, checkOutField);
            if(time.contains(" giờ"))
            {
                hour = Integer.parseInt(time.replace(" giờ", ""));
            }
            else
            {
                days = Integer.parseInt(time.replace(" ngày", ""));
            }
            
            sumTotalRoom();
        });

        checkOutField.addPropertyChangeListener("date", e -> {
            String time = pc.getUsedTime(checkInField, checkOutField);
            if(time.contains(" giờ"))
            {
                hour = Integer.parseInt(time.replace(" giờ", ""));
            }
            else
            {
                days = Integer.parseInt(time.replace(" ngày", ""));
            }
            sumTotalRoom();
        });
    }
    
    private void setUpBtn()
    {
        if(!booked)
        {
            if(idBill != -1)
            {
                if(slRoom.getStatus() == 0)
                {
                    saveBtn.setEnabled(false);
                    finishBtn.setEnabled(false);
                    printBtn.setVisible(true);
                }
                else if(slRoom.getStatus() == -1)
                {
                    java.util.Date in = checkInField.getDate();
                    LocalDateTime now = LocalDateTime.now();
                    if(now.compareTo(in.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) < 0)
                    {
                        cancelBtn.setVisible(true);
                        finishBtn.setEnabled(false);
                    }
                    else
                    {
                        saveBtn.setText("Nhận phòng");
                        cancelBtn.setVisible(false);
                        finishBtn.setEnabled(true);
                    }
                }
            }
            else
            {
                finishBtn.setEnabled(false);
            }
        }
        else
        {
            finishBtn.setEnabled(false);
        }
    }
    
    private void setupEventHandlers() {
        // Thêm event handler cho việc thay đổi số lượng trong bảng
        serviceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = serviceTable.getSelectedRow();
                    double price = Double.parseDouble(serviceTable.getValueAt(row, 3).toString().replaceAll(",", "").trim());
                    if (row != -1) {
                        int sl = Integer.parseInt(JOptionPane.showInputDialog(rootPane, "Số lượng: ", Integer.parseInt(tableModel.getValueAt(row, 1).toString())));
                        tableModel.setValueAt(sl, row, 1);
                        tableModel.setValueAt(pc.formatPrice(sl * price), row, 3);
                        calculateTotal();
                    }
                }
                else if (e.getButton() == MouseEvent.BUTTON3)
                {
                    JMenuItem delSer = new JMenuItem("Xóa");
                    JPopupMenu menu = new JPopupMenu();
                    delSer.addActionListener((l) -> {
                       if(JOptionPane.showConfirmDialog(rootPane, "Xóa các dịch vụ đã chọn ?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0)
                       {
                           int[] selectedrows = serviceTable.getSelectedRows();
                           delTableItems(selectedrows, serviceTable);
                           calculateTotal();
                       }
                    });
                    menu.add(delSer);
                    menu.show(serviceTable, e.getX(), e.getY());
                    
                }
            }
        });
                
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAction(1);
            }
        });
        
        cancelBtn.addActionListener((e) -> {
            
        });
        
        finishBtn.addActionListener((e) -> {
            btnAction(0);
        });

        printBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Object> datasrc = pc.setDataPrint(pc.getBDList(idBill),slRoom,pc.getBill(idBill));
                Map<String ,Object> mainparam = pc.setupParameter(pc.getBill(idBill), "");
                pc.printed(datasrc, mainparam);
            }
        });
        
        exitBtn.addActionListener(e -> {dispose();});
        
        discountCodeField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                discountPercentLable.setText(pc.autoGetDisValue(discountCodeField.getText().trim(), discountLst) + "%");
            }
        });
    }
    
    private void btnAction(int status)
    {
        Customer cs = (Customer) customerCBX.getSelectedItem();
        if(checkInField.getDate() == null || checkOutField == null)
         {
            JOptionPane.showMessageDialog(rootPane, "Chọn ngày đặt phòng !!!!");
            return;
        }
        Timestamp dt_in = new Timestamp(checkInField.getDate().getTime());
        Timestamp dt_out = new Timestamp(checkOutField.getDate().getTime());
        int stats = status;
        double totalroom = Double.parseDouble(totalRoom.getText().replaceAll("[^0-9]", ""));
        int totalservice = Integer.parseInt(totalService.getText().replaceAll("[^0-9]", ""));
        double total = totalroom + totalservice;
        double extra_charge = idBill != 0 ? pc.billExtra_chagre(idBill) : 0;
        double discount = Integer.parseInt(discountPercentLable.getText().replaceAll("%", "").trim()) * total;
        double actual_pay = 0;
        if(idBill == -1)
        {
           actual_pay = total - discount + extra_charge;
        }
        else
        {
            
        }
        double deposit = status == -1 ? actual_pay/2 : 0;
        Bill b = new Bill(idBill, slRoom.getNum(), cs.getId(), dt_in, dt_out, totalroom, totalservice, total, extra_charge, discount, deposit, actual_pay, stats);
        if(JOptionPane.showConfirmDialog(rootPane, "Luu hoa don", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0 && (idBill == -1 ? pc.insertBill(b) : pc.uptBill(b)) != 0)
        {
            slRoom.setStatus(stats);
            int bdID = pc.getRoomBill(slRoom.getNum(),slRoom.getStatus()).getId();
            List<BillDetail> lst = new ArrayList<>();
            List<Service> svl =  new ServiceDAO().getAll();
            for (int i = 0 ;i<serviceTable.getModel().getRowCount();i++)
            {
                int j = 0;
                while(!serviceTable.getModel().getValueAt(i, 0).equals(svl.get(j).getName()))
                {
                    j++;
                }
                int srId = svl.get(j).getId();
                int quan = Integer.parseInt(serviceTable.getModel().getValueAt(i, 1).toString().trim());
                int ttl = Integer.parseInt(serviceTable.getModel().getValueAt(i, 3).toString().replace(",", "").trim());
                lst.add(new BillDetail(bdID,srId,quan,ttl));
                
            }
            if(idBill != -1) pc.delAllByid(idBill);
            for (int i=0;i<lst.size();i++)
            {
                pc.insertDetail(lst.get(i));
            }
            JOptionPane.showMessageDialog(rootPane, "Lưu thành công");
            if(stats == 0)
            {
                saveBtn.setEnabled(false);
                printBtn.setVisible(true);
            }
            if(stats == -1)
            {
                slRoom.setStatus(0);
                pc.uptRoom(slRoom);
                idBill = bdID;
                dispose();
            }
            else
            {
                saveBtn.setText("💾 Lưu");
                slRoom.setStatus(stats);
                pc.uptRoom(slRoom);
                idBill = bdID;
            }
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "Lỗi! Lưu thất bại");
        }        
    }
    
    public void delTableItems(int[] row,JTable tbl)
    {
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        for (int i = row.length - 1 ; i >= 0; i--) {
            model.removeRow(row[i]);
        }
    }
    
    private void sumTotalRoom() {
        java.util.Date checkIn = checkInField.getDate();
        java.util.Date checkOut = checkOutField.getDate();

        if (checkIn == null || checkOut == null) {
            return;
        }
        
        Room_typeDAO rt = new Room_typeDAO();
        double roomRate = rt.getPricePH(slRoom.getType(),days > 0 ? true : false);
        double totalRoomPrice = roomRate * (days > 0 ? days : hour);
        totalRoom.setText(pc.formatPrice(totalRoomPrice));
        calculateTotal();
    }

    private void calculateTotal() {
        long tongDichVu = 0;

        // Tính tổng tiền dịch vụ từ bảng
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object thanhTienObj = tableModel.getValueAt(i, 3);
            if (thanhTienObj != null && !thanhTienObj.toString().trim().isEmpty()) {
                try {
                    String thanhTienStr = thanhTienObj.toString().replaceAll(",", "");
                    if (!thanhTienStr.isEmpty()) {
                        tongDichVu += Long.parseLong(thanhTienStr);
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua lỗi
                }
            }
        }

        // Cập nhật totalService theo định dạng tiền tệ
        totalService.setText(pc.formatPrice(tongDichVu));

//        // Parse lại giá trị từ totalRoom đang hiển thị có thể đã được format
            long tienPhong = Long.parseLong(totalRoom.getText().replaceAll(",", "").trim());

        // Tính và hiển thị tổng thanh toán
        long tongThanhToan = tienPhong + tongDichVu;
        totalAmountField.setText(pc.formatPrice(tongThanhToan));
        
        double deposit = 0, actual_pay = 0,discountMoney = 0;
        if(idBill == -1)
        {
            actual_pay = tongThanhToan - Double.parseDouble(discountPercentLable.getText().replace("%", "").trim()) * tongThanhToan;
            acutal_pay.setText(pc.formatPrice(actual_pay));
            if(booked)
            {
                deposit = actual_pay/2;
                this.deposit.setText(pc.formatPrice(deposit));
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Payment(1,false).setVisible(true);
        });
    }
}
