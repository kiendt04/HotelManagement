/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.text.DecimalFormat;

/**
 *
 * @author ADMIN
 */
public class Room {
    
    private String num,note;
    private int id,type,status,floor;
    private double ppn,pph;
    public Room() {
    }

    public Room(int id, String num,int floor, int type,int status, String note ,double pph, double ppn) {
        this.num = num;
        this.note = note;
        this.id = id;
        this.type = type;
        this.status = status;
        this.floor = floor;
        this.pph = pph;
        this.ppn = ppn;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getPpn() {
        return ppn;
    }

    public void setPpn(double ppn) {
        this.ppn = ppn;
    }

    public double getPph() {
        return pph;
    }

    public void setPph(double pph) {
        this.pph = pph;
    }

    @Override
    public String toString() {
        return "Phòng" + num + " - " + formatDouble(ppn) + " VND/đêm";
    }
    
    private String formatDouble(double d)
    {
        DecimalFormat df = new DecimalFormat("00,000");
        return df.format(d);
    }
}
