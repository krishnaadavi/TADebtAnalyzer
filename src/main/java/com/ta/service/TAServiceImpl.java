package com.ta.service;

import com.ta.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TAServiceImpl implements TAService {

    private static final String debtServiceURL = "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/debts";

    private static final String paymentPlansURL = "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payment_plans";

    private static final String paymentsURL = "https://my-json-server.typicode.com/druska/trueaccord-mock-payments-api/payments";

    /*
   Populates Debt bean object from JSON
   */
    public List<Debt> getDebts() {
        //fetch debt data from debt service
        List<Debt> debts = new ArrayList<>();
        try {
            String debtsJson = fetchDataFromService(debtServiceURL);
            JSONArray jsonArray = new JSONArray(debtsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                int id = object.getInt("id");
                float amount = object.getFloat("amount");
                debts.add(new Debt(id, amount));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return debts;
    }

    @Override
    /*
        Populates PaymentPlan bean objects from JSON into debt_id -> PaymentPlan HashMap
    */
    public Map<Integer, PaymentPlan> getAllDebtPaymentPlans() {
        //fetch payment plans data
        Map<Integer, PaymentPlan> plans = new HashMap<>();
        try {
            String plansJson = fetchDataFromService(paymentPlansURL);
            JSONArray jsonArray = new JSONArray(plansJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                int id = object.getInt("id");
                float amount_to_pay = object.getFloat("amount_to_pay");
                int debt_id = object.getInt("debt_id");
                float installment_amount = object.getFloat("installment_amount");
                String installment_frequency = object.getString("installment_frequency");
                String start_date = object.getString("start_date");
                plans.put(debt_id, new PaymentPlan(id, debt_id, amount_to_pay, installment_amount, installment_frequency, start_date));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return plans;
    }

    @Override
    public List<Payment> getPayments() {
        List<Payment>  payments = new ArrayList<>();
        try
        {
            String paymentsJson = fetchDataFromService(paymentsURL);
            JSONArray jsonArray=new JSONArray(paymentsJson);
            for (int i=0; i<jsonArray.length();i++)
            {
                JSONObject object=jsonArray.getJSONObject(i);

                int id=object.getInt("payment_plan_id");
                float amountPaid =object.getFloat("amount");
                payments.add(new Payment(id,amountPaid,object.getString("date")));
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return payments;
    }

    /*
   Util method to fetch data from HTTP service
   */
    private static String fetchDataFromService(String url) {
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            return readAll(rd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*
     Util
   */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
