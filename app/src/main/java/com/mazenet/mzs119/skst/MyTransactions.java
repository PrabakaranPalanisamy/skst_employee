package com.mazenet.mzs119.skst;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterDatewise;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterLoanDatewise;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterLoanPayments;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterMyTrans;
import com.mazenet.mzs119.skst.Model.DateWiseViewModel;
import com.mazenet.mzs119.skst.Model.LoanModelDatewise;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyTransactions extends AppCompatActivity {
    DateFormat df;
    TextView txt_tot;
    ListView viewlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    private static final String TAG = MainActivity.class.getSimpleName();
    String url = Config.reterivereceipts, cusid = "", enrollid = "";
    int tot = 0, tot1 = 0;
    CustomAdapterLoanPayments adapterlist;
    ArrayList<DateWiseViewModel> listmain = new ArrayList<>();
    CustomAdapterMyTrans adapterDatewise;
    Locale curLocale = new Locale("en", "IN");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transactions);
        viewlist = (ListView) findViewById(R.id.list_prevtransacs);
        txt_tot = (TextView) findViewById(R.id.txt_mytrans_total);
        try {
            Intent it = getIntent();
            cusid = it.getStringExtra("cusid");
            enrollid = it.getStringExtra("enrollid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        getall();
        viewlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateWiseViewModel dvm = listmain.get(position);
                Intent it = new Intent(MyTransactions.this, PrintActivityDatewise.class);
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
        });
    }

    public void getall() {
        tot1 = 0;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                listmain.clear();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String jsonob = jobj.getString("result");
                    System.out.println("hjbfsgjgh " + jsonob);
                    if (jsonob.equals("0")) {
                        Toast.makeText(MyTransactions.this, "No receipts Available", Toast.LENGTH_SHORT).show();
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
                                dvm.setDate(jObj.getString("Date"));
                                listmain.add(dvm);
                            }
                            if (listmain.size() > 0) {
                                adapterDatewise = new CustomAdapterMyTrans(MyTransactions.this, listmain);
                                adapterDatewise.notifyDataSetChanged();
                                viewlist.setAdapter(adapterDatewise);
                                viewlist.setVisibility(View.VISIBLE);
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
                                    System.out.println("ad " + advance);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                txt_tot.setText("Rs. " + advance);

                            } else {

                                viewlist.setVisibility(View.GONE);
                                Toast.makeText(MyTransactions.this, "No receipts Available", Toast.LENGTH_SHORT).show();

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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("custid", cusid);
                params.put("enrlid", enrollid);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }
}
