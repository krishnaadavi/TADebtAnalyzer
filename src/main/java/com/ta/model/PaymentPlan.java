package com.ta.model;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/*
Represents payment plan information.
 */
public class PaymentPlan {
    private final float amountToPay;
    private final int debtId;
    private final int id;
    private final float installmentAmount;
    private final String installmentFrequency;
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

    public Date getNextPaymentDate(Date paymentDate) {
        Calendar c = Calendar.getInstance();
        if(this.startDate == null) {
            return null;
        }
        if(!this.getInstallmentFrequency().equalsIgnoreCase("WEEKLY") && !this.installmentFrequency.equalsIgnoreCase("BI_WEEKLY")) {
            return null;
        }
        c.setTime(getStartDate());
        Date nextPaymentDueDate;
        //next payment due date is start + installment frequency that's after the most recent payment date
        int increment = this.getInstallmentFrequency().equalsIgnoreCase("WEEKLY") ? 7 : 14;
        while(c.getTime().before(paymentDate)){
            c.add(Calendar.DATE, increment);
        }
        nextPaymentDueDate = c.getTime();
        return nextPaymentDueDate;
    }
}