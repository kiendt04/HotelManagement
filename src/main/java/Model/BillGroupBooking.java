/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.sql.Timestamp;
/**
 *
 * @author ADMIN
 */
public class BillGroupBooking {
    private int id,status;
    private String cus;
    private Timestamp in,out;
    private double total_room,total_ser,total,ex_charge,discount,deposit,actual_pay;

    public BillGroupBooking(int id, String cus, Timestamp in, Timestamp out, double total_room, double total_ser, double total, double ex_charge, double discount, double deposit,double actual_pay, int status) {
        this.id = id;
        this.status = status;
        this.cus = cus;
        this.in = in;
        this.out = out;
        this.total_room = total_room;
        this.total_ser = total_ser;
        this.total = total;
        this.ex_charge = ex_charge;
        this.discount = discount;
        this.actual_pay = actual_pay;
        this.deposit = deposit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCus() {
        return cus;
    }

    public void setCus(String cus) {
        this.cus = cus;
    }

    public Timestamp getIn() {
        return in;
    }

    public void setIn(Timestamp in) {
        this.in = in;
    }

    public Timestamp getOut() {
        return out;
    }

    public void setOut(Timestamp out) {
        this.out = out;
    }

    public double getTotal_room() {
        return total_room;
    }

    public void setTotal_room(double total_room) {
        this.total_room = total_room;
    }

    public double getTotal_ser() {
        return total_ser;
    }

    public void setTotal_ser(double total_ser) {
        this.total_ser = total_ser;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getEx_charge() {
        return ex_charge;
    }

    public void setEx_charge(double ex_charge) {
        this.ex_charge = ex_charge;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getActual_pay() {
        return actual_pay;
    }

    public void setActual_pay(double actual_pay) {
        this.actual_pay = actual_pay;
    }
    
    
    
}
