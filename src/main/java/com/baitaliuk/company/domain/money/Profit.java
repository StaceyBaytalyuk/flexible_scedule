package com.baitaliuk.company.domain.money;

public class Profit {
    private int money;
    private int difference;
    private String source;

    public Profit(int money, int difference, String source) {
        this.money = money;
        this.difference = difference;
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }
}
