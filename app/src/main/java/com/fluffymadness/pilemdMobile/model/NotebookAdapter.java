package com.fluffymadness.pilemdMobile.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fluffymadness.pilemdMobile.ui.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by fluffymadness on 9/25/2016.
 */

public class NotebookAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<SingleNotebook> data;

    public NotebookAdapter(Context context, int resource, ArrayList<SingleNotebook> content) {
        super(context,resource,content);
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

        View row = inflater.inflate(R.layout.notebook_row, viewGroup, false);
        TextView notebookTitle = (TextView) row.findViewById(R.id.notebook_title);
        notebookTitle.setText(data.get(i).getName());
        TextView noteCount = (TextView) row.findViewById(R.id.note_count);
        noteCount.append(": "+String.valueOf(data.get(i).getNoteCount()));
        return row;
    }
    public void sort(SortBy sortBy){

        if(sortBy == SortBy.NAME) {
            Collections.sort(data, new Comparator<SingleNotebook>() {

                @Override
                public int compare(SingleNotebook singleNotebook, SingleNotebook t1) {
                    return singleNotebook.getName().compareTo(t1.getName());
                }


            });
        }
        this.notifyDataSetChanged();
    }

}
