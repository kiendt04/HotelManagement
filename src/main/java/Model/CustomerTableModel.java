/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ADMIN
 */
public class CustomerTableModel extends AbstractTableModel{

    private List<Customer> ds;
    private String[] header = {"Id/PassPort","Name","Gender","Phone","Region"};

    public CustomerTableModel(List<Customer> ds)
    {
        this.ds = ds;
    }
    
    @Override
    public int getRowCount() {
        return ds.size();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer cs = ds.get(rowIndex);
        switch (columnIndex) {
            case 0: return cs.getId();
            case 1: return cs.getName();
            case 2: return cs.getGender();
            case 3: return cs.getSdt();
            case 4: return cs.getRegion();
            default: return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return header[column];
    }
    
    public void addCustomer(Customer c) {
        ds.add(c);
        fireTableRowsInserted(ds.size() - 1, ds.size() - 1);
    }
    
    public void uptCus(Customer c,int row)
    {
        ds.remove(row);
        ds.add(row, c);
    }
    
    public void delCus(int row)
    {
        ds.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public void loadData(List<Customer> list)
    {
        this.ds = list;
        fireTableDataChanged();
    }
}
