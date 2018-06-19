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
import com.mazenet.mzs119.skst.R;
import com.mazenet.mzs119.skst.Utils.Config;

import java.util.ArrayList;


public class CustomAdaptercustomername extends BaseAdapter {

    private Activity activity;
    private ArrayList<Custmodel> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    Custmodel tempValues = null;
    int i = 0;

    Context mContext;

    public CustomAdaptercustomername(Activity a, ArrayList<Custmodel> d) {

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

    public Custmodel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txtname, txtpaid, txtpending;
        // txtno,

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.billdetails1, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_name);
            holder.txtpaid = (TextView) vi.findViewById(R.id.txt_paid);
            holder.txtpending = (TextView) vi.findViewById(R.id.txt_pending);


            holder.txtpaid.setTypeface(tf);
            holder.txtname.setTypeface(tf);
            holder.txtpending.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (Custmodel) data.get(position);

            String name, paid, pending;

            name = tempValues.getNAME();
            paid = tempValues.getEnrlpaid();
            pending = tempValues.getTotalenrlpending();

            Log.d("Collection Adapter", name);
            holder.txtpaid.setVisibility(View.GONE);
            holder.txtpending.setVisibility(View.GONE);

            holder.txtname.setText(name);
            holder.txtpaid.setText(paid);
            holder.txtpending.setText(pending);
        }
        return vi;
    }


}
