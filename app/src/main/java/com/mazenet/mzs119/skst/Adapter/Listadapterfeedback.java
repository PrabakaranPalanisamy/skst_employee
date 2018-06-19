package com.mazenet.mzs119.skst.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mazenet.mzs119.skst.Model.FeedbackModel;
import com.mazenet.mzs119.skst.R;
import com.mazenet.mzs119.skst.Utils.Config;

import java.util.ArrayList;


public class Listadapterfeedback extends BaseAdapter {

    private Activity activity;
    private ArrayList<FeedbackModel> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    int i = 0;

    Context mContext;

    public Listadapterfeedback(Activity a, ArrayList<FeedbackModel> d) {

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

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txt;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.tabitem2, null);

            holder = new ViewHolder();
            holder.txt = (TextView) vi.findViewById(R.id.textname);
            holder.txt.setTypeface(tf);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {

            FeedbackModel feed = data.get(position);
            String NAME;

            NAME = feed.getName();

            holder.txt.setText(NAME);

        }
        return vi;
    }

    public void refresh(ArrayList<FeedbackModel> mStrings1) {
        // TODO Auto-generated method stub
        data = mStrings1;
        notifyDataSetChanged();
    }

}
