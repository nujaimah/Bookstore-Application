/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book.store.application;

/**
 *
 * @author monir
 */
public class Customer {
    private String username;
    private String password;
    private Status customerStatus;
    private double points;
    
    public Customer() {
        this.username = "";
        this.password = "";
        this.points = 0;
        checkStatus();
    }
    
    public Customer(String username, String password, double points) {
        this.username = username;
        this.password = password;
        this.points = points;
        checkStatus();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getPoints() {
        return points;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPoints(double points) {
        this.points = points;
    }
    
    public void checkStatus() {
        if (getPoints() >= 1000) 
            customerStatus = new Gold();
        else
            customerStatus = new Silver();   
    }
    
    public String getStatusName() {
        return customerStatus.statusName();
    }
    
}

