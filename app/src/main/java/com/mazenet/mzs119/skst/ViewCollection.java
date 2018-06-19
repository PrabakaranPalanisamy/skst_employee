package com.mazenet.mzs119.skst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import com.google.zxing.common.reedsolomon.GenericGF;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterLoanDatewise;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterViewCol;
import com.mazenet.mzs119.skst.Database.Databaserecepit;

import com.mazenet.mzs119.skst.Model.LoanModelDatewise;
import com.mazenet.mzs119.skst.Model.TempEnrollModel;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewCollection extends AppCompatActivity {
    ArrayList<TempEnrollModel> viewreceipt = new ArrayList<>();
    ArrayList<LoanModelDatewise> viewloanreceipt = new ArrayList<>();
    Databaserecepit dbrecepit;
    ConnectionDetector cd;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView view_list;
    CustomAdapterViewCol cav;
    CustomAdapterLoanDatewise loanadaptrerdatewise;
    SimpleDateFormat dateFormat;
    String date = "";
    int tot, tot1;
    TextView txt_tot;
    Locale curLocale = new Locale("en", "IN");
    Button btn_colreceipt, btn_loanreceipt;
    Boolean isLoans = false;
    private static final String TAG = MainActivity.class.getSimpleName();

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private String getYesterdayDateString() {
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(yesterday());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_collection);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pref.getString("username", ""));
        String newuser = pref.getString("username", "");
        String olduser = pref.getString("olduser", "");

        cd = new ConnectionDetector(this);
        txt_tot = (TextView) findViewById(R.id.txt_vc_total);
        view_list = (ListView) findViewById(R.id.view_list);
        dbrecepit = new Databaserecepit(ViewCollection.this);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = new Date();
        date = dateFormat.format(date1);
        btn_colreceipt = (Button) findViewById(R.id.vc_collection_receipts);
        btn_loanreceipt = (Button) findViewById(R.id.vc_loan_receipts);
        String yesterday = getYesterdayDateString();
        dbrecepit.deletetableview(yesterday);
        dbrecepit.deletetableloansview(yesterday);
        if (olduser.equalsIgnoreCase(newuser) || olduser.equalsIgnoreCase("")) {
            viewreceipt.clear();
            System.out.println("run");
            viewloanreceipt.clear();
            viewloanreceipt = dbrecepit.getviewLoan(date);
            viewreceipt = dbrecepit.getAllViewenroll(date);
        } else {
            System.out.println("run2");
            editor.putString("olduser", newuser);
            editor.commit();
            dbrecepit.deletetableview1();
            dbrecepit.deletetableloansview1();
            viewreceipt.clear();
            viewloanreceipt.clear();

        }
        getviewlist();


        view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isLoans) {
                    if (viewloanreceipt.size() > 0) {
                        System.out.println("loan print");
                        LoanModelDatewise lmd = viewloanreceipt.get(i);
                        Intent it = new Intent(ViewCollection.this, PrintActivityLoansDatewise.class);
                        it.putExtra("Cusname", lmd.getCust_name());
                        it.putExtra("Amount", lmd.getAmount());
                        it.putExtra("Reference", lmd.getReference_Grp());
                        it.putExtra("date", lmd.getDate());
                        startActivity(it);
                    }
                } else {

                    if (viewreceipt.size() > 0) {
                        System.out.println("col print");
                        String cusid = "";
                        TempEnrollModel tem = viewreceipt.get(i);
                        String enrollid = tem.getEnrollid();
                        String bonusamnt = tem.getBonus_Amt();
                        String paidamnt = tem.getPaid_Amt();
                        String pendingamnt = tem.getPending_Amt();
                        String penaltyamnt = tem.getPenalty_Amt();
                        String Group = tem.getGroup_Name();
                        String ticketno = tem.getGroup_Ticket_Name();
                        String payableamnt = tem.getInsamt();
                        String payamont = tem.getPayamount();
                        String Remark = tem.getRemark();
                        String chitvalue = tem.getScheme();
                        String paytype = tem.getPaytype();
                        String cus_branch = tem.getCusbranch();
                        String pendingdays = tem.getPendingdys();
                        String status = tem.getStatus();
                        cusid = tem.getCusid();
                        String cusname = tem.getCusname();
                        String cheqno = tem.getChequeNo();
                        String cheqdate = tem.getChequeDate();
                        String cheqbank = tem.getChequeBank();
                        String cheqbranch = tem.getChequeBranch();
                        String transno = tem.getTransNo();
                        String rtgsdate = tem.getTransDate();
                        String advance = tem.getAdvance();
                        Intent it = new Intent(ViewCollection.this, PrintActivity.class);
                        it.putExtra("Cusname", cusname);
                        it.putExtra("Amount", payamont);
                        it.putExtra("Groupname", Group);
                        it.putExtra("ticketno", ticketno);
                        it.putExtra("paymode", paytype);
                        it.putExtra("bonusamnt", bonusamnt);
                        it.putExtra("pendingamnt", pendingamnt);
                        it.putExtra("penaltyamnt", penaltyamnt);
                        it.putExtra("instlmntamnt", payableamnt);
                        it.putExtra("advance", advance);
                        it.putExtra("Receiptno", "");
                        startActivity(it);

                    } else {

                    }
                }
            }
        });
        btn_colreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_list.setAdapter(cav);
                view_list.setVisibility(View.VISIBLE);
                isLoans = false;
                btn_colreceipt.setTextColor(getResources().getColor(R.color.white));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.black));
            }
        });
        btn_loanreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_list.setAdapter(loanadaptrerdatewise);
                txt_tot.setVisibility(View.VISIBLE);
                view_list.setVisibility(View.VISIBLE);
                btn_colreceipt.setTextColor(getResources().getColor(R.color.black));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.white));
                isLoans = true;
            }
        });

    }

    public void getviewlist() {
        tot1 = 0;
        if (viewreceipt.size() > 0) {
            tot1 = 0;
            for (int i = 0; i < viewreceipt.size(); i++) {
                System.out.println("receipt post entry");
                TempEnrollModel tem = viewreceipt.get(i);
                String tote = tem.getPayamount();

                try {
                    int tot = Integer.parseInt(tote);
                    tot1 += tot;

                } catch (Exception e) {

                }
            }


            cav = new CustomAdapterViewCol(ViewCollection.this, viewreceipt);
            cav.notifyDataSetChanged();

            btn_colreceipt.setEnabled(true);
            view_list.setAdapter(cav);
            view_list.setVisibility(View.VISIBLE);
            if (viewloanreceipt.size() > 0) {
                for (int i = 0; i < viewloanreceipt.size(); i++) {
                    System.out.println("receipt post entry");
                    LoanModelDatewise tem = viewloanreceipt.get(i);
                    String tote = tem.getAmount();

                    try {
                        int tot = Integer.parseInt(tote);
                        tot1 += tot;

                    } catch (Exception e) {

                    }
                }
                addtotal();
                loanadaptrerdatewise = new CustomAdapterLoanDatewise(ViewCollection.this, viewloanreceipt);
                loanadaptrerdatewise.notifyDataSetChanged();
            } else {
                addtotal();
                view_list.setVisibility(View.GONE);
               // Toast.makeText(ViewCollection.this, "No Loan Receipts Available", Toast.LENGTH_SHORT).show();
            }

        } else {
            view_list.setVisibility(View.GONE);
            if (viewloanreceipt.size() > 0) {
                for (int i = 0; i < viewloanreceipt.size(); i++) {
                    System.out.println("receipt post entry");
                    LoanModelDatewise tem = viewloanreceipt.get(i);
                    String tote = tem.getAmount();

                    try {
                        int tot = Integer.parseInt(tote);
                        tot1 += tot;

                    } catch (Exception e) {

                    }
                }
                addtotal();
                loanadaptrerdatewise = new CustomAdapterLoanDatewise(ViewCollection.this, viewloanreceipt);
                loanadaptrerdatewise.notifyDataSetChanged();
            } else {
                addtotal();
                view_list.setVisibility(View.GONE);
                // Toast.makeText(ViewCollection.this, "No Loan Receipts Available", Toast.LENGTH_SHORT).show();
            }
           // Toast.makeText(ViewCollection.this, "No Collection Receipts Available", Toast.LENGTH_SHORT).show();

        }

    }

    public void addtotal() {
        String advance = String.valueOf(tot1);
        try {
            if (advance.contains("-")) {
                advance = "0";
            }
            Double d = Double.parseDouble(advance);
            String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
            advance = moneyString2;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        System.out.println("toto " + tot1);
        txt_tot.setText("Rs. " + String.valueOf(advance));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_calender:
                if (cd.isConnectedToInternet()) {
                    Intent i = new Intent(ViewCollection.this, DateWiseView.class);
                    startActivity(i);
                } else {
                    Toast.makeText(ViewCollection.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}