package com.upt.cti.smartwallet.model;

import java.util.HashMap;
import java.util.Map;

public class MonthlyExpenses {
    public String month;
    private float income, expenses;

    public MonthlyExpenses(){
        // default constructor required for calls to DataSnapshot
    }

    public MonthlyExpenses(String month, float income, float expenses){
        this.month=month;
        this.income=income;
        this.expenses=expenses;
    }
    public String getMonth(){
        return month;
    }

    public float getExpenses(){
        return expenses;
    }
    public void setIncome(float income){
        this.income=income;
    }
    public void setExpenses(float expenses){
        this.expenses=expenses;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getIncome(){
        return income;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("expenses", expenses);
        result.put("income", income);

        return result;
    }
}
