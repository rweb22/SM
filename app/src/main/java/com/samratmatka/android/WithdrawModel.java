package com.samratmatka.android;

public class WithdrawModel {


    String account;
    String ifsc;
    String upi;
    String status;
    String amount;
    String date;

    public WithdrawModel(String account, String ifsc, String upi, String status, String amount, String date) {
        this.account = account;
        this.ifsc = ifsc;
        this.upi = upi;
        this.status = status;
        this.amount = amount;
        this.date = date;
    }

    public String getAccount() {
        if (account.equals("")){
            return "-";
        }
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIfsc() {
        if (ifsc.equals("")){
            return "-";
        }
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getUpi() {
        if (upi.equals("")){
            return "-";
        }
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount+" ₹";
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
