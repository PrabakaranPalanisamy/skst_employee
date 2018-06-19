package com.mazenet.mzs119.skst.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by PRABAKARAN on 12/19/2017.
 */

public class ListViewHeight {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
        // pre-condition
        return;
    }

    int totalHeight = 0;
    int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
    for (int i = 0; i < listAdapter.getCount(); i++) {
        View listItem = listAdapter.getView(i, null, listView);
        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
        totalHeight += listItem.getMeasuredHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
      //  params.height = totalHeight ;

        listView.setLayoutParams(params);
    listView.requestLayout();
}
}

