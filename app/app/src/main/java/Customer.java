package com.example.lewandowski.bank;

/**
            Customer Class
            Defines an customer to store customer details
            Author: Szymon Bialkowski
            Date: 02-11-2017
 */

public class Customer
{
    private int registrationNo;
    private int PAC;
    private String name;
    private double balance;

    public Customer()
    {}

    public Customer(int registrationNo, int PAC, String name, double balance)
    {
        this.registrationNo = registrationNo;
        this.PAC            = PAC;
        this.name           = name;
        this.balance        = balance;
    }

    public int getRegistrationNo()
    {
        return registrationNo;
    }

    public void setRegistrationNo(int registrationNo) {
        this.registrationNo = registrationNo;
    }

    public int getPAC() {
        return PAC;
    }

    public void setPAC(int PAC) {
        this.PAC = PAC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
