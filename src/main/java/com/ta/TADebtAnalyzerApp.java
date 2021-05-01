package com.ta;
import com.ta.model.Debt;
import com.ta.model.DebtPaymentInfo;
import com.ta.model.Payment;
import com.ta.model.PaymentPlan;
import com.ta.service.TAService;
import com.ta.service.TAServiceImpl;
import org.javatuples.Pair;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class encapsulates the functionality for TrueAccord Debt Analyzer admin utility.
 * Fetches data from Debts, PaymentPlans and Payments Web services.
 * Computes remaining debt, next installment due date and if the payment plan is in place etc.
 * Implementation spec used:
 * https://t.lever-analytics.com/email-link?dest=http%3A%2F%2Fgist.github.com%2Fjeffling%2F2dd661ff8398726883cff09839dc316c&eid=3ba500e1-94dc-4902-a2a6-68a5f319a4ce&idx=0&token=PtI6gUFthCmMRJDp3PJ0MlF6OII
 */
public class TADebtAnalyzerApp {
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        TADebtAnalyzerApp debtAnalyzer = new TADebtAnalyzerApp();
        TAService taService = new TAServiceImpl();
        List<Debt> debts = taService.getDebts();
        List<Payment> payments = taService.getPayments();
        Map<Integer, PaymentPlan> debtPaymentPlans = taService.getAllDebtPaymentPlans();
        List<DebtPaymentInfo> debtPaymentInfoList = debtAnalyzer.processDebtPayments(debts, debtPaymentPlans, payments);
        debtAnalyzer.displayDebtInfo(debtPaymentInfoList);
    }

    /*
    * Processes debts, computes next payment date, amount remaining by factoring in the payments made and payment plans setup.
    * */
    public List<DebtPaymentInfo> processDebtPayments(List<Debt> debts, Map<Integer, PaymentPlan> debtPaymentPlans, List<Payment> payments) {
        List<DebtPaymentInfo> debtPaymentInfoList = new ArrayList<>();
        try{
            //map plans to the payments info (total amount paid, most recent payment date)
            Map<Integer, Pair<Float, Date>> plansToPaymentsMade = computeTotalPaymentAndLatestPaymentDate(payments);
            //for a debt, collate info and invoke methods to compute and display in n lines
            debtPaymentInfoList = debts.stream().map(debt -> {
                PaymentPlan plan = debtPaymentPlans.get(debt.getId());
                float debtAmount = debt.getAmount();
                float amountRemaining = debt.getAmount();
                Date recentPaymentDate;
                Date nextPaymentDueDate = null;
                boolean isInPaymentPlan = false;
                if(plan != null){
                    isInPaymentPlan = true;
                    recentPaymentDate = plan.getStartDate(); //this will be computed with payment date, if any payments made
                    amountRemaining = plan.getAmountToPay(); //plan might have reduced amount to repay, if plan exists
                    if (plansToPaymentsMade.containsKey(plan.getId())) {
                        Pair<Float, Date> paymentInfo = plansToPaymentsMade.get(plan.getId());
                        //here deducting the already made payments for this plan
                        amountRemaining -= (paymentInfo.getValue0());
                        recentPaymentDate = paymentInfo.getValue1();
                    }
                    if(amountRemaining > 0) {
                        nextPaymentDueDate = plan.getNextPaymentDate(recentPaymentDate);
                    } else {
                        isInPaymentPlan = false;
                    }
                }
                return new DebtPaymentInfo(debt.getId(), debtAmount, amountRemaining,nextPaymentDueDate, isInPaymentPlan);

            }).collect(Collectors.toList());
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return debtPaymentInfoList;
    }

    /*
     Computes and outputs the debt attributes if there is no payment plan or the debt is paid off.
     */
    private void displayDebtInfo(List<DebtPaymentInfo> debtInfoList){
        debtInfoList.forEach(debtInfo -> {
            JSONObject debtJson = new JSONObject();
            debtJson.put("id", debtInfo.getId());
            debtJson.put("amount", debtInfo.getAmount());
            debtJson.put("is_in_payment_plan", debtInfo.getIsInPaymentPlan());
            debtJson.put("remaining_amount", debtInfo.getRemainingAmount());
            debtJson.put("next_payment_due_date", debtInfo.getNextPaymentDate() == null ? "null" : dateFormatter.format(debtInfo.getNextPaymentDate()));
            System.out.println(debtJson);
        });
    }

    /*
  Computes total payments made per payment plan and stores in planId -> totalPaid HashMap
  */
    private Map<Integer, Pair<Float, Date>> computeTotalPaymentAndLatestPaymentDate(List<Payment> payments){
        Map<Integer, Pair<Float, Date>> planToTotalPayments = new HashMap<>();
        payments.forEach(p -> {
            if (planToTotalPayments.containsKey(p.getPaymentPlanId())) {
                Pair<Float, Date> paymentInfo = planToTotalPayments.get(p.getPaymentPlanId());
                float totalPaid = paymentInfo.getValue0() + p.getAmount();
                //computing the recent payment date
                if (paymentInfo.getValue1().compareTo(p.getPaymentDate()) < 0) {
                    planToTotalPayments.put(p.getPaymentPlanId(), Pair.with(totalPaid, p.getPaymentDate()));
                } else {
                    planToTotalPayments.put(p.getPaymentPlanId(), Pair.with(totalPaid, paymentInfo.getValue1()));
                }
            } else {
                planToTotalPayments.put(p.getPaymentPlanId(), Pair.with(p.getAmount(), p.getPaymentDate()));
            }
        });
        return planToTotalPayments;
    }

}
