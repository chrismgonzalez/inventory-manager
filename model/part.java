/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author chris
 */
public abstract class part {
    
    protected int partID;
    protected String partName;
    protected Double partPrice = 0.0;
    protected int partInStock;
    protected int min;
    protected int max;
    
    public void setName(String name) {
        this.partName = name;
    }
    
    public String getName() {
        return this.partName;
    }
    
    public void setPrice(double price) {
        this.partPrice = price;
    }
    
    public double getPrice() {
        return partPrice;
    }
    
    public void setInStock(int quantity) {
        this.partInStock = quantity;
    }
    
    public int getInStock() {
        return this.partInStock;
    }
    
    public void setMin(int min) {
        this.min = min;
    }
    
    public int getMin() {
        return this.min;
    }
    
    public void setMax(int max) {
        this.max = max;
    }
    
    public int getMax() {
        return this.max;
    }
    
    public void setPartID(int partID) {
        this.partID = partID;
    }
    
    public int getPartID() {
        return this.partID;
    }
}
