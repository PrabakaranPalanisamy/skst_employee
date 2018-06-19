package com.mazenet.mzs119.skst.Model;

/**
 * Created by MZS119 on 4/30/2018.
 */

public class LoanModel {
    String First_Name;
    String Mobile_F;
    String branchname;
    String Loan_amount;
    String Loan_Date;
    String Paid_amt;
    String Pending_amt;
    String Loan_Type;
    String Reference_Grp;

    public String getTbl_id() {
        return Tbl_id;
    }

    public void setTbl_id(String tbl_id) {
        Tbl_id = tbl_id;
    }

    String Tbl_id;

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getMobile_F() {
        return Mobile_F;
    }

    public void setMobile_F(String mobile_F) {
        Mobile_F = mobile_F;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getLoan_amount() {
        return Loan_amount;
    }

    public void setLoan_amount(String loan_amount) {
        Loan_amount = loan_amount;
    }

    public String getLoan_Date() {
        return Loan_Date;
    }

    public void setLoan_Date(String loan_Date) {
        Loan_Date = loan_Date;
    }

    public String getPaid_amt() {
        return Paid_amt;
    }

    public void setPaid_amt(String paid_amt) {
        Paid_amt = paid_amt;
    }

    public String getPending_amt() {
        return Pending_amt;
    }

    public void setPending_amt(String pending_amt) {
        Pending_amt = pending_amt;
    }

    public String getLoan_Type() {
        return Loan_Type;
    }

    public void setLoan_Type(String loan_Type) {
        Loan_Type = loan_Type;
    }

    public String getReference_Grp() {
        return Reference_Grp;
    }

    public void setReference_Grp(String reference_Grp) {
        Reference_Grp = reference_Grp;
    }
}
