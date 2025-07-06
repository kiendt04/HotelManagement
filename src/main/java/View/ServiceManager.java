package View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

import Control.*;
import Model.Service;

public class ServiceManager extends JFrame {

    private ServiceControl uc = new ServiceControl();
    private DefaultTableModel model;
    private List<Service> list = uc.getAll();
    private JPanel header, main, footer;
    private JButton save, addBtn, remove, upt;
    private JTable tbl;
    private JTextField nameService, priceService;
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
        uptIcon = new ImageIcon(getClass().getResource("/img/save.png"));
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
    }

    public void initUI() {
        header.add(addBtn);
        header.add(remove);
        header.add(save);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        this.add(header, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.addColumn("STT");
        model.addColumn("Tên dịch vụ");
        model.addColumn("Giá dịch vụ");
        tbl.setModel(model);
        JScrollPane src = new JScrollPane(tbl);
        src.setBorder(new TitledBorder("Dịch vụ"));
        main.add(src, BorderLayout.CENTER);
        this.add(main, BorderLayout.CENTER);

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(0, 50));
        p1.add(new JLabel("Tên dịch vụ: "));
        p1.add(nameService);
        p1.add(new JLabel("Giá dịch vụ: "));
        p1.add(priceService);
        p2.add(upt);
        footer.add(p1);
        footer.add(p2);
        footer.setBorder(new TitledBorder("Thông tin"));
        this.add(footer, BorderLayout.SOUTH);
    }

    public void loadData() {
        list = uc.getAll();
        model.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            Service s = list.get(i);
            model.addRow(new Object[]{i + 1, s.getName(), s.getPrice()});
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
                }
            }
        });

        addBtn.addActionListener(e -> {
            if (func == 0 && JOptionPane.showConfirmDialog(rootPane, "Dừng thêm mới?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                save.setEnabled(false);
                remove.setEnabled(true);
                upt.setEnabled(true);
                addBtn.setIcon(addIcon);
                clearText();
                func = -1;
            } else {
                save.setEnabled(true);
                remove.setEnabled(false);
                upt.setEnabled(false);
                addBtn.setIcon(clIcon);
                clearText();
                func = 0;
            }
        });

        upt.addActionListener(e -> {
            if (row == -1) {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn một dịch vụ");
                return;
            }

            try {
                String name = nameService.getText().trim();
                double price = Double.parseDouble(priceService.getText().trim());
                Service old = list.get(row);
                Service updated = new Service(old.getId(), name, price);

                if (JOptionPane.showConfirmDialog(rootPane, "Cập nhật thông tin?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0
                        && uc.uptRoom(updated) == 1) {
                    loadData();
                    clearText();
                    JOptionPane.showMessageDialog(rootPane, "Cập nhật thành công");
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Cập nhật thất bại");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(rootPane, "Dữ liệu không hợp lệ");
            }
        });

        remove.addActionListener(e -> {
            if (row == -1) {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn một dịch vụ để xoá");
                return;
            }

            Service s = list.get(row);
            if (JOptionPane.showConfirmDialog(rootPane, "Xoá dịch vụ này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0
                    && uc.delRoom(s.getId()) > 0) {
                loadData();
                clearText();
                JOptionPane.showMessageDialog(rootPane, "Xoá thành công");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Xoá thất bại");
            }
        });

        save.addActionListener(e -> {
            try {
                String name = nameService.getText().trim();
                double price = Double.parseDouble(priceService.getText().trim());

                Service s = new Service(0, name, price);
                if (uc.insertRoom_type(s) > 0) {
                    loadData();
                    JOptionPane.showMessageDialog(rootPane, "Thêm thành công");
                    addBtn.setIcon(addIcon);
                    save.setEnabled(false);
                    remove.setEnabled(true);
                    upt.setEnabled(true);
                    func = -1;
                    clearText();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Thêm thất bại");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(rootPane, "Giá tiền không hợp lệ");
            }
        });
    }

    public static void main(String[] args) {
        new ServiceManager();
    }
}
