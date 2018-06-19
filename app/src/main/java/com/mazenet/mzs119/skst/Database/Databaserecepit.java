package com.mazenet.mzs119.skst.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mazenet.mzs119.skst.Model.Enrollmodel;
import com.mazenet.mzs119.skst.Model.LoanModel;
import com.mazenet.mzs119.skst.Model.LoanModelDatewise;
import com.mazenet.mzs119.skst.Model.TempEnrollModel;
import com.mazenet.mzs119.skst.Model.TempLoanModel;

import java.util.ArrayList;


public class Databaserecepit extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "chit_recepit";
    private static final String DB_RECEIPT = "fullreceipt";
    private static final String TABLE_CONTACTS = "recepit";
    private static final String KEY_ID = "id";
    private static final String KEY_enrollid = "enrollid";
    private static final String KEY_Scheme = "Scheme";
    private static final String KEY_Pending_Amt = "Pending_Amt";
    private static final String KEY_Penalty_Amt = "Penalty_Amt";
    private static final String KEY_Bonus_Amt = "Bonus_Amt";
    private static final String KEY_Paid_Amt = "Paid_Amt";
    private static final String KEY_Group_Name = "Group_Name";
    private static final String KEY_payamount = "payamount";
    private static final String KEY_Group_Ticket_Name = "Group_Ticket_Name";
    private static final String KEY_cusbranch = "cusbranch";
    private static final String KEY_pendingdays = "pendingdays";
    private static final String KEY_insamt = "insamt";
    private static final String KEY_CUSID = "cusid";
    private static final String KEY_Advance = "advance_amount";
    private static final String KEY_Paymenttype = "Payment_type";
    private static final String KEY_collectemp = "collect_emp";
    private static final String KEY_completed_action = "completed_action";
    private static final String KEY_paid_details = "paid_details";


    //------------------------------------------------------------------------
    private static final String TABLE_VIEW_RECEIPT = "Vrecepit";
    private static final String KEY_TEMP_ID = "Tid";
    private static final String TABLE_TEMP_RECEIPT = "Trecepit";
    private static final String KEY_TEMP_enrollid = "Tenrollid";
    private static final String KEY_TEMP_insamt = "Tinsamnt";
    private static final String KEY_TEMP_Pending_Amt = "TPending_Amt";
    private static final String KEY_TEMP_Penalty_Amt = "TPenalty_Amt";
    private static final String KEY_TEMP_Bonus_Amt = "TBonus_Amt";
    private static final String KEY_TEMP_Paid_Amt = "TPaid_Amt";
    private static final String KEY_TEMP_Group_Name = "TGroup_Name";
    private static final String KEY_TEMP_payamount = "Tpayamount";
    private static final String KEY_TEMP_Group_Ticket_Name = "TGroup_Ticket_Name";
    private static final String KEY_TEMP_cusbranch = "Tcusbranch";
    private static final String KEY_TEMP_pendingdays = "Tpendingdays";
    private static final String KEY_TEMP_PAYTYPE = "paytype";
    private static final String KEY_TEMP_tranno = "Ttransno";
    private static final String KEY_TEMP_chebranch = "Tcheqbranch";
    private static final String KEY_TEMP_chebank = "Tcheqbank";
    private static final String KEY_TEMP_chedate = "Tcheqdate";
    private static final String KEY_TEMP_cheno = "Tcheqno";
    private static final String KEY_TEMP_Cusname = "Tcusname";
    private static final String KEY_TEMP_Cusid = "Tcusid";
    private static final String KEY_TEMP_rtgsdate = "Ttransdate";
    private static final String KEY_TEMP_Scheme = "Tscheme";
    private static final String KEY_TEMP_REMARK = "Tremark";
    private static final String KEY_TEMP_STATUS = "Tstatus";
    private static final String KEY_TEMP_Advance = "Tadvance_amount";
    private static final String KEY_VIEW_Date = "VDate";
    //===========================================================================================
    private static final String TABLE_LOANS = "loans_table";
    private static final String KEY_LOAN_ID = "Lid";
    private static final String KEY_LOAN_Branch_Id = "Lbranchid";
    private static final String KEY_LOAN_First_Name_F = "Lfirstname";
    private static final String KEY_LOAN_Loan_amount = "Lloanamount";
    private static final String KEY_LOAN_Loan_Date = "Lloandate";
    private static final String KEY_LOAN_Loan_Type = "Lloantype";
    private static final String KEY_LOAN_Mobile_F = "Lmobile";
    private static final String KEY_LOAN_Paid_amt = "Lpaidamnt";
    private static final String KEY_LOAN_Pending_amt = "Lpendingamnt";
    private static final String KEY_LOAN_Reference_Grp = "Lreferegrp";

    private static final String TABLE_TEMP_LOANS = "TEMP_loans_table";
    private static final String KEY_TEMP_LOAN_ID = "TEMP_Lid";
    private static final String KEY_TEMP_LOAN_Branch_Id = "TEMP_Lbranchid";
    private static final String KEY_TEMP_LOAN_paymode = "TEMP_Lpaymode";
    private static final String KEY_TEMP_LOAN_Loan_amount = "TEMP_Lloanamount";
    private static final String KEY_TEMP_LOAN_chequeno = "TEMP_Lcheqnoe";
    private static final String KEY_TEMP_LOAN_chequebank = "TEMP_Lchebank";
    private static final String KEY_TEMP_LOAN_chequebranch = "TEMP_Lchebranch";
    private static final String KEY_TEMP_LOAN_chequedate = "TEMP_Lchedate";
    private static final String KEY_TEMP_LOAN_transdate = "TEMP_Ltransdate";
    private static final String KEY_TEMP_LOAN_transno = "TEMP_Ltransno";
    private static final String KEY_TEMP_LOAN_debitto = "TEMP_debittoo";

    private static final String TABLE_VIEW_LOANS = "VIEW_loans_table";
    private static final String KEY_VIEW_ID = "VIEW_ID";
    private static final String KEY_VIEW_CUSNAME = "VIEW_CUSNAME";
    private static final String KEY_VIEW_AMNT = "VIEW_AMNT";
    private static final String KEY_VIEW_DATE = "VIEW_DATE";
    private static final String KEY_VIEW_REFEREGRP = "VIEW_REFEREGRP";


    public void updatepayamount(String amount, String id, String amount1) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String updatequery = "UPDATE " + TABLE_CONTACTS + " SET " + KEY_payamount + " = '" + amount + "' , " + KEY_insamt + " = '" + amount1 + "' WHERE " + KEY_ID + " = '" + id + "'";
            db.execSQL(updatequery);
            System.out.println("amonut balanceamnt" + amount + "tableid" + id + " insamnt pay2 " + amount1);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Databaserecepit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOANS_VIEW = "CREATE TABLE " + TABLE_VIEW_LOANS + "("
                + KEY_VIEW_ID + " INTEGER PRIMARY KEY,"
                + KEY_VIEW_CUSNAME + " TEXT,"
                + KEY_VIEW_AMNT + " TEXT,"
                + KEY_VIEW_DATE + " TEXT,"
                + KEY_VIEW_REFEREGRP + " TEXT )";
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_enrollid + " TEXT,"
                + KEY_Scheme + " TEXT,"
                + KEY_Pending_Amt + " TEXT,"
                + KEY_Penalty_Amt + " TEXT,"
                + KEY_Bonus_Amt + " TEXT,"
                + KEY_Paid_Amt + " TEXT,"
                + KEY_Group_Name + " TEXT,"
                + KEY_payamount + " TEXT,"
                + KEY_Group_Ticket_Name + " TEXT,"
                + KEY_cusbranch + " TEXT,"
                + KEY_pendingdays + " TEXT,"
                + KEY_insamt + " TEXT,"
                + KEY_Advance + " TEXT,"
                + KEY_Paymenttype + " TEXT,"
                + KEY_paid_details + " TEXT,"
                + KEY_completed_action + " TEXT"
                + ")";

        String CREATE_DB_RECEIPT = "CREATE TABLE " + DB_RECEIPT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_enrollid + " TEXT,"
                + KEY_Scheme + " TEXT,"
                + KEY_Pending_Amt + " TEXT,"
                + KEY_Penalty_Amt + " TEXT,"
                + KEY_Bonus_Amt + " TEXT,"
                + KEY_Paid_Amt + " TEXT,"
                + KEY_Group_Name + " TEXT,"
                + KEY_payamount + " TEXT,"
                + KEY_Group_Ticket_Name + " TEXT,"
                + KEY_cusbranch + " TEXT,"
                + KEY_pendingdays + " TEXT,"
                + KEY_insamt + " TEXT,"
                + KEY_CUSID + " TEXT,"
                + KEY_Advance + " TEXT,"
                + KEY_Paymenttype + " TEXT,"
                + KEY_collectemp + " TEXT,"
                + KEY_completed_action + " TEXT,"
                + KEY_paid_details + " TEXT"
                + ")";
        String CREATE_TEMP_RECEIPT = "CREATE TABLE " + TABLE_TEMP_RECEIPT + "("
                + KEY_TEMP_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEMP_Cusid + " TEXT,"
                + KEY_TEMP_Cusname + " TEXT,"
                + KEY_TEMP_cheno + " TEXT,"
                + KEY_TEMP_chedate + " TEXT,"
                + KEY_TEMP_chebank + " TEXT,"
                + KEY_TEMP_chebranch + " TEXT,"
                + KEY_TEMP_tranno + " TEXT,"
                + KEY_TEMP_rtgsdate + " TEXT,"
                + KEY_TEMP_enrollid + " TEXT,"
                + KEY_TEMP_Scheme + " TEXT,"
                + KEY_TEMP_Pending_Amt + " TEXT,"
                + KEY_TEMP_Penalty_Amt + " TEXT,"
                + KEY_TEMP_Bonus_Amt + " TEXT,"
                + KEY_TEMP_Paid_Amt + " TEXT,"
                + KEY_TEMP_Group_Name + " TEXT,"
                + KEY_TEMP_payamount + " TEXT,"
                + KEY_TEMP_Group_Ticket_Name + " TEXT,"
                + KEY_TEMP_cusbranch + " TEXT,"
                + KEY_TEMP_pendingdays + " TEXT,"
                + KEY_TEMP_insamt + " TEXT,"
                + KEY_TEMP_PAYTYPE + " TEXT,"
                + KEY_TEMP_REMARK + " TEXT,"
                + KEY_TEMP_STATUS + " TEXT,"
                + KEY_TEMP_Advance + " TEXT"
                + ")";
        String CREATE_VIEW_RECEIPT = "CREATE TABLE " + TABLE_VIEW_RECEIPT + "("
                + KEY_TEMP_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEMP_Cusid + " TEXT,"
                + KEY_TEMP_Cusname + " TEXT,"
                + KEY_TEMP_cheno + " TEXT,"
                + KEY_TEMP_chedate + " TEXT,"
                + KEY_TEMP_chebank + " TEXT,"
                + KEY_TEMP_chebranch + " TEXT,"
                + KEY_TEMP_tranno + " TEXT,"
                + KEY_TEMP_rtgsdate + " TEXT,"
                + KEY_TEMP_enrollid + " TEXT,"
                + KEY_TEMP_Scheme + " TEXT,"
                + KEY_TEMP_Pending_Amt + " TEXT,"
                + KEY_TEMP_Penalty_Amt + " TEXT,"
                + KEY_TEMP_Bonus_Amt + " TEXT,"
                + KEY_TEMP_Paid_Amt + " TEXT,"
                + KEY_TEMP_Group_Name + " TEXT,"
                + KEY_TEMP_payamount + " TEXT,"
                + KEY_TEMP_Group_Ticket_Name + " TEXT,"
                + KEY_TEMP_cusbranch + " TEXT,"
                + KEY_TEMP_pendingdays + " TEXT,"
                + KEY_TEMP_insamt + " TEXT,"
                + KEY_TEMP_PAYTYPE + " TEXT,"
                + KEY_TEMP_REMARK + " TEXT,"
                + KEY_TEMP_STATUS + " TEXT,"
                + KEY_TEMP_Advance + " TEXT,"
                + KEY_VIEW_Date + " TEXT"
                + ")";
        String CREATE_LOAN_TABLE = "CREATE TABLE " + TABLE_LOANS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOAN_ID + " TEXT,"
                + KEY_LOAN_Branch_Id + " TEXT,"
                + KEY_LOAN_First_Name_F + " TEXT,"
                + KEY_LOAN_Loan_amount + " TEXT,"
                + KEY_LOAN_Loan_Date + " TEXT,"
                + KEY_LOAN_Loan_Type + " TEXT,"
                + KEY_LOAN_Mobile_F + " TEXT,"
                + KEY_LOAN_Paid_amt + " TEXT,"
                + KEY_LOAN_Pending_amt + " TEXT,"
                + KEY_LOAN_Reference_Grp + " TEXT"
                + ")";
        String CREATE_TEMP_LOAN_TABLE = "CREATE TABLE " + TABLE_TEMP_LOANS + "("
                + KEY_TEMP_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEMP_LOAN_ID + " TEXT,"
                + KEY_TEMP_LOAN_Branch_Id + " TEXT,"
                + KEY_TEMP_LOAN_paymode + " TEXT,"
                + KEY_TEMP_LOAN_Loan_amount + " TEXT,"
                + KEY_TEMP_LOAN_chequeno + " TEXT,"
                + KEY_TEMP_LOAN_chequebank + " TEXT,"
                + KEY_TEMP_LOAN_chequebranch + " TEXT,"
                + KEY_TEMP_LOAN_chequedate + " TEXT,"
                + KEY_TEMP_LOAN_transno + " TEXT,"
                + KEY_TEMP_LOAN_transdate + " TEXT,"
                + KEY_TEMP_LOAN_debitto + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_TEMP_RECEIPT);
        db.execSQL(CREATE_DB_RECEIPT);
        db.execSQL(CREATE_VIEW_RECEIPT);
        db.execSQL(CREATE_LOAN_TABLE);
        db.execSQL(CREATE_TEMP_LOAN_TABLE);
        db.execSQL(CREATE_LOANS_VIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMP_RECEIPT);
        db.execSQL("DROP TABLE IF EXISTS " + DB_RECEIPT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW_RECEIPT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMP_LOANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW_LOANS);
        onCreate(db);
    }

    public void deletetable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }

    public void deletetableaLLREC() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_RECEIPT, null, null);
        db.close();
    }

    public void deletetableloanreceipt() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOANS, null, null);
        db.close();
    }

    public void deletetableview(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(" deleted to view");
        db.execSQL("DELETE FROM " + TABLE_VIEW_RECEIPT + " WHERE " + KEY_VIEW_Date + "  <= date('" + date + "', '-1 day')");
        db.close();
    }

    public void deletetableview1() {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(" deleted to view");
        db.execSQL("DELETE FROM " + TABLE_VIEW_RECEIPT);
        db.close();
    }

    public void deletetableloansview1() {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(" deleted to view");
        db.execSQL("DELETE FROM " + TABLE_VIEW_LOANS);
        db.close();
    }

    public void deletetableloansview(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(" deleted to view");
        db.execSQL("DELETE FROM " + TABLE_VIEW_LOANS + " WHERE " + KEY_VIEW_DATE + "  <= date('" + date + "', '-1 day')");
        db.close();
    }

    public void deletetabletemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEMP_RECEIPT, null, null);
        db.close();
    }

    public void deletetableLOANtemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEMP_LOANS, null, null);
        db.close();
    }

    public void updatepaidamnt(String cusid, String enrollid, String pending, String paid, String advance) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateqry = "UPDATE " + DB_RECEIPT + " SET " + KEY_Paid_Amt + " = " + paid + "," + KEY_Pending_Amt + " = " + pending + "," + KEY_Advance + " = " + advance + " WHERE " + KEY_CUSID + " = " + "'" + cusid + "'" + " AND " + KEY_enrollid + " = " + "'" + enrollid + "'";
        db.execSQL(updateqry);
        System.out.println("query ooduthuUU");
        db.close();
    }

    public void addVIEWloanreceipt(String cusname, String amount, String date, String reference) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VIEW_CUSNAME, cusname);
        values.put(KEY_VIEW_AMNT, amount);
        values.put(KEY_VIEW_DATE, date);
        values.put(KEY_VIEW_REFEREGRP, reference);
        db.insert(TABLE_VIEW_LOANS, null, values);
        db.close();

    }

    public ArrayList<LoanModelDatewise> getviewLoan(String date) {
        ArrayList<LoanModelDatewise> contactList = new ArrayList<LoanModelDatewise>();
        String selectQuery = "SELECT  * FROM " + TABLE_VIEW_LOANS + " WHERE " + KEY_VIEW_DATE + " = '" + date + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LoanModelDatewise contact = new LoanModelDatewise();
                contact.setCust_name(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setDate(cursor.getString(3));
                contact.setReference_Grp(cursor.getString(4));
                contactList.add(contact);

            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public void addtemploanreceipt(String tblid, String brnid, String amount, String paymode, String chequeno, String chequedate, String chequebank, String chebranch, String trnsno, String transdate, String debitto) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEMP_LOAN_ID, tblid);
        values.put(KEY_TEMP_LOAN_Loan_amount, amount);
        values.put(KEY_TEMP_LOAN_Branch_Id, brnid);
        values.put(KEY_TEMP_LOAN_paymode, paymode);
        values.put(KEY_TEMP_LOAN_chequeno, chequeno);
        values.put(KEY_TEMP_LOAN_chequebank, chequebank);
        values.put(KEY_TEMP_LOAN_chequebranch, chebranch);
        values.put(KEY_TEMP_LOAN_chequedate, chequedate);
        values.put(KEY_TEMP_LOAN_transno, trnsno);
        values.put(KEY_TEMP_LOAN_transdate, transdate);
        values.put(KEY_TEMP_LOAN_debitto, debitto);
        db.insert(TABLE_TEMP_LOANS, null, values);

        db.close();

    }

    public void updateloanpaidpending(String tblid, String paidamnt, String pendingamnt) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String updatequery = "UPDATE " + TABLE_LOANS + " SET " + KEY_LOAN_Paid_amt + " = '" + paidamnt + "' , " + KEY_LOAN_Pending_amt + " = '" + pendingamnt + "' WHERE " + KEY_LOAN_ID + " = '" + tblid + "'";
            db.execSQL(updatequery);
            System.out.println("amonut paid" + paidamnt + "tableid" + tblid + " pending pay2 " + pendingamnt);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TempLoanModel> getTempLoan() {
        ArrayList<TempLoanModel> contactList = new ArrayList<TempLoanModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEMP_LOANS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TempLoanModel contact = new TempLoanModel();
                contact.setLoan_id(cursor.getString(1));
                contact.setBrnchid(cursor.getString(2));
                contact.setPaymode(cursor.getString(3));
                contact.setAmount(cursor.getString(4));
                contact.setCheqno(cursor.getString(5));
                contact.setCheqbank(cursor.getString(6));
                contact.setCheqbranch(cursor.getString(7));
                contact.setCheqdate(cursor.getString(8));
                contact.setTransno(cursor.getString(9));
                contact.setTransdate(cursor.getString(10));
                contact.setDebitto(cursor.getString(11));
                contactList.add(contact);


            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public void addallLoan(ArrayList<LoanModel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {

            ContentValues values = new ContentValues();
            LoanModel contact = sched.get(i);

            values.put(KEY_LOAN_ID, contact.getTbl_id());
            values.put(KEY_LOAN_Loan_amount, contact.getLoan_amount());
            values.put(KEY_LOAN_Branch_Id, contact.getBranchname());
            values.put(KEY_LOAN_First_Name_F, contact.getFirst_Name());
            values.put(KEY_LOAN_Loan_Date, contact.getLoan_Date());
            values.put(KEY_LOAN_Loan_Type, contact.getLoan_Type());
            values.put(KEY_LOAN_Mobile_F, contact.getMobile_F());
            values.put(KEY_LOAN_Paid_amt, contact.getPaid_amt());
            values.put(KEY_LOAN_Pending_amt, contact.getPending_amt());
            values.put(KEY_LOAN_Reference_Grp, contact.getReference_Grp());
            db.insert(TABLE_LOANS, null, values);
        }
        db.close();
    }

    public ArrayList<LoanModel> getallLoan() {
        ArrayList<LoanModel> contactList = new ArrayList<LoanModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOANS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LoanModel contact = new LoanModel();
                contact.setTbl_id(cursor.getString(1));
                contact.setBranchname(cursor.getString(2));
                contact.setFirst_Name(cursor.getString(3));
                contact.setLoan_amount(cursor.getString(4));
                contact.setLoan_Date(cursor.getString(5));
                contact.setLoan_Type(cursor.getString(6));
                contact.setMobile_F(cursor.getString(7));
                contact.setPaid_amt(cursor.getString(8));
                contact.setPending_amt(cursor.getString(9));
                contact.setReference_Grp(cursor.getString(10));
                contactList.add(contact);

            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<LoanModel> getallLoan(String tblid) {
        ArrayList<LoanModel> contactList = new ArrayList<LoanModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOANS + " WHERE " + KEY_LOAN_ID + " = '" + tblid + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LoanModel contact = new LoanModel();
                contact.setTbl_id(cursor.getString(1));
                contact.setBranchname(cursor.getString(2));
                contact.setFirst_Name(cursor.getString(3));
                contact.setLoan_amount(cursor.getString(4));
                contact.setLoan_Date(cursor.getString(5));
                contact.setLoan_Type(cursor.getString(6));
                contact.setMobile_F(cursor.getString(7));
                contact.setPaid_amt(cursor.getString(8));
                contact.setPending_amt(cursor.getString(9));
                contact.setReference_Grp(cursor.getString(10));
                contactList.add(contact);

            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public void addallreceipt(ArrayList<Enrollmodel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {

            ContentValues values = new ContentValues();
            Enrollmodel contact = sched.get(i);

            values.put(KEY_CUSID, contact.getCusid());
            values.put(KEY_enrollid, contact.getEnrollid());
            values.put(KEY_Scheme, contact.getScheme());
            values.put(KEY_Pending_Amt, contact.getPending_Amt());
            values.put(KEY_Penalty_Amt, contact.getPenalty_Amt());
            values.put(KEY_Bonus_Amt, contact.getBonus_Amt());
            values.put(KEY_Paid_Amt, contact.getPaid_Amt());
            values.put(KEY_Group_Name, contact.getGroup_Name());
            values.put(KEY_payamount, contact.getPayamount());
            values.put(KEY_Group_Ticket_Name, contact.getGroup_Ticket_Name());
            values.put(KEY_cusbranch, contact.getCusbranch());
            values.put(KEY_pendingdays, contact.getPendingdys());
            values.put(KEY_insamt, "0");
            values.put(KEY_Advance, contact.getAdvanceamnt());
            values.put(KEY_Paymenttype, contact.getPaymentType());
            values.put(KEY_collectemp, contact.getCollect_emp());
            values.put(KEY_completed_action, contact.getCompleted_auction());
            values.put(KEY_paid_details, contact.getPaid_details());
            db.insert(DB_RECEIPT, null, values);
        }
        db.close();
    }

    public ArrayList<Enrollmodel> getreceiptforcust(String cusid) {
        ArrayList<Enrollmodel> contactList = new ArrayList<Enrollmodel>();
        String selectQuery = "SELECT  * FROM " + DB_RECEIPT + " WHERE " + KEY_CUSID + " = " + "'" + cusid + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Enrollmodel contact = new Enrollmodel();
                contact.setTableid(cursor.getString(0));
                contact.setEnrollid(cursor.getString(1));
                contact.setScheme(cursor.getString(2));
                contact.setPending_Amt(cursor.getString(3));
                contact.setPenalty_Amt(cursor.getString(4));
                contact.setBonus_Amt(cursor.getString(5));
                contact.setPaid_Amt(cursor.getString(6));
                contact.setGroup_Name(cursor.getString(7));
                contact.setPayamount(cursor.getString(8));
                contact.setGroup_Ticket_Name(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPendingdys(cursor.getString(11));
                contact.setInsamt(cursor.getString(12));
                contact.setCusid(cursor.getString(13));
                contact.setAdvanceamnt(cursor.getString(14));
                contact.setPaymentType(cursor.getString(15));
                contact.setCompleted_auction(cursor.getString(17));
                contact.setPaid_details(cursor.getString(18));
                contactList.add(contact);


            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<Enrollmodel> getreceiptforcust(String cusid, String type) {
        ArrayList<Enrollmodel> contactList = new ArrayList<Enrollmodel>();
        String selectQuery = "SELECT  * FROM " + DB_RECEIPT + " WHERE " + KEY_CUSID + " = " + "'" + cusid + "' AND " + KEY_Paymenttype + " = " + "'" + type + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Enrollmodel contact = new Enrollmodel();
                contact.setTableid(cursor.getString(0));
                contact.setEnrollid(cursor.getString(1));
                contact.setScheme(cursor.getString(2));
                contact.setPending_Amt(cursor.getString(3));
                contact.setPenalty_Amt(cursor.getString(4));
                contact.setBonus_Amt(cursor.getString(5));
                contact.setPaid_Amt(cursor.getString(6));
                contact.setGroup_Name(cursor.getString(7));
                contact.setPayamount(cursor.getString(8));
                contact.setGroup_Ticket_Name(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPendingdys(cursor.getString(11));
                contact.setInsamt(cursor.getString(12));
                contact.setCusid(cursor.getString(13));
                contact.setAdvanceamnt(cursor.getString(14));
                contact.setCompleted_auction(cursor.getString(17));
                contact.setPaid_details(cursor.getString(18));
                contactList.add(contact);


            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<Enrollmodel> getreceiptforcustenroll(String cusid, String enroll) {
        ArrayList<Enrollmodel> contactList = new ArrayList<Enrollmodel>();
        String selectQuery = "SELECT  * FROM " + DB_RECEIPT + " WHERE " + KEY_CUSID + " = " + "'" + cusid + "' AND " + KEY_enrollid + " = " + "'" + enroll + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Enrollmodel contact = new Enrollmodel();
                contact.setTableid(cursor.getString(0));
                contact.setEnrollid(cursor.getString(1));
                contact.setScheme(cursor.getString(2));
                contact.setPending_Amt(cursor.getString(3));
                contact.setPenalty_Amt(cursor.getString(4));
                contact.setBonus_Amt(cursor.getString(5));
                contact.setPaid_Amt(cursor.getString(6));
                contact.setGroup_Name(cursor.getString(7));
                contact.setPayamount(cursor.getString(8));
                contact.setGroup_Ticket_Name(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPendingdys(cursor.getString(11));
                contact.setInsamt(cursor.getString(12));
                contact.setCusid(cursor.getString(13));
                contact.setAdvanceamnt(cursor.getString(14));
                contact.setCompleted_auction(cursor.getString(17));
                contact.setPaid_details(cursor.getString(18));

                contactList.add(contact);


            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public void addenroll(ArrayList<Enrollmodel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {
            ContentValues values = new ContentValues();

            Enrollmodel contact = sched.get(i);
            values.put(KEY_enrollid, contact.getEnrollid());
            values.put(KEY_Scheme, contact.getScheme());
            values.put(KEY_Pending_Amt, contact.getPending_Amt());
            values.put(KEY_Penalty_Amt, contact.getPenalty_Amt());
            values.put(KEY_Bonus_Amt, contact.getBonus_Amt());
            values.put(KEY_Paid_Amt, contact.getPaid_Amt());
            values.put(KEY_Group_Name, contact.getGroup_Name());
            values.put(KEY_payamount, contact.getPayamount());
            values.put(KEY_Group_Ticket_Name, contact.getGroup_Ticket_Name());
            values.put(KEY_cusbranch, contact.getCusbranch());
            values.put(KEY_pendingdays, contact.getPendingdys());
            values.put(KEY_insamt, "0");
            values.put(KEY_Advance, contact.getAdvanceamnt());
            values.put(KEY_Paymenttype, contact.getPaymentType());
            values.put(KEY_completed_action, contact.getCompleted_auction());
            values.put(KEY_paid_details, contact.getPaid_details());
            db.insert(TABLE_CONTACTS, null, values);
        }
        db.close();
    }

    public void addviewreceipt(String cusid, String Cusname, String enrollid, String scheme, String penaltyamnt, String bonusamnt, String paidamnt, String groupname, String payamout, String grp_tic_name, String cusbranch, String pend_days, String insamnt, String cheqno, String pendamnt, String cheqdate, String Cheqbank, String Cheqbranch, String tranno, String transdate, String paytype, String remark, String status, String advance, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(" add to view database");
        ContentValues values = new ContentValues();
        values.put(KEY_TEMP_enrollid, enrollid);
        values.put(KEY_TEMP_Scheme, scheme);
        values.put(KEY_TEMP_Pending_Amt, pendamnt);
        values.put(KEY_TEMP_Penalty_Amt, penaltyamnt);
        values.put(KEY_TEMP_Bonus_Amt, bonusamnt);
        values.put(KEY_TEMP_Paid_Amt, paidamnt);
        values.put(KEY_TEMP_Group_Name, groupname);
        values.put(KEY_TEMP_payamount, payamout);
        values.put(KEY_TEMP_Group_Ticket_Name, grp_tic_name);
        values.put(KEY_TEMP_cusbranch, cusbranch);
        values.put(KEY_TEMP_pendingdays, pend_days);
        values.put(KEY_TEMP_insamt, insamnt);
        values.put(KEY_TEMP_Cusid, cusid);
        values.put(KEY_TEMP_Cusname, Cusname);
        values.put(KEY_TEMP_cheno, cheqno);
        values.put(KEY_TEMP_chedate, cheqdate);
        values.put(KEY_TEMP_chebank, Cheqbank);
        values.put(KEY_TEMP_chebranch, Cheqbranch);
        values.put(KEY_TEMP_tranno, tranno);
        values.put(KEY_TEMP_rtgsdate, transdate);
        System.out.println("============database============");
        System.out.println(cheqno);
        System.out.println(cheqdate);
        System.out.println(Cheqbank);
        System.out.println(Cheqbranch);
        System.out.println(tranno);
        System.out.println(transdate);
        System.out.println("============database============");
        values.put(KEY_TEMP_PAYTYPE, paytype);
        values.put(KEY_TEMP_REMARK, remark);
        values.put(KEY_TEMP_STATUS, status);
        values.put(KEY_TEMP_Advance, advance);
        values.put(KEY_VIEW_Date, date);
        db.insert(TABLE_VIEW_RECEIPT, null, values);


        db.close();
    }

    public void addtempreceipt(String cusid, String Cusname, String enrollid, String scheme, String penaltyamnt, String bonusamnt, String paidamnt, String groupname, String payamout, String grp_tic_name, String cusbranch, String pend_days, String insamnt, String cheqno, String pendamnt, String cheqdate, String Cheqbank, String Cheqbranch, String tranno, String transdate, String paytype, String remark, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEMP_enrollid, enrollid);
        values.put(KEY_TEMP_Scheme, scheme);
        values.put(KEY_TEMP_Pending_Amt, pendamnt);
        values.put(KEY_TEMP_Penalty_Amt, penaltyamnt);
        values.put(KEY_TEMP_Bonus_Amt, bonusamnt);
        values.put(KEY_TEMP_Paid_Amt, paidamnt);
        values.put(KEY_TEMP_Group_Name, groupname);
        values.put(KEY_TEMP_payamount, payamout);
        values.put(KEY_TEMP_Group_Ticket_Name, grp_tic_name);
        values.put(KEY_TEMP_cusbranch, cusbranch);
        values.put(KEY_TEMP_pendingdays, pend_days);
        values.put(KEY_TEMP_insamt, insamnt);
        values.put(KEY_TEMP_Cusid, cusid);
        values.put(KEY_TEMP_Cusname, Cusname);
        values.put(KEY_TEMP_cheno, cheqno);
        values.put(KEY_TEMP_chedate, cheqdate);
        values.put(KEY_TEMP_chebank, Cheqbank);
        values.put(KEY_TEMP_chebranch, Cheqbranch);
        values.put(KEY_TEMP_tranno, tranno);
        values.put(KEY_TEMP_rtgsdate, transdate);
        System.out.println("============database============");
        System.out.println(cheqno);
        System.out.println(cheqdate);
        System.out.println(Cheqbank);
        System.out.println(Cheqbranch);
        System.out.println(tranno);
        System.out.println(transdate);
        System.out.println("============database============");
        values.put(KEY_TEMP_PAYTYPE, paytype);
        values.put(KEY_TEMP_REMARK, remark);
        values.put(KEY_TEMP_STATUS, status);
        db.insert(TABLE_TEMP_RECEIPT, null, values);

        db.close();
    }

    public ArrayList<TempEnrollModel> getAllViewenroll(String date) {
        ArrayList<TempEnrollModel> contactList = new ArrayList<TempEnrollModel>();
        System.out.println(date + "date no");
        String selectQuery = "SELECT  * FROM " + TABLE_VIEW_RECEIPT + " WHERE " + KEY_VIEW_Date + " = '" + date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TempEnrollModel contact = new TempEnrollModel();
                contact.setTableid(cursor.getString(0));
                contact.setCusid(cursor.getString(1));
                contact.setCusname(cursor.getString(2));
                contact.setChequeNo(cursor.getString(3));
                contact.setChequeDate(cursor.getString(4));
                contact.setChequeBank(cursor.getString(5));
                contact.setChequeBranch(cursor.getString(6));
                contact.setTransNo(cursor.getString(7));
                contact.setTransDate(cursor.getString(8));
                contact.setEnrollid(cursor.getString(9));
                contact.setScheme(cursor.getString(10));
                contact.setPending_Amt(cursor.getString(11));
                contact.setPenalty_Amt(cursor.getString(12));
                contact.setBonus_Amt(cursor.getString(13));
                contact.setPaid_Amt(cursor.getString(14));
                contact.setGroup_Name(cursor.getString(15));
                contact.setPayamount(cursor.getString(16));
                contact.setGroup_Ticket_Name(cursor.getString(17));
                contact.setCusbranch(cursor.getString(18));
                contact.setPendingdys(cursor.getString(19));
                contact.setInsamt(cursor.getString(20));
                contact.setPaytype(cursor.getString(21));
                contact.setRemark(cursor.getString(22));
                contact.setStatus(cursor.getString(23));
                contact.setAdvance(cursor.getString(24));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<TempEnrollModel> getAllTempenroll() {
        ArrayList<TempEnrollModel> contactList = new ArrayList<TempEnrollModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEMP_RECEIPT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TempEnrollModel contact = new TempEnrollModel();
                contact.setTableid(cursor.getString(0));
                contact.setCusid(cursor.getString(1));
                contact.setCusname(cursor.getString(2));
                contact.setChequeNo(cursor.getString(3));
                contact.setChequeDate(cursor.getString(4));
                contact.setChequeBank(cursor.getString(5));
                contact.setChequeBranch(cursor.getString(6));
                contact.setTransNo(cursor.getString(7));
                contact.setTransDate(cursor.getString(8));
                contact.setEnrollid(cursor.getString(9));
                contact.setScheme(cursor.getString(10));
                contact.setPending_Amt(cursor.getString(11));
                contact.setPenalty_Amt(cursor.getString(12));
                contact.setBonus_Amt(cursor.getString(13));
                contact.setPaid_Amt(cursor.getString(14));
                contact.setGroup_Name(cursor.getString(15));
                contact.setPayamount(cursor.getString(16));
                contact.setGroup_Ticket_Name(cursor.getString(17));
                contact.setCusbranch(cursor.getString(18));
                contact.setPendingdys(cursor.getString(19));
                contact.setInsamt(cursor.getString(20));
                contact.setPaytype(cursor.getString(21));
                contact.setRemark(cursor.getString(22));
                contact.setStatus(cursor.getString(23));
                contact.setAdvance(cursor.getString(24));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<Enrollmodel> getAllenroll() {
        ArrayList<Enrollmodel> contactList = new ArrayList<Enrollmodel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Enrollmodel contact = new Enrollmodel();
                contact.setTableid(cursor.getString(0));
                contact.setEnrollid(cursor.getString(1));
                contact.setScheme(cursor.getString(2));
                contact.setPending_Amt(cursor.getString(3));
                contact.setPenalty_Amt(cursor.getString(4));
                contact.setBonus_Amt(cursor.getString(5));
                contact.setPaid_Amt(cursor.getString(6));
                contact.setGroup_Name(cursor.getString(7));
                contact.setPayamount(cursor.getString(8));
                contact.setGroup_Ticket_Name(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPendingdys(cursor.getString(11));
                contact.setInsamt(cursor.getString(12));
                contact.setAdvanceamnt(cursor.getString(13));

                contactList.add(contact);


            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public String gettotal() {
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int total = 0;
        if (cursor.moveToFirst()) {
            do {
                try {
                    total = total + Integer.parseInt(cursor.getString(8));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }
        db.close();

        return String.valueOf(total);
    }

    public String gettotaladvancereceipt(String cusid, String enroll) {
        String selectQuery = "SELECT  * FROM " + DB_RECEIPT + " WHERE " + KEY_CUSID + " = " + "'" + cusid + "' AND " + KEY_enrollid + " = " + "'" + enroll + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int total = 0;
        if (cursor.moveToFirst()) {
            do {
                try {
                    total = total + Integer.parseInt(cursor.getString(14));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }
        db.close();

        return String.valueOf(total);
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int value = cursor.getCount();
        db.close();

        return value;
    }

    public int getoflinedbCount() {
        String countQuery = "SELECT  * FROM " + DB_RECEIPT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int value = cursor.getCount();
        db.close();

        return value;
    }

}
