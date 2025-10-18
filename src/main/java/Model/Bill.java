/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;
import java.sql.Timestamp;


/**
 *
 * @author ADMIN
 */
public class Bill {
    private int id,total_service;
    private String room,user;
    private Timestamp check_in,check_out;
    private double total,total_time,actual_pay,exchange,disount;
    private int status;

    public Bill() {
    }

    public Bill(int id, String room, String user, Timestamp check_in, Timestamp check_out,double total_time,int total_service, double total,double exchange, double discount, double actual_pay, int status) {
        this.id = id;
        this.room = room;
        this.user = user;
        this.check_in = check_in;
        this.check_out = check_out;
        this.total = total;
        this.status = status;
        this.total_time = total_time;
        this.total_service = total_service;
        this.actual_pay = actual_pay;
        this.exchange = exchange;
        this.disount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Timestamp getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Timestamp check_in) {
        this.check_in = check_in;
    }

    public Timestamp getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Timestamp check_out) {
        this.check_out = check_out;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal_service() {
        return total_service;
    }

    public void setTotal_service(int total_service) {
        this.total_service = total_service;
    }

    public double getTotal_time() {
        return total_time;
    }

    public void setTotal_time(double total_time) {
        this.total_time = total_time;
    }

    public double getActual_pay() {
        return actual_pay;
    }

    public void setActual_pay(double actual_pay) {
        this.actual_pay = actual_pay;
    }

    public double getExchange() {
        return exchange;
    }

    public void setExchange(double exchange) {
        this.exchange = exchange;
    }

    public double getDisount() {
        return disount;
    }

    public void setDisount(double disount) {
        this.disount = disount;
    }
    
    
    
}
