package com.mazenet.mzs119.skst;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skst.Adapter.Branchadapter;
import com.mazenet.mzs119.skst.Adapter.CustomAdapterCollectAgent;
import com.mazenet.mzs119.skst.Adapter.CustomAdaptercustomername;
import com.mazenet.mzs119.skst.Database.DatabaseCollectagent;
import com.mazenet.mzs119.skst.Database.Databasecustomers;
import com.mazenet.mzs119.skst.Model.BranchModel;
import com.mazenet.mzs119.skst.Model.CollectModel;
import com.mazenet.mzs119.skst.Model.Custmodel;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ListViewHeight;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddCustomer extends AppCompatActivity {
    public ArrayList<BranchModel> branchArray = new ArrayList<BranchModel>();
    public ArrayList<BranchModel> branchArrayMain = new ArrayList<BranchModel>();
    String[] salutation = {"Mr.", "Mrs", "Miss"};
    String[] salary = {"Salary", "Agriculture", "Business Income", "Investment Income"};
    int selfcheck = 0, custcheck = 0, emplycheck = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog;

    Branchadapter branchAdapter;
    ArrayAdapter<String> SaluteAdapt;
    ArrayAdapter<String> SalaryApapt;
    CheckBox cb_sameaddress;
    DatePickerDialog fromDatePickerDialog;
    Button btn_submit, btn_reset;
    Spinner SaluteSpinner, SalarySpinner;
    RadioGroup rg_refertype, rg_gender, rg_relation;
    RadioButton rb_male, rb_female, rb_shemale, rb_customer, rb_employee, rb_self, rb_father, rb_spouse;
    EditText edt_firstname, edt_branchName, edt_initial, edt_age, edt_mobileno, edt_alternateMobile, edt_email, edt_aadhar, edt_pan, edt_ration, edt_father, edt_spouse, edt_presentAddress, edt_presentCity,
            edt_presentDistrict, edt_presentstate, edt_presentPin, edt_addressCommunication, edt_retiremnetdate, edt_doj, edt_refercust, edt_referemply,
            edt_communicationCity, edt_CommunicationDistrict, edt_pfnumber, edt_occupation, edt_CommunicationState, edt_CommunicationPin, edt_netSalary;
    String str_saluteSpinner = "", str_sourceoffunds = "", str_fname = "", DOB1 = "", doj1 = "", str_edt_initial = "", str_age = "", str_mobileno = "", str_gender = "", str_alternatemobile = "", str_email = "", str_aadhar = "", str_pan = "", str_ration = "", str_presentAddress = "",
            str_presentCity = "", str_presentDistrict = "", str_presentState = "", str_presentPin = "", str_communicationAddress = "", str_retirementdate = "", str_communicationCity = "",
            str_communicationDistrict = "", str_spousename = "", str_fathername = "", str_communicationState = "", str_referType = "", str_pfnumber = "", str_occupation = "", str_communicationPin = "", str_netSalary = "";
    SimpleDateFormat df;
    ListView listBranch, listReferer, listReferEmplyee;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String Branchid = "0";
    String ReferCustomer = "0";
    String ReferEmployee = "0";
    String ReferId = "";
    int mailcheck = 0, mobilecheck = 0;
    private JSONObject jobj;
    private JSONArray jsonarray;
    private JSONArray customerNameArray;
    private ArrayList<Custmodel> customerArray = new ArrayList<Custmodel>();
    private Databasecustomers dbcust = new Databasecustomers(this);
    private DatabaseCollectagent dbcollect = new DatabaseCollectagent(this);
    private CustomAdapterCollectAgent CollectLIst;
    private ArrayList<Custmodel> customer_listmain = new ArrayList<Custmodel>();
    private CustomAdaptercustomername adapterlist;
    private ArrayList<CollectModel> collectArray = new ArrayList<>();
    private ArrayList<CollectModel> collectArrayMain = new ArrayList<>();
    private String str_relation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();


        SaluteSpinner = (Spinner) findViewById(R.id.spn_ac_salute);
        SalarySpinner = (Spinner) findViewById(R.id.spn_ac_sourceOfFunds);

        edt_firstname = (EditText) findViewById(R.id.edt_ac_fname);
        edt_branchName = (EditText) findViewById(R.id.edt_ac_b5ranchname);
        edt_initial = (EditText) findViewById(R.id.edt_ac_initial);
        edt_doj = (EditText) findViewById(R.id.edt_ac_doj);
        edt_age = (EditText) findViewById(R.id.edt_ac_age);
        edt_mobileno = (EditText) findViewById(R.id.edt_ac_mobileno);
        edt_alternateMobile = (EditText) findViewById(R.id.edt_ac_altermobile);
        edt_email = (EditText) findViewById(R.id.edt_ac_email);
        edt_aadhar = (EditText) findViewById(R.id.edt_ac_aadhar);
        edt_ration = (EditText) findViewById(R.id.edt_ac_ration);
        edt_pan = (EditText) findViewById(R.id.edt_ac_pan);
        edt_refercust = (EditText) findViewById(R.id.edt_ac_refercustomer);
        edt_referemply = (EditText) findViewById(R.id.edt_ac_referemployee);
        edt_father = (EditText) findViewById(R.id.edt_ac_fathername);
        edt_spouse = (EditText) findViewById(R.id.edt_ac_spousename);
        edt_retiremnetdate = (EditText) findViewById(R.id.edt_ac_retirementdate);
        edt_presentAddress = (EditText) findViewById(R.id.edt_ac_present_address);
        edt_presentCity = (EditText) findViewById(R.id.edt_ac_presentcity);
        edt_presentDistrict = (EditText) findViewById(R.id.edt_ac_presentdistrict);
        edt_presentstate = (EditText) findViewById(R.id.edt_ac_presentstate);
        edt_presentPin = (EditText) findViewById(R.id.edt_ac_presentpinno);
        edt_addressCommunication = (EditText) findViewById(R.id.edt_ac_addressCommunication);
        edt_communicationCity = (EditText) findViewById(R.id.edt_ac_communicationcity);
        edt_CommunicationDistrict = (EditText) findViewById(R.id.edt_ac_communicationdistrict);
        edt_CommunicationState = (EditText) findViewById(R.id.edt_ac_communicationstate);
        edt_CommunicationPin = (EditText) findViewById(R.id.edt_ac_communicationpinno);
        edt_occupation = (EditText) findViewById(R.id.edt_ac_occupation);
        edt_pfnumber = (EditText) findViewById(R.id.edt_ac_pfnumber);
        edt_netSalary = (EditText) findViewById(R.id.edt_ac_netsalary);

        rg_gender = (RadioGroup) findViewById(R.id.eg_ac_gendergroup);
        rg_refertype = (RadioGroup) findViewById(R.id.rg_ac_refertype);
        rg_relation = (RadioGroup) findViewById(R.id.rg_ac_relation);

        rb_male = (RadioButton) findViewById(R.id.rb_ac_male);
        rb_female = (RadioButton) findViewById(R.id.rb_ac_female);
        rb_shemale = (RadioButton) findViewById(R.id.rb_ac_shemale);
        rb_customer = (RadioButton) findViewById(R.id.rb_ac_customerRefer);
        rb_employee = (RadioButton) findViewById(R.id.rb_ac_employeeRefer);
        rb_self = (RadioButton) findViewById(R.id.rb_ac_selfjoin);
        rb_father = (RadioButton) findViewById(R.id.rb_ac_father);
        rb_spouse = (RadioButton) findViewById(R.id.rb_ac_spouse);

        cb_sameaddress = (CheckBox) findViewById(R.id.cb_ac_sameaddress);

        btn_submit = (Button) findViewById(R.id.btn_ac_submit);
        btn_reset = (Button) findViewById(R.id.btn_ac_reset);

        listBranch = (ListView) findViewById(R.id.list_ac_branchname);
        listReferer = (ListView) findViewById(R.id.list_ac_referername);
        listReferEmplyee = (ListView) findViewById(R.id.list_ac_referemployee);

        edt_referemply.setVisibility(View.GONE);
        edt_spouse.setVisibility(View.GONE);

        SaluteAdapt = new ArrayAdapter<String>(AddCustomer.this, android.R.layout.simple_dropdown_item_1line, salutation);
        SaluteSpinner.setAdapter(SaluteAdapt);
        SalaryApapt = new ArrayAdapter<String>(AddCustomer.this, android.R.layout.simple_dropdown_item_1line, salary);
        SalarySpinner.setAdapter(SalaryApapt);
        LoadBranch();
        // LoadCustomer();
        //LoadCollectAgent();


        df = new SimpleDateFormat("dd-MM-yyyy");
        rb_male.setChecked(false);
        rb_female.setChecked(false);
        rb_shemale.setChecked(false);
        rb_customer.setChecked(false);
        edt_refercust.setVisibility(View.GONE);
        rb_employee.setChecked(false);
        rb_self.setChecked(false);
        rb_father.setChecked(false);
        edt_father.setVisibility(View.GONE);
        //-------------------------------------------------------------------------------------------------------------------------------------
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

        try {

            Calendar newCalendar = Calendar.getInstance();

            int ddd = newCalendar.get(Calendar.DAY_OF_MONTH);
            int wwww = newCalendar.get(Calendar.WEEK_OF_YEAR);

            if (ddd == pref.getInt("dailycheckdaymain", 0) && wwww == pref.getInt("dailycheckmonthmain", 0) && dbcollect.getContactsCount() != 0) {
                reterivelocalCollect();

            } else {

                editor.putInt("dailycheckdaymain",
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                editor.putInt("dailycheckmonthmain",
                        newCalendar.get(Calendar.WEEK_OF_YEAR));
                editor.putString("companymain",
                        pref.getString("company", null));

                editor.commit();
                LoadCollectAgent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //--------------------------------------------------------------spinner value Getters---------------------------------------------------------------------------
        SaluteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_saluteSpinner = SaluteSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SalarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_sourceoffunds = SalarySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //-----------------------------------------------------------------Branch editext----------------------------------------------------------
        edt_branchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_branchName.getText().toString();
                if (text.equals("") || text.equals(null)) {
                    listBranch.setVisibility(View.GONE);
                    branchArrayMain.clear();

                } else {
                    setnewbranchadapter();
                    ListViewHeight.setListViewHeightBasedOnChildren(listBranch);
                    listBranch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (branchArrayMain.size() > 0) {
                    listBranch.setVisibility(View.VISIBLE);

                } else {

                    listBranch.setVisibility(View.GONE);
                }
            }
        });
        listBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BranchModel mod = branchArrayMain.get(i);
                edt_branchName.setText(mod.getName());
                Branchid = mod.getId();
                branchAdapter.notifyDataSetChanged();
                listBranch.setVisibility(View.GONE);
            }
        });

        //---------------------------------------------------------------------------------------------------------------------------------
        edt_refercust.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_refercust.getText().toString();
                if (text.equals("") || text.equals(null)) {
                    listReferer.setVisibility(View.GONE);
                    customer_listmain.clear();
                } else {
                    setnewrefercustomeradapter();
                    ListViewHeight.setListViewHeightBasedOnChildren(listReferer);
                    listReferer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (customer_listmain.size() > 0) {
                    listReferer.setVisibility(View.VISIBLE);

                } else {

                    listReferer.setVisibility(View.GONE);
                }

            }
        });
        listReferer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Custmodel cmd = customer_listmain.get(i);
                String name = cmd.getNAME();
                edt_refercust.setText(name);
                ReferCustomer = cmd.getCusid();
                adapterlist.notifyDataSetChanged();
                listReferer.setVisibility(View.GONE);
            }
        });
        //-------------------------------------------------------------------------------------------------------------------------------
        edt_referemply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_referemply.getText().toString();
                if (text.equals("") || text.equals(null)) {
                    listReferEmplyee.setVisibility(View.GONE);
                    collectArrayMain.clear();
                } else {
                    setnewcollectAdapter();
                    ListViewHeight.setListViewHeightBasedOnChildren(listReferEmplyee);
                    listReferEmplyee.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (collectArrayMain.size() > 0) {
                    listReferEmplyee.setVisibility(View.VISIBLE);

                } else {

                    listReferEmplyee.setVisibility(View.GONE);
                }

            }
        });
        listReferEmplyee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CollectModel cmd = collectArrayMain.get(i);
                String name = cmd.getName();
                edt_referemply.setText(name);
                ReferEmployee = cmd.getId();
                CollectLIst.notifyDataSetChanged();
                listReferEmplyee.setVisibility(View.GONE);
            }
        });
//--------------------------------------------------------email textWatcher--------------------------------------
        edt_email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (edt_email.getText().toString().matches(emailPattern) && s.length() > 0) {
                    mailcheck = 1;
                } else {
                    edt_email.setError("Enter Valid Email");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

//------------------------------------------------------mobile no. edittext-----------------------

        edt_mobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (edt_mobileno.getText().toString().length() < 10 && editable.length() > 0) {
                    edt_mobileno.setError("Enter 10 Digit Number");
                } else {
                    mobilecheck = 1;
                }
            }
        });
        //---------------------------------------Check box-----------------------------------------------------------------------------------
        cb_sameaddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_sameaddress.isChecked()) {
                    str_communicationAddress = edt_addressCommunication.getText().toString();
                    str_communicationCity = edt_communicationCity.getText().toString();
                    str_communicationDistrict = edt_CommunicationDistrict.getText().toString();
                    str_communicationState = edt_CommunicationState.getText().toString();
                    str_communicationPin = edt_CommunicationPin.getText().toString();

                    edt_presentAddress.setText(str_communicationAddress);
                    edt_presentCity.setText(str_communicationCity);
                    edt_presentDistrict.setText(str_communicationDistrict);
                    edt_presentstate.setText(str_communicationState);
                    edt_presentPin.setText(str_communicationPin);

                } else {
                    edt_presentAddress.setText("");
                    edt_presentCity.setText("");
                    edt_presentDistrict.setText("");
                    edt_presentstate.setText("");
                    edt_presentPin.setText("");
                }

            }
        });

//----------------------------------------Submit and Reset button----------------------------------------------------------------------------------
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Branchid.equalsIgnoreCase("0") || Branchid.equals("")) {
                    edt_branchName.setError("Please Select Branch Name");
                    edt_branchName.requestFocus();
                    return;
                } else if (edt_firstname.getText().toString().trim().equals("")) {
                    edt_firstname.setError("Please enter Name");
                    edt_firstname.requestFocus();
                    return;
                } else if (edt_initial.getText().toString().trim().equals("")) {
                    edt_initial.setError("Please enter Initial");
                    edt_initial.requestFocus();
                    return;
                } else if (edt_age.getText().toString().trim().equals("")) {
                    edt_age.setError("Please enter Date of Birth");
                    edt_age.requestFocus();
                } else if (edt_mobileno.getText().toString().trim().equals("") || mobilecheck == 0) {
                    edt_mobileno.setError("Please enter Mobile number");
                    edt_mobileno.requestFocus();
                    return;
                } else if (edt_father.getText().toString().trim().equals("") && edt_spouse.getText().toString().trim().equals("")) {
                    rb_father.requestFocus();
                    rb_father.setChecked(true);
                    edt_father.setError("Enter Name");
                    edt_father.requestFocus();
                } else if (edt_doj.getText().toString().trim().equals("")) {
                    edt_doj.setError("Please enter Date of Joining");
                    edt_doj.requestFocus();
                    return;
                } else if (edt_addressCommunication.getText().toString().trim().equals("")) {
                    edt_addressCommunication.setError("Please enter Communication Address");
                    edt_addressCommunication.requestFocus();
                    return;
                } else if (edt_communicationCity.getText().toString().trim().equals("")) {
                    edt_communicationCity.setError("Please enter City");
                    edt_communicationCity.requestFocus();
                    return;
                } else if (edt_CommunicationDistrict.getText().toString().trim().equals("")) {
                    edt_CommunicationDistrict.setError("Please enter present District");
                    edt_CommunicationDistrict.requestFocus();
                    return;
                } else if (edt_CommunicationState.getText().toString().trim().equals("")) {
                    edt_CommunicationState.setError("Please enter present State");
                    edt_CommunicationState.requestFocus();
                    return;
                } else if (edt_CommunicationPin.getText().toString().trim().equals("")) {
                    edt_CommunicationPin.setError("Please enter present Pin");
                    edt_CommunicationPin.requestFocus();
                    return;
                } else if (edt_presentAddress.getText().toString().trim().equals("")) {
                    edt_presentAddress.setError("Please enter Permanent Address");
                    edt_presentAddress.requestFocus();
                    return;
                } else if (edt_presentCity.getText().toString().trim().equals("")) {
                    edt_presentCity.setError("Please enter present City");
                    edt_presentCity.requestFocus();
                    return;
                } else if (edt_presentDistrict.getText().toString().trim().equals("")) {
                    edt_presentDistrict.setError("Please enter present District");
                    edt_presentDistrict.requestFocus();
                    return;
                } else if (edt_presentstate.getText().toString().trim().equals("")) {
                    edt_presentstate.setError("Please enter present State");
                    edt_presentstate.requestFocus();
                    return;
                } else if (edt_presentPin.getText().toString().trim().equals("")) {
                    edt_presentPin.setError("Please enter Pincode");
                    edt_presentPin.requestFocus();
                    return;
                } else if (custcheck == 1 && ReferCustomer.equalsIgnoreCase("0")) {
                    edt_refercust.requestFocus();
                    edt_refercust.setError("Select Name");
                    return;
                } else if (emplycheck == 1 && ReferEmployee.equalsIgnoreCase("0")) {
                    edt_referemply.requestFocus();
                    edt_referemply.setError("Select Name");
                    return;
                } else if (selfcheck == 0 && custcheck == 0 && emplycheck == 0) {

                    Snackbar snackbar = Snackbar
                            .make(view, "Select a Referer", Snackbar.LENGTH_SHORT);

                    snackbar.show();
                    return;
                } else if (edt_occupation.getText().toString().trim().equals("")) {
                    edt_occupation.setError("Please enter Occupation");
                    edt_occupation.requestFocus();
                    return;
                } else if (edt_netSalary.getText().toString().trim().equals("")) {
                    edt_netSalary.setError("Please enter Salary");
                    edt_netSalary.requestFocus();
                    return;
                } else {
                    str_fname = edt_firstname.getText().toString();
                    str_edt_initial = edt_initial.getText().toString();
                    final String firstname = str_fname + " . " + str_edt_initial;
                    str_aadhar = edt_aadhar.getText().toString();
                    str_mobileno = edt_mobileno.getText().toString();
                    str_alternatemobile = edt_alternateMobile.getText().toString();
                    str_pan = edt_pan.getText().toString();
                    str_email = edt_email.getText().toString();
                    str_ration = edt_ration.getText().toString();
                    str_fathername = edt_father.getText().toString();
                    str_spousename = edt_spouse.getText().toString();
                    str_presentAddress = edt_presentAddress.getText().toString();
                    str_presentCity = edt_presentCity.getText().toString();
                    str_presentDistrict = edt_presentDistrict.getText().toString();
                    str_presentState = edt_presentstate.getText().toString();
                    str_presentPin = edt_presentPin.getText().toString();
                    str_communicationAddress = edt_addressCommunication.getText().toString();
                    str_communicationCity = edt_communicationCity.getText().toString();
                    str_communicationDistrict = edt_CommunicationDistrict.getText().toString();
                    str_communicationState = edt_CommunicationState.getText().toString();
                    str_communicationPin = edt_CommunicationPin.getText().toString();
                    str_pfnumber = edt_pfnumber.getText().toString();
                    str_occupation = edt_occupation.getText().toString();
                    str_netSalary = edt_netSalary.getText().toString();
                    str_presentAddress = edt_presentAddress.getText().toString();
                    str_presentCity = edt_presentCity.getText().toString();
                    str_presentDistrict = edt_presentDistrict.getText().toString();
                    str_presentState = edt_presentstate.getText().toString();
                    str_presentPin = edt_presentPin.getText().toString();
                    str_netSalary = edt_netSalary.getText().toString();

                    showDialog();
                    StringRequest addreq = new StringRequest(Request.Method.POST,
                            Config.addcustomer, new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            Log.i("quer", response);
                            hidePDialog();
                            showSnackBar(AddCustomer.this, "Customer Added");

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
                            params.put("branch_id", Branchid);
                            params.put("salutation", str_saluteSpinner);
                            params.put("firstname", firstname);
                            params.put("gender", str_gender);
                            params.put("dob", DOB1);
                            params.put("doj", doj1);
                            params.put("Email_Id_F", str_email);
                            params.put("Mobile_F", str_mobileno);
                            params.put("Mobile_AF", str_alternatemobile);
                            params.put("Pan_No_F", str_pan);
                            params.put("Ration_No", str_ration);
                            params.put("Aadhar_F", str_aadhar);
                            params.put("Father_Spouse", str_relation);
                            params.put("Father_F", str_fathername);
                            params.put("Wife_F", str_spousename);
                            params.put("address", str_communicationAddress);
                            params.put("City_F", str_communicationCity);
                            params.put("State_F", str_communicationState);
                            params.put("District_F", str_communicationDistrict);
                            params.put("P_District_F", str_presentDistrict);
                            params.put("Pincode_F", str_communicationPin);
                            params.put("address_p", str_presentAddress);
                            params.put("P_City_F", str_presentCity);
                            params.put("P_State_F", str_presentState);
                            params.put("P_Pincode_F", str_presentPin);
                            params.put("Source_Of_Funds", str_sourceoffunds);
                            params.put("Gross_Annual_Income", str_netSalary);
                            params.put("PF_No", str_pfnumber);
                            params.put("Retir_Date", str_retirementdate);
                            params.put("Occupation", str_occupation);
                            params.put("Created_By", pref.getString("userid", "0"));
                            params.put("Introducer_Id", ReferId);
                            params.put("Introducer_type", str_referType);
                            return params;
                        }

                    };

                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(addreq);
                }


            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfcheck = 0;
                mailcheck = 0;
                edt_branchName.setText("");
                edt_firstname.setText("");
                edt_initial.setText("");
                edt_age.setText("");
                DOB1 = "";
                edt_mobileno.setText("");
                edt_alternateMobile.setText("");
                edt_email.setText("");
                edt_aadhar.setText("");
                edt_pan.setText("");
                edt_ration.setText("");
                doj1 = "";
                edt_doj.setText("");
                edt_father.setText("");
                edt_spouse.setText("");
                edt_presentAddress.setText("");
                edt_presentCity.setText("");
                edt_presentDistrict.setText("");
                edt_presentstate.setText("");
                edt_presentPin.setText("");
                cb_sameaddress.setChecked(false);
                rb_customer.setChecked(false);
                rb_employee.setChecked(false);
                rb_self.setChecked(false);
                rb_male.setChecked(false);
                rb_female.setChecked(false);
                rb_shemale.setChecked(false);
                edt_refercust.setVisibility(View.GONE);
                edt_referemply.setVisibility(View.GONE);
                edt_addressCommunication.setText("");
                edt_communicationCity.setText("");
                edt_CommunicationDistrict.setText("");
                edt_CommunicationState.setText("");
                edt_CommunicationPin.setText("");
                edt_refercust.setText("");
                edt_referemply.setText("");
                edt_pfnumber.setText("");
                edt_retiremnetdate.setText("");
                edt_occupation.setText("");
                edt_netSalary.setText("");
                Branchid = "0";
                custcheck = 0;
                emplycheck = 0;
                selfcheck = 0;

            }
        });
//------------------------------------------------------------------Radio groups-----------------------------------------------------------------------------------------
        rg_refertype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_customer.isChecked()) {

                    str_referType = rb_customer.getText().toString();
                    edt_refercust.setVisibility(View.VISIBLE);
                    edt_referemply.setVisibility(View.GONE);
                    custcheck = 1;
                    emplycheck = 0;
                    selfcheck = 0;
                    edt_referemply.setText("");
                    ReferEmployee = "0";
                    ReferId = ReferCustomer;
                    edt_refercust.requestFocus();

                } else if (rb_employee.isChecked()) {
                    str_referType = rb_employee.getText().toString();
                    edt_referemply.setVisibility(View.VISIBLE);
                    edt_refercust.setVisibility(View.GONE);
                    custcheck = 0;
                    emplycheck = 1;
                    selfcheck = 0;
                    edt_refercust.setText("");
                    ReferCustomer = "0";
                    ReferId = ReferEmployee;
                    edt_referemply.requestFocus();

                } else if (rb_self.isChecked()) {
                    str_referType = rb_self.getText().toString();
                    custcheck = 0;
                    emplycheck = 0;
                    selfcheck = 1;
                    edt_referemply.setVisibility(View.GONE);
                    edt_refercust.setVisibility(View.GONE);
                    edt_refercust.setText("");
                    edt_referemply.setText("");
                    ReferCustomer = "0";
                    ReferEmployee = "0";
                    edt_pfnumber.requestFocus();


                }
            }
        });

        rg_relation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_father.isChecked()) {
                    str_relation = "FatherName";
                    edt_father.setVisibility(View.VISIBLE);
                    edt_spouse.setVisibility(View.GONE);
                    edt_spouse.setText("");
                    edt_father.requestFocus();
                } else if (rb_spouse.isChecked()) {
                    str_relation = "SpouseName";
                    edt_father.setVisibility(View.GONE);
                    edt_spouse.setVisibility(View.VISIBLE);
                    edt_father.setText("");
                    edt_spouse.requestFocus();
                }
            }
        });

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_male.isChecked()) {
                    str_gender = rb_male.getText().toString();
                } else if (rb_female.isChecked()) {
                    str_gender = rb_female.getText().toString();
                } else if (rb_shemale.isChecked()) {
                    str_gender = rb_shemale.getText().toString();
                }
            }
        });
//---------------------------------------------------------------Retirement Date picker-----------------------------------------------------------

        edt_retiremnetdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(AddCustomer.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_retirementdate = df.format(newDate.getTime());
                            edt_retiremnetdate.setText(str_retirementdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000 * 48);
                fromDatePickerDialog.setTitle("Retiremnet date");

                fromDatePickerDialog.show();

            }
        });
        //-------------------------------Date of Joining Date picker-----------------------------------------------------------

        edt_doj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(AddCustomer.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();

                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);
                        try {
                            doj1 = df.format(newDate.getTime());
                            edt_doj.setText(doj1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("Date of Joining");
                fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 * 48);
                fromDatePickerDialog.show();
            }

        });


        //-------------------------------Date of Birth Date picker-----------------------------------------------------------

        edt_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(AddCustomer.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();

                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);
                        try {
                            StringBuilder str_dob = new StringBuilder();
                            DOB1 = df.format(newDate.getTime());
                            str_age = getAge(year, monthOfYear, dayOfMonth);
                            str_dob.append(DOB1 + "  Age: " + str_age);
                            edt_age.setText(str_dob);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("Date of Birth");
                fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 * 48);
                fromDatePickerDialog.show();
            }
        });


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void reterivelocal() {

        customerArray.clear();
        customer_listmain.clear();
        customerArray = dbcust.getAllContacts();
        customer_listmain.addAll(customerArray);

        adapterlist = new CustomAdaptercustomername(AddCustomer.this, customer_listmain);
        adapterlist.notifyDataSetChanged();
        listReferer.setAdapter(adapterlist);
        listReferer.setVisibility(View.GONE);

    }

    public void reterivelocalCollect() {

        collectArray.clear();
        collectArrayMain.clear();
        collectArray = dbcollect.getAllAgent();
        collectArrayMain.addAll(collectArray);
        CollectLIst = new CustomAdapterCollectAgent(AddCustomer.this, collectArrayMain);
        CollectLIst.notifyDataSetChanged();
        listReferEmplyee.setAdapter(CollectLIst);
        listReferEmplyee.setVisibility(View.GONE);

    }

    private void setnewcollectAdapter() {
        collectArrayMain.clear();

        String text = edt_referemply.getText().toString();
        text = text.toLowerCase(Locale.getDefault());
        if (text.equals(null)) {

            listReferEmplyee.setVisibility(View.GONE);
            return;

        } else {


            for (int i = 0; i < collectArray.size(); i++) {
                CollectModel cm = collectArray.get(i);
                String name = cm.getName();
                name = name.toLowerCase(Locale.getDefault());


                if (name.contains(text)) {
                    collectArrayMain.add(collectArray.get(i));
                    CollectLIst.notifyDataSetChanged();
                }

            }


        }

        if (collectArrayMain.size() > 0) {
            listReferEmplyee.setVisibility(View.VISIBLE);
            CollectLIst = new CustomAdapterCollectAgent(AddCustomer.this, collectArrayMain);
            CollectLIst.notifyDataSetChanged();
            listReferEmplyee.setAdapter(CollectLIst);

        } else {

            listReferEmplyee.setVisibility(View.GONE);
        }
    }


    private void setnewbranchadapter() {
        branchArrayMain.clear();

        String text = edt_branchName.getText().toString();
        text = text.toLowerCase(Locale.getDefault());
        if (text.equals(null)) {

            listBranch.setVisibility(View.GONE);
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
            listBranch.setVisibility(View.VISIBLE);
            branchAdapter = new Branchadapter(AddCustomer.this, branchArrayMain);
            branchAdapter.notifyDataSetChanged();
            listBranch.setAdapter(branchAdapter);

        } else {

            listBranch.setVisibility(View.GONE);
        }
    }

    //-----------------------------------------------------------------referer_edittext--------------------------------------------------------------

    private void setnewrefercustomeradapter() {

        customer_listmain.clear();

        String text = edt_refercust.getText().toString();
        text = text.toLowerCase(Locale.getDefault());
        if (text.equals(null) || text.equals("")) {

            listReferer.setVisibility(View.GONE);
            return;

        } else {


            for (int i = 0; i < customerArray.size(); i++) {

                Custmodel custm = customerArray.get(i);
                String name = custm.getNAME();
                Log.i("name", name);
                name = name.toLowerCase(Locale.getDefault());


                if (name.contains(text)) {
                    customer_listmain.add(customerArray.get(i));
                    adapterlist.notifyDataSetChanged();
                }

            }


        }

        if (customer_listmain.size() > 0) {
            listReferer.setVisibility(View.VISIBLE);

            adapterlist = new CustomAdaptercustomername(AddCustomer.this, customer_listmain);
            adapterlist.notifyDataSetChanged();
            listReferer.setAdapter(adapterlist);

        } else {

            listReferer.setVisibility(View.GONE);
        }


    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    //------------------------------Volley json adapter for branch_name spinner-----------------------------------------
    private void LoadBranch() {

        final StringRequest branchreq = new StringRequest(Request.Method.POST, Config.branchmaster, new Response.Listener<String>() {

            public void onResponse(String response) {
                Log.d("branch Activity", response.toString());

                try {
                    jobj = new JSONObject(response);
                    jsonarray = jobj.getJSONArray(Config.jsonarraybranch);
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

        branchAdapter = new Branchadapter(AddCustomer.this, branchArrayMain);
        branchAdapter.notifyDataSetChanged();

        listBranch.setAdapter(branchAdapter);
        listBranch.setVisibility(View.GONE);

    }

    //---------------------------------------------------customer name-----------------------------------------------------------------------------------
    private void LoadCustomer() {

        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.reteriveusers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("customer Activity", response.toString());
                hidePDialog();


                try {

                    customerArray.clear();


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
                            sched.setTotalenrlpending(jObj.getString("Total_Enrl_Paid"));
                            sched.setLevel(jObj.getString("Level"));
                            sched.setBonusamt(jObj.getString("Bonus_Amt"));
                            sched.setPenaltyamt(jObj.getString("Penalty_Amt"));
                            sched.setPendingamt(jObj.getString("Pending_Days"));


                            customerArray.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (customerArray.size() > 0) {


                        try {
                            dbcust.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            dbcust.addcustomer(customerArray);
                            customer_listmain.clear();
                            customer_listmain.addAll(customerArray);
                            adapterlist = new CustomAdaptercustomername(AddCustomer.this, customer_listmain);
                            adapterlist.notifyDataSetChanged();
                            ListViewHeight.setListViewHeightBasedOnChildren(listReferer);
                            listReferer.setAdapter(adapterlist);
                            listReferer.setVisibility(View.GONE);
                            Log.i("db_out", "storeed");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                AddCustomer.this);
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
                // params.put("compname", pref.getString("company", ""));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    //------------------------------------------------------------------------collect AGent--------------------------------------------------------------------------

    private void LoadCollectAgent() {
        showDialog();

        StringRequest CollectReq = new StringRequest(Request.Method.POST,
                Config.collectagent, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("collect Agent", response.toString());
                hidePDialog();


                try {

                    collectArray.clear();


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("Collecion Agent");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);
                            CollectModel dbc = new CollectModel();
                            dbc.setId(jObj.getString("Id"));
                            dbc.setName(jObj.getString("Name"));
                            dbc.setEmp_Code(jObj.getString("Emp_Code"));
                            collectArray.add(dbc);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (collectArray.size() > 0) {


                        try {
                            dbcollect.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            dbcollect.addCollectAgent(collectArray);
                            collectArrayMain.clear();
                            collectArrayMain.addAll(collectArray);
                            Log.i("colect db", "'stored");
                            CollectLIst = new CustomAdapterCollectAgent(AddCustomer.this, collectArrayMain);
                            CollectLIst.notifyDataSetChanged();
                            //ListViewHeight.setListViewHeightBasedOnChildren(listReferEmplyee);
                            listReferEmplyee.setAdapter(CollectLIst);
                            listReferEmplyee.setVisibility(View.GONE);
                            //Log.i("db_out", "storeed");
                            // customer_list.clear();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                AddCustomer.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Data from Server. colect db.contact Admin");

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
                // params.put("compname", pref.getString("company", ""));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(CollectReq);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void hidePDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog.setContentView(R.layout.my_progress);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
        pDialog.setContentView(R.layout.my_progress);

    }

    public void showSnackBar(Activity activity, String message) {
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).setAction("Add another", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = getIntent();
                finish();
                startActivity(mIntent);
            }
        }).show();
    }

}
