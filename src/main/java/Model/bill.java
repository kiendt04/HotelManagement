/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class bill {
    private int id,total_service;
    private String room,user;
    private Date check_in,check_out;
    private double total,total_time;
    private int status;

    public bill() {
    }

    public bill(int id, String room, String user, Date check_in, Date check_out,double total_time,int total_service, double total, int status) {
        this.id = id;
        this.room = room;
        this.user = user;
        this.check_in = check_in;
        this.check_out = check_out;
        this.total = total;
        this.status = status;
        this.total_time = total_time;
        this.total_service = total_service;
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

    public Date getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Date check_in) {
        this.check_in = check_in;
    }

    public Date getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Date check_out) {
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
    
    
}
