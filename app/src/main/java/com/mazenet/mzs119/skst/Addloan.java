package com.mazenet.mzs119.skst;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Adapter.Branchadapter;
import com.mazenet.mzs119.skst.Adapter.CustomAdaptercustomer;
import com.mazenet.mzs119.skst.Adapter.CustomAdaptercustomername;
import com.mazenet.mzs119.skst.Database.Databasecustomers;
import com.mazenet.mzs119.skst.Model.BranchModel;
import com.mazenet.mzs119.skst.Model.Custmodel;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;
import com.mazenet.mzs119.skst.Utils.ListViewHeight;
import com.mazenet.mzs119.skst.Utils.NDSpinner;
import com.mazenet.mzs119.skst.Utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Addloan extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    NDSpinner spn_loantype, spn_paytype;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat df;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list1 = new ArrayList<String>();
    LinearLayout lay_re_che, lay_re_dd, lay_re_rtgs;
    String paytype = "Cash", Branchid = "";
    ListView listbranch, listcustomer;
    public ArrayList<BranchModel> branchArray = new ArrayList<BranchModel>();
    public ArrayList<BranchModel> branchArrayMain = new ArrayList<BranchModel>();
    public ArrayList<Custmodel> customer_list = new ArrayList<Custmodel>();
    public ArrayList<Custmodel> customer_listmain = new ArrayList<Custmodel>();
    EditText edt_re_cheno, edt_re_chebank, edt_re_chebranch, edt_re_ddno, edt_re_ddbank, edt_re_ddbranch, edt_branch, edt_customer, edt_referername,edt_dateofpay;
    EditText edt_re_amount, edt_re_remark, edt_re_rtgsno;
    Button btn_submit, btn_che_date, btn_dd_date, btn_rtgs_date;
    ProgressDialog pDialog;
    Branchadapter branchAdapter;
    Databasecustomers dbcust;
    CustomAdaptercustomername adapterlist;
    private String customer_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addloan);

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        df = new SimpleDateFormat("dd-MM-yyyy");
        lay_re_che = (LinearLayout) findViewById(R.id.lay_al_cheque);
        lay_re_dd = (LinearLayout) findViewById(R.id.lay_al_dd);
        lay_re_rtgs = (LinearLayout) findViewById(R.id.lay_al_rtgs);

        spn_loantype = (NDSpinner) findViewById(R.id.spn_al_loantype);
        spn_paytype = (NDSpinner) findViewById(R.id.spn_al_paytype);

        edt_re_amount = (EditText) findViewById(R.id.edt_al_Amount);
        edt_re_remark = (EditText) findViewById(R.id.edt_al_remark);
        edt_re_cheno = (EditText) findViewById(R.id.edt_al_chequeno);
        edt_re_chebank = (EditText) findViewById(R.id.edt_al_chebank);
        edt_re_chebranch = (EditText) findViewById(R.id.edt_al_chebranch);
        edt_re_ddno = (EditText) findViewById(R.id.edt_al_ddno);
        edt_re_ddbank = (EditText) findViewById(R.id.edt_al_ddbank);
        edt_re_ddbranch = (EditText) findViewById(R.id.edt_al_ddbranch);
        edt_re_rtgsno = (EditText) findViewById(R.id.edt_al_rtgsno);

        edt_branch = (EditText) findViewById(R.id.edt_al_branchname);
        edt_customer = (EditText) findViewById(R.id.edt_al_custname);
        edt_referername = (EditText) findViewById(R.id.edt_al_ReferenceName);
        edt_dateofpay=(EditText)findViewById(R.id.edt_al_issuedate);

        btn_submit = (Button) findViewById(R.id.btn_al_submit);
        btn_che_date = (Button) findViewById(R.id.btn_al_chedate);
        btn_dd_date = (Button) findViewById(R.id.btn_al_dd_date);
        btn_rtgs_date = (Button) findViewById(R.id.btn_al_rtgs_date);
        dbcust = new Databasecustomers(this);
        listbranch = (ListView) findViewById(R.id.list_al_branchname);
        listcustomer = (ListView) findViewById(R.id.list_al_custname);

        list.clear();
        list1.clear();
        list.add("Cash");
        list.add("Cheque");
        list.add("D.D");
        list.add("RTGS/NEFT");
        list.add("Card");
        list1.add("Balance Collection");
        list1.add("Loan Collection");
        LoadBranch();

        try {

            Calendar newCalendar = Calendar.getInstance();

            int ddd = newCalendar.get(Calendar.DAY_OF_MONTH);
            int wwww = newCalendar.get(Calendar.WEEK_OF_YEAR);

            if (ddd == pref.getInt("dailycheckdaymain", 0) && wwww == pref.getInt("dailycheckmonthmain", 0) && dbcust.getContactsCount() != 0) {
                reterivelocal();

            } else {

                editor.putInt("dailycheckdaymain",
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                editor.putInt("dailycheckmonthmain",
                        newCalendar.get(Calendar.WEEK_OF_YEAR));
                editor.putString("companymain",
                        pref.getString("company", null));

                editor.commit();
                LoadCustomer();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                Addloan.this,
                android.R.layout.select_dialog_item, list);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_paytype.setAdapter(dataAdapter);
        ArrayAdapter<String> loanAdapter = new ArrayAdapter<String>(
                Addloan.this,
                android.R.layout.select_dialog_item, list1);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_loantype.setAdapter(loanAdapter);

        spn_paytype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        edt_dateofpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(Addloan.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();

                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);
                        try {
                            String doj1="";
                            doj1 = df.format(newDate.getTime());
                            edt_dateofpay.setText(doj1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("Date of payment");
                fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 * 24);
                fromDatePickerDialog.show();
            }

        });
        edt_branch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_branch.getText().toString();
                if (text.equals("") || text.equals(null)) {
                    listbranch.setVisibility(View.GONE);
                    branchArrayMain.clear();

                } else {
                    setnewbranchadapter();
                    ListViewHeight.setListViewHeightBasedOnChildren(listbranch);
                    listbranch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (branchArrayMain.size() > 0) {
                    listbranch.setVisibility(View.VISIBLE);

                } else {

                    listbranch.setVisibility(View.GONE);
                }
            }
        });
        listbranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BranchModel mod = branchArrayMain.get(i);
                edt_branch.setText(mod.getName());
                Branchid = mod.getId();
                branchAdapter.notifyDataSetChanged();
                listbranch.setVisibility(View.GONE);
            }
        });

        edt_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_customer.getText().toString();
                if (text.equals("") || text.equals(null)) {
                    listcustomer.setVisibility(View.GONE);
                    customer_listmain.clear();
                } else {
                    setnewrefercustomeradapter();
                    ListViewHeight.setListViewHeightBasedOnChildren(listcustomer);
                    listcustomer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (customer_listmain.size() > 0) {
                    listcustomer.setVisibility(View.VISIBLE);

                } else {

                    listcustomer.setVisibility(View.GONE);
                }

            }
        });
        listcustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Custmodel cmd = customer_listmain.get(i);
                String name = cmd.getNAME();
                edt_customer.setText(name);
                customer_id = cmd.getCusid();
                adapterlist.notifyDataSetChanged();
                listcustomer.setVisibility(View.GONE);
            }
        });

    }

    private void setnewrefercustomeradapter() {

        customer_listmain.clear();

        String text = edt_customer.getText().toString();
        text = text.toLowerCase(Locale.getDefault());
        if (text.equals(null) || text.equals("")) {

            listcustomer.setVisibility(View.GONE);
            return;

        } else {


            for (int i = 0; i < customer_list.size(); i++) {

                Custmodel custm = customer_list.get(i);
                String name = custm.getNAME();
                Log.i("name", name);
                name = name.toLowerCase(Locale.getDefault());


                if (name.contains(text)) {
                    customer_listmain.add(customer_list.get(i));
                    adapterlist.notifyDataSetChanged();
                }

            }


        }

        if (customer_listmain.size() > 0) {
            listcustomer.setVisibility(View.VISIBLE);
            adapterlist = new CustomAdaptercustomername(Addloan.this, customer_listmain);
            adapterlist.notifyDataSetChanged();
            listcustomer.setAdapter(adapterlist);

        } else {

            listcustomer.setVisibility(View.GONE);
        }


    }

    private void setnewbranchadapter() {
        branchArrayMain.clear();

        String text = edt_branch.getText().toString();
        text = text.toLowerCase(Locale.getDefault());
        if (text.equals(null)) {

            listbranch.setVisibility(View.GONE);
            return;

        } else {


            for (int i = 0; i < branchArray.size(); i++) {

                BranchModel mod = branchArray.get(i);
                String name = mod.getName();
                name = name.toLowerCase(Locale.getDefault());


                if (name.contains(text)) {
                    branchArrayMain.add(branchArray.get(i));
                    branchAdapter.notifyDataSetChanged();
                }

            }


        }

        if (branchArrayMain.size() > 0) {
            listbranch.setVisibility(View.VISIBLE);
            branchAdapter = new Branchadapter(Addloan.this, branchArrayMain);
            branchAdapter.notifyDataSetChanged();
            listbranch.setAdapter(branchAdapter);

        } else {

            listbranch.setVisibility(View.GONE);
        }
    }

    private void LoadBranch() {

        final StringRequest branchreq = new StringRequest(Request.Method.POST, Config.retrievebranch, new Response.Listener<String>() {

            public void onResponse(String response) {
                Log.d("branch Activity", response.toString());

                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONArray jsonarray = jobj.getJSONArray(Config.jsonarraybranch);
                    getBranch(jsonarray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                branchAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Unable to parse json array");
            }
        });
        AppController.getInstance().addToRequestQueue(branchreq);
    }

    private void getBranch(JSONArray jsonarraybranch) {
        if (jsonarraybranch != null) {
            for (int i = 0; i < jsonarraybranch.length(); i++) {
                try {

                    JSONObject json = jsonarraybranch.getJSONObject(i);

                    BranchModel mod = new BranchModel();
                    mod.setId(json.getString("Id"));
                    mod.setName(json.getString("Name"));
                    branchArray.add(mod);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        branchArrayMain.clear();
        branchArrayMain.addAll(branchArray);

        branchAdapter = new Branchadapter(Addloan.this, branchArrayMain);
        branchAdapter.notifyDataSetChanged();

        listbranch.setAdapter(branchAdapter);
        listbranch.setVisibility(View.GONE);

    }

    public void LoadCustomer() {

        showDialog();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.reteriveusers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {

                    customer_list.clear();


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            Custmodel sched = new Custmodel();
                            sched.setCusid(jObj.getString("Id"));
                            sched.setCustomer_id(jObj.getString("Customer_Id"));
                            sched.setNAME(jObj.getString("First_Name_F"));
                            sched.setMOBILE(jObj.getString("Mobile_F"));
                            sched.setAdvanceamt(jObj.getString("Advanced_Amt"));
                            sched.setPendingamt(jObj.getString("Pending_Amt"));
                            sched.setTotalenrlpending(jObj.getString("Total_Enrl_Pending"));
                            sched.setEnrlpaid(jObj.getString("Total_Enrl_Paid"));
                            sched.setLevel(jObj.getString("Level"));
                            sched.setBonusamt(jObj.getString("Bonus_Amt"));
                            sched.setPenaltyamt(jObj.getString("Penalty_Amt"));
                            sched.setPendingamt(jObj.getString("Pending_Days"));
                            sched.setPaymenttype(jObj.getString("Payment_Type"));
                            customer_list.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (customer_list.size() > 0) {


                        try {
                            dbcust.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            dbcust.addcustomer(customer_list);

                            customer_listmain.clear();
                            customer_listmain.addAll(customer_list);
                            adapterlist = new CustomAdaptercustomername(Addloan.this, customer_listmain);
                            adapterlist.notifyDataSetChanged();
                            listcustomer.setAdapter(adapterlist);

                            // customer_list.clear();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                Addloan.this);
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

    public void reterivelocal() {

        customer_list.clear();
        customer_listmain.clear();
        customer_list = dbcust.getAllContacts();
        customer_listmain.addAll(customer_list);

        adapterlist = new CustomAdaptercustomername(Addloan.this, customer_listmain);
        adapterlist.notifyDataSetChanged();
        listcustomer.setAdapter(adapterlist);
        listcustomer.setVisibility(View.GONE);
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
}
