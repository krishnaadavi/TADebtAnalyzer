package com.ta.model;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/*
Represents payment plan information.
 */
public class PaymentPlan {
    private float amountToPay;
    private int debtId;
    private int id;
    private float installmentAmount;
    private String installmentFrequency;
    private Date startDate;
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public PaymentPlan(int id, int debtId, float amountToPay, float installmentAmount,
                       String installmentFrequency, String startDate) {
        this.id = id;
        this.debtId = debtId;
        this.amountToPay = amountToPay;
        this.installmentAmount = installmentAmount;
        this.installmentFrequency = installmentFrequency;
        try{
            this.startDate = formatter.parse(startDate);
        }catch(ParseException pe){
            this.startDate = null;
        }
    }
    public float getAmountToPay(){
        return this.amountToPay;
    }

    public int getDebtId(){
        return this.debtId;
    }

    public int getId(){
        return this.id;
    }

    public float getInstallmentAmount(){
        return this.installmentAmount;
    }

    public String getInstallmentFrequency(){
        return this.installmentFrequency;
    }
    public Date getStartDate(){
        return this.startDate;
    }
}