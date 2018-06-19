package com.mazenet.mzs119.skst;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.mazenet.mzs119.skst.Utils.Config;
import com.mazenet.mzs119.skst.Utils.ConnectionDetector;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.N)
public class PrintActivityLoansDatewise extends AppCompatActivity {
    // Debugging
    private static final String TAG = "BloothPrinterActivity";
    private static final boolean D = true;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SimpleDateFormat dateFormat;
    String date = "", party = "", address = "", prty_gstin = "", phone = "", grand_total = "", cmpny_gst = "4587GH8VF875K", cmpny_phone = "",
            owner_nme = "", cmpny_street = "",
            cmpny_city = "", grnd = "", sg = "", cg = "", igs = "", tx_incld = "", customer_state = "", tx_incld1 = "", igst1 = "", bill_no = "0001";
    String[] item_array;
    String Cusname = "", Amount = "", Groupname = "", ticketno = "", paymode = "", Receiptno = "", pendingamnt = "", penaltyamnt = "", instlmntamnt = "", bonusamnt = "", username = "", advanceamnt = "", Receivedamnt = "", Pendingamnt = "", Advanceamntt = "", reference = "";
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    TextView txt_name, txt_receive, txt_pending, txt_advance;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static int revBytes = 0;
    public static boolean isHex = false;

    public static final int REFRESH = 8;

    ConnectionDetector cd;
    private Button mBtnConnetBluetoothDevice = null;
    private Button mBtnPrint = null;

    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;
    Locale curLocale;

    public PrintActivityLoansDatewise() throws ParseException {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        // dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        Date date1 = new Date();
        //date = dateFormat.format(date1);
        cd = new ConnectionDetector(this);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(pref.getString("username", ""));
        curLocale = new Locale("en", "IN");
        username = pref.getString("username", "");
        if (D) Log.e(TAG, "+++ ON CREATE +++");
        txt_name = (TextView) findViewById(R.id.txt_pa_name);
        txt_advance = (TextView) findViewById(R.id.txt_pa_advanceamnt);
        txt_pending = (TextView) findViewById(R.id.txt_pa_Pendingamnt);
        txt_receive = (TextView) findViewById(R.id.txt_pa_receiveamnt);

        setContentView(R.layout.main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // �����ʼ��
        InitUIControl();
        //fininvoice = "vjv-8";
        try {
            Intent it = getIntent();
            Cusname = it.getStringExtra("Cusname");
            Amount = it.getStringExtra("Amount");
            date = it.getStringExtra("date");
            reference = it.getStringExtra("Reference");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void InitUIControl() {

        mBtnConnetBluetoothDevice = (Button) findViewById(R.id.btn_connect_bluetooth_device);
        mBtnConnetBluetoothDevice.setOnClickListener(mBtnConnetBluetoothDeviceOnClickListener);
        mBtnPrint = (Button) findViewById(R.id.btn_print);
        mBtnPrint.setOnClickListener(mBtnPrintOnClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");


        if (mChatService != null) {
            if (mChatService.getState() == BluetoothPrintDriver.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        mChatService = new BluetoothPrintDriver(this, mHandler);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    @SuppressLint("NewApi")
    private void ensureDiscoverable() {
        if (D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintDriver.STATE_CONNECTED:

                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:

                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
                        case BluetoothPrintDriver.STATE_NONE:

                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String ErrorMsg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    float Voltage = 0;
                    if (D)
                        Log.i(TAG, "readBuf[0]:" + readBuf[0] + "  readBuf[1]:" + readBuf[1] + "  readBuf[2]:" + readBuf[2]);
                    if (readBuf[2] == 0)
                        ErrorMsg = "NO ERROR!         ";
                    else {
                        if ((readBuf[2] & 0x02) != 0)
                            ErrorMsg = "ERROR: No printer connected!";
                        if ((readBuf[2] & 0x04) != 0)
                            ErrorMsg = "ERROR: No paper!  ";
                        if ((readBuf[2] & 0x08) != 0)
                            ErrorMsg = "ERROR: Voltage is too low!  ";
                        if ((readBuf[2] & 0x40) != 0)
                            ErrorMsg = "ERROR: Printer Over Heat!  ";
                    }
                    Voltage = (float) ((readBuf[0] * 256 + readBuf[1]) / 10.0);
                    //if(D) Log.i(TAG, "Voltage: "+Voltage);
                    DisplayToast(ErrorMsg + "                                        " + "Battery voltage��" + Voltage + " V");
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    //��ʾ��Ϣ
    public void showMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }//showMessage

    // ��ʾToast
    public void DisplayToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        //����toast��ʾ��λ��
        toast.setGravity(Gravity.TOP, 0, 100);
        //��ʾ��Toast
        toast.show();
    }//DisplayToast

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    //Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    OnClickListener mBtnQuitOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Stop the Bluetooth chat services
            if (mChatService != null) mChatService.stop();
            finish();
        }
    };

    OnClickListener mBtnConnetBluetoothDeviceOnClickListener = new OnClickListener() {
        Intent serverIntent = null;

        public void onClick(View arg0) {
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(PrintActivityLoansDatewise.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }
    };

    OnClickListener mBtnPrintOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean connected = pref.getBoolean("connected", Boolean.parseBoolean(""));
            if (!connected) {
                mBtnConnetBluetoothDevice.setVisibility(View.VISIBLE);
            } else {
                mBtnConnetBluetoothDevice.setVisibility(View.GONE);
                connectautomatic();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (BluetoothPrintDriver.IsNoConnection()) {
                            System.out.println("retned");
                            return;
                        }
                        BluetoothPrintDriver.Begin();
                        System.out.println("printing daaaa");
                        //Receiptno = pref.getString("receiptno", "");
                        String Billname = Receiptno;
                        String Dateprint = "Date : " + date;


                        String owner_nme = "SKST Chit Funds Pvt.Ltd", cmpny_street = pref.getString("B_Address", ""), cmpny_city = pref.getString("B_city", "");
                        String Toparty = "Name :" + Cusname;
                     /*   String subtotal = Amount;
                        String payment = paymode;
                        String Group = Groupname;

                        String ticket = ticketno;
                        System.out.println("pending amnt" + pendingamnt + " Amount " + Amount);
                        int pend = (Integer.parseInt(pendingamnt)) - (Integer.parseInt(Amount));
                        String pendi = Integer.toString(pend);
                        if (pendi.contains("-")) {
                            pendi = "0.00";
                        }
                        if (advanceamnt.equalsIgnoreCase("0") || advanceamnt.contains("-")) {
                            advanceamnt = "0.00";
                        }
                        int lengthsubtotal = subtotal.length();
                        if (lengthsubtotal <= 32) {
                            int dumlength = 9 - lengthsubtotal;
                            for (int j = 0; j < dumlength; j++) {
                                subtotal = " " + subtotal;
                            }

                        } else {
                            //  itemname = itemname.substring(0, 19) + ".";
                        }
                        if (!Receiptno.equals("") && cd.isConnectedToInternet()) {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("Receipt No : " + Billname + "\n");
                            BluetoothPrintDriver.LF();
                        } else {

                        } */


                        BluetoothPrintDriver.SetAlignMode((byte) 2);
                        BluetoothPrintDriver.BT_Write(Dateprint + "\n");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x01);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//����

                        BluetoothPrintDriver.BT_Write(owner_nme + "\n");
                        BluetoothPrintDriver.SetBold((byte) 0x00);//����
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.BT_Write(cmpny_street + ",\n");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.BT_Write(cmpny_city + ", Pin:" + pref.getString("B_pincode", ""));
                        BluetoothPrintDriver.BT_Write("\n");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.BT_Write(cmpny_city + ", Ph.No:" + pref.getString("B_brnchphone", ""));
                        BluetoothPrintDriver.BT_Write("\n");
                        // BluetoothPrintDriver.LF();

                        // BluetoothPrintDriver.SetAlignMode((byte) 1);
                        //  BluetoothPrintDriver.BT_Write("GST No: " + cmpny_gst);
                        //BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write("\n");
                        // BluetoothPrintDriver.LF();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//����
                        // BluetoothPrintDriver.LF();
                        BluetoothPrintDriver.BT_Write(Toparty + "\n");
                        BluetoothPrintDriver.SetBold((byte) 0x00);//����

                        BluetoothPrintDriver.BT_Write(String.format("--------------------------------\n"), true);

                  /*      if(Groupname.equalsIgnoreCase("Commitment")){

                        }
                        else {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.BT_Write("Group : ");
                            int grouplen = Group.length();/
         /*   if (grouplen <= 32) {
                int dumlength = 32 - grouplen;
                for (int j = 0; j < dumlength; j++) {
                    Group = " "+Group ;
                }

            } else {

            } */
                        //    BluetoothPrintDriver.BT_Write(Group + "\n");
                        // }

                        //BluetoothPrintDriver.LF();

       /*     BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Installment : ");
     /*       int instllenth=instlmntamnt.length();
            if (instllenth <= 32) {
                int dumlength = 32 - instllenth;
                for (int j = 0; j < dumlength; j++) {
                    instlmntamnt = " "+instlmntamnt ;
                }

            } else {

            } */
                        //   BluetoothPrintDriver.BT_Write(pendingamnt + " Rs" + "\n");
                        //  BluetoothPrintDriver.LF();

       /*     BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Penalty : ");
       /*     int penlenth=penaltyamnt.length();
            if (penlenth <= 32) {
                int dumlength = 32 - penlenth;
                for (int j = 0; j < dumlength; j++) {
                    penaltyamnt = " "+penaltyamnt ;
                }
            } else {
            } *
            BluetoothPrintDriver.BT_Write(penaltyamnt + " Rs" + "\n");
            //  BluetoothPrintDriver.LF();

            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Bonus : ");
      /*      int bonlen=bonusamnt.length();
            if (penlenth <= 32) {
                int dumlength = 32 - bonlen;
                for (int j = 0; j < dumlength; j++) {
                    bonusamnt = " "+bonusamnt ;
                }
            } else {
            } *
            BluetoothPrintDriver.BT_Write(bonusamnt + " Rs" + "\n");
            //  BluetoothPrintDriver.LF();

            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.BT_Write("Receipt Amount : ");
       /*      int amntlen=Amount.length();
            if (amntlen <= 32) {
                int dumlength = 32 - amntlen;
                for (int j = 0; j < dumlength; j++) {
                    Amount = " "+Amount ;
                }
            } else {
            } */
                        //   BluetoothPrintDriver.BT_Write(Amount + " Rs" + "\n");
                        //  BluetoothPrintDriver.LF();
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write("Reference Group: ");
       /*     int paylen=paymode.length();
            if (amntlen <= 32) {
                int dumlength = 32 - paylen;
                for (int j = 0; j < dumlength; j++) {
                    paymode = " "+paymode ;
                }
            } else {
            } */
                        BluetoothPrintDriver.BT_Write(reference + "\n");

                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write("Received Amount : ");
       /*      int amntlen=Amount.length();
            if (amntlen <= 32) {
                int dumlength = 32 - amntlen;
                for (int j = 0; j < dumlength; j++) {
                    Amount = " "+Amount ;
                }
            } else {

            } */
                        try {
                            Double d = Double.parseDouble(Amount);
                            String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                            Receivedamnt = moneyString1;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        BluetoothPrintDriver.BT_Write(Receivedamnt + " Rs" + "\n");
                        //   BluetoothPrintDriver.LF();



                        //   BluetoothPrintDriver.SetAlignMode((byte) 0);
                        //   BluetoothPrintDriver.BT_Write("Advance Amount : ");
       /*     int paylen=paymode.length();
            if (amntlen <= 32) {
                int dumlength = 32 - paylen;
                for (int j = 0; j < dumlength; j++) {
                    paymode = " "+paymode ;
                }
            } else {
            } */
                   /*     try {
                            Double d = Double.parseDouble(advanceamnt);
                            String moneyString3 = NumberFormat.getNumberInstance(curLocale).format(d);
                            Advanceamntt = moneyString3;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        BluetoothPrintDriver.BT_Write(Advanceamntt + " Rs" + "\n"); */
                        // BluetoothPrintDriver.LF();


                        // BluetoothPrintDriver.LF();


                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.BT_Write(String.format("--------------------------------\n"), true);// ┏
                        // BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.BT_Write("*System generated Bill.\nNo signature Needed\n");
                        //  BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.BT_Write("Prepared by : " + username);
                        // BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                        BluetoothPrintDriver.BT_Write("\nThank You\n");
                        BluetoothPrintDriver.LF();
                        BluetoothPrintDriver.SetAlignMode((byte) 1);
                        BluetoothPrintDriver.BT_Write(String.format("- - - - - - - - - - - - - - - -\n"), true);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x00);
                        BluetoothPrintDriver.BT_Write("\n");
                        BluetoothPrintDriver.LF();

                        Intent i = new Intent(PrintActivityLoansDatewise.this, CollectionActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 1500);

            }
        }
    };


    OnClickListener mBtnTestOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (BluetoothPrintDriver.IsNoConnection()) {
                return;
            }
            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SelftestPrint();    //��ӡ�Բ�ҳ
        }
    };
    OnClickListener mBtnInquiryOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //Log.i(TAG, "inquiry btn");
            if (BluetoothPrintDriver.IsNoConnection()) {
                return;
            }
            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.StatusInquiry();    // ��ѯ����״̬���ص�ѹ

        }
    };

    private void connectautomatic() {
        Intent serverIntent = null;
        serverIntent = new Intent(PrintActivityLoansDatewise.this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }


}