package com.mazenet.mzs119.skst;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.mazenet.mzs119.skst.Database.Databasecustomers;
import com.mazenet.mzs119.skst.Database.Databaserecepit;
import com.mazenet.mzs119.skst.Model.Custmodel;
import com.mazenet.mzs119.skst.Model.Enrollmodel;
import com.mazenet.mzs119.skst.Utils.AppController;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;
import com.mazenet.mzs119.skst.Utils.NDSpinner;
import com.mazenet.mzs119.skst.Utils.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IndividualReceipt extends AppCompatActivity {

    public ArrayList<Enrollmodel> enroll_list = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listmain = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listpost = new ArrayList<Enrollmodel>();
    public ArrayList<Custmodel> custpaidpend = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    String url = Config.reteriveindienroll;
    String url2 = Config.saverecepit;
    String receipt = Config.getreceiptno;
    String Enroll_update_cust = Config.Enroll_update_cust;
    EditText edt_re_amount, edt_re_remark, edt_re_rtgsno;
    TextView txt_totalamt;
    NonScrollListView lst_re_enroll;
    int payatotal = 0, amntpayable = 0;
    String cusid = "0", cusname = "", enrollid = "", groupname = "", Toatal_payable = "0";
    Button btn_autopopulate, btn_submit, btn_che_date, btn_dd_date, btn_rtgs_date, btn_che_image;
    CustomAdapterenrollment adapterlist;
    Databaserecepit dbrecepit;
    Databasecustomers dbc;
    String recepitamt = "0", amntpayables = "";
    int Balanceamt = 0;
    NDSpinner spinner;
    String payamount = "";
    ArrayList<String> list = new ArrayList<String>();
    String paytype = "Cash";
    LinearLayout lay_re_che, lay_re_dd, lay_re_rtgs;
    EditText edt_re_cheno, edt_re_chebank, edt_re_chebranch, edt_re_ddno, edt_re_ddbank, edt_re_ddbranch;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat df;
    String str_chedate = "", str_dddate = "", str_rtgsdate = "", str_cheno = "", str_chebank = "", str_chebranch = "", receiptno = "", str_remark = "", str_tranno = "";
    int payable = 0;
    private ProgressDialog pDialog;
    String amount2 = "";
    SimpleDateFormat dateFormat;
    String date = "";
    TextView txt_cusname;
    Uri fileUri = null;
    String imgfile64 = "", isimage = "false";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String TAG = IndividualReceipt.class.getSimpleName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewreceipts, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_viewreceipts:
                Intent i = new Intent(IndividualReceipt.this, MyTransactions.class);
                i.putExtra("cusid", cusid);
                i.putExtra("enrollid", enrollid);
                startActivity(i);
                break;
        }
        return true;
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    public String getStringImage(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos); //bm is the bitmap object
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        System.out.println("encode  " + encodedImage);
        return encodedImage;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                imgfile64 = getStringImage(fileUri.getPath());
                isimage = "true";
                try {
                    fileUri = data.getData();
                    Log.i("pick image req", "succes");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("pick image req", "failss");
                }


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                isimage = "false";
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                isimage = "false";
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (resultCode == RESULT_CANCELED) {
            isimage = "false";
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();

        } else {
            // failed to capture image
            isimage = "false";
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_receipt);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pref.getString("username", ""));

        df = new SimpleDateFormat("dd-MM-yyyy");

        dbc = new Databasecustomers(IndividualReceipt.this);
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        lst_re_enroll = (NonScrollListView) findViewById(R.id.lst_re_enroll_individual);

        lay_re_che = (LinearLayout) findViewById(R.id.lay_re_che_indiv);
        lay_re_dd = (LinearLayout) findViewById(R.id.lay_re_dd_indiv);
        lay_re_rtgs = (LinearLayout) findViewById(R.id.lay_re_rtgs_indiv);

        spinner = (NDSpinner) findViewById(R.id.spn_paytype_indiv);

        edt_re_amount = (EditText) findViewById(R.id.edt_amount_indiv);
        edt_re_remark = (EditText) findViewById(R.id.edt_re_remark_indiv);

        edt_re_cheno = (EditText) findViewById(R.id.edt_re_cheno_indiv);
        edt_re_chebank = (EditText) findViewById(R.id.edt_re_chebank_indiv);
        edt_re_chebranch = (EditText) findViewById(R.id.edt_re_chebranch_indiv);


        edt_re_ddno = (EditText) findViewById(R.id.edt_re_ddno_indiv);
        edt_re_ddbank = (EditText) findViewById(R.id.edt_re_ddbank_indiv);
        edt_re_ddbranch = (EditText) findViewById(R.id.edt_re_ddbranch_indiv);

        edt_re_rtgsno = (EditText) findViewById(R.id.edt_re_rtgsno_indiv);

        // btn_autopopulate = (Button) findViewById(R.id.btn_autopopulate_indiv);
        btn_submit = (Button) findViewById(R.id.btn_submit_indiv);
        btn_che_date = (Button) findViewById(R.id.btn_che_date_indiv);
        btn_dd_date = (Button) findViewById(R.id.btn_dd_date_indiv);
        btn_rtgs_date = (Button) findViewById(R.id.btn_rtgs_date_indiv);
        btn_che_image = (Button) findViewById(R.id.btn_che_image);
        dbrecepit = new Databaserecepit(this);
        txt_cusname = (TextView) findViewById(R.id.ir_name);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = new Date();
        date = dateFormat.format(date1);

        try {
            Intent it = getIntent();
            cusid = it.getStringExtra("cusid");
            cusname = it.getStringExtra("cusname");
            groupname = it.getStringExtra("groupname");
            enrollid = it.getStringExtra("enrollid");
            txt_cusname.setText(cusname);
            System.out.println(cusid + "ffffffffffffg");
            System.out.println(cusname + "ffffffffffffg");


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cd.isConnectedToInternet()) {
            reteriveall();
        } else {
            reteriveloca();
        }

        list.clear();
        list.add("Cash");
        list.add("Cheque");
        list.add("D.D");
        list.add("RTGS/NEFT");
        list.add("Card");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                IndividualReceipt.this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        btn_che_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cd.isConnectedToInternet()){
                    takePicture();
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            IndividualReceipt.this);
                    alertDialog.setTitle("Information");
                    alertDialog
                            .setMessage("Connect to Internet to Upload Cheque Images");

                    alertDialog.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }

            }
        });

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
//============================================================================================================
        recepitamt = "0";
        Balanceamt = 0;
/*        btn_autopopulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recepitamt = edt_re_amount.getText().toString();

                if (recepitamt.equalsIgnoreCase("0") || recepitamt.equalsIgnoreCase("")) {
                    Toast.makeText(IndividualReceipt.this, "Enter Recepit Amount", Toast.LENGTH_LONG).show();
                } else {
                    Balanceamt = Integer.parseInt(recepitamt);

                    enroll_listmain = dbrecepit.getAllenroll();

                    for (int i = 0; i < enroll_listmain.size(); i++) {
                        Enrollmodel mod = enroll_listmain.get(i);
                        String pendingamt = mod.getPending_Amt();
                        String penaltyamt = mod.getPenalty_Amt();
                        String bonusamt = mod.getBonus_Amt();
                        String tableid = mod.getTableid();


                        if (Balanceamt > 0) {
                            payable = Integer.parseInt(pendingamt) + Integer.parseInt(penaltyamt) - Integer.parseInt(bonusamt);

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
                    adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
                    adapterlist.notifyDataSetChanged();
                    // ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                    lst_re_enroll.setAdapter(adapterlist);
                    Toatal_payable=dbrecepit.gettotal();


                }

            }
        }); */
//============================================================================================================
        btn_che_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(IndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

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

                fromDatePickerDialog = new DatePickerDialog(IndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

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

                fromDatePickerDialog = new DatePickerDialog(IndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

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
//=============================================================================================================
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FillAmount();
                // Fillindividual();
                btn_submit.setVisibility(View.GONE);
                String ticketno = "";
                String penaltyamt = "";
                String pendingamt = "";
                String paidamt = "";
                String bonusamt = "";
                String payableamount = "";
                String enrollid = "";
                String chitvalue = "";
                String cusbranch = "";
                String pendingdays = "";
                String Group = "";
                String Advance = "";
                //String amount = ;
                amount2 = edt_re_amount.getText().toString();

                str_remark = edt_re_remark.getText().toString();

                System.out.println("amountttt  " + amount2);
                if ((amount2.equals("")) || (amount2.equals("0"))) {

                    Toast.makeText(IndividualReceipt.this, "Enter Amount", Toast.LENGTH_LONG).show();

                } else {

                    enroll_listpost.clear();
                    enroll_listpost = dbrecepit.getAllenroll();
                    if (paytype.equalsIgnoreCase("Cheque")) {

                        str_chebank = edt_re_chebank.getText().toString();
                        str_chebranch = edt_re_chebranch.getText().toString();
                        str_cheno = edt_re_cheno.getText().toString();

                        if (str_chebank.equalsIgnoreCase("")) {
                            edt_re_chebank.setError("Enter Cheque Bank Name");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter Cheque Bank", Toast.LENGTH_LONG).show();

                        } else if (str_chebranch.equalsIgnoreCase("")) {
                            edt_re_chebranch.setError("Enter Cheque Bank Branch");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            //Toast.makeText(IndividualReceipt.this, "Enter Cheque Bank Branch", Toast.LENGTH_LONG).show();

                        } else if (str_cheno.equalsIgnoreCase("")) {
                            edt_re_cheno.setError("Enter Cheque Number");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter Cheque Number", Toast.LENGTH_LONG).show();

                        } else if (str_chedate.equalsIgnoreCase("")) {
                            Toast.makeText(IndividualReceipt.this, "Select Cheque Date", Toast.LENGTH_LONG).show();
                            btn_submit.setVisibility(View.VISIBLE);
                            return;

                        } else {
                            if (cd.isConnectedToInternet()) {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGroup_Name();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();

                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }

                                        postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "cheque", cusbranch, pendingdays, "Pending");

                                    }


                                }
                            } else {

                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGroup_Name();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        int pen = Integer.parseInt(pendingamt);
                                        if (pen <= 0) {
                                            System.out.println("pending amnty enter");
                                            int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                            Toatal_payable = String.valueOf(totadvance);
                                            System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                        }
                                        if (pen > 0) {

                                            int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                            if (totadvance <= 0) {
                                                Toatal_payable = "0";
                                            } else {
                                                Toatal_payable = String.valueOf(totadvance);
                                            }

                                        }
                                        dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, str_cheno, pendingamt, str_chedate, str_chebank, str_chebranch, str_tranno, "", "cheque", str_remark, "Pending", Toatal_payable, date);
                                        dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, str_cheno, pendingamt, str_chedate, str_chebank, str_chebranch, str_tranno, "", "cheque", str_remark, "Pending");
                                    }
                                }
                            }
                        }


                    } else if (paytype.equalsIgnoreCase("D.D")) {

                        str_chebank = edt_re_ddbank.getText().toString();
                        str_chebranch = edt_re_ddbranch.getText().toString();
                        str_cheno = edt_re_ddno.getText().toString();
                        str_chedate = str_dddate;
                        if (str_chebank.equalsIgnoreCase("")) {
                            edt_re_ddbank.setError("Enter DD Bank");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter DD Bank", Toast.LENGTH_LONG).show();

                        } else if (str_chebranch.equalsIgnoreCase("")) {
                            edt_re_ddbranch.setError("Enter DD Bank Branch");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            //Toast.makeText(IndividualReceipt.this, "Enter DD Bank Branch", Toast.LENGTH_LONG).show();

                        } else if (str_cheno.equalsIgnoreCase("")) {
                            edt_re_ddno.setError("Enter DD Number");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter DD Number", Toast.LENGTH_LONG).show();

                        } else if (str_dddate.equalsIgnoreCase("")) {
                            Toast.makeText(IndividualReceipt.this, "Select DD Date", Toast.LENGTH_LONG).show();
                            btn_submit.setVisibility(View.VISIBLE);
                            return;


                        } else {
                            if (cd.isConnectedToInternet()) {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGroup_Name();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "dd", cusbranch, pendingdays, "Active");
                                    }


                                }
                            } else {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGroup_Name();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        System.out.println("temppppppppppppp_tableeeeeeeeeee");
                                       /* if (((Integer.parseInt(amount2)) > (Integer.parseInt(pendingamt)))) {
                                            payatotal = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)));
                                            Toatal_payable = String.valueOf(payatotal);
                                            amntpayable = ((Integer.parseInt(amount2)) - (Integer.parseInt(Toatal_payable)));
                                            amntpayables = String.valueOf(amntpayable);
                                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, amntpayables, ticketno, cusbranch, pendingdays, amntpayables, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", Toatal_payable);
                                            dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, amntpayables, ticketno, cusbranch, pendingdays, amntpayables, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", Toatal_payable);
                                        } else { */
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        int pen = Integer.parseInt(pendingamt);
                                        if (pen <= 0) {
                                            System.out.println("pending amnty enter");
                                            int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                            Toatal_payable = String.valueOf(totadvance);
                                            System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                        }
                                        if (pen > 0) {

                                            int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                            if (totadvance <= 0) {
                                                Toatal_payable = "0";
                                            } else {
                                                Toatal_payable = String.valueOf(totadvance);
                                            }

                                        }
                                        dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", Toatal_payable, date);
                                        dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active");
                                    }
                                }
                            }
                        }


                    } else if (paytype.equalsIgnoreCase("RTGS/NEFT")) {


                        str_tranno = edt_re_rtgsno.getText().toString();


                        if (str_rtgsdate.equalsIgnoreCase("")) {
                            Toast.makeText(IndividualReceipt.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            if (cd.isConnectedToInternet()) {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGroup_Name();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "neft", cusbranch, pendingdays, "Active");

                                    }

                                }
                            } else {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGroup_Name();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        System.out.println("temppppppppppppp_tableeeeeeeeeee");
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        int pen = Integer.parseInt(pendingamt);
                                        if (pen <= 0) {
                                            System.out.println("pending amnty enter");
                                            int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                            Toatal_payable = String.valueOf(totadvance);
                                            System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                        }
                                        if (pen > 0) {

                                            int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                            if (totadvance <= 0) {
                                                Toatal_payable = "0";
                                            } else {
                                                Toatal_payable = String.valueOf(totadvance);
                                            }

                                        }
                                        dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "neft", str_remark, "Active", Toatal_payable, date);
                                        dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "neft", str_remark, "Active");
                                    }
                                }
                            }

                            // updateenroll();

                        }

                    } else if (paytype.equalsIgnoreCase("Card")) {


                        str_tranno = edt_re_rtgsno.getText().toString();


                        if (str_rtgsdate.equalsIgnoreCase("")) {
                            Toast.makeText(IndividualReceipt.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            if (cd.isConnectedToInternet()) {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGroup_Name();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }

                                        postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "card", cusbranch, pendingdays, "Active");
                                    }


                                }
                            } else {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGroup_Name();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        System.out.println("temppppppppppppp_tableeeeeeeeeee");
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        int pen = Integer.parseInt(pendingamt);
                                        if (pen <= 0) {
                                            System.out.println("pending amnty enter");
                                            int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                            Toatal_payable = String.valueOf(totadvance);
                                            System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                        }
                                        if (pen > 0) {

                                            int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                            if (totadvance <= 0) {
                                                Toatal_payable = "0";
                                            } else {
                                                Toatal_payable = String.valueOf(totadvance);
                                            }

                                        }
                                        dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "card", str_remark, "Active", Toatal_payable, date);
                                        dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "card", str_remark, "Active");
                                    }
                                }
                            }
                        }
                        //updateenroll();


                    } else {
                        if (cd.isConnectedToInternet()) {

                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                enrollid = mod.getEnrollid();
                                bonusamt = mod.getBonus_Amt();
                                paidamt = mod.getPaid_Amt();
                                pendingamt = mod.getPending_Amt();
                                penaltyamt = mod.getPenalty_Amt();
                                Group = mod.getGroup_Name();
                                ticketno = mod.getGroup_Ticket_Name();
                                payamount = amount2;
                                chitvalue = mod.getScheme();
                                cusbranch = mod.getCusbranch();
                                pendingdays = mod.getPendingdys();
                                payableamount = amount2;
                                Advance = mod.getAdvanceamnt();
                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {
                                    System.out.println("internetttttttttttttttttttt");
                                    try {
                                        if (Advance.isEmpty()) {
                                            Advance = "0";
                                        }
                                    } catch (Exception e) {

                                    }

                                    postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "cash", cusbranch, pendingdays, "Active");

                                }
                            }


                        } else {
                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                enrollid = mod.getEnrollid();
                                bonusamt = mod.getBonus_Amt();
                                paidamt = mod.getPaid_Amt();
                                pendingamt = mod.getPending_Amt();
                                penaltyamt = mod.getPenalty_Amt();
                                Group = mod.getGroup_Name();
                                ticketno = mod.getGroup_Ticket_Name();
                                payamount = amount2;
                                chitvalue = mod.getScheme();
                                cusbranch = mod.getCusbranch();
                                pendingdays = mod.getPendingdys();
                                payableamount = amount2;
                                Advance = mod.getAdvanceamnt();
                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {
                                    System.out.println("temppppppppppppp_tableeeeeeeeeee");
                                    try {
                                        if (Advance.isEmpty()) {
                                            Advance = "0";
                                        }
                                    } catch (Exception e) {

                                    }
                                    // payatotal = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)));
                                    System.out.println("pending amnty " + pendingamt);
                                    System.out.println("pending amnty " + Integer.parseInt(pendingamt));
                                    int pen = Integer.parseInt(pendingamt);
                                    if (pen <= 0) {
                                        System.out.println("pending amnty enter");
                                        int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                        Toatal_payable = String.valueOf(totadvance);
                                        System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                    }
                                    if (pen > 0) {

                                        int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                        if (totadvance <= 0) {
                                            Toatal_payable = "0";
                                        } else {
                                            Toatal_payable = String.valueOf(totadvance);
                                        }

                                    }
                                    //String.valueOf(payatotal);
                                    // amntpayable = ((Integer.parseInt(amount2)) - (Integer.parseInt(Toatal_payable)));
                                    //  amntpayables = String.valueOf(amntpayable);

                                    System.out.println(" add to view");
                                    dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", "", "", "cash", str_remark, "Active", Toatal_payable, date);
                                    dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", "", "", "cash", str_remark, "Active");
                                }
                            }
                        }

                    }
                    System.out.println("pendingggggg" + pendingamt);
                    System.out.println("paayyyamounttttt" + paidamt);
                    int pend = (Integer.parseInt(pendingamt)) - (Integer.parseInt(payamount));
                    String pendi = Integer.toString(pend);
                    int pai = (Integer.parseInt(paidamt)) + (Integer.parseInt(payamount));
                    String paid = Integer.toString(pai);

                    //onBackPressed();
                    if (!cd.isConnectedToInternet()) {
                        String Enrl_Paid = "", Enrl_Pending = "", Enrl_advance = "", Total_Enrl_Paid = "", Total_Enrl_Pending = "", Total_Enrl_advance = "";
                        //   String receiptadv = dbrecepit.gettotaladvancereceipt(cusid, enrollid);
                        //  System.out.println("receiptadvvv "+receiptadv);
                        //  int receiptadvt = ((Integer.parseInt(Toatal_payable) + (Integer.parseInt(receiptadv))));
                        // String receiptadvtot = Integer.toString(receiptadvt);
                        // System.out.println("receiptadvtot "+receiptadv);
                        dbrecepit.updatepaidamnt(cusid, enrollid, pendi, paid, Toatal_payable);
                        custpaidpend = dbc.getpaidpending(cusid);
                        for (int i = 0; i < custpaidpend.size(); i++) {
                            Custmodel cmo = custpaidpend.get(i);
                            Enrl_Paid = cmo.getEnrlpaid();
                            Enrl_Pending = cmo.getTotalenrlpending();
                            Enrl_advance = cmo.getAdvanceamt();

                        }
                        int Total_Enrl_Pendin = (Integer.parseInt(Enrl_Pending)) - (Integer.parseInt(payamount));
                        int Total_Enrl_Pai = (Integer.parseInt(Enrl_Paid)) + (Integer.parseInt(payamount));
                        Total_Enrl_Pending = Integer.toString(Total_Enrl_Pendin);
                        Total_Enrl_Paid = Integer.toString(Total_Enrl_Pai);
                        //Total_Enrl_advance=Toatal_payable;
                        //String advancetotal = dbc.gettotaladvance(cusid);
                        //  System.out.println("advancetotal cust "+receiptadv);
                        //  int advancetot = (Integer.parseInt(Toatal_payable) + (Integer.parseInt(Enrl_advance)));
                        // String advancxeupdate = String.valueOf(advancetot);
                        //  System.out.println("advancxeupdate custy "+receiptadv);
                        dbc.updateamnt(cusid, Total_Enrl_Paid, Total_Enrl_Pending);
                        Intent it = new Intent(IndividualReceipt.this, PrintActivity.class);
                        it.putExtra("Cusname", cusname);
                        it.putExtra("Amount", amount2);
                        it.putExtra("Groupname", groupname);
                        it.putExtra("ticketno", ticketno);
                        it.putExtra("paymode", paytype);
                        it.putExtra("bonusamnt", bonusamt);
                        it.putExtra("pendingamnt", pendingamt);
                        it.putExtra("penaltyamnt", penaltyamt);
                        it.putExtra("instlmntamnt", payableamount);
                        it.putExtra("advance", Toatal_payable);
                        it.putExtra("Receiptno", "");
                        startActivity(it);
                        finish();
                    } else {

                    }
                    // } else {
                    //  Toast.makeText(IndividualReceipt.this, "Amount Mismatch", Toast.LENGTH_LONG).show();


                }

            }
        });
//==============================================================================================================
    }


    private void reteriveloca() {
        showDialog();
        enroll_list.clear();
        dbrecepit.deletetable();
        enroll_list = dbrecepit.getreceiptforcustenroll(cusid, enrollid);
        dbrecepit.addenroll(enroll_list);
        adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
        adapterlist.notifyDataSetChanged();
        //   ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
        lst_re_enroll.setAdapter(adapterlist);
        hidePDialog();

    }

    public void reteriveall() {

        showDialog();
        enroll_list.clear();
        // dbrecepit.deletetable();
        StringRequest internetreq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
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
                            sched.setPenalty_Amt("0");
                            sched.setBonus_Amt("0");
                            sched.setGroup_Name(jObj.getString("Group_Name"));
                            sched.setGroup_Ticket_Name(jObj.getString("Group_Ticket_Name"));
                            sched.setCusbranch(jObj.getString("Branch_Id"));
                            sched.setPendingdys(jObj.getString("Pending_Days"));
                            sched.setAdvanceamnt(jObj.getString("Advance_Amt"));
                            sched.setCompleted_auction(jObj.getString("Completed_auction"));
                            sched.setPaid_details(jObj.getString("Paid_Details"));
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

                        }

                        try {

                            dbrecepit.addenroll(enroll_list);
                            adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
                            adapterlist.notifyDataSetChanged();
                            //ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                            lst_re_enroll.setAdapter(adapterlist);

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                IndividualReceipt.this);
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
                params.put("enrollid", enrollid);
                System.out.println("cusid " + cusid);
                System.out.println("enrollid " + enrollid);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(internetreq);
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

    //============================================================================================================================
    public void postentry(final String enrollidd, final String bonusamt, final String paidamt, final String pendingamt, final String penaltyamt, final String Group, final String ticketno, final String payableamount, final String Remark, final String chitvalue, final String paytype, final String Cus_Branch, final String Pending_Days, final String status) {

        showDialog();
       // btn_submit.setVisibility(View.GONE);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response);
                System.out.println("coleeeeeeeeeee" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    receiptno = obj.getString("receiptno");
                    String status = obj.getString("status");
                    String receivedamnt = obj.getString("receivedamnt");
                    String Pending_Amt = obj.getString("Pending_Amt");
                    String Total_Enrl_Paid = obj.getString("Total_Enrl_Paid");
                    String Total_Enrl_Pending = obj.getString("Total_Enrl_Pending");
                    String advanceamnt = obj.getString("advanceamnt");
                    System.out.println("advancee amnt postentry" + advanceamnt);
                    int pend = (Integer.parseInt(pendingamt)) - (Integer.parseInt(payamount));
                    String pendi = Integer.toString(pend);
                    int pai = (Integer.parseInt(paidamt)) + (Integer.parseInt(payamount));
                    String paid = Integer.toString(pai);

                    System.out.println("pending amnt postentry" + pendi);
                    System.out.println("paid amnt postentry" + paid);
                    dbrecepit.updatepaidamnt(cusid, enrollid, pendi, paid, advanceamnt);
                    System.out.println("Collection gbewingb" + status);
                    System.out.println("receivedddddddddddn gbewrgvuivivbnrbgnringb" + receivedamnt);
                    System.out.println("Pending_Amt gbewrgvuivivbnrbgnringb" + Pending_Amt);
                    System.out.println("Total_Enrl_Paid gbewrgvuivivbnrbgnringb" + Total_Enrl_Paid);
                    System.out.println("Total_Enrl_Pending gbewrgvuivivbnrbgnringb" + Total_Enrl_Pending);

                    if (status.equals("1")) {
                        hidePDialog();
                        btn_submit.setVisibility(View.VISIBLE);
                        System.out.println("Collection Activity + receir");
                        String advancetotal = dbc.gettotaladvance(cusid);
                        int advancetot = ((Integer.parseInt(advanceamnt) + (Integer.parseInt(advancetotal))));
                        String advancxeupdate = String.valueOf(advancetot);
                        dbc.updateamnt(cusid, Total_Enrl_Paid, Total_Enrl_Pending);

                        editor.putString("receiptamnt", receivedamnt);
                        editor.commit();
                        if (paytype.equalsIgnoreCase("cash")) {
                            System.out.println(" add to view");
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, "", pendingamt, "", "", "", "", "", "cash", str_remark, "Active", advanceamnt, date);
                        } else if (paytype.equalsIgnoreCase("cheque")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, str_cheno, pendingamt, str_chedate, str_chebank, str_chebranch, str_tranno, "", "cheque", str_remark, "Pending", advanceamnt, date);
                        } else if (paytype.equalsIgnoreCase("dd")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", advanceamnt, date);
                        } else if (paytype.equalsIgnoreCase("neft")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "neft", str_remark, "Active", advanceamnt, date);
                        } else if (paytype.equalsIgnoreCase("card")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "card", str_remark, "Active", advanceamnt, date);
                        }
                        Intent it = new Intent(IndividualReceipt.this, PrintActivity.class);
                        System.out.println("Collection Activity + intented");
                        it.putExtra("Cusname", cusname);
                        it.putExtra("Amount", amount2);
                        it.putExtra("Groupname", groupname);
                        it.putExtra("ticketno", ticketno);
                        it.putExtra("paymode", paytype);
                        it.putExtra("bonusamnt", bonusamt);
                        it.putExtra("pendingamnt", pendingamt);
                        it.putExtra("pendingnow", Pending_Amt);
                        it.putExtra("penaltyamnt", penaltyamt);
                        it.putExtra("instlmntamnt", payableamount);
                        it.putExtra("advance", advanceamnt);
                        it.putExtra("Receiptno", receiptno);
                        System.out.println("Collection Activity + receir" + receiptno);
                        startActivity(it);
                        finish();
                    } else {
                        btn_submit.setVisibility(View.VISIBLE);
                        Toast.makeText(IndividualReceipt.this, "Receipt Entry Not Saved Try Again", Toast.LENGTH_SHORT).show();
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
                params.put("Cust_Id", cusid);
                params.put("enrollid", enrollidd);
                params.put("bonusamt", "0");
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
                if (isimage.equalsIgnoreCase("true")) {
                    params.put("isimage", isimage);
                    params.put("image", imgfile64);
                } else {
                    params.put("isimage", isimage);
                }
                params.put("Emp_Branch", pref.getString("empbranch", "3"));
                params.put("status", status);
                System.out.println(cusid + "internet on");
                System.out.println(enrollidd + "internet on");
                System.out.println(bonusamt + "internet on");
                System.out.println(paidamt + "internet on");
                System.out.println(pendingamt + "internet on");
                System.out.println(penaltyamt + "internet on PENALTYYYY");
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

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void Fillindividual() {
        recepitamt = edt_re_amount.getText().toString();
        String pendingamt = "", tableid = "";
        if (recepitamt.equalsIgnoreCase("0") || recepitamt.equalsIgnoreCase("")) {
            Toast.makeText(IndividualReceipt.this, "Enter Recepit Amount", Toast.LENGTH_LONG).show();
        } else {
            Balanceamt = Integer.parseInt(recepitamt);

            enroll_listmain = dbrecepit.getAllenroll();

            for (int i = 0; i < enroll_listmain.size(); i++) {
                Enrollmodel mod = enroll_listmain.get(i);
                pendingamt = mod.getPending_Amt();
                String penaltyamt = mod.getPenalty_Amt();
                String bonusamt = mod.getBonus_Amt();
                tableid = mod.getTableid();
            }
            if (Balanceamt > 0) {
                dbrecepit.updatepayamount(String.valueOf(Balanceamt), tableid, String.valueOf(pendingamt));
            }
            enroll_list = dbrecepit.getAllenroll();
            adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
            adapterlist.notifyDataSetChanged();
            // ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
            lst_re_enroll.setAdapter(adapterlist);
            Toatal_payable = dbrecepit.gettotal();
        }
    }

    private void FillAmount() {
        recepitamt = edt_re_amount.getText().toString();

        if (recepitamt.equalsIgnoreCase("0") || recepitamt.equalsIgnoreCase("")) {
            Toast.makeText(IndividualReceipt.this, "Enter Recepit Amount", Toast.LENGTH_LONG).show();
        } else {
            Balanceamt = Integer.parseInt(recepitamt);

            enroll_listmain = dbrecepit.getAllenroll();

            for (int i = 0; i < enroll_listmain.size(); i++) {
                Enrollmodel mod = enroll_listmain.get(i);
                String pendingamt = mod.getPending_Amt();
                String penaltyamt = mod.getPenalty_Amt();
                String bonusamt = mod.getBonus_Amt();
                String tableid = mod.getTableid();


                if (Balanceamt > 0) {
                    payable = Integer.parseInt(pendingamt);//+ Integer.parseInt(penaltyamt) - Integer.parseInt(bonusamt);

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
                    // dbrecepit.updatepayamount(String.valueOf(payable), tableid, String.valueOf(pendingamt));
                } else {
                    dbrecepit.updatepayamount(String.valueOf(0), tableid, String.valueOf(0));
                }


            }

            enroll_list = dbrecepit.getAllenroll();
            adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
            adapterlist.notifyDataSetChanged();
            // ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
            lst_re_enroll.setAdapter(adapterlist);
            Toatal_payable = dbrecepit.gettotal();


        }
    }

}
