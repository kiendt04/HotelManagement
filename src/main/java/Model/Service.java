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
public class Service {

    private int id,quant;
    private String name,unit;
    private double price;

    public Service() {
    }

    public Service(int id, String name, double price, int quant,String unit) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quant = quant;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    @Override
    public String toString() {
        return name +" - " +  formatPrice(price);
    }
    
    private String formatPrice(double p)
    {
        DecimalFormat df = new DecimalFormat("00,000");
        return df.format(p);
    }
}
