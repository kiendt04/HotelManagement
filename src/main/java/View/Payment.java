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
    private JTextField totalService, deposit, totalAmountField, discountCodeField, totalRoom, noteArea, acutal_pay;
    private JLabel discountPercentLable;
    private JComboBox<String> statusComboBox;
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JLabel totalAmountLabel;
    private NumberFormat currencyFormat;
    private JButton saveBtn, printBtn, exitBtn, finishBtn, cancelBtn;
    private int idRoom, idBill = -1, days = 0, hour = 0;
    private boolean isClick = false, booked = false;
    private List<Discount> discountLst;
    private Room slRoom;
    private PaymentControl pc = new PaymentControl();
    private CustomerListControl clc = new CustomerListControl();
    private JPanel mainPanel;
    private PaymentListener paymentListener; // optional

    public Payment(int billId) {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // 1) Lấy bill theo billId
        BillDAO billDAO = new BillDAO();
        Bill bill = billDAO.getBill(billId);
        if (bill == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hoá đơn #" + billId);
            dispose();
            return;
        }

        // 2) Chuyển room number (String) trong bill -> Room object -> roomId
        //    Tên hàm RoomDAO có thể là getByNum(String) hoặc tương tự.
        RoomDAO roomDAO = new RoomDAO();
        Room room = roomDAO.getByNum(bill.getRoom()); // ← nếu project của bạn đặt tên khác (getByNumber/getRoomByNum) hãy đổi lại.
        if (room != null) {
            this.idRoom = room.getId();
            this.slRoom = room;
        }

        // 3) Khởi tạo UI
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setSize(950, 600);
        setLocationRelativeTo(null);
        setTitle("Thanh toán");

        // 4) Đổ dữ liệu bill lên UI
        billData(bill);

        // 5) Khóa/mở thao tác theo trạng thái bill
        setUpBtnByBillStatus(bill.getStatus());
    }

    public Payment(int id, boolean booked) {
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
        if ((slRoom.getStatus() == 1 || slRoom.getStatus() == -1) && pc.getRoomBill(slRoom.getNum(), slRoom.getStatus()) != null) {
            billData(pc.getRoomBill(slRoom.getNum(), slRoom.getStatus()));
        }
        setUpBtn();
    }

    private void setUpBtnByBillStatus(int status) {
        // reset mặc định
        saveBtn.setEnabled(true);
        finishBtn.setEnabled(true);
        printBtn.setVisible(false);

        switch (status) {
            case -2: // Hủy đặt
                saveBtn.setEnabled(false);
                finishBtn.setEnabled(false);
                printBtn.setVisible(true);
                break;
            case -1: // Đặt trước
                // chưa cho thanh toán, cho phép lưu/nhận phòng
                finishBtn.setEnabled(false);
                printBtn.setVisible(false);
                break;
            case 1:  // Đang dùng
                // cho phép thanh toán & lưu
                finishBtn.setEnabled(true);
                printBtn.setVisible(false);
                break;
            case 0:  // Đã thanh toán
                saveBtn.setEnabled(false);
                finishBtn.setEnabled(false);
                printBtn.setVisible(true);
                break;
            default:
                // an toàn
                finishBtn.setEnabled(true);
                printBtn.setVisible(false);
        }
        // cập nhật lại giao diện
        saveBtn.revalidate();
        saveBtn.repaint();
        finishBtn.revalidate();
        finishBtn.repaint();
        printBtn.revalidate();
        printBtn.repaint();
    }

    public interface PaymentListener {

        void onPaymentPersisted(int billId, String roomNum, int newRoomStatus);
    }

    private void fireDashboardRefresh(int newStatus) {
        if (paymentListener != null && slRoom != null && idBill > 0) {
            paymentListener.onPaymentPersisted(idBill, slRoom.getNum(), newStatus);
        }
    }

    // Payment.java
// GỌI HÀM NÀY SAU KHI insert/upt BILL THÀNH CÔNG
    private void refreshAfterPayment(int persistedStatus) {
        try {
            BillDAO billDAO = new BillDAO();
            RoomDAO roomDAO = new RoomDAO();

            // 1) Reload room để lấy trạng thái mới nhất
            if (slRoom != null && slRoom.getId() > 0) {
                slRoom = roomDAO.getbyID(slRoom.getId());
            } else if (idRoom > 0) {
                slRoom = roomDAO.getbyID(idRoom);
            }

            // 2) Tìm lại bill vừa lưu (ưu tiên idBill; fallback theo phòng + trạng thái)
            Bill b = null;
            if (idBill > 0) {
                b = billDAO.getBill(idBill);
            }
            if (b == null && slRoom != null) {
                // ưu tiên trạng thái vừa lưu (persistedStatus), nếu không có thì lấy bill gần nhất
                b = billDAO.getRoomBill(slRoom.getNum(), persistedStatus);
                if (b == null) {
                    // bạn có thể tự hiện thực hàm này trong DAO: SELECT bill ORDER BY created_at DESC LIMIT 1
                    try {
                        b = billDAO.getLatestByRoom(slRoom.getNum());
                    } catch (Exception ignore) {
                    }
                }
            }

            // 3) Nếu không còn bill phù hợp → reset UI
            if (b == null) {
                clearPaymentUI();
                // nếu vẫn muốn set nút theo persistedStatus thì bật dòng dưới
                setUpBtnByBillStatus(persistedStatus);
                return;
            }

            // 4) Gán lại idBill & đổ dữ liệu đầy đủ lên UI
            idBill = b.getId();
            billData(b);          // billData đã set: total_service, total_time, total, bảng dịch vụ, ngày...
            calculateTotal();     // tính lại tổng hiển thị (sau giảm nếu bạn đang áp giảm)

            // 5) Đảm bảo hiển thị đúng ĐÃ THU và ĐẶT CỌC từ DB
            acutal_pay.setText(pc.formatPrice(b.getActual_pay()));
            deposit.setText(pc.formatPrice(b.getDeposit()));

            // 6) Bật/tắt nút theo trạng thái bill
            setUpBtnByBillStatus(b.getStatus());

            // 7) Điều chỉnh UI phụ
            setTitle(
                    b.getStatus() == 0 ? "Hoá đơn đã thanh toán"
                    : b.getStatus() == -1 ? "Đặt trước"
                    : b.getStatus() == 1 ? "Đang sử dụng" : "Thanh toán"
            );

            // In chỉ hiển thị khi ĐÃ THANH TOÁN hoặc HỦY
            printBtn.setVisible(b.getStatus() == 0 || b.getStatus() == -2);

            // Khóa sửa ngày & bảng DV nếu đã thanh toán/hủy
            boolean editable = (b.getStatus() == 1 || b.getStatus() == -1);
            checkInField.setEnabled(editable);
            checkOutField.setEnabled(editable);
            serviceTable.setEnabled(editable);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải lại hoá đơn sau khi lưu: " + ex.getMessage());
        }
    }

// Dùng khi không tìm thấy bill hợp lệ để hiển thị (reset màn hình)
    private void clearPaymentUI() {
        idBill = -1;
        totalService.setText(pc.formatPrice(0));
        totalRoom.setText(pc.formatPrice(0));
        totalAmountField.setText(pc.formatPrice(0));
        acutal_pay.setText(pc.formatPrice(0));
        deposit.setText(pc.formatPrice(0));
        discountCodeField.setText("");
        discountPercentLable.setText("0%");
        if (tableModel != null) {
            tableModel.setRowCount(0);
        }
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
        String[] statusOptions = {"Đang dùng", "Hoàn tất", "Đặt trước"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setSelectedItem("Chưa hoàn tất");

        // Khởi tạo bảng
        String[] columnNames = {"TÊN SP - DV", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        tableModel = new DefaultTableModel(columnNames, 0);

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

        acutal_pay.setText("0");

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
        add(createTotalPanel(), BorderLayout.SOUTH);
        initAndsetupDate();
    }

    private void billData(Bill b) {
        idBill = b.getId();
        Customer csInfo = clc.getById(b.getUser());
        customerCBX.setSelectedItem(csInfo);
        checkInField.setDate(b.getCheck_in());
        checkOutField.setDate(b.getCheck_out());
        totalService.setText(pc.formatPrice(b.getTotal_service()));
        totalRoom.setText(pc.formatPrice(b.getTotal_time()));
        totalAmountField.setText(pc.formatPrice(b.getTotal()));
        acutal_pay.setText(pc.formatPrice(b.getActual_pay()));
        deposit.setText(pc.formatPrice(b.getDeposit()));

        // 🚫 Quan trọng: xoá bảng trước khi nạp lại để không bị nhân đôi
        if (tableModel != null) {
            tableModel.setRowCount(0);
        }

        // nạp lại detail từ DB
        pc.setTblData(tableModel, new BillDetailDAO().getByBill(b.getId()));

        // chuẩn hoá đúng: col2 = đơn giá, col3 = thành tiền (nếu bạn đã thêm 2 hàm helper trước đó)
        normalizeServiceTable();
        calculateTotal();
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
        // ... trong topRow (thay thế đoạn bạn đang tạo biến local)
        topRow.add(new JLabel("Mã giảm giá:"));
        discountCodeField = new JTextField(10);

// DÙNG field đã khai báo ở class: discountPercentLable
        discountPercentLable.setText("0%");
        discountPercentLable.setPreferredSize(new Dimension(40, 25));
        discountPercentLable.setHorizontalAlignment(SwingConstants.CENTER);

        topRow.add(discountCodeField);
        topRow.add(discountPercentLable);

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
                if (selected == null) {
                    return;
                }

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
        String infor = "Phòng " + slRoom.getNum();
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

    private void initAndsetupDate() {
        if (booked) {
            Calendar cal = Calendar.getInstance();
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
        } else {
            Calendar cal = Calendar.getInstance();
            checkInField.getJCalendar().setMinSelectableDate(cal.getTime());
            checkOutField.getJCalendar().setMinSelectableDate(cal.getTime());
            checkInField.setDate(cal.getTime());
            checkInField.setEnabled(false);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            checkOutField.setMaxSelectableDate(cal.getTime());
        }

        checkInField.addPropertyChangeListener("date", e -> {
            String time = pc.getUsedTime(checkInField, checkOutField);
            if (time.contains(" giờ")) {
                hour = Integer.parseInt(time.replace(" giờ", ""));
            } else {
                days = Integer.parseInt(time.replace(" ngày", ""));
            }

            sumTotalRoom();
        });

        checkOutField.addPropertyChangeListener("date", e -> {
            String time = pc.getUsedTime(checkInField, checkOutField);
            if (time.contains(" giờ")) {
                hour = Integer.parseInt(time.replace(" giờ", ""));
            } else {
                days = Integer.parseInt(time.replace(" ngày", ""));
            }
            sumTotalRoom();
        });
    }

    private void setUpBtn() {
        if (!booked) {
            if (idBill != -1) {
                if (slRoom.getStatus() == 0) {
                    saveBtn.setEnabled(false);
                    finishBtn.setEnabled(false);
                    printBtn.setVisible(true);
                } else if (slRoom.getStatus() == -1) {
                    java.util.Date in = checkInField.getDate();
                    LocalDateTime now = LocalDateTime.now();
                    if (now.compareTo(in.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) < 0) {
                        cancelBtn.setVisible(true);
                        finishBtn.setEnabled(false);
                    } else {
                        saveBtn.setText("Nhận phòng");
                        cancelBtn.setVisible(false);
                        finishBtn.setEnabled(true);
                    }
                }
            } else {
                finishBtn.setEnabled(false);
            }
        } else {
            finishBtn.setEnabled(false);
        }
    }

    // === ADD: đặt trong class Payment ===
    private void normalizeServiceTable() {
        // Đảm bảo mọi dòng đều có: col2 = đơn giá, col3 = SL * đơn giá
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int qty = 0;
            try {
                qty = Integer.parseInt(String.valueOf(tableModel.getValueAt(i, 1)).trim());
            } catch (Exception ignored) {
            }
            double unit = parseMoney(tableModel.getValueAt(i, 2));
            // Nếu lỡ col2 đang là 'thành tiền' => suy đơn giá từ col3
            if (unit <= 0) {
                double curTotal = parseMoney(tableModel.getValueAt(i, 3));
                if (qty > 0 && curTotal > 0) {
                    unit = curTotal / qty;
                }
            }
            // Gán lại đúng chuẩn
            tableModel.setValueAt(pc.formatPrice(unit), i, 2);                 // đơn giá
            tableModel.setValueAt(pc.formatPrice(unit * Math.max(0, qty)), i, 3); // thành tiền
        }
    }

// === ADD: đặt trong class Payment ===
    private static double parseMoney(Object v) {
        if (v == null) {
            return 0;
        }
        String s = v.toString().replaceAll("[^0-9.-]", "").trim();
        if (s.isEmpty() || "-".equals(s)) {
            return 0;
        }
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private void setupEventHandlers() {
        // Thêm event handler cho việc thay đổi số lượng trong bảng
        serviceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = serviceTable.getSelectedRow();
                    if (row != -1) {
                        double unitPrice = Double.parseDouble(serviceTable.getValueAt(row, 2).toString().replaceAll(",", "").trim()); // ✅ cột 2
                        int currentQty = Integer.parseInt(tableModel.getValueAt(row, 1).toString());
                        String input = JOptionPane.showInputDialog(rootPane, "Số lượng: ", currentQty);
                        if (input == null) {
                            return;
                        }
                        int sl = Math.max(0, Integer.parseInt(input.trim()));
                        tableModel.setValueAt(sl, row, 1);
                        tableModel.setValueAt(pc.formatPrice(sl * unitPrice), row, 3); // ✅ chỉ cập nhật thành tiền
                        calculateTotal();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    JMenuItem delSer = new JMenuItem("Xóa");
                    JPopupMenu menu = new JPopupMenu();
                    delSer.addActionListener((l) -> {
                        if (JOptionPane.showConfirmDialog(rootPane, "Xóa các dịch vụ đã chọn ?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
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
                List<Object> datasrc = pc.setDataPrint(pc.getBDList(idBill), slRoom, pc.getBill(idBill));
                Map<String, Object> mainparam = pc.setupParameter(pc.getBill(idBill), "");
                pc.printed(datasrc, mainparam);
            }
        });

        exitBtn.addActionListener(e -> {
            dispose();
        });

        discountCodeField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                int pct = pc.autoGetDisValue(discountCodeField.getText().trim(), discountLst);
                discountPercentLable.setText(pct + "%");
                calculateTotal(); // đổi mã giảm giá xong thì tính lại tổng phải trả (KHÔNG đụng actual_pay)
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }
        });

    }

    private void btnAction(int status) {
        Customer cs = (Customer) customerCBX.getSelectedItem();
        if (checkInField.getDate() == null || checkOutField.getDate() == null) {
            JOptionPane.showMessageDialog(rootPane, "Chọn ngày đặt phòng !!!!");
            return;
        }
        Timestamp dt_in = new Timestamp(checkInField.getDate().getTime());
        Timestamp dt_out = new Timestamp(checkOutField.getDate().getTime());

        double totalroom = Double.parseDouble(totalRoom.getText().replaceAll("[^0-9]", ""));
        int totalservice = Integer.parseInt(totalService.getText().replaceAll("[^0-9]", ""));
        double total = totalroom + totalservice;
        double extra_charge = idBill > 0 ? pc.billExtra_chagre(idBill) : 0;

        int pct = 0;
        try {
            pct = Integer.parseInt(discountPercentLable.getText().replace("%", "").trim());
        } catch (Exception ignored) {
        }
        double discountMoney = total * pct / 100.0;
        double amountDue = total - discountMoney + extra_charge;

        double actual_pay_to_save;
        double deposit_to_save;
        int stats = status;

        if (status == -1) { // ĐẶT TRƯỚC
            actual_pay_to_save = 0;                         // chưa thu
            // nếu có quy tắc đặt cọc: ví dụ 50%
            deposit_to_save = booked ? amountDue * 0.5 : 0; // hoặc giữ theo field deposit người dùng nhập
        } else if (status == 0) { // HOÀN TẤT/THANH TOÁN
            actual_pay_to_save = amountDue;                 // thu đủ khi trả phòng
            deposit_to_save = 0;                            // hoặc giữ nguyên nếu bạn muốn show cọc đã thu trước đó
        } else { // ĐANG DÙNG
            actual_pay_to_save = (idBill > -1) ? pc.getBill(idBill).getActual_pay() : 0; // mặc định không đổi
            deposit_to_save = booked ? amountDue * 0.5 : 0; // tùy nghiệp vụ, có thể = 0
        }

        Bill b = new Bill(
                idBill, slRoom.getNum(), cs.getId(),
                dt_in, dt_out,
                totalroom, totalservice, total,
                extra_charge, discountMoney, // discount là SỐ TIỀN, không phải %
                deposit_to_save, actual_pay_to_save, status
        );
        if (JOptionPane.showConfirmDialog(rootPane, "Luu hoa don", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0 && (idBill == -1 ? pc.insertBill(b) : pc.uptBill(b)) != 0) {
            // 1) Lấy đúng bill vừa lưu theo phòng + trạng thái vừa lưu
            slRoom.setStatus(stats);
            Bill justSaved = pc.getRoomBill(slRoom.getNum(), stats);
            int bdID = (justSaved != null) ? justSaved.getId() : idBill;
            if (bdID <= 0) {
                JOptionPane.showMessageDialog(rootPane, "Không tìm thấy ID hoá đơn để lưu chi tiết.");
                return;
            }

// 2) XÓA SẠCH detail của bill này trước khi chèn lại (tránh cộng dồn ở DB)
            pc.delAllByid(bdID);

// 3) Map tên → id dịch vụ để không bị vòng lặp vô hạn
            List<Service> svAll = new ServiceDAO().getAll();
            Map<String, Integer> nameToId = new HashMap<>();
            for (Service s : svAll) {
                nameToId.put(s.getName().toLowerCase(Locale.ROOT), s.getId());
            }

// 4) Duyệt từng dòng đang hiển thị trên bảng và insert lại
            for (int i = 0; i < serviceTable.getRowCount(); i++) {
                String name = String.valueOf(serviceTable.getValueAt(i, 0)).trim();
                Integer sId = nameToId.get(name.toLowerCase(Locale.ROOT));
                if (sId == null) {
                    continue;
                }

                int quan = 0;
                try {
                    quan = Integer.parseInt(serviceTable.getValueAt(i, 1).toString().trim());
                } catch (Exception ignored) {
                }

                int ttl = 0;
                try {
                    ttl = Integer.parseInt(serviceTable.getValueAt(i, 3).toString().replace(",", "").trim());
                } catch (Exception ignored) {
                }

                if (quan > 0) {
                    pc.insertDetail(new BillDetail(bdID, sId, quan, ttl));
                }
            }

            JOptionPane.showMessageDialog(rootPane, "Lưu thành công");
            try {
                int newRoomStatus = status; // map theo hệ thống của bạn
                // ví dụ: đã thanh toán -> room về 0 (trống)
                if (status == 0) {
                    newRoomStatus = 0;
                }
                // đặt trước giữ -1, đang dùng giữ 1

                slRoom.setStatus(newRoomStatus);
                pc.uptRoom(slRoom); // (hoặc new RoomDAO().uptRoom(slRoom))
            } catch (Exception ignore) {
            }

// 2) Refresh lại Payment để cập nhật nút/tiền cho đúng
            SwingUtilities.invokeLater(() -> {
                refreshAfterPayment(status);    // đã viết ở các bước trước
                setUpBtnByBillStatus(status);   // đảm bảo nút đúng tức thì
            });

// 3) Báo cho Dashboard/BookedHistory reload ngay
            fireDashboardRefresh(slRoom.getStatus());
            if (stats == 0) {
                saveBtn.setEnabled(false);
                printBtn.setVisible(true);
            }
            if (stats == -1) {
                slRoom.setStatus(0);
                pc.uptRoom(slRoom);
                idBill = bdID;
                dispose();
            } else {
                saveBtn.setText("💾 Lưu");
                slRoom.setStatus(stats);
                pc.uptRoom(slRoom);
                idBill = bdID;
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Lỗi! Lưu thất bại");
        }
    }

    public void delTableItems(int[] row, JTable tbl) {
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        for (int i = row.length - 1; i >= 0; i--) {
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
        double roomRate = rt.getPricePH(slRoom.getType(), days > 0 ? true : false);
        double totalRoomPrice = roomRate * (days > 0 ? days : hour);
        totalRoom.setText(pc.formatPrice(totalRoomPrice));
        calculateTotal();
    }

    private void calculateTotal() {
        long tongDichVu = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object thanhTienObj = tableModel.getValueAt(i, 3);
            if (thanhTienObj != null && !thanhTienObj.toString().trim().isEmpty()) {
                try {
                    String thanhTienStr = thanhTienObj.toString().replaceAll(",", "");
                    if (!thanhTienStr.isEmpty()) {
                        tongDichVu += Long.parseLong(thanhTienStr);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        totalService.setText(pc.formatPrice(tongDichVu));
        long tienPhong = Long.parseLong(totalRoom.getText().replaceAll(",", "").trim());
        long tongThanhToan = tienPhong + tongDichVu;

        // Áp giảm giá (nếu muốn hiển thị tổng phải trả sau giảm)
        int pct = 0;
        try {
            pct = Integer.parseInt(discountPercentLable.getText().replace("%", "").trim());
        } catch (Exception ignored) {
        }
        long tongSauGiam = Math.round(tongThanhToan * (1 - pct / 100.0));

        totalAmountField.setText(pc.formatPrice(tongSauGiam));

        // TUYỆT ĐỐI KHÔNG động vào acutal_pay ở đây
        // Đặt cọc chỉ hiển thị nếu là luồng đặt phòng (booked == true) nhưng không cộng vào actual_pay
        if (booked) {
            // ví dụ: hiển thị gợi ý đặt cọc 50% (tuỳ quy định)
            long suggestedDeposit = Math.round(tongSauGiam * 0.5);
            deposit.setText(pc.formatPrice(suggestedDeposit));
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Payment(1, false).setVisible(true);
        });
    }
}
