package com.mazenet.mzs119.skst.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mazenet.mzs119.skst.Model.Enrollmodel;
import com.mazenet.mzs119.skst.R;
import com.mazenet.mzs119.skst.Utils.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CustomAdapterenrollment extends BaseAdapter {

    private Activity activity;
    private ArrayList<Enrollmodel> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    Enrollmodel tempValues = null;
    int i = 0;

    Context mContext;

    public CustomAdapterenrollment(Activity a, ArrayList<Enrollmodel> d) {

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

    public Enrollmodel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txt_grpname, txt_paid, txt_pending, txt_scheme, txt_penaulty, txt_amount, txt_advance, txt_paid_details, txt_completed_auction;
        // txtno,

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.enrolldetails, null);

            holder = new ViewHolder();
            holder.txt_grpname = (TextView) vi.findViewById(R.id.txt_grpname);
            holder.txt_paid = (TextView) vi.findViewById(R.id.txt_paid);
            holder.txt_pending = (TextView) vi.findViewById(R.id.txt_pending);
            holder.txt_scheme = (TextView) vi.findViewById(R.id.txt_scheme);
            holder.txt_paid_details = (TextView) vi.findViewById(R.id.txt_bonus);
            holder.txt_penaulty = (TextView) vi.findViewById(R.id.txt_penaulty);
            holder.txt_amount = (TextView) vi.findViewById(R.id.txt_amount);
            holder.txt_advance = (TextView) vi.findViewById(R.id.txt_advanceam);


            holder.txt_grpname.setTypeface(tf);
            holder.txt_paid.setTypeface(tf);
            holder.txt_pending.setTypeface(tf);
            holder.txt_scheme.setTypeface(tf);
            holder.txt_paid_details.setTypeface(tf);
            holder.txt_penaulty.setTypeface(tf);
            holder.txt_amount.setTypeface(tf);
            holder.txt_advance.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (Enrollmodel) data.get(position);

            String grpname, paid, pending, scheme, bonus, penaulty, amount, advance,completed_aution;
            String paid_d, pending_d, scheme_d, bonus_d, penaulty_d, amount_d, advance_d;

            grpname = tempValues.getGroup_Name() + "/" + tempValues.getGroup_Ticket_Name();

            // NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            Locale curLocale = new Locale("en", "IN");

            paid_d = tempValues.getPaid_Amt();
            try {
                Double d = Double.parseDouble(paid_d);
                String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                paid_d = moneyString1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            advance_d = tempValues.getAdvanceamnt();
            try {
                Double d = Double.parseDouble(advance_d);
                String moneyString6 = NumberFormat.getNumberInstance(curLocale).format(d);
                advance_d = moneyString6;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            //=====================================
            pending_d = tempValues.getPending_Amt();
            if (pending_d.contains("-")) {
                pending_d = "0.00";
            }
            try {
                Double d = Double.parseDouble(pending_d);
                String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                pending_d = moneyString2;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            //=====================================
            scheme_d = tempValues.getScheme();
            try {
                Double d = Double.parseDouble(scheme_d);
                String moneyString3 = NumberFormat.getNumberInstance(curLocale).format(d);
                scheme_d = moneyString3;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            //=====================================
            bonus_d = tempValues.getBonus_Amt();
            try {
                Double d = Double.parseDouble(bonus_d);
                String moneyString4 = NumberFormat.getNumberInstance(curLocale).format(d);
                bonus_d = moneyString4;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            //=====================================
            penaulty_d = tempValues.getPenalty_Amt();
            try {
                Double d = Double.parseDouble(penaulty_d);
                String moneyString5 = NumberFormat.getNumberInstance(curLocale).format(d);
                penaulty_d = moneyString5;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            //=====================================
            amount_d = tempValues.getPayamount();
            try {
                Double d = Double.parseDouble(amount_d);
                String moneyString6 = NumberFormat.getNumberInstance(curLocale).format(d);
                amount_d = moneyString6;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            paid = "Paid : Rs." + paid_d;
            pending = "Pending : Rs." + pending_d;
            scheme = "Chit : Rs." + scheme_d;
            bonus = "Paid details : " + tempValues.getPaid_details();
            penaulty = "Penaulty : Rs." + penaulty_d;
            amount = "Amount Payable : Rs." + amount_d;
            advance = "Advance Amount : Rs." + advance_d;
            completed_aution="Completed Auction : "+tempValues.getCompleted_auction();

            holder.txt_grpname.setText(grpname);
            holder.txt_paid.setText(paid);
            holder.txt_pending.setText(pending);
            holder.txt_scheme.setText(scheme);
            holder.txt_paid_details.setText(bonus);
            holder.txt_penaulty.setText(penaulty);
            holder.txt_amount.setText(amount);
            holder.txt_advance.setText(advance);
            holder.txt_amount.setText(completed_aution);

        }
        return vi;
    }


}
