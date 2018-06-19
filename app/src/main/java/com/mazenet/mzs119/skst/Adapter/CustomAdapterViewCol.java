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

import com.mazenet.mzs119.skst.Model.Custmodel;
import com.mazenet.mzs119.skst.Model.TempEnrollModel;
import com.mazenet.mzs119.skst.R;
import com.mazenet.mzs119.skst.Utils.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CustomAdapterViewCol extends BaseAdapter {

    private Activity activity;
    private ArrayList<TempEnrollModel> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    TempEnrollModel tempValues = null;
    int i = 0;

    Context mContext;

    public CustomAdapterViewCol(Activity a, ArrayList<TempEnrollModel> d) {

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

    public TempEnrollModel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txtname, txtpaid, txtpending, txtcolected;
        // txtno,

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.billdetailsview, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_custname);
            holder.txtcolected = (TextView) vi.findViewById(R.id.txt_colectedamnt);
            //  holder.txtpaid = (TextView) vi.findViewById(R.id.txt_paid);
            //  holder.txtpending = (TextView) vi.findViewById(R.id.txt_pending);


            //   holder.txtpaid.setTypeface(tf);
            holder.txtname.setTypeface(tf);
            holder.txtcolected.setTypeface(tf);
            //   holder.txtpending.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (TempEnrollModel) data.get(position);

            String name, paid, pending, advance;

            name = tempValues.getCusname();
            // paid = tempValues.getEnrlpaid();
            advance = tempValues.getPayamount();
            //  pending = tempValues.getTotalenrlpending();


            // NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            Locale curLocale = new Locale("en", "IN");
            Log.d("Collection Adapter", name);
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
            holder.txtname.setText(name);
            //  holder.txtpaid.setText(paid);
            holder.txtcolected.setText(advance);
            //holder.txtpending.setText(pending);
        }
        return vi;
    }


}
