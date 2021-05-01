package com.ta.model;
/*
Represents Debt information
 */
public class Debt {

    private int id;
    private float amount;

    public Debt(int id, float amount){
        this.id = id;
        this.amount = amount;
    }
    public int getId(){
        return this.id;
    }

    public float getAmount(){
        return this.amount;
    }
}
