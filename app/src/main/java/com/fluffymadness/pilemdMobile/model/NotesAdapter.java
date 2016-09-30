package com.fluffymadness.pilemdMobile.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fluffymadness.pilemdMobile.ui.R;

import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/25/2016.
 */

public class NotesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SingleNote> data;

    public NotesAdapter(Context context, ArrayList<SingleNote> content) {
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

        View row = inflater.inflate(R.layout.notes_row, viewGroup, false);
        TextView noteTitle = (TextView) row.findViewById(R.id.note_title);
        noteTitle.setText(data.get(i).getName());
        TextView noteText = (TextView) row.findViewById(R.id.note_text);
        noteText.setText(data.get(i).getSummary());
        TextView noteDate  = (TextView) row.findViewById(R.id.note_date);
        noteDate.setText(data.get(i).getLastModified());
        return row;
    }
}