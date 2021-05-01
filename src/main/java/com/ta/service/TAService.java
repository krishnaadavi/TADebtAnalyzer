package com.ta.service;
import com.ta.model.*;
import java.util.List;
import java.util.Map;

/*
Backend service providing debt and payments data
* */
public interface TAService {

    public List<Debt> getDebts();

    public Map<Integer, PaymentPlan> getAllDebtPaymentPlans();

    public List<Payment> getPayments();

}
