import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ta.model.*;
import com.ta.*;

import static org.junit.jupiter.api.Assertions.*;

class TADebtAnalyzerTest {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    @Description("Test a debt associated with an active payment plan displays the correct attributes.")
    void test_debt_with_payment_plan() {
        TADebtAnalyzerApp taDebtAnalyzer = new TADebtAnalyzerApp();

        List<Debt> debts = new ArrayList<>();
        Debt debt = new Debt(0, 125F);
        debts.add(debt);
        Map<Integer, PaymentPlan> debtPaymentPlans = new HashMap<>();
        PaymentPlan plan = new PaymentPlan(0, 0, 100F, 25F, "WEEKLY", "2020-08-01");
        debtPaymentPlans.put(debt.getId(), plan);

        List<Payment> payments = new ArrayList<>();
        Payment payment = new Payment(plan.getId(), 50F, "2020-08-10");
        payments.add(payment);

        List<DebtPaymentInfo> debtPaymentInfoList = taDebtAnalyzer.processDebtPayments(debts, debtPaymentPlans, payments);

        assertNotNull(debtPaymentInfoList);
        assertEquals(debtPaymentInfoList.get(0).getId(), debt.getId());
        assertEquals(debtPaymentInfoList.get(0).getAmount(), debt.getAmount());
        assertEquals(debtPaymentInfoList.get(0).getRemainingAmount(), 50F);
        assertTrue(debtPaymentInfoList.get(0).getIsInPaymentPlan());
        assertEquals(formatter.format(debtPaymentInfoList.get(0).getNextPaymentDate()), "2020-08-15");

    }

    @Test
    @Description("Test a debt not associated with an active payment plan displays the correct attributes.")
    void test_debt_no_payment_plan() {
        TADebtAnalyzerApp taDebtAnalyzer = new TADebtAnalyzerApp();
        List<Debt> debts = new ArrayList<>();
        Map<Integer, PaymentPlan> paymentPlanMap = new HashMap<>();
        List<Payment> payments = new ArrayList<>();
        Debt debt = new Debt(0, 125F);
        debts.add(debt);
        List<DebtPaymentInfo> debtPaymentInfoList = taDebtAnalyzer.processDebtPayments(debts, paymentPlanMap, payments);
        assertNotNull(debtPaymentInfoList);
        assertEquals(debtPaymentInfoList.get(0).getId(), debt.getId());
        assertEquals(debtPaymentInfoList.get(0).getAmount(), debt.getAmount());
        assertEquals(debtPaymentInfoList.get(0).getRemainingAmount(), 125F);
        assertFalse(debtPaymentInfoList.get(0).getIsInPaymentPlan());
        assertNull(debtPaymentInfoList.get(0).getNextPaymentDate());
    }

    @Test
    @Description("Test a debt associated with a payment plan that's paid off show false for 'isInPaymentPlan' and null for 'nextPaymentDueDate'")
    void test_debt_paid_off() {
        TADebtAnalyzerApp taDebtAnalyzer = new TADebtAnalyzerApp();
        List<Debt> debts = new ArrayList<>();
        Debt debt = new Debt(0, 125F);
        debts.add(debt);
        Map<Integer, PaymentPlan> debtPaymentPlans = new HashMap<>();
        PaymentPlan plan = new PaymentPlan(0, 0, 100F, 25F, "WEEKLY", "2020-08-01");
        debtPaymentPlans.put(debt.getId(), plan);
        List<Payment> payments = new ArrayList<>();
        Payment payment = new Payment(plan.getId(), 100F, "2020-08-10");
        payments.add(payment);
        List<DebtPaymentInfo> debtPaymentInfoList = taDebtAnalyzer.processDebtPayments(debts, debtPaymentPlans, payments);
        assertNotNull(debtPaymentInfoList);
        assertEquals(debtPaymentInfoList.get(0).getId(), debt.getId());
        assertEquals(debtPaymentInfoList.get(0).getAmount(), debt.getAmount());
        assertEquals(debtPaymentInfoList.get(0).getRemainingAmount(), 0F);
        assertFalse(debtPaymentInfoList.get(0).getIsInPaymentPlan());
        assertNull(debtPaymentInfoList.get(0).getNextPaymentDate());
    }

    @Test
    @Description("Test a debt associated with a weekly payment plan that has multiple payments should show the correct remaining amount and next payment due date")
    void test_debt_multiple_payments_weekly_payment_plan() {
        TADebtAnalyzerApp taDebtAnalyzer = new TADebtAnalyzerApp();
        List<Debt> debts = new ArrayList<>();
        Debt debt = new Debt(0, 125F);
        debts.add(debt);
        Map<Integer, PaymentPlan> debtPaymentPlans = new HashMap<>();
        PaymentPlan plan = new PaymentPlan(0, 0, 100F, 25F, "WEEKLY", "2020-08-01");
        debtPaymentPlans.put(debt.getId(), plan);
        List<Payment> payments = new ArrayList<>();
        Payment payment1 = new Payment(plan.getId(), 25F, "2020-08-05");
        Payment payment2 = new Payment(plan.getId(), 50F, "2020-08-10");
        payments.add(payment1);
        payments.add(payment2);
        List<DebtPaymentInfo> debtPaymentInfoList = taDebtAnalyzer.processDebtPayments(debts, debtPaymentPlans, payments);
        assertNotNull(debtPaymentInfoList);
        assertEquals(debtPaymentInfoList.get(0).getId(), debt.getId());
        assertEquals(debtPaymentInfoList.get(0).getAmount(), debt.getAmount());
        assertEquals(debtPaymentInfoList.get(0).getRemainingAmount(), 25F);
        assertTrue(debtPaymentInfoList.get(0).getIsInPaymentPlan());
        assertEquals(formatter.format(debtPaymentInfoList.get(0).getNextPaymentDate()), "2020-08-15");
    }

    @Test
    @Description("Test that out of schedule payments don't change the payment plan schedule")
    void test_debt_out_of_schedule_payment() {
        TADebtAnalyzerApp taDebtAnalyzer = new TADebtAnalyzerApp();
        List<Debt> debts = new ArrayList<>();
        Debt debt = new Debt(0, 125F);
        debts.add(debt);
        Map<Integer, PaymentPlan> debtPaymentPlans = new HashMap<>();
        PaymentPlan plan = new PaymentPlan(0, 0, 100F, 25F, "WEEKLY", "2020-01-01");
        debtPaymentPlans.put(debt.getId(), plan);
        List<Payment> payments = new ArrayList<>();
        Payment payment1 = new Payment(plan.getId(), 25F, "2020-01-07");
        Payment payment2 = new Payment(plan.getId(), 50F, "2020-01-09");
        payments.add(payment1);
        payments.add(payment2);
        List<DebtPaymentInfo> debtPaymentInfoList = taDebtAnalyzer.processDebtPayments(debts, debtPaymentPlans, payments);
        assertNotNull(debtPaymentInfoList);
        assertEquals(debtPaymentInfoList.get(0).getId(), debt.getId());
        assertEquals(debtPaymentInfoList.get(0).getAmount(), debt.getAmount());
        assertEquals(debtPaymentInfoList.get(0).getRemainingAmount(), 25F);
        assertTrue(debtPaymentInfoList.get(0).getIsInPaymentPlan());
        assertEquals(formatter.format(debtPaymentInfoList.get(0).getNextPaymentDate()), "2020-01-15");
    }

    @Test
    @Description("Test for a debt associated with biweekly payment plan with multiple payments.")
    void test_debt_multiple_payments_biweekly_payment_plan() {
        TADebtAnalyzerApp taDebtAnalyzer = new TADebtAnalyzerApp();
        List<Debt> debts = new ArrayList<>();
        Debt debt = new Debt(0, 125F);
        debts.add(debt);
        Map<Integer, PaymentPlan> debtPaymentPlans = new HashMap<>();
        PaymentPlan plan = new PaymentPlan(0, 0, 100F, 25F, "BI_WEEKLY", "2020-08-01");
        debtPaymentPlans.put(debt.getId(), plan);

        List<Payment> payments = new ArrayList<>();
        Payment payment1 = new Payment(plan.getId(), 25F, "2020-08-05");
        Payment payment2 = new Payment(plan.getId(), 50F, "2020-08-10");
        payments.add(payment1);
        payments.add(payment2);
        List<DebtPaymentInfo> debtPaymentInfoList = taDebtAnalyzer.processDebtPayments(debts, debtPaymentPlans, payments);
        assertNotNull(debtPaymentInfoList);
        assertEquals(debtPaymentInfoList.get(0).getId(), debt.getId());
        assertEquals(debtPaymentInfoList.get(0).getAmount(), debt.getAmount());
        assertEquals(debtPaymentInfoList.get(0).getRemainingAmount(), 25F);
        assertTrue(debtPaymentInfoList.get(0).getIsInPaymentPlan());
        assertEquals(formatter.format(debtPaymentInfoList.get(0).getNextPaymentDate()), "2020-08-15");
    }

    @Test
    @Description("Test for a debt associated with unknown payment plan displays null for next payment due date.")
    void test_debt_multiple_payments_unknown_payment_plan() {
        TADebtAnalyzerApp taDebtAnalyzer = new TADebtAnalyzerApp();
        List<Debt> debts = new ArrayList<>();
        Debt debt = new Debt(0, 125F);
        debts.add(debt);
        Map<Integer, PaymentPlan> debtPaymentPlans = new HashMap<>();
        PaymentPlan plan = new PaymentPlan(0, 0, 100F, 25F, "MONTHLY", "2020-08-01");
        debtPaymentPlans.put(debt.getId(), plan);

        List<Payment> payments = new ArrayList<>();
        Payment payment1 = new Payment(plan.getId(), 25F, "2020-08-05");
        Payment payment2 = new Payment(plan.getId(), 50F, "2020-08-10");
        payments.add(payment1);
        payments.add(payment2);
        List<DebtPaymentInfo> debtPaymentInfoList = taDebtAnalyzer.processDebtPayments(debts, debtPaymentPlans, payments);
        assertNotNull(debtPaymentInfoList);
        assertEquals(debtPaymentInfoList.get(0).getId(), debt.getId());
        assertEquals(debtPaymentInfoList.get(0).getAmount(), debt.getAmount());
        assertEquals(debtPaymentInfoList.get(0).getRemainingAmount(), 25F);
        assertTrue(debtPaymentInfoList.get(0).getIsInPaymentPlan());
        assertNull(debtPaymentInfoList.get(0).getNextPaymentDate());
    }

}