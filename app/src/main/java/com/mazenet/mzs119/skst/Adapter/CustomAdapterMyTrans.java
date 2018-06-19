package com.mazenet.mzs119.skst.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mazenet.mzs119.skst.Model.DateWiseViewModel;
import com.mazenet.mzs119.skst.R;
import com.mazenet.mzs119.skst.Utils.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CustomAdapterMyTrans extends BaseAdapter {

    private Activity activity;
    private ArrayList<DateWiseViewModel> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    DateWiseViewModel tempValues = null;
    int i = 0;

    Context mContext;

    public CustomAdapterMyTrans(Activity a, ArrayList<DateWiseViewModel> d) {

        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mContext = activity.getApplicationContext();
        tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);

    }

    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public DateWiseViewModel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView tx_sno, txt_date, txt_amount, txt_paytype;
        // txtno,

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.billdetailsmytrans, null);

            holder = new ViewHolder();
            holder.tx_sno = (TextView) vi.findViewById(R.id.txt_rc_sno);
            holder.txt_date = (TextView) vi.findViewById(R.id.txt_rc_date);
              holder.txt_amount = (TextView) vi.findViewById(R.id.txt_rc_amount);
              holder.txt_paytype = (TextView) vi.findViewById(R.id.txt_rc_paytype);


             holder.tx_sno.setTypeface(tf);
            holder.txt_amount.setTypeface(tf);
            holder.txt_date.setTypeface(tf);
              holder.txt_paytype.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (DateWiseViewModel) data.get(position);

            String name, paid, pending, advance;
            advance=tempValues.getTotal_Amount();
            //name = tempValues.getFirst_Name_F();
            // paid = tempValues.getEnrlpaid();
            //advance = tempValues.getTotal_Amount();
            //  pending = tempValues.getTotalenrlpending();


            // NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            Locale curLocale = new Locale("en", "IN");
            //Log.d("Collection Adapter", name);

/*
            try {
                String moneyString1 = formatter.format(Double.valueOf(paid));
                paid = moneyString1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                if (pending.contains("-")) {
                    pending = "0.00";
                }
                String moneyString = formatter.format(Double.valueOf(pending));
                pending = moneyString;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } */
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
            advance = "Rs. " + advance;
            holder.tx_sno.setText(String.valueOf(position+1));
              holder.txt_date.setText(tempValues.getDate());
            holder.txt_amount.setText(advance);
            holder.txt_paytype.setText(tempValues.getPayment_Type());
        }
        return vi;
    }


}
