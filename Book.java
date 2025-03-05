/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book.store.application;

import javafx.scene.control.CheckBox;

/**
 *
 * @author monir
 */
public class Book {
    private String name;
    private double price;
    private CheckBox select;
    
    public Book(){
        this.name = "";
        this.price = 0;
        this.select = new CheckBox();
    }
    
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
        this.select = new CheckBox();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }
    
    
}
