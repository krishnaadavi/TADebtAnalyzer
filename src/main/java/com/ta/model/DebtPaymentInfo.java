package com.ta.model;
import java.util.Date;
/*
Represents the payment related information of debt. This is the output of processing
debt, payment plan and payment information by the TADebtAnalyzerApp.
 */
public class DebtPaymentInfo {
    private int id;
    private float remainingAmount;
    private Date nextPaymentDate;
    private boolean isInPaymentPlan;
    private float amount;

    public DebtPaymentInfo(int id, float amount, float remainingAmount, Date nextPaymentDate, boolean isInPaymentPlan){
        this.id = id;
        this.amount = amount;
        this.remainingAmount = remainingAmount;
        this.nextPaymentDate = nextPaymentDate;
        this.isInPaymentPlan = isInPaymentPlan;
    }
    public int getId(){
        return this.id;
    }

    public float getAmount(){
        return this.amount;
    }

    public float getRemainingAmount(){
        return this.remainingAmount;
    }

    public boolean getIsInPaymentPlan() {
        return this.isInPaymentPlan;
    }

    public Date getNextPaymentDate() {
        return this.nextPaymentDate;
    }

}
