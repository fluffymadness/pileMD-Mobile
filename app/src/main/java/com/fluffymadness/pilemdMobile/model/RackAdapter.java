package com.fluffymadness.pilemdMobile.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fluffymadness.pilemdMobile.ui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by fluffymadness on 9/25/2016.
 */

public class RackAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SingleRack> data;

    public RackAdapter(Context context, ArrayList<SingleRack> content) {
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

        View row = inflater.inflate(R.layout.rack_row, viewGroup, false);
        TextView rackName = (TextView) row.findViewById(R.id.rowText);
        rackName.setText(data.get(i).getName());

        return row;
    }

    public void sort(SortBy sortBy){

        if(sortBy == SortBy.NAME) {
            Collections.sort(data, new Comparator<SingleRack>() {

                @Override
                public int compare(SingleRack singleRack, SingleRack t1) {
                    return singleRack.getName().compareTo(t1.getName());
                }


            });
        }
        this.notifyDataSetChanged();
    }
}
