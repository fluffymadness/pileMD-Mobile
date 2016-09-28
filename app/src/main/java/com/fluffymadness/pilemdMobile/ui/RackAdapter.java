package com.fluffymadness.pilemdMobile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/25/2016.
 */

public class RackAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<File> data;

    public RackAdapter(Context context, ArrayList<File> content) {
        this.context = context;
        this.data = content;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.item_row, viewGroup, false);
        TextView rackName = (TextView) row.findViewById(R.id.rowText);
        rackName.setText(getItem(i).toString());

        return row;
    }
}
