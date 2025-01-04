package com.tripleseven.android.dto;

import java.io.Serializable;

public class BetItem implements Serializable {
    public String getNumber1() {
        return number1;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public String getNumber2() {
        return number1;
    }

    public void setNumber2(String number1) {
        this.number1 = number1;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String number1;
    public String number2;
    public String gameType;
    public String amount;
}
