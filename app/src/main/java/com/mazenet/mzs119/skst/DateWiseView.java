package com.mazenet.mzs119.skst;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.widget.RelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterDatewise;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterLoanDatewise;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterLoanPayments;
import com.mazenet.mzs119.skst.Model.DateWiseViewModel;
import com.mazenet.mzs119.skst.Model.LoanModel;
import com.mazenet.mzs119.skst.Model.LoanModelDatewise;
import com.mazenet.mzs119.skst.Model.TempEnrollModel;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateWiseView extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    DateFormat df;
    TextView txt_date, txt_tot;
    LinearLayout laydate, total_lay;
    RelativeLayout homelay;
    ListView viewlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    private ProgressDialog pDialog;
    Boolean isLoans = false;
    private static final String TAG = MainActivity.class.getSimpleName();
    String url = Config.getallviewbydate;
    int tot = 0, tot1 = 0;
    ArrayList<LoanModelDatewise> loan_list = new ArrayList<>();
    ArrayList<LoanModelDatewise> loan_list_main = new ArrayList<>();
    CustomAdapterLoanPayments adapterlist;
    ArrayList<DateWiseViewModel> listmain = new ArrayList<>();
    CustomAdapterDatewise adapterDatewise;
    CustomAdapterLoanDatewise loanadaptrerdatewise;
    Locale curLocale = new Locale("en", "IN");
    Button btn_collectreceipt, btn_loanreceipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_view);
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


        df = new SimpleDateFormat("dd-MM-yyyy");
        txt_date = (TextView) findViewById(R.id.datetext);
        laydate = (LinearLayout) findViewById(R.id.datelay);
        homelay = (RelativeLayout) findViewById(R.id.lay_view_home);
        viewlist = (ListView) findViewById(R.id.list_view_datewise);
        txt_tot = (TextView) findViewById(R.id.txt_datew_total);
        total_lay = (LinearLayout) findViewById(R.id.hl_tot);

        btn_collectreceipt = (Button) findViewById(R.id.collection_receipts);
        btn_loanreceipt = (Button) findViewById(R.id.loan_receipts);
        showcal();
        btn_collectreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewlist.setAdapter(adapterDatewise);
                viewlist.setVisibility(View.VISIBLE);
                total_lay.setVisibility(View.VISIBLE);
                isLoans = false;
                btn_collectreceipt.setTextColor(getResources().getColor(R.color.white));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.black));
            }
        });
        btn_loanreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewlist.setAdapter(loanadaptrerdatewise);
                txt_tot.setVisibility(View.VISIBLE);
                viewlist.setVisibility(View.VISIBLE);
                total_lay.setVisibility(View.VISIBLE);
                btn_collectreceipt.setTextColor(getResources().getColor(R.color.black));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.white));
                isLoans = true;
            }
        });
        viewlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isLoans) {
                    System.out.println("loan print");
                    LoanModelDatewise dvm = loan_list_main.get(i);
                    Intent it = new Intent(DateWiseView.this, PrintActivityLoansDatewise.class);
                    it.putExtra("Cusname", dvm.getCust_name());
                    it.putExtra("Amount", dvm.getAmount());
                    it.putExtra("Reference", dvm.getReference_Grp());
                    it.putExtra("date", dvm.getDate());
                    startActivity(it);
                } else {
                    System.out.println("colee print");
                    DateWiseViewModel dvm = listmain.get(i);
                    Intent it = new Intent(DateWiseView.this, PrintActivityDatewise.class);
                    it.putExtra("Cusname", dvm.getFirst_Name_F());
                    it.putExtra("Amount", dvm.getTotal_Amount());
                    it.putExtra("Groupname", dvm.getGroup_Id());
                    it.putExtra("ticketno", dvm.getGroup_Ticket_Id());
                    it.putExtra("paymode", dvm.getPayment_Type());
                    it.putExtra("pendingamnt", dvm.getPending_Amt());
                    it.putExtra("advance", dvm.getAdvance_Amt());
                    it.putExtra("Receiptno", dvm.getReceipt_No());
                    startActivity(it);
                }
            }
        });


    }

    public void getall() {
        showDialog();
        tot1 = 0;
        txt_tot.setVisibility(View.GONE);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                hidePDialog();

                listmain.clear();
                loan_list.clear();
                loan_list_main.clear();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String jsonob = jobj.getString("result");
                    System.out.println("hjbfsgjgh " + jsonob);
                    if (jsonob.equals("0")) {
                        Load_loans();
                        viewlist.setVisibility(View.GONE);
                        Toast.makeText(DateWiseView.this, "No receipts Available", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray object = jobj.getJSONArray("result");
                        try {

                            for (int i = 0; i < object.length(); i++) {
                                JSONObject jObj = object.getJSONObject(i);

                                DateWiseViewModel dvm = new DateWiseViewModel();
                                dvm.setCust_Id(jObj.getString("Cust_Id"));
                                dvm.setCus_Branch(jObj.getString("Cus_Branch"));
                                dvm.setEnrl_Id(jObj.getString("Enrl_Id"));
                                dvm.setTotal_Amount(jObj.getString("Total_Amount"));
                                dvm.setReceipt_No(jObj.getString("Receipt_No"));
                                dvm.setCreated_By(jObj.getString("Created_By"));
                                dvm.setCheque_No(jObj.getString("Cheque_No"));
                                dvm.setCheque_Date(jObj.getString("Cheque_Date"));
                                dvm.setBank_Name(jObj.getString("Bank_Name"));
                                dvm.setBranch_Name(jObj.getString("Branch_Name"));
                                dvm.setTrn_No(jObj.getString("Trn_No"));
                                dvm.setTrn_Date(jObj.getString("Trn_Date"));
                                dvm.setPayment_Type(jObj.getString("Payment_Type"));
                                dvm.setGroup_Id(jObj.getString("Group_Id"));
                                dvm.setGroup_Ticket_Id(jObj.getString("Group_Ticket_Id"));
                                dvm.setFirst_Name_F(jObj.getString("First_Name_F"));
                                dvm.setPending_Amt(jObj.getString("Pending_Amt"));
                                dvm.setTotal_Enrl_Paid(jObj.getString("Total_Enrl_Paid"));
                                dvm.setTotal_Enrl_Pending(jObj.getString("Total_Enrl_Pending"));
                                dvm.setAdvance_Amt(jObj.getString("Advance_Amt"));
                                listmain.add(dvm);
                            }
                            if (listmain.size() > 0) {
                                adapterDatewise = new CustomAdapterDatewise(DateWiseView.this, listmain);
                                adapterDatewise.notifyDataSetChanged();
                                viewlist.setAdapter(adapterDatewise);
                                viewlist.setVisibility(View.VISIBLE);
                                btn_collectreceipt.setEnabled(true);
                                Load_loans();
                                for (int i = 0; i < listmain.size(); i++) {

                                    DateWiseViewModel tem = listmain.get(i);
                                    String tote = tem.getTotal_Amount();

                                    try {
                                        int tot = Integer.parseInt(tote);
                                        tot1 += tot;

                                    } catch (Exception e) {

                                    }
                                }
                                System.out.println("toto " + tot1);
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
                                txt_tot.setText("Rs. " + String.valueOf(advance));

                            } else {
                                Load_loans();
                                total_lay.setVisibility(View.GONE);
                                viewlist.setVisibility(View.GONE);
                                Toast.makeText(DateWiseView.this, "No receipts Available", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception e) {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Emp_Id", pref.getString("userid", ""));
                System.out.println(pref.getString("userid", ""));
                params.put("Date", txt_date.getText().toString());
                System.out.println(txt_date.getText().toString());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    public void showcal() {
        listmain.clear();
        laydate.setVisibility(View.GONE);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(DateWiseView.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                calendar.set(year, monthOfYear, dayOfMonth);
                try {
                    String str_frodate = df.format(newDate.getTime());
                    txt_date.setText(str_frodate);
                    laydate.setVisibility(View.VISIBLE);
                    homelay.setVisibility(View.VISIBLE);
                    getall();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Date");
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 * 24);
        datePickerDialog.show();
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
                showcal();
                return true;
          /*  case R.id.menu_viewloans:
                if (!isLoans) {

                    viewlist.setAdapter(loanadaptrerdatewise);
                    txt_tot.setVisibility(View.VISIBLE);
                    viewlist.setVisibility(View.VISIBLE);
                    total_lay.setVisibility(View.VISIBLE);
                    isLoans = true;
                } else {

                    viewlist.setAdapter(adapterDatewise);
                    viewlist.setVisibility(View.VISIBLE);
                    total_lay.setVisibility(View.VISIBLE);
                    isLoans = false;
                }
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
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

    public void Load_loans() {

        showDialog();
        System.out.println("loans go");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.loanreceiptdatevise, new Response.Listener<String>() {
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


                            LoanModelDatewise sched = new LoanModelDatewise();
                            sched.setCust_name(jObj.getString("cust_name"));
                            sched.setAmount(jObj.getString("amount"));
                            sched.setDate(jObj.getString("Date"));
                            sched.setReference_Grp(jObj.getString("Reference_Grp"));
                            loan_list.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                    if (loan_list.size() > 0) {
                        try {
                            System.out.println("loans go 1");
                            loan_list_main.clear();
                            loan_list_main.addAll(loan_list);
                            btn_collectreceipt.setEnabled(true);
                            loanadaptrerdatewise = new CustomAdapterLoanDatewise(DateWiseView.this, loan_list_main);
                            loanadaptrerdatewise.notifyDataSetChanged();
                            for (int i = 0; i < loan_list_main.size(); i++) {
                                LoanModelDatewise lm = loan_list_main.get(i);
                                String loantot = lm.getAmount();
                                System.out.println("loan to " + loantot);
                                int to = Integer.parseInt(loantot);
                                tot1 += to;
                            }
                            String advance = String.valueOf(tot1);
                            try {
                                if (advance.contains("-")) {
                                    advance = "0";
                                }
                                Double d = Double.parseDouble(advance);
                                String moneyString3 = NumberFormat.getNumberInstance(curLocale).format(d);
                                advance = moneyString3;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            txt_tot.setText("Rs. " + advance);
                            System.out.println("loans go 2 " + advance);
                            txt_tot.setVisibility(View.VISIBLE);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    } else {
                        txt_tot.setVisibility(View.VISIBLE);
                       // Toast.makeText(DateWiseView.this, "No Loan receipts Available", Toast.LENGTH_SHORT).show();
                     /*   android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(
                                DateWiseView.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Loan receipts Available");

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
                        alertDialog.show(); */
                    }

                    hidePDialog();

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
                params.put("empid", pref.getString("userid", "0"));
                params.put("date", txt_date.getText().toString());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
}
