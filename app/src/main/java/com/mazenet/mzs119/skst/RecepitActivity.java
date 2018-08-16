package com.mazenet.mzs119.skst;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterenrollment;
import com.mazenet.mzs119.skst.Database.Databaserecepit;
import com.mazenet.mzs119.skst.Model.Enrollmodel;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;
import com.mazenet.mzs119.skst.Utils.NDSpinner;
import com.mazenet.mzs119.skst.Utils.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RecepitActivity extends AppCompatActivity {

    public ArrayList<Enrollmodel> enroll_list = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_list_local = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listmain = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listpost = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listlocmain = new ArrayList<Enrollmodel>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    String url = Config.reteriveenroll;
    String url2 = Config.saverecepit;
    String url3 = Config.retrievealluser;
    String Enroll_update_cust = Config.Enroll_update_cust;
    EditText edt_re_amount, edt_re_remark, edt_re_rtgsno;
    TextView txt_totalamt;
    NonScrollListView lst_re_enroll;
    String cusid = "0", cusname = "";
    Button btn_autopopulate, btn_submit, btn_che_date, btn_dd_date, btn_rtgs_date;
    CustomAdapterenrollment adapterlist;
    Databaserecepit dbrecepit;
    String recepitamt = "0";
    int Balanceamt = 0;
    NDSpinner spinner;
    StringBuilder groupnam = new StringBuilder();
    StringBuilder penaly = new StringBuilder();
    StringBuilder payable = new StringBuilder();
    StringBuilder pending = new StringBuilder();
    StringBuilder bonus = new StringBuilder();
    TextView txt_cusname;
    ArrayList<String> list = new ArrayList<String>();
    String paytype = "Cash";
    LinearLayout lay_re_che, lay_re_dd, lay_re_rtgs;
    EditText edt_re_cheno, edt_re_chebank, edt_re_chebranch, edt_re_ddno, edt_re_ddbank, edt_re_ddbranch;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat df;
    String str_chedate = "", str_dddate = "", str_rtgsdate = "", str_cheno = "", str_chebank = "", str_chebranch = "", str_remark = "", str_tranno = "", ReceiptGroup = "", typee = "";
    Double BonusAmnt = 0.0, penaltyAmnt = 0.0, pendingnt = 0.0, payableamnt = 0.0;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepit);

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();


        df = new SimpleDateFormat("dd-MM-yyyy");


        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);

        lay_re_che = (LinearLayout) findViewById(R.id.lay_re_che);
        lay_re_dd = (LinearLayout) findViewById(R.id.lay_re_dd);
        lay_re_rtgs = (LinearLayout) findViewById(R.id.lay_re_rtgs);

        spinner = (NDSpinner) findViewById(R.id.spn_paytype);

        edt_re_amount = (EditText) findViewById(R.id.edt_re_amount);
        edt_re_remark = (EditText) findViewById(R.id.edt_re_remark);

        edt_re_cheno = (EditText) findViewById(R.id.edt_re_cheno);
        edt_re_chebank = (EditText) findViewById(R.id.edt_re_chebank);
        edt_re_chebranch = (EditText) findViewById(R.id.edt_re_chebranch);


        edt_re_ddno = (EditText) findViewById(R.id.edt_re_ddno);
        edt_re_ddbank = (EditText) findViewById(R.id.edt_re_ddbank);
        edt_re_ddbranch = (EditText) findViewById(R.id.edt_re_ddbranch);

        edt_re_rtgsno = (EditText) findViewById(R.id.edt_re_rtgsno);

        lst_re_enroll = (NonScrollListView) findViewById(R.id.lst_re_enroll);

        btn_autopopulate = (Button) findViewById(R.id.btn_autopopulate);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_che_date = (Button) findViewById(R.id.btn_che_date);
        btn_dd_date = (Button) findViewById(R.id.btn_dd_date);
        btn_rtgs_date = (Button) findViewById(R.id.btn_rtgs_date);

        txt_cusname = (TextView) findViewById(R.id.ra_name);
        txt_totalamt = (TextView) findViewById(R.id.txt_totalamt);

        dbrecepit = new Databaserecepit(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pref.getString("username", ""));
        try {
            Intent it = getIntent();
            cusid = it.getStringExtra("cusid");
            cusname = it.getStringExtra("cusname");
            typee = it.getStringExtra("type");
            txt_cusname.setText(cusname);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //


        try {

            Calendar newCalendar = Calendar.getInstance();

            int ddd = newCalendar.get(Calendar.DAY_OF_MONTH);
            int wwww = newCalendar.get(Calendar.WEEK_OF_YEAR);
            if (ddd == pref.getInt("dailycheckdaymain", 0) && wwww == pref.getInt("dailycheckmonthmain", 0) && dbrecepit.getoflinedbCount() != 0) {


            } else {

                editor.putInt("dailycheckdaymain",
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                editor.putInt("dailycheckmonthmain",
                        newCalendar.get(Calendar.WEEK_OF_YEAR));

                editor.commit();
                reterivelocal();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cd.isConnectedToInternet()) {
            System.out.println("int");
            reteriveall();
        } else {
            System.out.println("noint");
            reterivefromdb();
        }

        recepitamt = "0";
        Balanceamt = 0;
        btn_autopopulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recepitamt = edt_re_amount.getText().toString();

                if (recepitamt.equalsIgnoreCase("0") || recepitamt.equalsIgnoreCase("")) {
                    Toast.makeText(RecepitActivity.this, "Enter Receipt Amount", Toast.LENGTH_LONG).show();
                } else {
                    Balanceamt = Integer.parseInt(recepitamt);

                    enroll_listmain = dbrecepit.getAllenroll();

                    for (int i = 0; i < enroll_listmain.size(); i++) {
                        Enrollmodel mod = enroll_listmain.get(i);
                        String Groupname = mod.getGroup_Name();
                        String pendingamt = mod.getPending_Amt();
                        String penaltyamt = mod.getPenalty_Amt();
                        String bonusamt = mod.getBonus_Amt();
                        String tableid = mod.getTableid();
                        String insamnt = mod.getInsamt();
                        groupnam.append(Groupname + ",");
                        if (Balanceamt > 0) {
                            int payable = Integer.parseInt(pendingamt) + Integer.parseInt(penaltyamt) - Integer.parseInt(bonusamt);

                            if (Balanceamt >= payable) {

                                int pay2 = Integer.parseInt(pendingamt) - Integer.parseInt(bonusamt);
                                dbrecepit.updatepayamount(String.valueOf(payable), tableid, String.valueOf(pay2));

                                Balanceamt = Balanceamt - payable;
                            } else {
                                int payable1 = Integer.parseInt(pendingamt) + Integer.parseInt(penaltyamt);
                                // int payable2 = payable1 - Balanceamt;
                                int pay2 = Balanceamt - Integer.parseInt(penaltyamt);

                                dbrecepit.updatepayamount(String.valueOf(Balanceamt), tableid, String.valueOf(pay2));

                                Balanceamt = Balanceamt - payable1;
                                if (Balanceamt < 0) {
                                    Balanceamt = 0;
                                }
                            }

                        } else {
                            dbrecepit.updatepayamount(String.valueOf(0), tableid, String.valueOf(0));

                        }


                    }


                    enroll_list = dbrecepit.getAllenroll();
                    adapterlist = new CustomAdapterenrollment(RecepitActivity.this, enroll_list);
                    adapterlist.notifyDataSetChanged();
                    // ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                    lst_re_enroll.setAdapter(adapterlist);
                    txt_totalamt.setText(dbrecepit.gettotal());


                }

            }
        });

        list.clear();
        list.add("Cash");
        list.add("Cheque");
        list.add("D.D");
        list.add("RTGS/NEFT");
        list.add("Card");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                RecepitActivity.this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int i, long l) {
                paytype = list.get(i);

                if (paytype.equalsIgnoreCase("Cash")) {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.GONE);

                } else if (paytype.equalsIgnoreCase("Cheque")) {
                    lay_re_che.setVisibility(View.VISIBLE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.GONE);


                } else if (paytype.equalsIgnoreCase("D.D")) {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.VISIBLE);
                    lay_re_rtgs.setVisibility(View.GONE);


                } else if (paytype.equalsIgnoreCase("RTGS/NEFT")) {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.VISIBLE);


                } else if (paytype.equalsIgnoreCase("Card")) {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.VISIBLE);


                } else {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.GONE);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = txt_totalamt.getText().toString();
                String amount2 = edt_re_amount.getText().toString();
                str_remark = edt_re_remark.getText().toString();


                if (amount.equalsIgnoreCase(amount2)) {
                    enroll_listpost.clear();
                    enroll_listpost = dbrecepit.getAllenroll();
                    if (paytype.equalsIgnoreCase("Cheque")) {

                        str_chebank = edt_re_chebank.getText().toString();
                        str_chebranch = edt_re_chebranch.getText().toString();
                        str_cheno = edt_re_cheno.getText().toString();

                        if (str_chebank.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Enter Cheque Bank", Toast.LENGTH_LONG).show();

                        } else if (str_chebranch.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Enter Cheque Bank Branch", Toast.LENGTH_LONG).show();

                        } else if (str_cheno.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Enter Cheque Number", Toast.LENGTH_LONG).show();

                        } else if (str_chedate.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Select Cheque Date", Toast.LENGTH_LONG).show();

                        } else {

                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                String enrollid = mod.getEnrollid();
                                String bonusamt = mod.getBonus_Amt();
                                String paidamt = mod.getPaid_Amt();
                                String pendingamt = mod.getPending_Amt();
                                String penaltyamt = mod.getPenalty_Amt();
                                String Group = mod.getGroup_Name();
                                String ticketno = mod.getGroup_Ticket_Name();
                                String payamount = mod.getPayamount();
                                String chitvalue = mod.getScheme();
                                String cusbranch = mod.getCusbranch();
                                String pendingdays = mod.getPendingdys();
                                String payableamount = mod.getInsamt();

                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {
                                    //  postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payableamount, str_remark, chitvalue, "cheque", cusbranch, pendingdays, "Pending");
                                }

                            }

                            // updateenroll();

                        }

                    } else if (paytype.equalsIgnoreCase("D.D")) {

                        str_chebank = edt_re_ddbank.getText().toString();
                        str_chebranch = edt_re_ddbranch.getText().toString();
                        str_cheno = edt_re_ddno.getText().toString();
                        str_chedate = str_dddate;
                        if (str_chebank.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Enter DD Bank", Toast.LENGTH_LONG).show();

                        } else if (str_chebranch.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Enter DD Bank Branch", Toast.LENGTH_LONG).show();

                        } else if (str_cheno.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Enter DD Number", Toast.LENGTH_LONG).show();

                        } else if (str_dddate.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Select DD Date", Toast.LENGTH_LONG).show();

                        } else {

                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                String enrollid = mod.getEnrollid();
                                String bonusamt = mod.getBonus_Amt();
                                String paidamt = mod.getPaid_Amt();
                                String pendingamt = mod.getPending_Amt();
                                String penaltyamt = mod.getPenalty_Amt();
                                String Group = mod.getGroup_Name();
                                String ticketno = mod.getGroup_Ticket_Name();
                                String payamount = mod.getPayamount();
                                String chitvalue = mod.getScheme();
                                String cusbranch = mod.getCusbranch();
                                String pendingdays = mod.getPendingdys();
                                String payableamount = mod.getInsamt();
                           /*     pendingnt+=Double.parseDouble(pendingamt);
                                System.out.println("legalityyyyyyyyyyyyyyyyy"+pendingnt);
                                penaltyAmnt+=Double.parseDouble(penaltyamt);
                                BonusAmnt+=Double.parseDouble(bonusamt);
                                payableamnt+=Double.parseDouble(payableamount);
                                System.out.println("legalityyyyyyyyyyyyyyyyy"+BonusAmnt);
                                System.out.println("legalityyyyyyyyyyyyyyyyy"+penaltyAmnt);
                                System.out.println("legalityyyyyyyyyyyyyyyyy"+payableamnt); */
                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {
                                    //  postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payableamount, str_remark, chitvalue, "dd", cusbranch, pendingdays, "Active");
                                }

                            }
                            //  updateenroll();

                        }

                    } else if (paytype.equalsIgnoreCase("RTGS/NEFT")) {


                        str_tranno = edt_re_rtgsno.getText().toString();


                        if (str_rtgsdate.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Select Transaction Date", Toast.LENGTH_LONG).show();

                        } else {

                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                String enrollid = mod.getEnrollid();
                                String bonusamt = mod.getBonus_Amt();
                                String paidamt = mod.getPaid_Amt();
                                String pendingamt = mod.getPending_Amt();
                                String penaltyamt = mod.getPenalty_Amt();
                                String Group = mod.getGroup_Name();
                                String ticketno = mod.getGroup_Ticket_Name();
                                String payamount = mod.getPayamount();
                                String chitvalue = mod.getScheme();
                                String cusbranch = mod.getCusbranch();
                                String pendingdays = mod.getPendingdys();
                                String payableamount = mod.getInsamt();

                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {
                                    // postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payableamount, str_remark, chitvalue, "neft", cusbranch, pendingdays, "Active");
                                }

                            }
                            //  updateenroll();

                        }

                    } else if (paytype.equalsIgnoreCase("Card")) {


                        str_tranno = edt_re_rtgsno.getText().toString();


                        if (str_rtgsdate.equalsIgnoreCase("")) {
                            Toast.makeText(RecepitActivity.this, "Select Transaction Date", Toast.LENGTH_LONG).show();

                        } else {

                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                String enrollid = mod.getEnrollid();
                                String bonusamt = mod.getBonus_Amt();
                                String paidamt = mod.getPaid_Amt();
                                String pendingamt = mod.getPending_Amt();
                                String penaltyamt = mod.getPenalty_Amt();
                                String Group = mod.getGroup_Name();
                                String ticketno = mod.getGroup_Ticket_Name();
                                String payamount = mod.getPayamount();
                                String chitvalue = mod.getScheme();
                                String cusbranch = mod.getCusbranch();
                                String pendingdays = mod.getPendingdys();
                                String payableamount = mod.getInsamt();

                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {
                                    //   postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payableamount, str_remark, chitvalue, "card", cusbranch, pendingdays, "Active");
                                }

                            }

                            //updateenroll();


                        }

                    } else {
                        if (cd.isConnectedToInternet()) {
                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                String enrollid = mod.getEnrollid();
                                String bonusamt = mod.getBonus_Amt();
                                String paidamt = mod.getPaid_Amt();
                                String pendingamt = mod.getPending_Amt();
                                String penaltyamt = mod.getPenalty_Amt();
                                String Group = mod.getGroup_Name();
                                String ticketno = mod.getGroup_Ticket_Name();
                                String payamount = mod.getPayamount();
                                String chitvalue = mod.getScheme();
                                String cusbranch = mod.getCusbranch();
                                String pendingdays = mod.getPendingdys();
                                String payableamount = mod.getInsamt();

                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {

                                }

                            }
                        } else {

                        }


                    }


                    onBackPressed();
                    ReceiptGroup = groupnam.toString();


                } else {
                    Toast.makeText(RecepitActivity.this, "Amount Mismatch", Toast.LENGTH_LONG).show();
                }

            }
        });


        btn_che_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(RecepitActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_chedate = df.format(newDate.getTime());
                            btn_che_date.setText(str_chedate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("Cheque date");

                fromDatePickerDialog.show();

            }
        });
        btn_rtgs_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(RecepitActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_rtgsdate = df.format(newDate.getTime());
                            btn_rtgs_date.setText(str_rtgsdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("RTGS date");

                fromDatePickerDialog.show();

            }
        });


        btn_dd_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(RecepitActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_dddate = df.format(newDate.getTime());
                            btn_dd_date.setText(str_dddate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("DD date");

                fromDatePickerDialog.show();

            }
        });
        //======================================================================================================
        lst_re_enroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Enrollmodel sched = enroll_list.get(i);
                String groupname = sched.getGroup_Name();
                String Enrolid = sched.getEnrollid();
                Intent iy = new Intent(RecepitActivity.this, IndividualReceipt.class);
                iy.putExtra("cusid", cusid);
                iy.putExtra("cusname", cusname);
                iy.putExtra("groupname", groupname);
                iy.putExtra("enrollid", Enrolid);
                startActivity(iy);
                finish();

            }
        });

        //========================================================================================================


    }


    public void postentry(final String enrollidd, final String bonusamt, final String paidamt, final String pendingamt, final String penaltyamt, final String Group, final String ticketno, final String payableamount, final String Remark, final String chitvalue, final String paytype, final String Cus_Branch, final String Pending_Days, final String status) {

        showDialog();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cust_Id", cusid);
                params.put("enrollid", enrollidd);
                params.put("bonusamt", bonusamt);
                params.put("paidamt", paidamt);
                params.put("pendingamt", pendingamt);
                params.put("penaltyamt", penaltyamt);
                params.put("Groupid", Group);
                params.put("ticketno", ticketno);
                params.put("payamount", payableamount);
                params.put("Emp_Id", pref.getString("userid", "0"));
                params.put("Created_By", pref.getString("username", "DEMO"));
                params.put("Remark", Remark);
                params.put("Customer_Name", cusname);
                params.put("Chit_Value", chitvalue);
                params.put("Payment_Type", paytype);
                params.put("Cheque_No", str_cheno);
                params.put("Cheque_Date", str_chedate);
                params.put("Bank_Name", str_chebank);
                params.put("Branch_Name", str_chebranch);
                params.put("Trn_No", str_tranno);
                params.put("Trn_Date", str_rtgsdate);
                params.put("Cus_Branch", Cus_Branch);
                params.put("Pending_Days", Pending_Days);
                params.put("Emp_Branch", pref.getString("empbranch", "3"));
                params.put("status", status);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    // ===========================================================================
    private void reterivefromdb() {
        showDialog();
        enroll_list.clear();

        if (typee.equalsIgnoreCase("Monthly")) {
            System.out.println("month");
            enroll_list = dbrecepit.getreceiptforcust(cusid, typee);
        } else if (typee.equalsIgnoreCase("Weekly")) {
            System.out.println("week");
            enroll_list = dbrecepit.getreceiptforcust(cusid, typee);
        } else if (typee.equalsIgnoreCase("Daily")) {
            System.out.println("da");
            enroll_list = dbrecepit.getreceiptforcust(cusid, typee);
        } else {
            System.out.println("a");
            enroll_list = dbrecepit.getreceiptforcust(cusid);
        }
        if (enroll_list.size() > 0) {
            enroll_listlocmain.addAll(enroll_list);
            adapterlist = new CustomAdapterenrollment(RecepitActivity.this, enroll_listlocmain);
            adapterlist.notifyDataSetChanged();
            System.out.println(enroll_listlocmain + " locmain");
            lst_re_enroll.setAdapter(adapterlist);
        }


        hidePDialog();
    }

    private void reterivefromdblocal() {
        showDialog();
        enroll_list.clear();

        if (typee.equalsIgnoreCase("Monthly")) {
            System.out.println("month");
            enroll_list = dbrecepit.getreceiptforcustlocal(cusid, typee);
        } else if (typee.equalsIgnoreCase("Weekly")) {
            System.out.println("week");
            enroll_list = dbrecepit.getreceiptforcustlocal(cusid, typee);
        } else if (typee.equalsIgnoreCase("Daily")) {
            System.out.println("da");
            enroll_list = dbrecepit.getreceiptforcustlocal(cusid, typee);
        } else {
            System.out.println("a");
            enroll_list = dbrecepit.getreceiptforcustlocal(cusid);
        }
        if (enroll_list.size() > 0) {
            enroll_listlocmain.addAll(enroll_list);
            adapterlist = new CustomAdapterenrollment(RecepitActivity.this, enroll_listlocmain);
            adapterlist.notifyDataSetChanged();
            System.out.println(enroll_listlocmain + " locmain");
            lst_re_enroll.setAdapter(adapterlist);
        }else
        {
            reterivefromdb();
        }


        hidePDialog();
    }


    public void reteriveall() {

        showDialog();
        enroll_list.clear();
        dbrecepit.deletetable();
        StringRequest stringreq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());

                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);

                            Enrollmodel sched = new Enrollmodel();
                            sched.setEnrollid(jObj.getString("Id"));
                            sched.setScheme(jObj.getString("Chit_value"));
                            sched.setPending_Amt(jObj.getString("Pending_Amt"));
                            sched.setPaid_Amt(jObj.getString("Paid_Amt"));
                            sched.setPenalty_Amt(jObj.getString("Penalty_Amt"));
                            sched.setBonus_Amt(jObj.getString("Bonus_Amt"));
                            sched.setGroup_Name(jObj.getString("Group_Name"));
                            sched.setGroup_Ticket_Name(jObj.getString("Group_Ticket_Name"));
                            sched.setCusbranch(jObj.getString("Branch_Id"));
                            sched.setPendingdys(jObj.getString("Pending_Days"));
                            sched.setAdvanceamnt(jObj.getString("Advance_Amt"));
                            sched.setPaymentType(jObj.getString("Payment_Type"));
                            sched.setCompleted_auction(jObj.getString("Completed_auction"));
                            sched.setPaid_details(jObj.getString("Paid_Details"));
                            sched.setCusid(cusid);

                            System.out.println("Paiddetails ="+jObj.getString("Paid_Details"));
                            sched.setPayamount("0");
                            enroll_list.add(sched);



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (enroll_list.size() > 0) {

                        try {
                            dbrecepit.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            dbrecepit.addenroll(enroll_list);
                            reterivefromdblocal();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                RecepitActivity.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Data from Server. contact Admin");

                        alertDialog.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();


                                    }
                                });
                        alertDialog.setNegativeButton("Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        onBackPressed();
                                    }
                                });
                        alertDialog.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cust_Id", cusid);
                System.out.println("cusod " + cusid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringreq);
    }


    // ===========================================================================


    // ===========================================================================


    //============================================================================

    private void hidePDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();

        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
        pDialog.setContentView(R.layout.my_progress);

    }

    // ===========================================================================
    private void reterivelocal() {


        enroll_list_local.clear();
        System.out.println("locallllllll retriveeeeeeeee");
        StringRequest localreq = new StringRequest(Request.Method.POST,
                url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());


                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");
                    System.out.println("locallllllll  responseeeeeeeee");
                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);

                            Enrollmodel sched = new Enrollmodel();
                            sched.setEnrollid(jObj.getString("enrollid"));
                            sched.setCusid(jObj.getString("Cust_Id"));
                            sched.setScheme(jObj.getString("chitvalue"));
                            sched.setPending_Amt(jObj.getString("pendingamnt"));
                            sched.setPaid_Amt(jObj.getString("paidamnt"));
                            sched.setPenalty_Amt("0");
                            sched.setBonus_Amt("0");
                            sched.setGroup_Name(jObj.getString("grpname"));
                            sched.setPendingdys(jObj.getString("pendingdays"));
                            sched.setCusbranch(pref.getString("empbranch", ""));
                            sched.setGroup_Ticket_Name(jObj.getString("grpticketno"));
                            sched.setPayamount("0");
                            sched.setPaymentType(jObj.getString("Payment_Type"));
                            sched.setCollect_emp(jObj.getString("Collect_Emp"));
                            sched.setCompleted_auction(jObj.getString("Completed_auction"));
                            sched.setPaid_details(jObj.getString("Paid_Details"));
                            System.out.println(jObj.getString("Payment_Type"));
                            sched.setAdvanceamnt(jObj.getString("advanceamnt"));
                            enroll_list_local.add(sched);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (enroll_list_local.size() > 0) {

                        try {
                            dbrecepit.deletetableaLLREC();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            dbrecepit.addallreceipt(enroll_list_local);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                RecepitActivity.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Data from Server  locall. contact Admin");

                        alertDialog.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();


                                    }
                                });
                        alertDialog.setNegativeButton("Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        onBackPressed();
                                    }
                                });
                        alertDialog.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", pref.getString("userid", ""));
                System.out.println("cuseidd " + pref.getString("userid", ""));
                return params;
            }

        };
        localreq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }
}
