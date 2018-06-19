package com.mazenet.mzs119.skst;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterEditSettle;
import com.mazenet.mzs119.skst.Model.SettleModel;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CashInHandActivity extends AppCompatActivity {
    String url = Config.getcashinhand;
    String settleurl = Config.settleurl;
    EditText edt_cxash;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    ScrollView srl_settle;
    Boolean show = false;
    ArrayList<SettleModel> amounts = new ArrayList<>();
    String tot = "";
    ListView list_editsettle;
    String tot_amnnt = "";
    CustomAdapterEditSettle cav;
    Boolean IsEditting = false;
    TextView txt_total;
    public LinearLayout lay_edit;
    String str_2000 = "", str_1000 = "", str_500 = "", str_200 = "", str_100 = "", str_50 = "", str_20 = "", str_10 = "", str_5 = "", str_2 = "", str_1 = "", str_others = "", str_total = "", tbl_id = "", s2 = "", s1 = "", s5 = "", s10 = "", s20 = "", s50 = "", s100 = "", s200 = "", s500 = "", s1000 = "", s2000 = "", sothers = "", stotal = "", sremark = "";
    Button btn_settle, btn_view, btn_cash_settle, btn_editsettle, btn_editsettlesave;
    EditText edt_2000, edt_1000, edt_500, edt_200, edt_100, edt_50, edt_20, edt_10, edt_5, edt_2, edt_1, edt_total, edt_misc, edt_remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in_hand);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pref.getString("username", ""));
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        btn_settle = (Button) findViewById(R.id.btn_settlement);
        btn_editsettle = (Button) findViewById(R.id.btn_edtsettle);
        btn_editsettlesave = (Button) findViewById(R.id.btn_edtsettsave);
        btn_view = (Button) findViewById(R.id.btn_ch_view);
        srl_settle = (ScrollView) findViewById(R.id.srl_settle);
        edt_cxash = (EditText) findViewById(R.id.edt_cashinhand);
        list_editsettle = (ListView) findViewById(R.id.list_editsettle);
        lay_edit = (LinearLayout) findViewById(R.id.showedit_lay);
        edt_1 = (EditText) findViewById(R.id.edt_ch_1);
        edt_2 = (EditText) findViewById(R.id.edt_ch_2);
        edt_5 = (EditText) findViewById(R.id.edt_ch_5);
        edt_10 = (EditText) findViewById(R.id.edt_ch_10);
        edt_20 = (EditText) findViewById(R.id.edt_ch_20);
        edt_50 = (EditText) findViewById(R.id.edt_ch_50);
        edt_100 = (EditText) findViewById(R.id.edt_ch_100);
        edt_200 = (EditText) findViewById(R.id.edt_ch_200);
        edt_500 = (EditText) findViewById(R.id.edt_ch_500);
        edt_1000 = (EditText) findViewById(R.id.edt_ch_1000);
        edt_2000 = (EditText) findViewById(R.id.edt_ch_2000);
        edt_total = (EditText) findViewById(R.id.edt_ch_total);
        edt_misc = (EditText) findViewById(R.id.edt_ch_misc);
        edt_remark = (EditText) findViewById(R.id.edt_ch_remark);
        btn_cash_settle = (Button) findViewById(R.id.btn_sett);
        txt_total = (TextView) findViewById(R.id.txt_showTot);
        if (cd.isConnectedToInternet()) {
            getcashinhand();
        } else {
            Toast.makeText(CashInHandActivity.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
        }

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectedToInternet()) {
                    Intent i = new Intent(CashInHandActivity.this, DateWiseView.class);
                    startActivity(i);
                } else {
                    Toast.makeText(CashInHandActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_editsettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectedToInternet()) {
                    edt_1.setText("");
                    edt_2.setText("");
                    edt_5.setText("");
                    edt_10.setText("");
                    edt_20.setText("");
                    edt_50.setText("");
                    edt_100.setText("");
                    edt_200.setText("");
                    edt_500.setText("");
                    edt_1000.setText("");
                    edt_2000.setText("");
                    edt_misc.setText("");
                    edt_total.setText("");
                    edt_remark.setText("");
                    txt_total.setVisibility(View.GONE);
                    srl_settle.setVisibility(View.GONE);
                    IsEditting = true;
                    geteditableamount();
                    // srl_settle.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(CashInHandActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        list_editsettle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SettleModel sm = amounts.get(i);
                str_2000 = sm.getS2000();
                str_1000 = sm.getS1000();
                str_500 = sm.getS500();
                str_200 = sm.getS200();
                str_100 = sm.getS100();
                str_50 = sm.getS50();
                str_20 = sm.getS20();
                str_10 = sm.getS10();
                str_5 = sm.getS5();
                str_2 = sm.getS2();
                str_1 = sm.getS1();
                str_others = sm.getSothers();
                str_total = sm.getStotal();
                tbl_id = sm.getStblid();
                lay_edit.setVisibility(View.GONE);
                edt_2000.setText(str_2000);
                edt_1000.setText(str_1000);
                edt_500.setText(str_500);
                edt_100.setText(str_100);
                edt_200.setText(str_200);
                edt_50.setText(str_50);
                edt_20.setText(str_20);
                edt_10.setText(str_10);
                edt_5.setText(str_5);
                edt_2.setText(str_2);
                edt_1.setText(str_1);
                edt_misc.setText(str_others);
                edt_total.setText(str_total);
                txt_total.setText("Total : " + str_total);
                txt_total.setVisibility(View.VISIBLE);
                if (IsEditting) {
                    btn_editsettlesave.setVisibility(View.VISIBLE);
                    btn_cash_settle.setVisibility(View.GONE);
                } else {
                    btn_editsettlesave.setVisibility(View.GONE);
                    btn_cash_settle.setVisibility(View.VISIBLE);
                }
                srl_settle.setVisibility(View.VISIBLE);

            }
        });
        btn_editsettlesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s2000 = edt_2000.getText().toString();
                s1000 = edt_1000.getText().toString();
                s500 = edt_500.getText().toString();
                s200 = edt_200.getText().toString();
                s100 = edt_100.getText().toString();
                s50 = edt_50.getText().toString();
                s20 = edt_20.getText().toString();
                s10 = edt_10.getText().toString();
                s5 = edt_5.getText().toString();
                s2 = edt_2.getText().toString();
                s1 = edt_1.getText().toString();
                sothers = edt_misc.getText().toString();
                stotal = edt_total.getText().toString();
                sremark = edt_remark.getText().toString();
                String Str_tott = edt_total.getText().toString();
                if (Integer.parseInt(Str_tott) <= Integer.parseInt(tot_amnnt)) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            CashInHandActivity.this);
                    alertDialog.setTitle("Confirmation");
                    alertDialog
                            .setMessage("Do you Confirm to Edit the Settlement for Rs. " + stotal);

                    alertDialog.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    updatesettlement(tbl_id, s1, s2, s5, s10, s20, s50, s100, s200, s500, s1000, s2000, sothers, sremark, stotal);

                                }
                            });
                    alertDialog.setNegativeButton("Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    // onBackPressed();
                                }
                            });
                    alertDialog.show();
                } else {
                    Toast.makeText(CashInHandActivity.this, "Amount Mismatch!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edt_1000.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_misc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_20.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_50.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_100.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_200.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_500.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_2000.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });

        btn_settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectedToInternet()) {
                    IsEditting = false;
                    edt_1.setText("");
                    edt_2.setText("");
                    edt_5.setText("");
                    edt_10.setText("");
                    edt_20.setText("");
                    edt_50.setText("");
                    edt_100.setText("");
                    edt_200.setText("");
                    edt_500.setText("");
                    edt_1000.setText("");
                    edt_2000.setText("");
                    edt_misc.setText("");
                    edt_total.setText("");
                    edt_remark.setText("");
                    btn_editsettlesave.setVisibility(View.GONE);
                    btn_cash_settle.setVisibility(View.VISIBLE);
                    txt_total.setVisibility(View.GONE);
                    lay_edit.setVisibility(View.GONE);

                    srl_settle.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(CashInHandActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_cash_settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tot = edt_total.getText().toString();
                if (edt_total.getText().toString().isEmpty()) {
                    Toast.makeText(CashInHandActivity.this, "Enter Settlement Amount", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(tot) > Integer.parseInt(tot_amnnt)) {
                    Toast.makeText(CashInHandActivity.this, "Amount Mismatch!", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            CashInHandActivity.this);
                    alertDialog.setTitle("Confirmation");
                    alertDialog
                            .setMessage("Do you Confirm to Settle Rs. " + tot);

                    alertDialog.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    settleamount();

                                }
                            });
                    alertDialog.setNegativeButton("Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    // onBackPressed();
                                }
                            });
                    alertDialog.show();

                }


            }
        });


    }

    private void updatesettlement(final String tbl_id, final String s1, final String s2, final String s5, final String s10, final String s20, final String s50, final String s100, final String s200, final String s500, final String s1000, final String s2000, final String sothers, final String sremark, final String stotal) {
        showDialog();
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.updatesettle, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());


                try {


                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        hidePDialog();
                        edt_1.setText("");
                        edt_2.setText("");
                        edt_5.setText("");
                        edt_10.setText("");
                        edt_20.setText("");
                        edt_50.setText("");
                        edt_100.setText("");
                        edt_200.setText("");
                        edt_500.setText("");
                        edt_1000.setText("");
                        edt_2000.setText("");
                        edt_misc.setText("");
                        edt_total.setText("");
                        edt_remark.setText("");
                        srl_settle.setVisibility(View.GONE);
                        IsEditting = false;
                        txt_total.setVisibility(View.GONE);
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(CashInHandActivity.this, "Amount Settled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CashInHandActivity.this, "No response from Server. Try after few minutes", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
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
                params.put("amt_1", s1);
                params.put("amt_2", s2);
                params.put("amt_5", s5);
                params.put("amt_10", s10);
                params.put("amt_20", s20);
                params.put("amt_50", s50);
                params.put("amt_100", s100);
                params.put("amt_200", s200);
                params.put("amt_500", s500);
                params.put("amt_1000", s1000);
                params.put("amt_2000", s2000);
                params.put("amt_othercharges", sothers);
                params.put("Emp_Id", pref.getString("userid", "0"));
                params.put("Id", tbl_id);
                params.put("total_amnt", stotal);
                params.put("Total_Amount", tot_amnnt);
                params.put("remark", sremark);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    public void textwatch() {
        int edt2000 = 0, edt1 = 0, edt2 = 0, edt5 = 0, edt10 = 0, edt20 = 0, edt50 = 0, edt100 = 0, edt200 = 0, edt500 = 0, edtmisc = 0, edt1000 = 0;
        try {
            String str1 = edt_1.getText().toString();
            if (str1.equalsIgnoreCase("")) {
                str1 = "0";
                edt1 = Integer.parseInt(str1);
            } else {
                edt1 = Integer.parseInt(str1);
            }
            String str2 = edt_2.getText().toString();
            if (str2.equalsIgnoreCase("")) {
                str2 = "0";
                edt2 = Integer.parseInt(str2);
            } else {
                edt2 = Integer.parseInt(str2);
            }
            String str5 = edt_5.getText().toString();
            if (str5.equalsIgnoreCase("")) {
                str5 = "0";
                edt5 = Integer.parseInt(str5);
            } else {
                edt5 = Integer.parseInt(str5);
            }
            String str10 = edt_10.getText().toString();
            if (str10.equalsIgnoreCase("")) {
                str10 = "0";
                edt10 = Integer.parseInt(str10);
            } else {
                edt10 = Integer.parseInt(str10);
            }
            String str20 = edt_20.getText().toString();
            if (str20.equalsIgnoreCase("")) {
                str20 = "0";
                edt20 = Integer.parseInt(str20);
            } else {
                edt20 = Integer.parseInt(str20);
            }
            String str50 = edt_50.getText().toString();
            if (str50.equalsIgnoreCase("")) {
                str50 = "0";
                edt50 = Integer.parseInt(str50);
            } else {
                edt50 = Integer.parseInt(str50);
            }
            String str100 = edt_100.getText().toString();
            if (str100.equalsIgnoreCase("")) {
                str100 = "0";
                edt100 = Integer.parseInt(str100);
            } else {
                edt100 = Integer.parseInt(str100);
            }
            String str200 = edt_200.getText().toString();
            if (str200.equalsIgnoreCase("")) {
                str200 = "0";
                edt200 = Integer.parseInt(str200);
            } else {
                edt200 = Integer.parseInt(str200);
            }
            String str500 = edt_500.getText().toString();
            if (str500.equalsIgnoreCase("")) {
                str500 = "0";
                edt500 = Integer.parseInt(str500);
            } else {
                edt500 = Integer.parseInt(str500);
            }
            String str1000 = edt_1000.getText().toString();
            if (str1000.equalsIgnoreCase("")) {
                str1000 = "0";
                edt1000 = Integer.parseInt(str1000);
            } else {
                edt1000 = Integer.parseInt(str1000);
            }
            String str2000 = edt_2000.getText().toString();
            System.out.println("str 2000  " + str2000);
            if (str2000.equalsIgnoreCase("")) {
                str2000 = "0";
                edt2000 = Integer.parseInt(str2000);
            } else {
                edt2000 = Integer.parseInt(str2000);
            }
            String strmisc = edt_misc.getText().toString();
            if (strmisc.equalsIgnoreCase("")) {
                strmisc = "0";
                edtmisc = Integer.parseInt(strmisc);
            } else {
                edtmisc = Integer.parseInt(strmisc);
            }

            tot = String.valueOf((edt1) + (edt2 * 2) + (edt5 * 5) + (edt10 * 10) + (edt20 * 20) + (edt50 * 50) + (edt100 * 100) + (edt200 * 200) + (edt500 * 500) + (edt2000 * 2000) + (edt1000 * 1000) + (edtmisc));
            System.out.println(tot + "  hhoesogjoie");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settleamount() {
        showDialog();
        int total1 = Integer.parseInt(tot_amnnt);
        int received = Integer.parseInt(edt_total.getText().toString());
        int grandtot = total1 + received;
        final String Total_Amount = String.valueOf(grandtot);
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                settleurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        edt_1.setText("");
                        edt_2.setText("");
                        edt_5.setText("");
                        edt_10.setText("");
                        edt_20.setText("");
                        edt_50.setText("");
                        edt_100.setText("");
                        edt_200.setText("");
                        edt_500.setText("");
                        edt_1000.setText("");
                        edt_2000.setText("");
                        edt_misc.setText("");
                        edt_total.setText("");
                        edt_remark.setText("");
                        srl_settle.setVisibility(View.GONE);
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(CashInHandActivity.this, "Amount Settled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CashInHandActivity.this, "No response from Server. Try after few minutes", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
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
                params.put("amt_1", edt_1.getText().toString());
                params.put("amt_2", edt_2.getText().toString());
                params.put("amt_5", edt_5.getText().toString());
                params.put("amt_10", edt_10.getText().toString());
                params.put("amt_20", edt_20.getText().toString());
                params.put("amt_50", edt_50.getText().toString());
                params.put("amt_100", edt_100.getText().toString());
                params.put("amt_200", edt_200.getText().toString());
                params.put("amt_500", edt_500.getText().toString());
                params.put("amt_1000", edt_1000.getText().toString());
                params.put("amt_2000", edt_2000.getText().toString());
                System.out.println("200000  " + edt_2000.getText().toString());
                params.put("amt_misc", edt_misc.getText().toString());
                params.put("Emp_Id", pref.getString("userid", "0"));
                params.put("branch_Id", pref.getString("empbranch", "0"));
                params.put("Total_Amount", tot_amnnt);
                params.put("Received_Amount", edt_total.getText().toString());
                params.put("Remark", edt_remark.getText().toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    public void getcashinhand() {
        showDialog();
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    tot_amnnt = object.getString("tot_amnt");
                    String totl = tot_amnnt;
                    if (!totl.isEmpty()) {

                        Double d = Double.parseDouble(totl);
                        String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                        totl = moneyString2;

                        edt_cxash.setText("Rs. " + totl);

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                CashInHandActivity.this);
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
                params.put("emp_id", pref.getString("userid", "0"));
                System.out.println(pref.getString("userid", "0"));
                System.out.println(pref.getString("empbranch", "0"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

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

    public void geteditableamount() {
        showDialog();
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.geteditsettle, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray jobject = object.getJSONArray("result");
                    try {
                        amounts.clear();
                        for (int i = 0; i < jobject.length(); i++) {
                            JSONObject jObj = jobject.getJSONObject(i);
                            SettleModel dvm = new SettleModel();
                            dvm.setS2000(jObj.getString("amt_2000"));
                            dvm.setS1000(jObj.getString("amt_1000"));
                            dvm.setS500(jObj.getString("amt_500"));
                            dvm.setS200(jObj.getString("amt_200"));
                            dvm.setS100(jObj.getString("amt_100"));
                            dvm.setS50(jObj.getString("amt_50"));
                            dvm.setS20(jObj.getString("amt_20"));
                            dvm.setS10(jObj.getString("amt_10"));
                            dvm.setS5(jObj.getString("amt_5"));
                            dvm.setS2(jObj.getString("amt_2"));
                            dvm.setS1(jObj.getString("amt_1"));
                            dvm.setSothers(jObj.getString("amt_otherchrgs"));
                            dvm.setStotal(jObj.getString("Received_Amount"));
                            dvm.setStblid(jObj.getString("Table_id"));
                            dvm.setsDate(jObj.getString("Date"));
                           /* */
                            amounts.add(dvm);
                        }
                        if (amounts.size() > 0) {
                            cav = new CustomAdapterEditSettle(CashInHandActivity.this, amounts);
                            cav.notifyDataSetChanged();
                            list_editsettle.setAdapter(cav);
                            lay_edit.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(CashInHandActivity.this, "No Editable Settlement details Available", Toast.LENGTH_SHORT).show();
                            lay_edit.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {

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
                params.put("emp_id", pref.getString("userid", "0"));
                System.out.println(pref.getString("userid", "0"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }
}
