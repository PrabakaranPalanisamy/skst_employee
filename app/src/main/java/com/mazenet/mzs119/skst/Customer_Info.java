package com.mazenet.mzs119.skst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Customer_Info extends AppCompatActivity {
    TextView name, address, mobile, intoname, collect_area, grpname, grpticket;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String str_name = "", str_address = "", str_mobile = "", str_custid = "", str_collect_area = "", str_introname = "", str_grpname = "", str_grpticket = "";
    ConnectionDetector cd;
    LinearLayout lay_intro, lay_area;
    CardView lay_grpname, laygrpticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__info);
        cd = new ConnectionDetector(this);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        name = (TextView) findViewById(R.id.txt_prf_name);
        address = (TextView) findViewById(R.id.txt_prf_addr);
        mobile = (TextView) findViewById(R.id.txt_prf_mobile);
        intoname = (TextView) findViewById(R.id.txt_Intro_name);
        collect_area = (TextView) findViewById(R.id.txt_collectarea);
        grpname = (TextView) findViewById(R.id.txt_prf_non_prizedchit);
        grpticket = (TextView) findViewById(R.id.txt_prf_sub_paidamt);
        lay_area = (LinearLayout) findViewById(R.id.lay_coolectarea);
        lay_intro = (LinearLayout) findViewById(R.id.lay_introname);
        lay_grpname = (CardView) findViewById(R.id.card_non_pricedchit);
        laygrpticket = (CardView) findViewById(R.id.card_sub_paidamt);
        lay_intro.setVisibility(View.GONE);
        lay_area.setVisibility(View.GONE);

        try {
            Intent i = getIntent();
            str_custid = i.getStringExtra("custid");
            str_mobile = i.getStringExtra("mobile");
            str_name = i.getStringExtra("name");
            str_grpname = i.getStringExtra("grpname");
            str_grpticket = i.getStringExtra("grpticket");
            System.out.println("custid " + str_custid+"grpn "+str_grpname+" grpti "+str_grpticket);

        } catch (Exception e) {

        }
        if (!str_grpname.equalsIgnoreCase("")) {
            System.out.println("custid " + str_custid+"grpn "+str_grpname+" grpti "+str_grpticket);
            lay_grpname.setVisibility(View.VISIBLE);
            grpname.setText(str_grpname);
        }
        if (!str_grpticket.equalsIgnoreCase("")) {
            System.out.println("custid " + str_custid+"grpn "+str_grpname+" grpti "+str_grpticket);
            laygrpticket.setVisibility(View.VISIBLE);
            grpticket.setText(str_grpticket);
        }
        if (cd.isConnectedToInternet()) {
            name.setText(str_name);
            mobile.setText("Mobile No. " + str_mobile);
            getinfo();

        } else {
            Toast.makeText(Customer_Info.this, "No Internet Connection ! ", Toast.LENGTH_SHORT).show();
            name.setText(str_name);
            mobile.setText("Mobile No. " + str_mobile);
        }
    }

    private void getinfo() {
        StringRequest req = new StringRequest(Request.Method.POST, Config.getcustinfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    str_address = object.getString("address");
                    str_collect_area = object.getString("collect_area");
                    str_introname = object.getString("Introducer_name");

                    if (!str_introname.trim().equalsIgnoreCase("")) {
                        intoname.setText(str_introname);
                        lay_intro.setVisibility(View.VISIBLE);
                    } else {
                        intoname.setVisibility(View.GONE);
                        lay_intro.setVisibility(View.GONE);
                    }
                    if (!str_collect_area.trim().equalsIgnoreCase("")) {
                        collect_area.setText(str_collect_area);
                        lay_area.setVisibility(View.VISIBLE);
                    } else {
                        collect_area.setVisibility(View.GONE);
                        lay_area.setVisibility(View.GONE);
                    }
                    if (!str_address.trim().equalsIgnoreCase("")) {
                        address.setText(str_address);
                    } else {
                        address.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("custid", str_custid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(req);

    }

}
