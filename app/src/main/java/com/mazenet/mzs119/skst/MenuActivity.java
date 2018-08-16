package com.mazenet.mzs119.skst;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Database.Databasecustomers;
import com.mazenet.mzs119.skst.Database.Databaserecepit;
import com.mazenet.mzs119.skst.Model.Enrollmodel;
import com.mazenet.mzs119.skst.Model.TempEnrollModel;
import com.mazenet.mzs119.skst.Model.TempLoanModel;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    Button btn_addcustomer, btn_mycust, btn_collection, btn_logout, btn_outstanding, btn_viewcol, btn_cashinhand, btn_monthy, btn_weekly, btn_loan;
    ArrayList<TempEnrollModel> tempreceipt = new ArrayList<>();
    ArrayList<TempLoanModel> temploanreceipt = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Databaserecepit dbrecepit;
    Databasecustomers dbcustomers;
    ConnectionDetector cd;
    ArrayList<Enrollmodel> groupreceipt = new ArrayList<>();
    ArrayList<Enrollmodel> groupreceiptmain = new ArrayList<>();
    String url = Config.reteriveindienroll;
    String url2 = Config.saverecepit, android_id;
    String Enroll_update_cust = Config.Enroll_update_cust;
    private ProgressDialog pDialog;

    public void logout() {
        String username = pref.getString("username", "DEMO");
        editor.putString("olduser", username);
        editor.putString("username", "");
        editor.putString("userid", "");
        editor.putString("email", "");
        editor.putString("password", "");
        editor.commit();
        dbrecepit.deletetable();
        dbcustomers.deletetable();
        Intent irt = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(irt);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        // btn_addcustomer = (Button) findViewById(R.id.btn_addcustomer);
        //  btn_addenrollment = (Button) findViewById(R.id.btn_addenrollment);
        btn_collection = (Button) findViewById(R.id.btn_collection);
        btn_outstanding = (Button) findViewById(R.id.btn_dailyCol);
        btn_viewcol = (Button) findViewById(R.id.btn_viewCol);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_cashinhand = (Button) findViewById(R.id.btn_cashinhand);
        btn_monthy = (Button) findViewById(R.id.btn_monthlyCol);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        btn_addcustomer = (Button) findViewById(R.id.btn_addcustomer);
        btn_mycust = (Button) findViewById(R.id.btn_mycustomers);
        btn_loan = (Button) findViewById(R.id.btn_loans);
        btn_weekly = (Button) findViewById(R.id.btn_WeeklyCol);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        cd = new ConnectionDetector(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pref.getString("username", ""));

        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        dbrecepit = new Databaserecepit(MenuActivity.this);
        dbcustomers = new Databasecustomers(MenuActivity.this);
        if (cd.isConnectedToInternet()) {
            posttoserver();
            approvedevice(pref.getString("username", ""), android_id);
        } else {

        }
        btn_addcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, AddCustomer.class);
                startActivity(i);
            }
        });
        btn_weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, WeeklyCollectionActivity.class);
                startActivity(i);
            }
        });
        btn_monthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MonthlyCollectionActivity.class);
                startActivity(i);
            }
        });
        btn_viewcol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, ViewCollection.class);
                startActivity(it);
            }
        });
        btn_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, CollectionActivity.class);
                startActivity(it);
            }
        });
        btn_cashinhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, CashInHandActivity.class);
                startActivity(it);
            }
        });
        btn_outstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, DailyCollectionActivity.class);
                startActivity(it);
            }
        });
        btn_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, LoanPayments.class);
                startActivity(it);
            }
        });
        btn_mycust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MenuActivity.this, MyCustomers.class);
                startActivity(it);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    public void posttoserver() {
        System.out.println("server posting");
        tempreceipt = dbrecepit.getAllTempenroll();
        temploanreceipt = dbrecepit.getTempLoan();
        if (tempreceipt.size() > 0) {
            String cusid = "";

            for (int i = 0; i < tempreceipt.size(); i++) {
                System.out.println("receipt post entry");
                TempEnrollModel tem = tempreceipt.get(i);
                String enrollid = tem.getEnrollid();
                String bonusamnt = tem.getBonus_Amt();
                String paidamnt = tem.getPaid_Amt();
                String pendingamnt = tem.getPending_Amt();
                String penaltyamnt = tem.getPenalty_Amt();
                String Group = tem.getGroup_Name();
                String ticketno = tem.getGroup_Ticket_Name();
                String payableamnt = tem.getInsamt();
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
                postentry(enrollid, bonusamnt, paidamnt, pendingamnt, penaltyamnt, Group, ticketno, payableamnt, Remark, chitvalue, paytype, cus_branch, pendingdays, status, cusid, cusname, cheqno, cheqdate, cheqbank, cheqbranch, transno, rtgsdate);
            }
            //updateenroll(cusid);

        }
        if (temploanreceipt.size() > 0) {
            for (int i = 0; i < temploanreceipt.size(); i++) {
                TempLoanModel teme = temploanreceipt.get(i);
                String tblid = teme.getLoan_id();
                String brnid = teme.getBrnchid();
                String amount = teme.getAmount();
                String paymode = teme.getPaymode();
                String cheno = teme.getCheqno();
                String chebsnk = teme.getCheqbank();
                String chebranch = teme.getCheqbranch();
                String chedate = teme.getCheqdate();
                String transno = teme.getTransno();
                String transdate = teme.getTransdate();
                String debitto = teme.getDebitto();
                postentryloan(brnid, tblid, amount, paymode, debitto, cheno, chebsnk, chebranch, chedate, transno, transdate);


            }
        }
    }

    private void postentryloan(final String brnchid, final String tblid, final String amount, final String paytype, final String str_debit, final String str_cheno, final String str_chebank, final String str_chebranch, final String str_chedate, final String trn_no, final String trn_date) {

        showDialog();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.saveloanreceipt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response);
                System.out.println("coleeeeeeeeeee" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    System.out.println("stat " + status);

                    if (status.equals("1")) {
                        hidePDialog();
                        temploanreceipt.clear();
                        dbrecepit.deletetableLOANtemp();
                        Toast.makeText(MenuActivity.this, "Offline Entry Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MenuActivity.this, "Receipt Entry Not Saved Try Again", Toast.LENGTH_SHORT).show();
                    }
                    hidePDialog();


                } catch (Exception e) {
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
                params.put("branchname", brnchid);
                params.put("Emp_Id", pref.getString("userid", "0"));
                params.put("Employee_Name", pref.getString("username", "DEMO"));
                params.put("loanid", tblid);
                params.put("amount", amount);
                params.put("pay_mode", paytype);
                params.put("cheque_no", str_cheno);
                params.put("cheque_date", str_chedate);
                params.put("cheque_bank", str_chebank);
                params.put("cheque_brnch", str_chebranch);
                params.put("trn_no", trn_no);
                params.put("trn_date", trn_date);
                params.put("d_bank", str_debit);
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void postentry(final String enrollidd, final String bonusamt, final String paidamt, final String pendingamt, final String penaltyamt, final String Group, final String ticketno, final String payableamount, final String Remark, final String chitvalue, final String paytype, final String Cus_Branch, final String Pending_Days, final String status, final String cusid, final String cusname, final String str_cheno, final String str_chedate, final String str_chebank, final String str_chebranch, final String str_tranno, final String str_rtgsdate) {

        showDialog();
        StringRequest menureq = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    if (status.equals("1")) {

                        tempreceipt.clear();
                        dbrecepit.deletetabletemp();
                        System.out.println("posted");
                        hidePDialog();
                    } else {
                        System.out.println("noty posted");
                        hidePDialog();
                    }
                } catch (Exception e) {
                    hidePDialog();
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
                params.put("enrollid", enrollidd);
                params.put("bonusamt", bonusamt);
                params.put("paidamt", paidamt);
                params.put("pendingamt", pendingamt);
                params.put("penaltyamt", "0");
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
                System.out.println("===============tempppppppppp=========================");
                System.out.println(cusid + "internet on");
                System.out.println(enrollidd + "internet on");
                System.out.println(bonusamt + "internet on");
                System.out.println(paidamt + "internet on");
                System.out.println(pendingamt + "internet on");
                System.out.println(penaltyamt + "internet on");
                System.out.println(Group + "internet on");
                System.out.println(ticketno + "internet on");
                System.out.println(payableamount + "internet on");
                System.out.println(pref.getString("userid", "0" + "internet on"));
                System.out.println(pref.getString("username", "DEMO") + "internet on");
                System.out.println(Remark + "internet on");
                System.out.println(cusname + "internet on");
                System.out.println(chitvalue + "internet on");
                System.out.println(paytype + "internet on");
                System.out.println(str_cheno + "internet on");
                System.out.println(str_chedate + "internet on");
                System.out.println(str_chebank + "internet on");
                System.out.println(str_chebranch + "internet on");
                System.out.println(str_tranno + "internet on");
                System.out.println(str_rtgsdate + "internet on");
                System.out.println(Cus_Branch + "internet on");
                System.out.println(Pending_Days + "internet on");
                System.out.println(status + "internet on");
                System.out.println("===================temppppppppppppp=====================");
                return params;
            }

        };
        menureq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(menureq);
    }

    //=============================================================================================================================
    public void updateenroll(String cuid) {
        System.out.println("receipt post ");
        showDialog();
        final String cusid = cuid;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Enroll_update_cust, new Response.Listener<String>() {
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
                params.put("custid", cusid);


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

    public void reteriveall() {

        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {

                    groupreceipt.clear();


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            Enrollmodel sched = new Enrollmodel();
                            sched.setCusid(jObj.getString("Id"));
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
                            sched.setPayamount("0");
                            groupreceipt.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (groupreceipt.size() > 0) {


                        try {
                            dbrecepit.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            dbrecepit.addenroll(groupreceipt);

                            //    groupreceiptmain.clear();
                            //   groupreceiptmain.addAll(groupreceipt);
                            // adapterlist = new CustomAdaptercustomer(MenuActivity.this, groupreceiptmain);
                            //  adapterlist.notifyDataSetChanged();
                            // list.setAdapter(adapterlist);

                            // customer_list.clear();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                MenuActivity.this);
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
                params.put("userid", pref.getString("userid", "0"));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void approvedevice(final String email, final String regiid) {
        System.out.println("approving");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.get_registration, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("repso " + response);
                //hidePDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String Success = jObj.getString("status");
                    String Details = jObj.getString("details");
                    try {
                        if (Success.equals("1")) {
                            // posttoserver();
                            Toast.makeText(MenuActivity.this, Details, Toast.LENGTH_LONG).show();
                        } else {
                            logout();
                            Toast.makeText(MenuActivity.this, Details, Toast.LENGTH_LONG).show();
                        }

                        // retrivedetails();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MenuActivity.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", email);
                params.put("regid", regiid);
                System.out.println("emil" + email);
                System.out.println("reg " + regiid);
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }
}