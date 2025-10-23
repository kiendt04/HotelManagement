package View;

import Control.ServiceControl;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

import Model.Service;

public class ServiceManager extends JFrame {

    private ServiceControl serviceControl = new ServiceControl();
    private DefaultTableModel model;
    private List<Service> list;
    private JPanel header, main, footer;
    private JButton save, addBtn, remove, upt;
    private JTable tbl;
    private JTextField nameService, priceService,quantService,idService, donviService;
    private int func = -1, row = -1;
    private ImageIcon addIcon, rmIcon, uptIcon, saveIcon, clIcon;

    public ServiceManager() throws HeadlessException {
        super("Danh mục dịch vụ");
        this.setResizable(false);
        this.setSize(new Dimension(600, 420));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 0));
        initComp();
        initUI();
        action();
        loadData();
        this.setVisible(true);
    }

    public void initComp() {
        addIcon = new ImageIcon(getClass().getResource("/img/add.png"));
        rmIcon = new ImageIcon(getClass().getResource("/img/trash.png"));
        uptIcon = new ImageIcon(getClass().getResource("/img/refresh.png"));
        saveIcon = new ImageIcon(getClass().getResource("/img/check.png"));
        clIcon = new ImageIcon(getClass().getResource("/img/cross.png"));

        header = new JPanel();
        header.setPreferredSize(new Dimension(0, 40));
        main = new JPanel(new BorderLayout());
        footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setPreferredSize(new Dimension(0, 100));

        save = new JButton(saveIcon);
        save.setEnabled(false);
        addBtn = new JButton(addIcon);
        remove = new JButton(rmIcon);
        upt = new JButton(uptIcon);
        tbl = new JTable();
        tbl.setAutoCreateRowSorter(true);

        nameService = new JTextField(10);
        priceService = new JTextField(10);
        quantService = new JTextField();
        idService = new JTextField();
        donviService = new JTextField();
    }

    public void initUI() {
        header.add(addBtn);
        header.add(remove);
        header.add(upt);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        this.add(header, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.addColumn("STT");
        model.addColumn("Tên dịch vụ");
        model.addColumn("Giá dịch vụ");
        model.addColumn("Số lượng");
        model.addColumn("Đơn vị");
        tbl.setModel(model);
        JScrollPane src = new JScrollPane(tbl);
        src.setBorder(new TitledBorder("Dịch vụ"));
        main.add(src, BorderLayout.CENTER);
        this.add(main, BorderLayout.CENTER);

//        JPanel p1 = new JPanel();
//        JPanel p2 = new JPanel();
//        p2.setPreferredSize(new Dimension(0, 50));
//        p1.add(new JLabel("Tên dịch vụ: "));
//        p1.add(nameService);
//        p1.add(new JLabel("Giá dịch vụ: "));
//        p1.add(priceService);
//        //p2.add(upt);
//        footer.add(p1);
//        footer.add(p2);
//        footer.setBorder(new TitledBorder("Thông tin"));
//        this.add(footer, BorderLayout.SOUTH);
    }

    public void loadData() {
        list = serviceControl.getAllServices();
        model.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            Service s = list.get(i);
            model.addRow(new Object[]{i + 1, s.getName(), s.getPrice(), s.getQuant(),s.getUnit()});
        }
    }

    public void clearText() {
        nameService.setText("");
        priceService.setText("");
    }

    public void action() {
        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                row = tbl.getSelectedRow();
                if (row != -1) {
                    Service s = list.get(row);
                    nameService.setText(s.getName());
                    priceService.setText(String.valueOf(s.getPrice()));
                    quantService.setText(String.valueOf(s.getQuant()));
                    idService.setText(String.valueOf(s.getId()));
                    donviService.setText(s.getUnit());
                }
            }
        });

        addBtn.addActionListener(e -> {
            ServiceDialog sdl = new ServiceDialog(this, null);
            sdl.showDialog();
            loadData();
        });

        upt.addActionListener(e -> {
            if (row == -1) {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn một dịch vụ");
                return;
            }

            String name = nameService.getText().trim();
            String priceStr = priceService.getText().trim();
            String quantStr = quantService.getText().trim();
            // doan nay phai co data no ms ko bi loi nhe
            String unitStr = donviService.getText().trim();
            // Kiểm tra tính hợp lệ của dữ liệu thông qua controller
            String errorMessage = serviceControl.getValidationErrorMessage(name, priceStr, quantStr, unitStr);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(rootPane, errorMessage);
                return;
            }
            Service crt = serviceControl.createService(name, priceStr, quantStr, unitStr);
            crt.setId(Integer.parseInt(idService.getText().trim()));
            ServiceDialog uptDia = new ServiceDialog(this,crt);
            uptDia.showDialog();
            loadData();
        });

        remove.addActionListener(e -> {
            if (row == -1) {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn một dịch vụ để xoá");
                return;
            }

            Service s = list.get(row);
            if (JOptionPane.showConfirmDialog(rootPane, "Xoá dịch vụ này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                if (serviceControl.deleteService(s.getId())) {
                    loadData();
                    clearText();
                    JOptionPane.showMessageDialog(rootPane, "Xoá thành công");
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Xoá thất bại");
                }
            }
        });

//        save.addActionListener(e -> {
//            String name = nameService.getText().trim();
//            String priceStr = priceService.getText().trim();
//            
//            // Kiểm tra tính hợp lệ của dữ liệu thông qua controller
//            String errorMessage = serviceControl.getValidationErrorMessage(name, priceStr);
//            if (errorMessage != null) {
//                JOptionPane.showMessageDialog(rootPane, errorMessage);
//                return;
//            }
//
//            try {
//                Service s = serviceControl.createService(0, name, priceStr);
//                if (s != null && serviceControl.addService(s)) {
//                    loadData();
//                    JOptionPane.showMessageDialog(rootPane, "Thêm thành công");
//                    addBtn.setIcon(addIcon);
//                    save.setEnabled(false);
//                    remove.setEnabled(true);
//                    upt.setEnabled(true);
//                    func = -1;
//                    clearText();
//                } else {
//                    JOptionPane.showMessageDialog(rootPane, "Thêm thất bại");
//                }
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra khi thêm dịch vụ");
//            }
//        });
    }

    private class ServiceDialog extends JDialog {
    private JTextField txtId, txtName, txtPrice, txtQuantity, txtDonvi;
    private JButton btnSave, btnCancel;

    public ServiceDialog(Frame parent, Service sr) {
        super(parent, sr!=null ? "Sửa dịch vụ" : "Thêm dịch vụ", true);
        this.setUndecorated(true);
        this.getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setSize(400, 280);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ======= Form nhập liệu =======
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblId = new JLabel("Mã dịch vụ:");
        JLabel lblName = new JLabel("Tên dịch vụ:");
        JLabel lblPrice = new JLabel("Giá:");
        JLabel lblQuantity = new JLabel("Số lượng:");
        JLabel lblUnit = new JLabel("Đơn vị:");

        txtId = new JTextField();
        txtId.setEnabled(false);
        txtName = new JTextField();
        txtPrice = new JTextField();
        txtQuantity = new JTextField();
        txtDonvi = new JTextField();
        
        if(sr != null)
        {
            txtName.setText(sr.getName());
            txtId.setText(sr.getId() + "");
            txtPrice.setText(sr.getPrice() + "");
            txtQuantity.setText(sr.getQuant() + "");
            txtDonvi.setText(sr.getUnit());
        }

        formPanel.add(lblId); formPanel.add(txtId);
        formPanel.add(lblName); formPanel.add(txtName);
        formPanel.add(lblPrice); formPanel.add(txtPrice);
        formPanel.add(lblQuantity); formPanel.add(txtQuantity);
        formPanel.add(lblUnit); formPanel.add(txtDonvi);

        add(formPanel, BorderLayout.CENTER);

        // ======= Panel nút =======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);
        serviceControl.action(btnSave,btnCancel,txtId,txtName,txtPrice,txtQuantity,txtDonvi, this);
    }
    
    public void showDialog()
    {
        this.setVisible(true);
    }
    }
    public static void main(String[] args) {
        new ServiceManager();
    }
}