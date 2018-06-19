package com.mazenet.mzs119.skst.Model;

/**
 * Created by PRABAKARAN on 4/30/2018.
 */

public class TempLoanModel {
    String loan_id;
    String brnchid;
    String amount;
    String cheqno;
    String cheqbank;
    String cheqbranch;
    String cheqdate;
    String transno;
    String transdate;
    String debitto;

    public String getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(String loan_id) {
        this.loan_id = loan_id;
    }

    public String getBrnchid() {
        return brnchid;
    }

    public void setBrnchid(String brnchid) {
        this.brnchid = brnchid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCheqno() {
        return cheqno;
    }

    public void setCheqno(String cheqno) {
        this.cheqno = cheqno;
    }

    public String getCheqbank() {
        return cheqbank;
    }

    public void setCheqbank(String cheqbank) {
        this.cheqbank = cheqbank;
    }

    public String getCheqbranch() {
        return cheqbranch;
    }

    public void setCheqbranch(String cheqbranch) {
        this.cheqbranch = cheqbranch;
    }

    public String getCheqdate() {
        return cheqdate;
    }

    public void setCheqdate(String cheqdate) {
        this.cheqdate = cheqdate;
    }

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    public String getTransdate() {
        return transdate;
    }

    public void setTransdate(String transdate) {
        this.transdate = transdate;
    }

    public String getDebitto() {
        return debitto;
    }

    public void setDebitto(String debitto) {
        this.debitto = debitto;
    }

    public String getPaymode() {
        return paymode;
    }

    public void setPaymode(String paymode) {
        this.paymode = paymode;
    }

    String paymode;
}
