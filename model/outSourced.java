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
public class outSourced extends part {
    private String companyName; 
    
    public outSourced(int partID, String name, double price, int numInStock, int min, int max, String company) {
        
        setPartID(partID);
        setName(name);
        setPrice(price);
        setInStock(numInStock);
        setMin(min);
        setMax(max);
        setCompanyName(company);
    }
    
    public void setCompanyName(String name) {
        this.companyName = name;
    }
    
    public String getCompanyName() {
        return companyName;
    }
}
