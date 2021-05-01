package com.ta.model;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
Represents payment information.
 */

public class Payment {
    private int paymentPlanId;
    private Date paymentDate;
    private float amount;

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Payment(int paymentPlanId, float amount, String paymentDate) {
        this.paymentPlanId = paymentPlanId;
        this.amount = amount;
        try{
            this.paymentDate = formatter.parse(paymentDate);
        }catch(ParseException pe){
            this.paymentDate = null;
        }
    }
    public float getAmount(){
        return this.amount;
    }

    public int getPaymentPlanId(){
        return this.paymentPlanId;
    }

    public Date getPaymentDate(){
        return this.paymentDate;
    }
}
