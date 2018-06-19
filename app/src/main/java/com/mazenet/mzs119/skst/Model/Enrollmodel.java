package com.mazenet.mzs119.skst.Model;

/**
 * Created by admin1 on 12/6/2017.
 */

public class Enrollmodel {

    String Scheme;

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getCusid() {
        return cusid;
    }

    public void setCusid(String cusid) {
        this.cusid = cusid;
    }

    String cusid;
    String tableid;
    String enrollid;
    String Pending_Amt;
    String Paid_Amt;
    String Penalty_Amt;
    String Bonus_Amt;
    String Group_Name;
    String payamount;
    String Advanceamnt;
    String collect_emp;
    String completed_auction,paid_details;

    public String getCompleted_auction() {
        return completed_auction;
    }

    public void setCompleted_auction(String completed_auction) {
        this.completed_auction = completed_auction;
    }

    public String getPaid_details() {
        return paid_details;
    }

    public void setPaid_details(String paid_details) {
        this.paid_details = paid_details;
    }

    public String getCollect_emp() {
        return collect_emp;
    }

    public void setCollect_emp(String collect_emp) {
        this.collect_emp = collect_emp;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    String PaymentType;

    public String getAdvanceamnt() {
        return Advanceamnt;
    }

    public void setAdvanceamnt(String advanceamnt) {
        Advanceamnt = advanceamnt;
    }

    String Group_Ticket_Name;
    String cusbranch;

    public String getInsamt() {
        return insamt;
    }

    public void setInsamt(String insamt) {
        this.insamt = insamt;
    }

    String insamt;

    public String getCusbranch() {
        return cusbranch;
    }

    public void setCusbranch(String cusbranch) {
        this.cusbranch = cusbranch;
    }

    public String getPendingdys() {
        return pendingdys;
    }

    public void setPendingdys(String pendingdys) {
        this.pendingdys = pendingdys;
    }

    String pendingdys;

    public String getEnrollid() {
        return enrollid;
    }

    public void setEnrollid(String enrollid) {
        this.enrollid = enrollid;
    }


    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }


    public String getScheme() {
        return Scheme;
    }

    public void setScheme(String scheme) {
        Scheme = scheme;
    }

    public String getPending_Amt() {
        return Pending_Amt;
    }

    public void setPending_Amt(String pending_Amt) {
        Pending_Amt = pending_Amt;
    }

    public String getPaid_Amt() {
        return Paid_Amt;
    }

    public void setPaid_Amt(String paid_Amt) {
        Paid_Amt = paid_Amt;
    }

    public String getPenalty_Amt() {
        return Penalty_Amt;
    }

    public void setPenalty_Amt(String penalty_Amt) {
        Penalty_Amt = penalty_Amt;
    }

    public String getBonus_Amt() {
        return Bonus_Amt;
    }

    public void setBonus_Amt(String bonus_Amt) {
        Bonus_Amt = bonus_Amt;
    }

    public String getGroup_Name() {
        return Group_Name;
    }

    public void setGroup_Name(String group_Name) {
        Group_Name = group_Name;
    }

    public String getGroup_Ticket_Name() {
        return Group_Ticket_Name;
    }

    public void setGroup_Ticket_Name(String group_Ticket_Name) {
        Group_Ticket_Name = group_Ticket_Name;
    }



}
