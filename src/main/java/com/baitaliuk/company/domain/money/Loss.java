package com.baitaliuk.company.domain.money;

public class Loss {
    private int money;
    private String source;
    private String advice;

    public Loss(int money, String source) {
        this.money = money;
        this.source = source;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
