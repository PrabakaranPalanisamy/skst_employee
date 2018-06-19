package com.mazenet.mzs119.skst;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterLoanPayments;
import com.mazenet.mzs119.skst.Adapter.CustomAdaptercustomer;
import com.mazenet.mzs119.skst.Database.Databaserecepit;
import com.mazenet.mzs119.skst.Model.Custmodel;
import com.mazenet.mzs119.skst.Model.LoanModel;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoanPayments extends AppCompatActivity {
    ListView list_cust;
    ArrayList<LoanModel> loan_list = new ArrayList<>();
    ArrayList<LoanModel> loan_list_main = new ArrayList<>();
    CustomAdapterLoanPayments adapterlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ProgressDialog pDialog;
    ConnectionDetector cd;
    Databaserecepit dbc;
    SwipeRefreshLayout refreshLayout;
    EditText edt_search;
    Boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_payments);
        list_cust = (ListView) findViewById(R.id.list_lp_viewloans);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pref.getString("username", ""));
        edt_search = (EditText) findViewById(R.id.edt_lp_search);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlay_lp);
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        dbc = new Databaserecepit(this);
        loan_list.clear();
        loan_list_main.clear();
        if (cd.isConnectedToInternet()) {
            Load_loans();
        } else {
            Load_Local_Loans();

        }
        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {

                String text = edt_search.getText().toString();
                if (text.equals("") || text.equals(null)) {

                    loan_list_main.clear();

                    loan_list_main.addAll(loan_list);


                    if (loan_list_main.size() > 0) {
                        list_cust.setVisibility(View.VISIBLE);
                        adapterlist = new CustomAdapterLoanPayments(LoanPayments.this, loan_list_main);
                        adapterlist.notifyDataSetChanged();
                        list_cust.setAdapter(adapterlist);

                    } else {

                        list_cust.setVisibility(View.GONE);
                    }
                } else {
                    setnewadapter();
                    list_cust.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                // lv.setVisibility(View.GONE);

                if (loan_list_main.size() > 0) {
                    list_cust.setVisibility(View.VISIBLE);

                } else {

                    list_cust.setVisibility(View.GONE);

                }

            }
        });
        refreshLayout.setEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectedToInternet()) {
                    Load_loans();
                } else {
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(LoanPayments.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        list_cust.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LoanModel lm = loan_list_main.get(i);
                String tblid = lm.getTbl_id();
                String brnid = lm.getBranchname();
                String pending = lm.getPending_amt();
                Intent iu = new Intent(LoanPayments.this, LoanPaymentReceipt.class);
                iu.putExtra("tblid", tblid);
                iu.putExtra("brnid", brnid);
                iu.putExtra("pending", pending);
                iu.putExtra("cusname", lm.getFirst_Name());
                iu.putExtra("paidamnt", lm.getPaid_amt());
                iu.putExtra("reference", lm.getReference_Grp());
                startActivity(iu);
                finish();
            }
        });
    }

    private void setnewadapter() {
        loan_list_main.clear();

        String text = edt_search.getText().toString();
        text = text.toLowerCase(Locale.getDefault());
        if (text.equals(null)) {

            list_cust.setVisibility(View.GONE);
            return;

        } else {


            for (int i = 0; i < loan_list.size(); i++) {
                LoanModel schedte = loan_list.get(i);
                String name = schedte.getFirst_Name();
                String cusid = schedte.getMobile_F();

                name = name.toLowerCase(Locale.getDefault());
                cusid = cusid.toLowerCase(Locale.getDefault());

                if (name.contains(text) || cusid.contains(text)) {
                    loan_list_main.add(loan_list.get(i));

                }

            }


        }

        if (loan_list_main.size() > 0) {
            list_cust.setVisibility(View.VISIBLE);
            adapterlist = new CustomAdapterLoanPayments(LoanPayments.this, loan_list_main);
            adapterlist.notifyDataSetChanged();
            list_cust.setAdapter(adapterlist);

        } else {

            list_cust.setVisibility(View.GONE);
        }

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

    public void Load_Local_Loans() {
        loan_list.clear();
        loan_list_main.clear();
        loan_list = dbc.getallLoan();
        loan_list_main.addAll(loan_list);
        loan_list.clear();
        adapterlist = new CustomAdapterLoanPayments(LoanPayments.this, loan_list_main);
        adapterlist.notifyDataSetChanged();
        list_cust.setAdapter(adapterlist);
    }

    public void Load_loans() {



        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.viewloans, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                System.out.println(response.toString());


                try {

                    loan_list.clear();
                    loan_list_main.clear();

                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("loans");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            LoanModel sched = new LoanModel();
                            sched.setBranchname(jObj.getString("Branch_Id"));
                            sched.setFirst_Name(jObj.getString("First_Name_F"));
                            sched.setLoan_amount(jObj.getString("Loan_amount"));
                            sched.setLoan_Date(jObj.getString("Loan_Date"));
                            sched.setLoan_Type(jObj.getString("Loan_Type"));
                            sched.setMobile_F(jObj.getString("Mobile_F"));
                            sched.setPaid_amt(jObj.getString("Paid_amt"));
                            sched.setPending_amt(jObj.getString("Pending_amt"));
                            sched.setReference_Grp(jObj.getString("Reference_Grp"));
                            sched.setTbl_id(jObj.getString("Id"));
                            loan_list.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        refreshLayout.setRefreshing(false);
                    }


                    if (loan_list.size() > 0) {
                        try {
                            dbc.deletetableloanreceipt();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            dbc.addallLoan(loan_list);
                            loan_list_main.clear();
                            loan_list_main.addAll(loan_list);
                            adapterlist = new CustomAdapterLoanPayments(LoanPayments.this, loan_list_main);
                            adapterlist.notifyDataSetChanged();
                            list_cust.setAdapter(adapterlist);
                            refreshLayout.setRefreshing(false);
                            // customer_list.clear();

                        } catch (Exception e) {
                            refreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }


                    } else {
                        refreshLayout.setRefreshing(false);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                LoanPayments.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Data from Server. contact apdi Admin");

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

                    hidePDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                    refreshLayout.setRefreshing(false);
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
                refreshLayout.setRefreshing(false);

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", pref.getString("userid", "0"));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addloan, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_addloan:
                if (check) {
                    edt_search.setVisibility(View.VISIBLE);
                    check = false;
                } else {
                    edt_search.setVisibility(View.GONE);
                    check = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
